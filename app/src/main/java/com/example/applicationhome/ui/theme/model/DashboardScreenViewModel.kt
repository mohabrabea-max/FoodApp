package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){
    var state by mutableStateOf(true)
    var sheetState by mutableStateOf(false)

    val isLogin = userRepository.isLogin

    val userData = userRepository.userData

    init {
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

    fun stateTrue(){
        state = true
        sheetState = true
    }
    fun stateFalse(){
        state = false
        sheetState = false
    }

    fun logout(){
        viewModelScope.launch {
            userRepository.logOut(userData.value.email)
            userRepository.isLogout()
        }
    }
}