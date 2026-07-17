package com.example.applicationhome.domain

import com.example.applicationhome.data.data.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.data.repository.FavoriteRepository
import com.example.applicationhome.data.data.repository.UserRepository
import javax.inject.Inject

class GetFavoriteUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository : FavoriteRepository
){
    suspend fun addMealFavorite(food : FavoriteFoodDatabase){
        favoriteRepository.addFoodToFavorite(food.copy(userId = userRepository.userData.value.id))
    }

    suspend fun addSnackFavorite(snack : FavoriteSnacksDatabase){
        favoriteRepository.addSnackToFavorite(snack.copy(userId = userRepository.userData.value.id))
    }

    suspend fun addRestaurantsFavorite(restaurants: FavoriteRestaurantDatabase){
        favoriteRepository.addRestaurantToFavorite(restaurants.copy(userId = userRepository.userData.value.id))
    }

    suspend fun removeMealFavorite(mealId : Int){
        favoriteRepository.deleteFoodFromFavorite(userRepository.userData.value.id, mealId)
    }

    suspend fun removeSnackFavorite(snackId : Int){
        favoriteRepository.deleteSnackFromFavorite(userRepository.userData.value.id, snackId)
    }

    suspend fun removeRestaurantsFavorite(resId : Int){
        favoriteRepository.deleteRestaurantFromFavorite(userRepository.userData.value.id, resId)
    }
}