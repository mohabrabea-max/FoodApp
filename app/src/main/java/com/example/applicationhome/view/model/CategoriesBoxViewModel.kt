package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.CategoryType
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants

class CategoriesBoxViewModel(private val apiData: APIData): ViewModel(){
    var selected by mutableStateOf(0)
    var typ by mutableStateOf(CategoryType.ALL)

    val filterMenu: List<FoodItem> // (تأكد من اسم كلاس الوجبة عندك بدل FoodItem)
        get() = if (typ == CategoryType.ALL) {
            apiData.foodMenuList
        } else {
            apiData.foodMenuList.filter { it.typ == typ }
        }

    val filterrestaurants: List<Restaurants> // (تأكد من اسم كلاس المطعم عندك)
        get() = if (typ == CategoryType.ALL) {
            apiData.restaurantsMenu
        } else {
            apiData.restaurantsMenu.filter { it.typ.contains(typ) }
        }





    fun selected(item : Categories, apiData : APIData){
        selected = item.id
        typ = item.typ
    }

    fun unSelected(apiData : APIData){
        selected = 0
        typ = CategoryType.ALL
    }
}