package com.example.applicationhome.core.ui.components.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardScreenViewModel @Inject constructor(
    private val userRepository : UserRepository,
    private val favoriteRepository : FavoriteRepository,
    cartRepository : CartRepository
) : ViewModel(){
    private val _state = MutableStateFlow(true)
    val state = _state.asStateFlow()

    val isLogin = userRepository.isLogin

    val userData = userRepository.userData

    val totalInFavorite = favoriteRepository.totalCountInFavorite


    val totalNumberInCart = cartRepository.totalNumber

    init {
        viewModelScope.launch {
            userData.collect { currentUser ->
                if(currentUser.id.isNotEmpty()){
                    userRepository.login()
                }else{
                    userRepository.logOut()
                }
            }
        }
    }

    fun stateTrue(){
        _state.value = true
    }
    fun stateFalse(){
        _state.value = false
    }

    fun logout(){
        viewModelScope.launch {
            favoriteRepository.deleteAllFromFavorite()
            userRepository.logOut()
        }
    }
}