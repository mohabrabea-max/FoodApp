package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack

class ItemScreenViewModel : ViewModel() {
    var selectedSnak by mutableStateOf<Snack?>(null)
    var selectedSnackSize by mutableStateOf("Small")
    var selectedItem by mutableStateOf<FoodItem?>(null)
    var selectedSize by mutableStateOf("Small")
    var selectedRestaurant by mutableStateOf<Restaurants?>(null)


    fun selectItem(item: FoodItem, size : String) {
        selectedItem = item
        selectedSize = size
    }
    fun selectSnak(item: Snack, size : String){
        selectedSnak = item
        selectedSnackSize = size
    }
    fun selectRestaurant(item : Restaurants){
        selectedRestaurant = item
    }
}