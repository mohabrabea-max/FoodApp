package com.example.applicationhome.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.applicationhome.core.data.local.dao.ReviewsDao
import com.example.applicationhome.core.data.local.entity.ReviewsStarsEntity
import com.example.applicationhome.core.data.mapper.reviewsEntityToReviewsDomainClass
import com.example.applicationhome.core.data.mapper.reviewsForGetToReviewsEntity
import com.example.applicationhome.core.data.remote.FoodAppAPIs
import com.example.applicationhome.core.data.remote.dto.ReviewsForPut
import com.example.applicationhome.core.data.remote.dto.ReviewsStars
import com.example.applicationhome.core.data.remote.util.retryLocally
import com.example.applicationhome.core.domain.model.ReviewsDomainClass
import com.example.applicationhome.core.domain.repository.ReviewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

class ReviewsRepositoryImpl @Inject constructor(
    private val api : FoodAppAPIs,
    private val reviewsDao : ReviewsDao
): ReviewsRepository {
    override fun getAllReviewsFromDatabase(resId : Int, userId : String): Flow<PagingData<ReviewsDomainClass>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                reviewsDao.getAllReviews(resId, userId)
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.reviewsEntityToReviewsDomainClass()
            }
        }

    override fun getUserReviewFromDatabase(
        resId: Int,
        userId: String
    ): Flow<ReviewsDomainClass?> =
        reviewsDao.getUserReview(resId, userId).map {
            it?.reviewsEntityToReviewsDomainClass()
        }



    override suspend fun syncRestaurantReviewsFromAPI(resId : Int): Result<Unit> {
        val lastSyncTimestamp = reviewsDao.getLastSyncTimestamp(resId)?: 0L

        return retryLocally {
            val response = api.getRestaurantReviews(
                restaurantId = resId,
                lastSyncTimestamp = lastSyncTimestamp
            )

            if(!response.isSuccessful){
                throw HttpException(response)
            }

            val reviews = response.body()

            if(!reviews.isNullOrEmpty()){
                val entityReviews = reviews.map { it.value.reviewsForGetToReviewsEntity(it.key) }
                val newLastSyncTime = entityReviews.maxOfOrNull { it.createdAt }?: lastSyncTimestamp

                reviewsDao.addReviewsAndUpdateSyncTime(
                    reviews = entityReviews,
                    resId = resId,
                    newTimestamp = newLastSyncTime
                )
            }
        }
    }

    override suspend fun putReview(review : ReviewsForPut, userId : String): Result<Unit> =
        retryLocally {
            val response = api.putRestaurantReview(
                restaurantId = review.resId,
                userId = userId,
                review = review
            )

            if(!response.isSuccessful){
                throw HttpException(response)
            }

            val userReview = response.body()



            val stars = reviewsDao.getRestaurantStars(review.resId)?: ReviewsStarsEntity()
            val newStars = ReviewsStars(
                resId = review.resId,
                stars = stars.stars + review.stars,
                number = stars.number + 1
            )

            val starsResponse = api.putRestaurantReviewStars(
                restaurantId = review.resId,
                review = newStars
            )

            if(!starsResponse.isSuccessful){
                throw HttpException(starsResponse)
            }

            if(userReview != null){
                val entityReview = userReview.reviewsForGetToReviewsEntity(userId)
                reviewsDao.addReviews(
                    reviews = listOf(entityReview)
                )
                reviewsDao.addStars(
                    listOf(
                        ReviewsStarsEntity(
                            resId = newStars.resId,
                            stars = newStars.stars,
                            number = newStars.number
                        )
                    )
                )
            }
        }

    override suspend fun deleteReview(
        resId : Int,
        userId : String
    ): Result<Unit> =
        retryLocally {
            val response = api.deleteReview(
                restaurantId = resId,
                userId = userId
            )

            if(!response.isSuccessful){
                throw HttpException(response)
            }

            reviewsDao.deleteReview(
                userId = userId,
                resId = resId
            )
        }

    override suspend fun checkIfUserDidOrderedFromRestaurant(
        resId : Int,
        userId : String
    ): Result<Boolean> =
        retryLocally {
            val response = api.checkUserOrderedFromRestaurant(
                userId = userId,
                equalTo = resId
            )

            if(!response.isSuccessful){
                throw HttpException(response)
            }

            val result = response.body()

            !result.isNullOrEmpty()
        }
}