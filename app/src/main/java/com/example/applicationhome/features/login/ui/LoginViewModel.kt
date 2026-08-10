package com.example.applicationhome.features.login.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.SearchRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val searchRepository: SearchRepository,
    networkObserver: NetworkObserver
) : ViewModel() {
    val loading : StateFlow<Boolean> = userRepository.loading
    val emailstate = TextFieldState()
    val passwordstate = TextFieldState()

    val isNetworkAvailable = networkObserver.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val isLogin = userRepository.isLogin

    val userData = userRepository.userData

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


    fun logout(){
        viewModelScope.launch {
            userRepository.logOut(userData.value.email)
            userRepository.logout()
        }
    }

    fun login(){
        viewModelScope.launch {
            val result = userRepository.setUserDataToDatabase(emailstate.text.toString(), passwordstate.text.toString())

            when (result.first) {
                "Password is true" -> {
                    userRepository.login()

                    favoriteRepository.addGuestFavoriteToUser(result.second.id)

                    searchRepository.addGuestSearchHistoryToUser(result.second.id)
                }
                "Password is false" -> {
                    println("Password is false")
                }
                else -> {
                    println("Email is false")
                }
            }
        }
    }
}