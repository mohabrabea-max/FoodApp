package com.example.applicationhome.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.Food
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Type
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
            addToFavorite(food.id, Type.MEAL, "food.restaurants")
        }
    }
    fun removeFavorite(food : Food){
        viewModelScope.launch {
            deleteFavorite(food.id)
        }
    }

    fun addRestaurantsFavorite(restaurants: Restaurants){
        viewModelScope.launch {
            addToFavorite(restaurants.id, Type.RESTAURANTS, restaurants.name)
        }
    }
    fun removeRestaurantsFavorite(restaurants: Restaurants){
        viewModelScope.launch {
            deleteFavorite(restaurants.id)
        }
    }
}