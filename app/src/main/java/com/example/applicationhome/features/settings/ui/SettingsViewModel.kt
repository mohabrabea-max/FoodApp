package com.example.applicationhome.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository : FavoriteRepository
): ViewModel() {
    val userData = userRepository.userData
    val isLogin = userRepository.isLogin


    fun logout(){
        viewModelScope.launch {
            favoriteRepository.deleteAllFromFavorite()
            userRepository.logOut()
            userRepository.logout()
        }
    }
}