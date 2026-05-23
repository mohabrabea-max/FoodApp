package com.example.applicationhome.view.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.UserClass
import com.example.applicationhome.data.models.repository.UserRepository
import com.example.applicationhome.data.models.repository.UserRepository.isLogin
import com.example.applicationhome.data.models.repository.UserRepository.userData
import com.example.applicationhome.data.models.repository.UserRepository.userId
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val emailstate = TextFieldState()
    val passwordstate = TextFieldState()
    var isEmailTrue by mutableStateOf(true)
    var isPasswordTrue by mutableStateOf(true)

    fun getDataInScreen(){
        getData(emailstate.text.toString(), passwordstate.text.toString())
    }

    fun bottonstate(){
        emailstate.clearText()
        passwordstate.clearText()
    }

    fun getData(emailstate : String, passwordstate : String?){
        viewModelScope.launch {
            val dataState = UserRepository.getUserData(emailstate, passwordstate)
            when(dataState){
                "Email is true" -> {isEmailTrue = true}
                "Email is false" -> {isEmailTrue = false}
                "Password is true" -> {isPasswordTrue = true}
                "Password is false" -> {isPasswordTrue = false}
            }
        }
    }

    fun logout(){
        isLogin = false
        userData = UserClass(
            "Guest",
            null,
            null,
            null,
            null,
            null
        )
    }

    fun login(userdata : UserClass, userid : String){
        userData = userdata
        userId = userid
        isLogin = true
    }
}