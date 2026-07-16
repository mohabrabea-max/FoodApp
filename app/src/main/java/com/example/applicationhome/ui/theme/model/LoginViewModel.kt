package com.example.applicationhome.ui.theme.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.remote.NetworkObserver
import com.example.applicationhome.data.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkObserver: NetworkObserver
) : ViewModel() {
    val loading : StateFlow<Boolean> = userRepository.loading
    val emailstate = TextFieldState()
    val passwordstate = TextFieldState()

    var isNetworkAvailable by mutableStateOf(false)

    val isLogin = userRepository.isLogin

    val userData = userRepository.userData

    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable = available
            }

        }

        viewModelScope.launch {
            userData.collect { currentUser ->
                if(currentUser.id.isNotEmpty()){
                    userRepository.isLogin()
                }else{
                    userRepository.isLogout()
                }
            }
        }
    }


    fun clearFields(){
        emailstate.clearText()
        passwordstate.clearText()
    }


    fun logout(){
        viewModelScope.launch {
            userRepository.logOut(userData.value.email)
            userRepository.isLogout()
        }
    }

    fun login(){
        viewModelScope.launch {
            val result = userRepository.setUserDataToDatabase(emailstate.text.toString(), passwordstate.text.toString())
            if(result == "Password is true"){
                userRepository.isLogin()
            }else if(result == "Password is false"){
                println("Password is false")
            }else{
                println("Email is false")
            }
        }
    }
}