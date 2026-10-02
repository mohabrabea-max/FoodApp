package com.example.applicationhome.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.data.local.source.LanguageManager
import com.example.applicationhome.core.data.local.source.ThemeModeManager
import com.example.applicationhome.core.domain.model.SettingsConfirmDialog
import com.example.applicationhome.core.domain.model.ShowBottomSheets
import com.example.applicationhome.core.domain.model.ThemeMode
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.DeleteAccountUseCase
import com.example.applicationhome.core.ui.model.UiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository : UserRepository,
    private val favoriteRepository : FavoriteRepository,
    private val languageManager : LanguageManager,
    private val themeModeManager : ThemeModeManager,
    private val deleteAccountUseCase : DeleteAccountUseCase
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

    fun confirmPassword(){
        _confirmLogoutDialog.value = SettingsConfirmDialog.ConfirmPassword
    }

    fun closeDialog(){
        _confirmLogoutDialog.value = SettingsConfirmDialog.None
        _incorrectPassword.value = false
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



    private val _showBottomSheets = MutableStateFlow<ShowBottomSheets>(ShowBottomSheets.None)
    val showBottomSheets = _showBottomSheets.asStateFlow()


    fun showLanguageBottomSheet(){
        _showBottomSheets.value = ShowBottomSheets.Language
    }

    fun showDarkModeBottomSheet(){
        _showBottomSheets.value = ShowBottomSheets.DarkMode
    }

    fun closeBottomSheet(){
        _showBottomSheets.value = ShowBottomSheets.None
    }



    val currentThemeMode =
        themeModeManager.getCurrentThemeMode()
            .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeMode.SYSTEM
        )


    fun updateAppTheme(mode: ThemeMode){
        viewModelScope.launch {
            themeModeManager.updateAppTheme(mode)
        }
    }


    private val _uiStates = MutableStateFlow<UiStates>(UiStates.Success)
    val uiStates = _uiStates.asStateFlow()

    private val _incorrectPassword = MutableStateFlow(false)
    val incorrectPassword = _incorrectPassword.asStateFlow()
    fun deleteAccount(password : String){
        if(_uiStates.value == UiStates.Loading) return

        viewModelScope.launch {
            _uiStates.value = UiStates.Loading

            val email = userData.value.email

            deleteAccountUseCase(
                email = email,
                password = password
            ).onSuccess {
                closeDialog()
            }.onFailure {
                _incorrectPassword.value = true
            }

            _uiStates.value = UiStates.Success
        }
    }
}