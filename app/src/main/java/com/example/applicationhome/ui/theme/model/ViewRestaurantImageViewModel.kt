package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewRestaurantImageViewModel @Inject constructor() : ViewModel() {
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