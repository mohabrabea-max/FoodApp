package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.CategoriesImage

class CategoriesBoxViewModel: ViewModel(){
    var selectedItem by mutableStateOf(false)
    var selected by mutableStateOf(0)
    fun selected(item : CategoriesImage){
        selected = item.id
        selectedItem = true
    }
}