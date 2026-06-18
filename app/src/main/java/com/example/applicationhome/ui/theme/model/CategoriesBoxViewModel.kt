package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuList
import com.example.applicationhome.data.models.repository.MenuRepository.restaurantsMenu

class CategoriesBoxViewModel: ViewModel(){
    var selectedCategorieInFavoriteScreen by mutableIntStateOf(0)
    var selected by mutableStateOf(0)
    var selectedTypeIndex by mutableStateOf(0)
    var typ by mutableStateOf("All")
    var typeInRestaurantScreen by mutableStateOf("Pizza")
    val filterMenu get() = foodMenuList.filter { it.value.category == typeInRestaurantScreen }

    val filterrestaurants get() =
        if(typ == "All"){
            restaurantsMenu
        }else{
            restaurantsMenu.filter { it.value.typ.contains(typ) }
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

    fun selectedFavoriteScreen(index: Int){
        selectedCategorieInFavoriteScreen = index
    }

}