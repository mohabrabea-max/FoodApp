package com.example.applicationhome.data.data.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.applicationhome.R

data class Options(
    @StringRes val title : Int,
    val icon : ImageVector,
    val screen: String
)

data class Settings(
    @StringRes val title : Int,
    val icon : ImageVector,
    val option : SettingsScreens
)

sealed interface SettingsScreens {
    data object DarkMode : SettingsScreens
    data object Language : SettingsScreens
    data object AboutApp : SettingsScreens
    data object LogoIn : SettingsScreens
    data object Logout : SettingsScreens
    data object DeleteAccount : SettingsScreens
}

enum class AppLanguage(
    val code: String,
    val titleRes: String
){
    ARABIC(code = "ar", titleRes = "العربية"),
    ENGLISH(code = "en", titleRes = "English")
}

enum class ThemeMode(
    @StringRes val titleRes : Int
){
    SYSTEM(titleRes = R.string.device_settings),
    DARK(titleRes = R.string.on),
    LIGHT(titleRes = R.string.off)
}

sealed interface ShowBottomSheets {
    data object None : ShowBottomSheets
    data object Language : ShowBottomSheets
    data object DarkMode : ShowBottomSheets
}

sealed interface SettingsConfirmDialog{
    data object None : SettingsConfirmDialog

    data class ConfirmLogout(
        @StringRes val message : Int =
            R.string.are_you_sure_you_want_to_logout
    ) : SettingsConfirmDialog

    data class ConfirmDeleteAccount(
        @StringRes val message : Int =
            R.string.are_you_sure_you_want_to_delete_your_account
    ) : SettingsConfirmDialog
}