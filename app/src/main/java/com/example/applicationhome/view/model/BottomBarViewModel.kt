package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BottomBarViewModel : ViewModel() {
    var selected by mutableStateOf("Home")
    fun home(){
        selected = "Home"
    }
    fun favorite(){
        selected = "Favorite"
    }
    fun cart(){
        selected = "Cart"
    }
    fun settings(){
        selected = "Settings"
    }
}