package com.example.applicationhome.ui.theme.model

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.NetworkObserver
import com.example.applicationhome.data.models.local.UserClass
import com.example.applicationhome.data.models.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository, application : Application) : AndroidViewModel(application) {
    val emailstate = TextFieldState()
    val passwordstate = TextFieldState()
    var isEmailTrue by mutableStateOf(true)
    var isPasswordTrue by mutableStateOf(true)
    private val networkObserver = NetworkObserver(application.applicationContext)
    var isNetworkAvailable by mutableStateOf(false)

    var isLogin by mutableStateOf(false)

    val userData : StateFlow<UserClass> =
        userRepository.getActiveUserFromDatabase()
            .map { userInDb ->
                userInDb ?: UserClass(firstname = "Guest")
            }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserClass(firstname = "Guest")
        )

    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable = available
            }
        }
    }


    fun bottonstate(){
        emailstate.clearText()
        passwordstate.clearText()
    }

    suspend fun getData(): String{
        val dataState = userRepository.setUserDataToDatabase(emailstate.text.toString(), passwordstate.text.toString())
        when(dataState){
            "Password is true" -> {
                isEmailTrue = true
                isPasswordTrue = true
            }
            "Password is false" -> {
                isEmailTrue = true
                isPasswordTrue = false
            }
            "Email is false" -> {
                isEmailTrue = false
                isPasswordTrue = true
            }
            "Network error" -> {
               println("Error")
            }
        }
        return dataState
    }

    fun logout(){
        viewModelScope.launch {
            isLogin = false
            userRepository.logOut(userData.value.email)
        }
    }

    fun login(){
        viewModelScope.launch {
            val result = getData()
            if(result == "Password is true"){
                isLogin = true
            }
        }
    }
}