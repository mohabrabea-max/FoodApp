package com.example.applicationhome.data.data.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

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