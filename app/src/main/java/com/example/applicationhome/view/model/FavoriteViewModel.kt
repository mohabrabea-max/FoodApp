package com.example.applicationhome.view.model

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.Favorite
import com.example.applicationhome.data.models.Food

class FavoriteViewModel : ViewModel(){
    var itemsCount = mutableStateMapOf<Int, Boolean>()
    var favoriteList = Favorite.favoritelist
    fun addFavorite(food :  Food){
        itemsCount[food.id] = true
        favoriteList.add(food)

    }
    fun removeFavorite(food :  Food){
        itemsCount[food.id] = false
        favoriteList.remove(food)
    }
}