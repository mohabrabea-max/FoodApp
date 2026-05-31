package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.CategoryType
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.repository.MenuRepository

class CategoriesBoxViewModel(private val apiData: APIData): ViewModel(){
    var selected by mutableStateOf(0)
    var typ by mutableStateOf(CategoryType.ALL)

    val filterMenu: List<FoodItem> // (تأكد من اسم كلاس الوجبة عندك بدل FoodItem)
        get() = if (typ == CategoryType.ALL) {
            MenuRepository.foodMenuList
        } else {
            MenuRepository.foodMenuList.filter { it.category == typ }
        }

    val filterrestaurants: List<Restaurants> // (تأكد من اسم كلاس المطعم عندك)
        get() = if (typ == CategoryType.ALL) {
            MenuRepository.restaurantsMenu
        } else {
            MenuRepository.restaurantsMenu.filter { it.typ.contains(typ) }
        }





    fun selected(item : Categories){
        selected = item.id
        typ = item.typ
    }

    fun unSelected(){
        selected = 0
        typ = CategoryType.ALL
    }
}