package com.example.applicationhome.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentReturn
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FoodBank
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import com.example.applicationhome.R
import com.example.applicationhome.ui.theme.BrandBlue
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.Orange

object FoodDataSource {
    private var pizzaimageList = mutableListOf<Int>(
        R.drawable.pezzapng,
        R.drawable.pizzaicon,
        R.drawable.pezzapng
    )
    private var chickenimageList = mutableListOf<Int>(
        R.drawable.chickenpng,
        R.drawable.frenchfries,
        R.drawable.friedchickenicon
    )
    private var burgerimageList = mutableListOf<Int>(
        R.drawable.burgerpng,
        R.drawable.burgerpng,
        R.drawable.burgericon
    )


    private val foodmenu = listOf(
        FoodItem(
            1,
            "Pizza",
            "Peprony Pizza",
            pizzaimageList,
            listOf(
                MealSizeDetail("Big", 200.0, mapOf(
                    12 to "Big",
                    13 to "Big",
                    14 to "6 Pieces"
                )),
                MealSizeDetail("Medium", 150.0, mapOf(
                    12 to "Medium",
                    13 to "Medium",
                    14 to "4 Pieces"
                )),
                MealSizeDetail("Small", 100.0, mapOf(
                    12 to "Small",
                    13 to "Small",
                    14 to "2 Pieces"
                ))
            ),
            4.9
        ),
        FoodItem(
            2,
            "Pizza",
            "Chicken Pizza",
            pizzaimageList,
            listOf(
                MealSizeDetail("Big", 200.0, mapOf(
                    12 to "Big",
                    13 to "Big",
                    14 to "6 Pieces"
                )),
                MealSizeDetail("Medium", 150.0, mapOf(
                    12 to "Medium",
                    13 to "Medium",
                    14 to "4 Pieces"
                )),
                MealSizeDetail("Small", 110.0, mapOf(
                    12 to "Small",
                    13 to "Small",
                    14 to "2 Pieces"
                ))
            ),
            4.5
        ),
        FoodItem(
            3,
            "Pizza",
            "Beef Pizza",
            pizzaimageList,
            listOf(
                MealSizeDetail("Big", 200.0, mapOf(
                    12 to "Big",
                    13 to "Big",
                    14 to "6 Pieces"
                )),
                MealSizeDetail("Medium", 150.0, mapOf(
                    12 to "Medium",
                    13 to "Medium",
                    14 to "4 Pieces"
                )),
                MealSizeDetail("Small", 120.0, mapOf(
                    12 to "Small",
                    13 to "Small",
                    14 to "2 Pieces"
                ))
            ),
            4.7
        ),
        FoodItem(
            4,
            "Chicken",
            "10 Pieces Of Chicken",
            chickenimageList,
            listOf(
                MealSizeDetail("10 Pieces", 380.0, mapOf(
                    12 to "Big",
                    13 to "Big",
                    14 to "6 Pieces"
                ))
            ),
            4.5
        ),
        FoodItem(
            5,
            "Chicken",
            "5 Pieces Of Chicken",
            chickenimageList,
            listOf(
                MealSizeDetail("5 Pieces", 210.0, mapOf(
                    12 to "Big",
                    13 to "Big",
                    14 to "6 Pieces"
                ))
            ),
            4.2
        ),
        FoodItem(
            6,
            "Chicken",
            "3 Pieces Of Chicken",
            chickenimageList,
            listOf(
                MealSizeDetail("3 Pieces", 130.0, mapOf(
                    12 to "Big",
                    13 to "Big",
                    14 to "6 Pieces"
                ))
            ),
            5.0
        ),
        FoodItem(
            7,
            "Burger",
            "Beef Burger Cheez",
            burgerimageList,
            listOf(
                MealSizeDetail("Medium", 200.0, mapOf(
                    12 to "Medium",
                    13 to "Medium",
                    14 to "4 Pieces"
                )),
                MealSizeDetail("Small", 160.0, mapOf(
                    12 to "Small",
                    13 to "Small",
                    14 to "2 Pieces"
                ))
            ),
            4.5
        ),
        FoodItem(
            8,
            "Burger",
            "Chicken Burger Cheez",
            burgerimageList,
            listOf(
                MealSizeDetail("Medium", 200.0, mapOf(
                    12 to "Medium",
                    13 to "Medium",
                    14 to "4 Pieces"
                )),
                MealSizeDetail("Small", 170.0, mapOf(
                    12 to "Small",
                    13 to "Small",
                    14 to "2 Pieces"
                ))
            ),
            3.5
        ),
        FoodItem(
            9,
            "Burger",
            "Beef Burger",
            burgerimageList,
            listOf(
                MealSizeDetail("Medium", 200.0, mapOf(
                    12 to "Medium",
                    13 to "Medium",
                    14 to "4 Pieces"
                )),
                MealSizeDetail("Small", 180.0, mapOf(
                    12 to "Small",
                    13 to "Small",
                    14 to "2 Pieces"
                ))
            ),
            4.8
        ),
        FoodItem(
            10,
            "Burger",
            "Chicken Burger",
            burgerimageList,
            listOf(
                MealSizeDetail("Medium", 200.0, mapOf(
                    12 to "Medium",
                    13 to "Medium",
                    14 to "4 Pieces"
                )),
                MealSizeDetail("Small", 190.0, mapOf(
                    12 to "Small",
                    13 to "Small",
                    14 to "2 Pieces"
                ))
            ),
            4.1
        )
    )

