package com.example.applicationhome.view.model

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class FavoriteViewModel : ViewModel(){
    var itemsCount = mutableStateMapOf<Int, Boolean>()
    fun addFavorite(id : Int){
        itemsCount[id] = true

    }
    fun removeFavorite(id : Int){
        itemsCount[id] = false
    }
}