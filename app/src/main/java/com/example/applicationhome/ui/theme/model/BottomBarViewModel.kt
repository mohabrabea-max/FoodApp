package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.domain.GetCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor(private val getCartUseCase: GetCartUseCase) : ViewModel() {
    val totalNumber = getCartUseCase.totalNumber
    var selected by mutableStateOf("Home")
    fun home(){
        selected = "Home"
    }
    fun favorite(){
        selected = "Favorite"
    }
    fun settings(){
        selected = "Settings"
    }
}