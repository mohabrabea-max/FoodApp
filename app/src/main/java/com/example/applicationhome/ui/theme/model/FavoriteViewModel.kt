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
import com.example.applicationhome.data.models.repository.FavoriteRepository.mealsFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.restaurantsFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.snacksFavorite
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel(){

    fun isMealInFavorite(foodId : Int?): Boolean{
        return favoritList.any{ it.value?.id == foodId }
    }

    fun addFavorite(food : Food){
        viewModelScope.launch {
            when(food){
                is FoodItem -> {
                    val mealKey = "Meal_${food.id}"
                    addToFavorite(food.id, "Meal", food.restaurantId)
                    if(mealsFavorite.none { it.value.id == food.id }){
                        mealsFavorite += (mealKey to food)
                    }
                }
                is Snack -> {
                    val snackKey = "Snack_${food.id}"
                    addToFavorite(food.id, "Snack", food.restaurantId)
                    if(snacksFavorite.none { it.value.id == food.id }){
                        snacksFavorite += (snackKey to food)
                    }
                }
            }
        }
    }
    fun addRestaurantsFavorite(restaurants: Restaurants){
        viewModelScope.launch {
            val resKey = "Restaurant_${restaurants.id}"
            addToFavorite(restaurants.id, "Restaurant", restaurants.id)
            if(snacksFavorite.none { it.value.id == restaurants.id }){
                restaurantsFavorite += (resKey to restaurants)
            }
        }
    }

    fun removeFavorite(food : Food, type : String){
        viewModelScope.launch {
            deleteFavorite(food.id, type)
            when(food){
                is FoodItem -> {
                    val mealKey = "Meal_${food.id}"
                    mealsFavorite.remove(mealKey)
                }
                is Snack -> {
                    val snackKey = "Snack_${food.id}"
                    snacksFavorite.remove(snackKey)
                }
            }
        }
    }
    fun removeRestaurantsFavorite(restaurants: Restaurants){
        viewModelScope.launch {
            val resKey = "Restaurant_${restaurants.id}"
            deleteFavorite(restaurants.id, "Restaurant")
            restaurantsFavorite.remove(resKey)
        }
    }
}