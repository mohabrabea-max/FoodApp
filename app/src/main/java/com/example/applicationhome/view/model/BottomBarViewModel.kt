package com.example.applicationhome.view.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BottomBarViewModel : ViewModel() {
    var selected = mutableStateOf("Home")

    fun home(){
        selected.value = "Home"
    }
    fun favorit(){
        selected.value = "Favorite"
    }
    fun cart(){
        selected.value = "Cart"
    }
    fun profile(){
        selected.value = "Profile"
    }
}