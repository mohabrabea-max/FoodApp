package com.example.applicationhome.ui.theme.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.Food
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.repository.FavoriteRepository.addToFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.deleteFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.favoritList
import com.example.applicationhome.data.models.repository.FavoriteRepository.favoriteMeals
import com.example.applicationhome.data.models.repository.FavoriteRepository.favoriteRestaurants
import com.example.applicationhome.data.models.repository.FavoriteRepository.favoriteSnacks
import com.example.applicationhome.data.models.repository.FavoriteRepository.mealsFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.restaurantsFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.snacksFavorite
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel(){

    fun isMealInFavorite(foodId : Int?): Boolean{
        return favoritList.any{ it.value?.id == foodId }
    }

    fun addFavorite(food : Food){
        viewModelScope.launch {
            when(food){
                is FoodItem -> {
                    addToFavorite(food.id, "Meal", food.restaurantId)
                }
                is Snack -> {
                    addToFavorite(food.id, "Snack", food.restaurantId)
                }
            }

        }
    }
    fun removeFavorite(food : Food, type : String){
        viewModelScope.launch {
            deleteFavorite(food.id, type)
        }
    }

    fun addRestaurantsFavorite(restaurants: Restaurants){
        viewModelScope.launch {
            addToFavorite(restaurants.id, "Restaurant", restaurants.id)
        }
    }
    fun removeRestaurantsFavorite(restaurants: Restaurants){
        viewModelScope.launch {
            deleteFavorite(restaurants.id, "Restaurant")
        }
    }
    fun getFavorite(){
        viewModelScope.launch {
            mealsFavorite += async { favoriteMeals() }.await()
            snacksFavorite += async { favoriteSnacks() }.await()
            restaurantsFavorite += async { favoriteRestaurants() }.await()
        }
    }
}