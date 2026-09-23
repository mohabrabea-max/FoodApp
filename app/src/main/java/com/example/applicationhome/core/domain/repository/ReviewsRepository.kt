package com.example.applicationhome.core.domain.repository

import androidx.paging.PagingData
import com.example.applicationhome.core.data.remote.dto.ReviewsForPut
import com.example.applicationhome.core.domain.model.ReviewsDomainClass
import kotlinx.coroutines.flow.Flow

interface ReviewsRepository {
    fun getAllReviewsFromDatabase(resId : Int, userId : String): Flow<PagingData<ReviewsDomainClass>>
    fun getUserReviewFromDatabase(resId : Int, userId : String): Flow<ReviewsDomainClass?>

    suspend fun syncRestaurantReviewsFromAPI(resId : Int): Result<Unit>
    suspend fun putReview(review : ReviewsForPut, userId : String): Result<Unit>
    suspend fun deleteReview(resId : Int, userId : String): Result<Unit>

    suspend fun checkIfUserDidOrderedFromRestaurant(resId : Int, userId : String): Result<Boolean>
}