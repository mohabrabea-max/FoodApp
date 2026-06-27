package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CategoriesBoxViewModel: ViewModel(){
    var selectedCategorieInFavoriteScreen by mutableIntStateOf(0)
    fun selectedFavoriteScreen(index: Int){
        selectedCategorieInFavoriteScreen = index
    }
}