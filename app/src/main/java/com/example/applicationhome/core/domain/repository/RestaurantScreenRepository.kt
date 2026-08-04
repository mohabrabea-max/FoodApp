package com.example.applicationhome.core.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.applicationhome.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantScreenRepository @Inject constructor(
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao
) {
    suspend fun getRestaurantByIdFromDatabase(resId : Int): RestaurantWithFavoriteStatus =
        foodAndRestaurantsDao.getOneRestaurantFromDatabase(resId)

    suspend fun getMealByIdFromDatabase(mealId : Int): MealWithFavoriteStatus =
        foodAndRestaurantsDao.getOneMealFromDatabase(mealId)

    fun getMealsFromDatabase(resId : Int, type : String): Flow<PagingData<MealWithFavoriteStatus>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                foodAndRestaurantsDao.getMealsFromDatabase(resId, type)
            }
        ).flow

    fun getSnacksFromDatabase(resId : Int): Flow<PagingData<SnackWithFavoriteStatus>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                foodAndRestaurantsDao.getSnacksFromDatabase(resId)
            }
        ).flow

    fun getRestaurantOffersFromDatabase(resId : Int): Flow<List<OffersEntity>> =
        foodAndRestaurantsDao.getRestaurantOffersFromDatabase(resId)
}