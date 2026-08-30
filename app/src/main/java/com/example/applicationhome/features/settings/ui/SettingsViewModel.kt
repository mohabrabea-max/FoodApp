package com.example.applicationhome.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.SettingsConfirmDialog
import com.example.applicationhome.data.local.source.LanguageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _confirmLogoutDialog = MutableStateFlow<SettingsConfirmDialog>(SettingsConfirmDialog.None)
    val confirmLogoutDialog = _confirmLogoutDialog.asStateFlow()


    fun confirmLogout(){
        _confirmLogoutDialog.value = SettingsConfirmDialog.ConfirmLogout()
    }

    fun confirmDeleteAccount(){
        _confirmLogoutDialog.value = SettingsConfirmDialog.ConfirmDeleteAccount()
    }

    fun closeDialog(){
        _confirmLogoutDialog.value = SettingsConfirmDialog.None
    }

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