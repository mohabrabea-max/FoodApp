package com.example.applicationhome.core.ui.theme.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardScreenViewModel @Inject constructor(
    private val userRepository : UserRepository,
    private val favoriteRepository : FavoriteRepository,
    private val cartRepository : CartRepository
) : ViewModel(){
    val state = MutableStateFlow(true)

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
                    userRepository.logout()
                }
            }
        }
    }

    fun stateTrue(){
        state.value = true
    }
    fun stateFalse(){
        state.value = false
    }

    fun logout(){
        viewModelScope.launch {
            favoriteRepository.deleteAllFromFavorite()
            userRepository.logOut(userData.value.email)
            userRepository.logout()
        }
    }
}