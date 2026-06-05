package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuList
import com.example.applicationhome.data.models.repository.MenuRepository.restaurantsMenu

class CategoriesBoxViewModel: ViewModel(){
    var selected by mutableStateOf(0)
    var selectedTypeIndex by mutableStateOf(0)
    var typ by mutableStateOf("All")
    var typeInRestaurantScreen by mutableStateOf("Pizza")
    val filterMenu : List<FoodItem>
        get() = foodMenuList.values.toList().filter { it.category == typeInRestaurantScreen }

    val filterrestaurants : List<Restaurants>
        get() = if(typ == "All"){
            restaurantsMenu.values.toList()
        }else{
            restaurantsMenu.values.toList().filter { it.typ.contains(typ) }
        }


    fun selected(item : Categories){
        selected = item.id
        typ = item.type
    }

    fun unSelected(){
        selected = 0
        typ = "All"
    }

    fun selectedtype(index : Int, type : String){
        selectedTypeIndex = index
        typeInRestaurantScreen = type
    }

}