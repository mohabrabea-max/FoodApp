package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.data.repository.CartRepository
import com.example.applicationhome.data.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor(
    cartRepository: CartRepository,
    favoriteRepository: FavoriteRepository
) : ViewModel() {

    val totalInFavorite = favoriteRepository.totalCountInFavorite


    val totalNumberInCart = cartRepository.totalNumber


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