package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import kotlinx.coroutines.flow.StateFlow

interface FavoriteRepository {
    // *** ---------------------- \\***  Favorite Items  ***// ---------------------- ***
    val favoriteMeals : StateFlow<List<MealWithFavoriteStatus>>
    val favoriteSnacks : StateFlow<List<SnackWithFavoriteStatus>>
    val favoriteRestaurantsFromDatabase : StateFlow<List<RestaurantWithFavoriteStatus>>

    // *** ---------------------- \\***  Favorite Count  ***// ---------------------- ***
    val favoriteFoodCount : StateFlow<Int>
    val favoriteSnacksCount : StateFlow<Int>
    val favoriteRestaurantsCount : StateFlow<Int>
    val totalCountInFavorite : StateFlow<Int>


    // *** ---------------------- \\***  Favorite Functions  ***// ---------------------- ***
    suspend fun addFoodToFavorite(userId : String, foodItem : FavoriteMealEntity)
    suspend fun addSnackToFavorite(userId : String, snackItem : FavoriteSnackEntity)
    suspend fun addRestaurantToFavorite(userId : String, restaurantItem : FavoriteRestaurantEntity)
    suspend fun deleteFoodFromFavorite(userId : String, mealId : Int)
    suspend fun deleteSnackFromFavorite(userId : String, snackId : Int)
    suspend fun deleteRestaurantFromFavorite(userId : String, resId : Int)
    suspend fun addGuestFavoriteToUser(userId : String)
    suspend fun deleteAllFromFavorite()
}