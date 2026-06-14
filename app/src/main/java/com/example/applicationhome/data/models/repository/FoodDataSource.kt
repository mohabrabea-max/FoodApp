package com.example.applicationhome.data.models.repository

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentReturn
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.applicationhome.data.models.model.Account
import com.example.applicationhome.data.models.model.Options
import com.example.applicationhome.data.models.model.ProfileOptions
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.data.models.model.Settings
import com.example.applicationhome.data.models.model.TextFieldClassFromConfirmOrderScreen
import java.util.Calendar

object ProfileData {
    val days = (1..31).toList()
    val months = (1..12).toList()
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (1900..currentYear).toList()
    private val profile = listOf(
        Account(
            101,
            "Email",
            "mohabrabea@gmail.com",
            "mohabrabea@gmail.com",
            null
        ),
        Account(
            102,
            "First Name",
            "First Name",
            null,
            Icons.Default.Edit
        ),
        Account(
            103,
            "Last Name",
            "Last Name",
            null,
            Icons.Default.Edit
        ),
        Account(
            104,
            "Phone number",
            "01011223344",
            null,
            Icons.Default.Edit
        ),
        Account(
            105,
            "Birthday",
            "Get offers on your special day",
            null,
            Icons.Default.Add
        ),
        Account(
            106,
            "Country",
            "Egypt",
            null,
            Icons.Default.Add
        )
    )

    private val profileoptions = listOf(
        ProfileOptions(
            "Orders",
            "Manage & track",
            Icons.Default.ShoppingCartCheckout,
            Screens.LastOrdersScreen
        ),
        ProfileOptions(
            "Returns",
            "active requests",
            Icons.Default.AssignmentReturn,
            Screens.HomeScreen
        ),
        ProfileOptions(
            "Credit Cards",
            null,
            Icons.Default.CreditCard,
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
        Settings("My Addresses", Icons.Default.LocationOn),
        Settings("Payment Methods", Icons.Default.CreditCard),
        Settings("Notifications", Icons.Default.Notifications),
        Settings("Appearance", Icons.Default.SettingsBrightness),
        Settings("Language", Icons.Default.Language),
        Settings("Privacy & Security", Icons.Default.Lock),
        Settings("Help Center", Icons.Default.Call),
        Settings("FAQ", Icons.Default.Help),
        Settings("About App", Icons.Default.Info),
        Settings("Logout", Icons.Default.ExitToApp),
        Settings("Delete Account", Icons.Default.Delete)
    )
    fun profileData(): List<Account>{
        return profile
    }

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
        Options("Menu", Icons.Default.RestaurantMenu, Screens.Menu.screen),
        Options("Restaurants", Icons.Default.Restaurant, Screens.Restaurants.screen)

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

object ConfirmOrderScreenTextField{
    val phoneNumberState = TextFieldState()
    val houseState = TextFieldState()
    var housetextFieldState by mutableStateOf(false)
    val streetState = TextFieldState()
    var streettextFieldState by mutableStateOf(false)

    val additionalDirectionsState = TextFieldState()
    val addressLabelState = TextFieldState()

    val textFieldConfirmOrderScreenList1 = listOf(
        TextFieldClassFromConfirmOrderScreen(houseState, "House"),
        TextFieldClassFromConfirmOrderScreen(streetState, "Street"),
    )
    val textFieldConfirmOrderScreenList2 = listOf(
        TextFieldClassFromConfirmOrderScreen(additionalDirectionsState, "Additional directions (optional)"),
        TextFieldClassFromConfirmOrderScreen(addressLabelState, "Address label (optional)"),
    )
}