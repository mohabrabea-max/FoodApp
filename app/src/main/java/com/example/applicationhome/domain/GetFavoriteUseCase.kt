package com.example.applicationhome.domain

import com.example.applicationhome.data.data.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.data.repository.FavoriteRepository
import com.example.applicationhome.data.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFavoriteUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository : FavoriteRepository,
    @ApplicationScope private val externalScope: CoroutineScope
){
    suspend fun addMealFavorite(food : FavoriteFoodDatabase){
        withContext(Dispatchers.IO){
            favoriteRepository.addFoodToFavorite(food.copy(userId = userRepository.userData.value.id))
        }
    }

    suspend fun addSnackFavorite(snack : FavoriteSnacksDatabase){
        withContext(Dispatchers.IO){
            favoriteRepository.addSnackToFavorite(snack.copy(userId = userRepository.userData.value.id))
        }
    }

    suspend fun addRestaurantsFavorite(restaurants: FavoriteRestaurantDatabase){
        withContext(Dispatchers.IO){
            favoriteRepository.addRestaurantToFavorite(restaurants.copy(userId = userRepository.userData.value.id))
        }
    }

    suspend fun removeMealFavorite(mealId : Int){
        withContext(Dispatchers.IO){
            favoriteRepository.deleteFoodFromFavorite(userRepository.userData.value.id, mealId)
        }
    }

    suspend fun removeSnackFavorite(snackId : Int){
        withContext(Dispatchers.IO){
            favoriteRepository.deleteSnackFromFavorite(userRepository.userData.value.id, snackId)
        }
    }

    suspend fun removeRestaurantsFavorite(resId : Int){
        withContext(Dispatchers.IO){
            favoriteRepository.deleteRestaurantFromFavorite(userRepository.userData.value.id, resId)
        }
    }

    fun isMealInFavorite(foodId : Int): Flow<Boolean> {
        return favoriteRepository.favoriteMeals.map { list ->
            list.any{ it.mealId == foodId }
        }
    }

    fun isSnackInFavorite(snackId : Int): Flow<Boolean> {
        return favoriteRepository.favoriteSnacks.map { list ->
            list.any{ it.snackId == snackId }
        }
    }

    fun isRestaurantInFavorite(resId : Int): Flow<Boolean> {
        return favoriteRepository.favoriteRestaurantsFromDatabase.map { list ->
            list.any{ it.restaurantId == resId }
        }
    }
}