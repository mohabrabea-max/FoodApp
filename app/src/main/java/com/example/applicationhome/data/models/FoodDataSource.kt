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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance{
    private const val BASE_URL = "https://food-app-9d163-default-rtdb.firebaseio.com/"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api : FoodAppAPIs by lazy {
        retrofit.create(FoodAppAPIs::class.java)
    }
}



//object FoodDataSource {
//    private var pizzaimageList = mutableListOf(
//        R.drawable.pepronypizza,
//        R.drawable.mixpizza,
//        R.drawable.pepronypizza2
//    )
//    private var chickenimageList = mutableListOf(
//        R.drawable.chickenpng,
//        R.drawable.frenchfries,
//        R.drawable.friedchickenicon
//    )
//
//
//    private val foodmenu = listOf(
//        FoodItem(
//            1,
//            CategoryType.PIZZA,
//            "Peprony Pizza",
//            pizzaimageList,
//            listOf(
//                MealSizeDetail("Big", 200.0, mapOf(
//                    12 to "Big",
//                    13 to "Big",
//                    14 to "6 Pieces"
//                )),
//                MealSizeDetail("Medium", 150.0, mapOf(
//                    12 to "Medium",
//                    13 to "Medium",
//                    14 to "4 Pieces"
//                )),
//                MealSizeDetail("Small", 100.0, mapOf(
//                    12 to "Small",
//                    13 to "Small",
//                    14 to "2 Pieces"
//                ))
//            ),
//            4.9
//        ),
//        FoodItem(
//            2,
//            CategoryType.PIZZA,
//            "Chicken Pizza",
//            pizzaimageList,
//            listOf(
//                MealSizeDetail("Big", 200.0, mapOf(
//                    12 to "Big",
//                    13 to "Big",
//                    14 to "6 Pieces"
//                )),
//                MealSizeDetail("Medium", 150.0, mapOf(
//                    12 to "Medium",
//                    13 to "Medium",
//                    14 to "4 Pieces"
//                )),
//                MealSizeDetail("Small", 110.0, mapOf(
//                    12 to "Small",
//                    13 to "Small",
//                    14 to "2 Pieces"
//                ))
//            ),
//            4.5
//        ),
//        FoodItem(
//            3,
//            CategoryType.PIZZA,
//            "Beef Pizza",
//            pizzaimageList,
//            listOf(
//                MealSizeDetail("Big", 200.0, mapOf(
//                    12 to "Big",
//                    13 to "Big",
//                    14 to "6 Pieces"
//                )),
//                MealSizeDetail("Medium", 150.0, mapOf(
//                    12 to "Medium",
//                    13 to "Medium",
//                    14 to "4 Pieces"
//                )),
//                MealSizeDetail("Small", 120.0, mapOf(
//                    12 to "Small",
//                    13 to "Small",
//                    14 to "2 Pieces"
//                ))
//            ),
//            4.7
//        ),
//        FoodItem(
//            4,
//            CategoryType.CHICKEN,
//            "10 Pieces Of Chicken",
//            chickenimageList,
//            listOf(
//                MealSizeDetail("10 Pieces", 380.0, mapOf(
//                    12 to "Big",
//                    13 to "Big",
//                    14 to "6 Pieces"
//                ))
//            ),
//            4.5
//        ),
//        FoodItem(
//            5,
//            CategoryType.CHICKEN,
//            "5 Pieces Of Chicken",
//            chickenimageList,
//            listOf(
//                MealSizeDetail("5 Pieces", 210.0, mapOf(
//                    12 to "Big",
//                    13 to "Big",
//                    14 to "6 Pieces"
//                ))
//            ),
//            4.2
//        ),
//        FoodItem(
//            6,
//            CategoryType.CHICKEN,
//            "3 Pieces Of Chicken",
//            chickenimageList,
//            listOf(
//                MealSizeDetail("3 Pieces", 130.0, mapOf(
//                    12 to "Big",
//                    13 to "Big",
//                    14 to "6 Pieces"
//                ))
//            ),
//            5.0
//        ),
//        FoodItem(
//            7,
//            CategoryType.BURGER,
//            "Beef Burger Cheez",
//            listOf(
//                R.drawable.bigbeefburgercheez,
//                R.drawable.burgercheez
//            ),
//            listOf(
//                MealSizeDetail("Medium", 200.0, mapOf(
//                    12 to "Medium",
//                    13 to "Medium",
//                    14 to "4 Pieces"
//                )),
//                MealSizeDetail("Small", 160.0, mapOf(
//                    12 to "Small",
//                    13 to "Small",
//                    14 to "2 Pieces"
//                ))
//            ),
//            4.5
//        ),
//        FoodItem(
//            8,
//            CategoryType.BURGER,
//            "Chicken Burger Cheez",
//            listOf(R.drawable.chickenburger),
//            listOf(
//                MealSizeDetail("Medium", 200.0, mapOf(
//                    12 to "Medium",
//                    13 to "Medium",
//                    14 to "4 Pieces"
//                )),
//                MealSizeDetail("Small", 170.0, mapOf(
//                    12 to "Small",
//                    13 to "Small",
//                    14 to "2 Pieces"
//                ))
//            ),
//            3.5
//        ),
//        FoodItem(
//            9,
//            CategoryType.BURGER,
//            "Beef Burger",
//            listOf(
//                R.drawable.beefburger,
//                R.drawable.beefburger2
//            ),
//            listOf(
//                MealSizeDetail("Medium", 200.0, mapOf(
//                    12 to "Medium",
//                    13 to "Medium",
//                    14 to "4 Pieces"
//                )),
//                MealSizeDetail("Small", 180.0, mapOf(
//                    12 to "Small",
//                    13 to "Small",
//                    14 to "2 Pieces"
//                ))
//            ),
//            4.8
//        ),
//        FoodItem(
//            10,
//            CategoryType.BURGER,
//            "Chicken Burger",
//            listOf(R.drawable.chickenburger),
//            listOf(
//                MealSizeDetail("Medium", 200.0, mapOf(
//                    12 to "Medium",
//                    13 to "Medium",
//                    14 to "4 Pieces"
//                )),
//                MealSizeDetail("Small", 190.0, mapOf(
//                    12 to "Small",
//                    13 to "Small",
//                    14 to "2 Pieces"
//                ))
//            ),
//            4.1
//        )
//    )
//
//    private val typsList = listOf(
//        "Pizza",
//        "Chicken",
//        "Burger"
//    )
//    fun allMenu(): List<FoodItem>{
//        return foodmenu
//    }
//    fun typslist(): List<String>{
//        return typsList
//    }
//}

