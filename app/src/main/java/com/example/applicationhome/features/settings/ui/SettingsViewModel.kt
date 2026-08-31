package com.example.applicationhome.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.SettingsConfirmDialog
import com.example.applicationhome.data.data.model.ShowBottomSheets
import com.example.applicationhome.data.data.model.ThemeMode
import com.example.applicationhome.data.local.source.LanguageManager
import com.example.applicationhome.data.local.source.ThemeModeManager
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
    private val themeModeManager : ThemeModeManager
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
}