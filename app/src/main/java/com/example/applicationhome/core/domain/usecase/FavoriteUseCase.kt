package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository : FavoriteRepository
){
    suspend fun addMealFavorite(food : FavoriteMealEntity){
        favoriteRepository.addFoodToFavorite(
            userId = food.userId,
            foodItem = food.copy(userId = userRepository.userData.value.id)
        )
    }

    suspend fun addSnackFavorite(snack : FavoriteSnackEntity){
        favoriteRepository.addSnackToFavorite(
            userId = snack.userId,
            snackItem = snack.copy(userId = userRepository.userData.value.id)
        )
    }

    suspend fun addRestaurantsFavorite(restaurants : FavoriteRestaurantEntity){
        favoriteRepository.addRestaurantToFavorite(
            userId = restaurants.userId,
            restaurantItem = restaurants.copy(userId = userRepository.userData.value.id)
        )
    }

    suspend fun removeMealFavorite(mealId : Int){
        favoriteRepository.deleteFoodFromFavorite(
            userId = userRepository.userData.value.id,
            mealId = mealId
        )
    }

    suspend fun removeSnackFavorite(snackId : Int){
        favoriteRepository.deleteSnackFromFavorite(
            userId = userRepository.userData.value.id,
            snackId = snackId
        )
    }

    suspend fun removeRestaurantsFavorite(resId : Int){
        favoriteRepository.deleteRestaurantFromFavorite(
            userId = userRepository.userData.value.id,
            resId = resId
        )
    }
}