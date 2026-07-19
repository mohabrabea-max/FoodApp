package com.example.applicationhome.core.ui.theme.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){
    val state = MutableStateFlow(true)
    val sheetState = MutableStateFlow(false)

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
        state.value = true
        sheetState.value = true
    }
    fun stateFalse(){
        state.value = false
        sheetState.value = false
    }

    fun logout(){
        viewModelScope.launch {
            userRepository.logOut(userData.value.email)
            userRepository.isLogout()
        }
    }
}