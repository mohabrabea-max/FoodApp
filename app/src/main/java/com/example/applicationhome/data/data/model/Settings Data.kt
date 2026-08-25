package com.example.applicationhome.data.data.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Options(
    val title : String,
    val icon : ImageVector,
    val screen: String
)

data class Settings(
    val title : String,
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