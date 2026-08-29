package com.example.applicationhome.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.local.source.LanguageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository : UserRepository,
    private val favoriteRepository : FavoriteRepository,
    private val languageManager : LanguageManager
): ViewModel() {
    val userData = userRepository.userData
    val isLogin = userRepository.isLogin

    val currentLanguage : String
        get() = languageManager.getCurrentLanguage()


    fun logout(){
        viewModelScope.launch {
            favoriteRepository.deleteAllFromFavorite()
            userRepository.logOut()
        }
    }

    fun setAppLanguage(languageCode : String){
        languageManager.setAppLanguage(languageCode)
    }
}