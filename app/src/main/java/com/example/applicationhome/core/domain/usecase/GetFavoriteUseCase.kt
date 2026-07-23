package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import javax.inject.Inject

class GetFavoriteUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository : FavoriteRepository
){
    suspend fun addMealFavorite(food : FavoriteMealEntity){
        favoriteRepository.addFoodToFavorite(food.copy(userId = userRepository.userData.value.id))
    }

    suspend fun addSnackFavorite(snack : FavoriteSnackEntity){
        favoriteRepository.addSnackToFavorite(snack.copy(userId = userRepository.userData.value.id))
    }

    suspend fun addRestaurantsFavorite(restaurants: FavoriteRestaurantEntity){
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