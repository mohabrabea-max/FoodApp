package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class DrawerViewModel : ViewModel(){
    var state by mutableStateOf(true)
    var sheetState by mutableStateOf(false)

    fun stateTrue(){
        state = true
        sheetState = true
    }
    fun stateFalse(){
        state = false
        sheetState = false
    }
}