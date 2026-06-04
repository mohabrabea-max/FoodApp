package com.example.applicationhome.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.Food
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.repository.FavoriteRepository.addToFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.deleteFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.favoritList
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
    fun removeFavorite(food : Food){
        viewModelScope.launch {
            deleteFavorite(food.id)
        }
    }

    fun addRestaurantsFavorite(restaurants: Restaurants){
        viewModelScope.launch {
            addToFavorite(restaurants.id, "Restaurant", restaurants.id)
        }
    }
    fun removeRestaurantsFavorite(restaurants: Restaurants){
        viewModelScope.launch {
            deleteFavorite(restaurants.id)
        }
    }
}