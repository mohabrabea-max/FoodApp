package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.CategoryType
import com.example.applicationhome.data.models.repository.MenuRepository

class CategoriesBoxViewModel(): ViewModel(){
    var selected by mutableStateOf(0)
    var typ by mutableStateOf("ALL")

    val filterMenu = if (typ == CategoryType.ALL.toString()) {
            MenuRepository.foodMenuList.values
        } else {
            MenuRepository.foodMenuList.values.filter { it.category == typ }
        }

    val filterrestaurants = if (typ == CategoryType.ALL.toString()) {
            MenuRepository.restaurantsMenu.values
        } else {
            MenuRepository.restaurantsMenu.values.filter { it.typ.toString().contains(typ) }
        }





    fun selected(item : Categories){
        selected = item.id
        typ = item.type
    }

    fun unSelected(){
        selected = 0
        typ = CategoryType.ALL.toString()
    }
}