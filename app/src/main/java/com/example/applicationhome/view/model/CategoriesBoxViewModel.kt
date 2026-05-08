package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.CategoriesImage
import com.example.applicationhome.data.models.FoodDataSource
import com.example.applicationhome.data.models.RestaurantsMenu

class CategoriesBoxViewModel: ViewModel(){
    var selected by mutableStateOf(0)
    var typ by mutableStateOf("All")
    val menu = FoodDataSource.allMenu()
    var filterMenu by mutableStateOf(menu)
    val restaurantsMenu = RestaurantsMenu.restaurantsMenu()
    var restaurants by mutableStateOf(restaurantsMenu)
    fun selected(item : CategoriesImage){
        selected = item.id
        typ = item.name
        filterMenu = menu.filter { it.typ == typ }
        restaurants = restaurantsMenu.filter {it.typ.contains(typ)}
    }

    fun unSelected(){
        selected = 0
        typ = "All"
        filterMenu = menu
        restaurants = restaurantsMenu
    }
}