package com.example.applicationhome.core.ui.theme.model

import androidx.lifecycle.ViewModel
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor(
    cartRepository: CartRepository,
    favoriteRepository: FavoriteRepository
) : ViewModel() {

    val totalInFavorite = favoriteRepository.totalCountInFavorite


    val totalNumberInCart = cartRepository.totalNumber


    val selected = MutableStateFlow("Home")

    fun home(){
        selected.value = "Home"
    }
    fun favorite(){
        selected.value = "Favorite"
    }
    fun settings(){
        selected.value = "Settings"
    }
}