//object Snakes {
//    private val snakesMenu = mutableListOf(
//            Snack(
//            12,
//            "French Fries",
//            listOf(R.drawable.frenchfries),
//            mapOf(
//                "Big" to 50.0,
//                "Medium" to 40.0,
//                "Small" to 30.0
//            ),
//            4.1
//        ),
//        Snack(
//            13,
//            "Coleslaw",
//            listOf(R.drawable.coleslaw),
//            mapOf(
//                "Big" to 60.0,
//                "Medium" to 45.0,
//                "Small" to 30.0
//            ),
//            4.1
//        ),
//        Snack(
//            14,
//            "Mozzarella Sticks",
//            listOf(R.drawable.mozzarellasticks),
//            mapOf(
//                "6 Pieces" to 70.0,
//                "4 Pieces" to 50.0,
//                "2 Pieces" to 30.0
//            ),
//            4.1
//        )
//    )
//
//    fun snakes(): List<Snack>{
//        return snakesMenu
//    }
//}

//object VarietiesMenu {
//    private val categorieslist = listOf(
//        Categories(
//            11,
//            "Burger",
//            CategoryType.BURGER,
//            R.drawable.burgerpng,
//            R.drawable.burgericon
//        ),
//        Categories(
//            22,
//            "Pizza",
//            CategoryType.PIZZA,
//            R.drawable.pezzapng,
//            R.drawable.pizzaicon
//        ),
//        Categories(
//            33,
//            "Chicken",
//            CategoryType.CHICKEN,
//            R.drawable.chickenpng,
//            R.drawable.friedchickenicon
//        ),
//        Categories(
//            33,
//            "Chicken",
//            CategoryType.CHICKEN,
//            R.drawable.chickenpng,
//            R.drawable.friedchickenicon
//        ),
//        Categories(
//            33,
//            "Chicken",
//            CategoryType.CHICKEN,
//            R.drawable.chickenpng,
//            R.drawable.friedchickenicon
//        ),
//        Categories(
//            33,
//            "Chicken",
//            CategoryType.CHICKEN,
//            R.drawable.chickenpng,
//            R.drawable.friedchickenicon
//        ),
//        Categories(
//            33,
//            "Chicken",
//            CategoryType.CHICKEN,
//            R.drawable.chickenpng,
//            R.drawable.friedchickenicon
//        ),
//    )
//    fun categoriesList(): List<Categories>{
//        return categorieslist
//    }
//}

//object RestaurantsMenu {
//    private val restaurants = listOf(
//        Restaurants(
//            501,
//            listOf(CategoryType.CHICKEN, CategoryType.BURGER),
//            "KFC",
//            R.drawable.kfc,
//            R.drawable.chickenpng,
//            4.5,
//            Color.Red
//        ),
//        Restaurants(
//            502,
//            listOf(CategoryType.BURGER),
//            "McDonald's",
//            R.drawable.mcdonalds,
//            R.drawable.burgerpng,
//            4.5,
//            Color.Orange
//        ),
//        Restaurants(
//            503,
//            listOf(CategoryType.CHICKEN, CategoryType.BURGER),
//            "Bazooka",
//            R.drawable.bazooka,
//            R.drawable.chickenpng,
//            4.5,
//            Color.DarkOrange
//        ),
//        Restaurants(
//            504,
//            listOf(CategoryType.BURGER),
//            "Burger King",
//            R.drawable.burgerking,
//            R.drawable.burgerpng,
//            4.5,
//            Color.Blue
//        ),
//        Restaurants(
//            505,
//            listOf(CategoryType.PIZZA),
//            "Pizza Hut",
//            R.drawable.pizzahut,
//            R.drawable.pezzapng,
//            4.5,
//            Color.Red
//        ),
//        Restaurants(
//            506,
//            listOf(CategoryType.SWEET),
//            "B.Laban",
//            R.drawable.belaban,
//            R.drawable.belaban,
//            4.5,
//            Color.BrandBlue
//        )
//    )
//    fun restaurantsMenu(): List<Restaurants>{
//        return restaurants
//    }
//}


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

//object OffersData {
//    private val offers = listOf(
//        Offers(71, "Offer 1", R.drawable.offer1),
//        Offers(72, "Offer 2", R.drawable.ic_launcher_background),
//        Offers(73, "Offer 3", R.drawable.ic_launcher_background),
//        Offers(74, "Offer 4", R.drawable.ic_launcher_background),
//        Offers(75, "Offer 5", R.drawable.ic_launcher_background)
//    )
//    fun offersMenu(): List<Offers>{
//        return offers
//    }
//}

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
        Options("Restaurants", Icons.Default.Restaurant, Screens.Restaurants.screen)

    )
    fun optionsData(): List<Options>{
        return options
    }
    fun menuOptionsData(): List<Options>{
        return menuOptions
    }
}