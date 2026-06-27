package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ViewRestaurantImageViewModel : ViewModel() {
    var viewImageState by mutableStateOf(false)
        private set
    var image by mutableStateOf("")
        private set

    fun view(resImage : String){
        image = resImage
        viewImageState = true
    }

    fun unView(){
        viewImageState = false
        image = ""
    }
}