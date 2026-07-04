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
import com.example.applicationhome.data.models.local.entity.UserClass
import com.example.applicationhome.data.models.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository, application : Application) : AndroidViewModel(application) {
    val loading : StateFlow<Boolean> = userRepository.loading
    val emailstate = TextFieldState()
    val passwordstate = TextFieldState()
    private val networkObserver = NetworkObserver(application.applicationContext)
    var isNetworkAvailable by mutableStateOf(false)
    private val _isLogin = MutableStateFlow(false)
    var isLogin : StateFlow<Boolean> = _isLogin

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

        viewModelScope.launch {
            userRepository.getActiveUserFromDatabase().collect { currentUser ->
                if(currentUser != null && currentUser.id.isNotEmpty()){
                    _isLogin.value = true
                }else{
                    _isLogin.value = false
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
            _isLogin.value = false
        }
    }

    fun login(){
        viewModelScope.launch {
            val result = userRepository.setUserDataToDatabase(emailstate.text.toString(), passwordstate.text.toString())
            if(result == "Password is true"){
                _isLogin.value = true
            }else if(result == "Password is false"){
                println("Password is false")
            }else{
                println("Email is false")
            }
        }
    }
}