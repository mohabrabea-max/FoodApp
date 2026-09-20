package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.core.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.core.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.core.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.core.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.core.domain.model.MealDomain
import com.example.applicationhome.core.domain.model.SnackDomain
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    // *** ---------------------- \\***  Favorite Items  ***// ---------------------- ***
    fun getFavoriteMeals(userId : String) : Flow<List<MealDomain>>
    fun getFavoriteSnacks(userId : String) : Flow<List<SnackDomain>>
    fun favoriteRestaurantsFromDatabase(userId : String) : Flow<List<RestaurantWithFavoriteStatus>>

    // *** ---------------------- \\***  Favorite Count  ***// ---------------------- ***
    fun favoriteFoodCount(userId : String) : Flow<Int>
    fun favoriteSnacksCount(userId : String) : Flow<Int>
    fun favoriteRestaurantsCount(userId : String) : Flow<Int>
    fun totalCountInFavorite(userId : String) : Flow<Int>


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