package com.example.applicationhome.core.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import com.example.applicationhome.R

object ProfileData {
    fun profileOptions() = listOf(
        ProfileOptions(
            R.string.my_orders,
            R.string.manage_track,
            Icons.Default.ShoppingCartCheckout,
            Screens.LastOrdersScreen.screen
        ),
        ProfileOptions(
            R.string.notifications,
            null,
            Icons.Default.Notifications,
            Screens.Notifications.screen
        ),
        ProfileOptions(
            R.string.edit_profile,
            null,
            Icons.Default.Person,
            Screens.Profile.screen
        ),
        ProfileOptions(
            R.string.addresses,
            R.string.your_addresses,
            Icons.Default.LocationOn,
            Screens.Locations.screen
        )
    )

    fun notificationsOptions() = listOf(
        ProfileOptions(
            R.string.login,
            null,
            Icons.AutoMirrored.Filled.Login,
            Screens.LoginScreen.screen
        ),
        ProfileOptions(
            R.string.notifications,
            null,
            Icons.Default.Notifications,
            Screens.Notifications.screen
        )
    )

    fun settings1() = listOf(
        Settings(R.string.dark_mode, Icons.Default.DarkMode, SettingsScreens.DarkMode),
        Settings(R.string.language, Icons.Default.Language, SettingsScreens.Language),
        Settings(R.string.about_app, Icons.Default.Info, SettingsScreens.AboutApp)
    )

    fun settings2() = listOf(
        Settings(R.string.logout, Icons.AutoMirrored.Filled.ExitToApp, SettingsScreens.Logout),
        Settings(R.string.delete_account, Icons.Default.Delete, SettingsScreens.DeleteAccount)
    )
}

object Drawer {
    fun optionsData1() = listOf(
        Options(R.string.search, Icons.Default.Search, Screens.Search.screen),
        Options(R.string.cart, Icons.Default.ShoppingCart, Screens.Cart.screen)
    )

    fun optionsData2() = listOf(
        Options(R.string.edit_profile, Icons.Default.Person, Screens.Profile.screen),
        Options(R.string.notifications, Icons.Default.Notifications, Screens.Notifications.screen),
    )
}

object TapRowData {
    fun FavoriteTapRow() = listOf(
        R.string.meals,
        R.string.snacks,
        R.string.restaurants
    )
}