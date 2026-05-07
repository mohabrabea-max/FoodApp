package com.example.applicationhome.view.model

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.Favorite
import com.example.applicationhome.data.models.Food
import com.example.applicationhome.data.models.Restaurants

class FavoriteViewModel : ViewModel(){
    var itemsCount = mutableStateMapOf<Int, Boolean>()
    var favoriteList = Favorite.favoritelist

    var itemsrestaurantsCount = mutableStateMapOf<Int, Boolean>()
    var favoriterestaurantsList = Favorite.favoriteRestaurantslist


    fun addFavorite(food :  Food){
        itemsCount[food.id] = true
        favoriteList.add(food)

    }
    fun removeFavorite(food :  Food){
        itemsCount[food.id] = false
        favoriteList.remove(food)
    }


    fun addRestaurantsFavorite(restaurants: Restaurants){
        itemsrestaurantsCount[restaurants.id] = true
        favoriterestaurantsList.add(restaurants)

    }
    fun removeRestaurantsFavorite(restaurants: Restaurants){
        itemsrestaurantsCount[restaurants.id] = false
        favoriterestaurantsList.remove(restaurants)
    }
}