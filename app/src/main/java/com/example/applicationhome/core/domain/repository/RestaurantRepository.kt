package com.example.applicationhome.core.domain.repository

import androidx.paging.PagingData
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    fun getRestaurantByIdFromDatabase(resId : Int): Flow<RestaurantWithFavoriteStatus?>
    fun getMealByIdFromDatabase(mealId : Int): Flow<MealWithFavoriteStatus?>
    fun getSnackByIdFromDatabase(snackId : Int): Flow<SnackWithFavoriteStatus?>
    fun getMealsFromDatabase(resId : Int, type : String): Flow<PagingData<MealWithFavoriteStatus>>
    fun getSnacksFromDatabase(resId : Int): Flow<PagingData<SnackWithFavoriteStatus>>
    fun getRestaurantOffersFromDatabase(resId : Int): Flow<List<OffersEntity>>
}