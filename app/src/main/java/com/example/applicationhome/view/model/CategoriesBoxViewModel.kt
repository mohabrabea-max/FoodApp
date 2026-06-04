package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuList
import com.example.applicationhome.data.models.repository.MenuRepository.restaurantsMenu

class CategoriesBoxViewModel: ViewModel(){
    var selected by mutableStateOf(0)
    var selectedTypeIndex by mutableStateOf(0)
    var typ by mutableStateOf("ALL")
    var typeInRestaurantScreen by mutableStateOf("ALL")
    var filterMenu by mutableStateOf(foodMenuList.values.toList())

    var filterrestaurants by mutableStateOf(restaurantsMenu.values.toList())


    fun selected(item : Categories){
        selected = item.id
        typ = item.type
        filterMenu = foodMenuList.values.toList().filter { it.category == typ }
        filterrestaurants = restaurantsMenu.values.toList().filter { it.typ.toString().contains(typ) }
    }

    fun unSelected(){
        selected = 0
        typ = "ALL"
        filterMenu = foodMenuList.values.toList()
        filterrestaurants = restaurantsMenu.values.toList()
    }

    fun selectedtype(index : Int, type : String){
        selectedTypeIndex = index
        typeInRestaurantScreen = type
        typ = type
        filterMenu = foodMenuList.values.toList().filter { it.category == type }
    }

}