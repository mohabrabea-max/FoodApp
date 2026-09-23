package com.example.applicationhome.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.applicationhome.core.data.local.entity.ReviewsEntity
import com.example.applicationhome.core.data.local.entity.ReviewsStarsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewsDao {
    //--------------------------------------   Add    -------------------------------------

    @Upsert
    suspend fun addReviews(reviews : List<ReviewsEntity>)

    @Upsert
    suspend fun addStars(stars : List<ReviewsStarsEntity>)


    //--------------------------------------   Get    -------------------------------------

    @Query("SELECT * FROM reviews_entity WHERE resId = :resId AND userId != :userId")
    fun getAllReviews(resId : Int, userId : String): PagingSource<Int, ReviewsEntity>

    @Query("SELECT * FROM reviews_entity WHERE resId = :resId AND userId = :userId")
    fun getUserReview(resId : Int, userId : String): Flow<ReviewsEntity?>

    @Query("SELECT lastUpdate FROM last_update_reviews WHERE resId = :resId")
    suspend fun getLastSyncTimestamp(resId : Int): Long?


    //--------------------------------------   Update    -------------------------------------
    @Query("UPDATE last_update_reviews SET lastUpdate = :time WHERE resId = :resId")
    suspend fun updateLastSyncTimestamp(resId : Int, time : Long)


    @Transaction
    suspend fun addReviewsAndUpdateSyncTime(
        reviews : List<ReviewsEntity>,
        resId : Int,
        newTimestamp : Long
    ){
        addReviews(reviews)
        updateLastSyncTimestamp(resId, newTimestamp)
    }
}