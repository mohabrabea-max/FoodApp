package com.example.applicationhome.core.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import com.example.applicationhome.data.data.model.Options
import com.example.applicationhome.data.data.model.ProfileOptions
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.data.model.Settings
import java.util.Calendar

object ProfileData {
    val days = (1..31).toList()
    val months = (1..12).toList()
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (1900..currentYear).toList()

    private val profileoptions = listOf(
        ProfileOptions(
            "Orders",
            "Manage & track",
            Icons.Default.ShoppingCartCheckout,
            Screens.LastOrdersScreen
        ),
        ProfileOptions(
            "Notifications",
            null,
            Icons.Default.Notifications,
            Screens.HomeScreen
        ),
        ProfileOptions(
            "Account Center",
            null,
            Icons.Default.Person,
            Screens.HomeScreen
        ),
        ProfileOptions(
            "Wishlist",
            "saved items",
            Icons.Default.FavoriteBorder,
            Screens.Favorite
        )
    )

    val settings = listOf(
        Settings("Language", Icons.Default.Language),
        Settings("Privacy & Security", Icons.Default.Lock),
        Settings("Help Center", Icons.Default.Call),
        Settings("About App", Icons.Default.Info),
        Settings("Logout", Icons.Default.ExitToApp),
        Settings("Delete Account", Icons.Default.Delete)
    )

    fun profileOptions(): List<ProfileOptions>{
        return profileoptions
    }

    fun settingsata(): List<Settings>{
        return settings
    }
}

object Drawer {
    private val options = listOf(
        Options("Home", Icons.Default.Home, Screens.HomeScreen.screen),
        Options("Profile", Icons.Default.Person, Screens.Profile.screen),
        Options("Settings", Icons.Default.Settings, Screens.Settings.screen)
    )
    private val menuOptions = listOf(
        Options("Menu", Icons.Default.RestaurantMenu, Screens.RestaurantScreen.screen)
    )
    fun optionsData(): List<Options>{
        return options
    }
    fun menuOptionsData(): List<Options>{
        return menuOptions
    }
}

object TapRowData {
    val FavoriteTapRow = listOf("Meals", "Snacks", "Restaurants")
}