    private val typsList = listOf(
        "Pizza",
        "Chicken",
        "Burger"
    )
    fun allMenu(): List<FoodItem>{
        return foodmenu
    }
    fun typslist(): List<String>{
        return typsList
    }
}

object Snakes {
    private val snakesMenu = mutableListOf(
        Snake(
            12,
            "French Fries",
            R.drawable.frenchfries,
            mapOf(
                "Big" to 50.0,
                "Medium" to 40.0,
                "Small" to 30.0
            ),
            4.1
        ),
        Snake(
            13,
            "Coleslaw",
            R.drawable.coleslaw,
            mapOf(
                "Big" to 60.0,
                "Medium" to 45.0,
                "Small" to 30.0
            ),
            4.1
        ),
        Snake(
            14,
            "Mozzarella Sticks",
            R.drawable.mozzarellasticks,
            mapOf(
                "6 Pieces" to 70.0,
                "4 Pieces" to 50.0,
                "2 Pieces" to 30.0
            ),
            4.1
        )
    )

    fun snakes(): List<Snake>{
        return snakesMenu
    }
}

object VarietiesMenu {
    private val categorieslist = listOf(
        CategoriesImage(
            11,
            "Burger",
            R.drawable.burgerpng,
            R.drawable.burgericon
        ),
        CategoriesImage(
            22,
            "Pizza",
            R.drawable.pezzapng,
            R.drawable.pizzaicon
        ),
        CategoriesImage(
            33,
            "Chicken",
            R.drawable.chickenpng,
            R.drawable.friedchickenicon
        ),
        CategoriesImage(
            33,
            "Chicken",
            R.drawable.chickenpng,
            R.drawable.friedchickenicon
        ),
        CategoriesImage(
            33,
            "Chicken",
            R.drawable.chickenpng,
            R.drawable.friedchickenicon
        ),
        CategoriesImage(
            33,
            "Chicken",
            R.drawable.chickenpng,
            R.drawable.friedchickenicon
        ),
        CategoriesImage(
            33,
            "Chicken",
            R.drawable.chickenpng,
            R.drawable.friedchickenicon
        ),
    )
    fun categoriesList(): List<CategoriesImage>{
        return categorieslist
    }
}

object RestaurantsMenu {
    private val restaurants = listOf(
        Restaurants(
            501,
            listOf("Chicken", "Burger"),
            "KFC",
            R.drawable.kfc,
            R.drawable.chickenpng,
            4.5,
            Color.Red
        ),
        Restaurants(
            502,
            listOf("Burger"),
            "McDonald's",
            R.drawable.mcdonalds,
            R.drawable.burgerpng,
            4.5,
            Color.Orange
        ),
        Restaurants(
            503,
            listOf("Chicken", "Burger"),
            "Bazooka",
            R.drawable.bazooka,
            R.drawable.chickenpng,
            4.5,
            Color.DarkOrange
        ),
        Restaurants(
            504,
            listOf("Burger"),
            "Burger King",
            R.drawable.burgerking,
            R.drawable.burgerpng,
            4.5,
            Color.Blue
        ),
        Restaurants(
            505,
            listOf("Pizza"),
            "Pizza Hut",
            R.drawable.pizzahut,
            R.drawable.pezzapng,
            4.5,
            Color.Red
        ),
        Restaurants(
            506,
            listOf("Sweet"),
            "B.Laban",
            R.drawable.belaban,
            R.drawable.belaban,
            4.5,
            Color.BrandBlue
        )
    )
    fun restaurantsMenu(): List<Restaurants>{
        return restaurants
    }
}


object ProfileData {
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
        Account(106,
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
            Screens.HomeScreen
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

object OffersData {
    private val offers = listOf(
        Offers(71, "Offer 1", R.drawable.offer1),
        Offers(72, "Offer 2", R.drawable.ic_launcher_background),
        Offers(73, "Offer 3", R.drawable.ic_launcher_background),
        Offers(74, "Offer 4", R.drawable.ic_launcher_background),
        Offers(75, "Offer 5", R.drawable.ic_launcher_background)
    )
    fun offersMenu(): List<Offers>{
        return offers
    }
}

object Cart {
    var cartmap = mutableStateMapOf<CartKey, Int>()
    fun cartMap(): Map<CartKey, Int>{
        return cartmap
    }
}

object Favorite {
    var favoritelist = mutableStateListOf<Food>()
    var favoriteRestaurantslist = mutableStateListOf<Restaurants>()

    fun favoriteList(): List<Food>{
        return favoritelist
    }

    fun favoriteRestaurantsList(): List<Restaurants>{
        return favoriteRestaurantslist
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
        Options("Varieties", Icons.Default.FoodBank, Screens.Varieties.screen),
        Options("Restaurants", Icons.Default.Restaurant, Screens.Restaurants.screen)

    )
    fun optionsData(): List<Options>{
        return options
    }
    fun menuOptionsData(): List<Options>{
        return menuOptions
    }
}