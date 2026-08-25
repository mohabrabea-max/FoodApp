package com.example.applicationhome.core.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import com.example.applicationhome.data.data.model.Options
import com.example.applicationhome.data.data.model.ProfileOptions
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.data.model.Settings
import com.example.applicationhome.data.data.model.SettingsScreens

object ProfileData {
    fun profileOptions() = listOf(
        ProfileOptions(
            "Orders",
            "Manage & track",
            Icons.Default.ShoppingCartCheckout,
            Screens.LastOrdersScreen.screen
        ),
        ProfileOptions(
            "Notifications",
            null,
            Icons.Default.Notifications,
            Screens.Notifications.screen
        ),
        ProfileOptions(
            "Account Center",
            null,
            Icons.Default.Person,
            Screens.Profile.screen
        ),
        ProfileOptions(
            "Wishlist",
            "saved items",
            Icons.Default.FavoriteBorder,
            Screens.Favorite.screen
        )
    )

    fun settings1() = listOf(
        Settings("Language", Icons.Default.Language, SettingsScreens.Language),
        Settings("About App", Icons.Default.Info, SettingsScreens.AboutApp),
    )

    fun settings2() = listOf(
        Settings("Logout", Icons.AutoMirrored.Filled.ExitToApp, SettingsScreens.Logout),
        Settings("Delete Account", Icons.Default.Delete, SettingsScreens.DeleteAccount)
    )
}

object Drawer {
    fun optionsData1() = listOf(
        Options("Search", Icons.Default.Search, Screens.Search.screen),
        Options("Cart", Icons.Default.ShoppingCart, Screens.Cart.screen)
    )

    fun optionsData2() = listOf(
        Options("Profile", Icons.Default.Person, Screens.Profile.screen),
        Options("Notifications", Icons.Default.Notifications, Screens.Notifications.screen),
    )
}

object TapRowData {
    val FavoriteTapRow = listOf("Meals", "Snacks", "Restaurants")
}