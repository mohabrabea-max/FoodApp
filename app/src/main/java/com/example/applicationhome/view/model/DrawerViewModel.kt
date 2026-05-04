package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class DrawerViewModel : ViewModel(){
    var state by mutableStateOf(true)

    fun stateTrue(){
        state = true
    }
    fun stateFalse(){
        state = false
    }
}