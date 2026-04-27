package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.FoodItem

class ItemScreenViewModel : ViewModel() {
    var selectedItem by mutableStateOf<FoodItem?>(null)
    var selectedSize = mutableStateOf("Small")
    var selectedPieces = mutableStateOf("10 Pieces")

    fun selectItem(item: FoodItem) {
        selectedItem = item
    }
    fun bigSize(){
        selectedSize.value = "Big"
    }
    fun mediumSize(){
        selectedSize.value = "Medium"
    }
    fun smallSize(item: FoodItem){
        if(item.name.contains("Chicken")){   //هنا السطر دا بنسأل فيه لو الاسم فيه كلمةChicken
            selectedSize.value = item.name
        }else{
            selectedSize.value = "Small"
        }
    }
}