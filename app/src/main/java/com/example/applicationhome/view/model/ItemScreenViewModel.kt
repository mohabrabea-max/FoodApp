package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.FoodItem

class ItemScreenViewModel : ViewModel() {
    var selectedItem by mutableStateOf<FoodItem?>(null)
    var selectedSize = mutableStateOf("Small")

    fun selectItem(item: FoodItem, size : String) {
        selectedItem = item
        selectedSize.value = size
    }
}