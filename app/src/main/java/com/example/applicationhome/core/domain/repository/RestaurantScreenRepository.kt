package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantScreenRepository @Inject constructor(
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao
) {
    fun getMealsFromDatabase(resId : Int): Flow<List<MealWithFavoriteStatus>> =
        foodAndRestaurantsDao.getMealsFromDatabase(resId)

    fun getSnacksFromDatabase(resId : Int): Flow<List<SnackWithFavoriteStatus>> =
        foodAndRestaurantsDao.getSnacksFromDatabase(resId)

    fun getRestaurantOffersFromDatabase(resId : Int): Flow<List<OffersEntity>> =
        foodAndRestaurantsDao.getRestaurantOffersFromDatabase(resId)
}