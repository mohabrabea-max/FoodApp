package com.example.applicationhome.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import com.example.applicationhome.R

object FoodDataSource {
    private var pizzaimageList = mutableListOf<Int>(
        R.drawable.pezzapng,
        R.drawable.chickenpng,
        R.drawable.myphoto
    )
    private var chickenimageList = mutableListOf<Int>(
        R.drawable.chickenpng,
        R.drawable.pezzapng,
        R.drawable.myphoto
    )


    private val pizzamenu = listOf(
        FoodItem(
            1,
            "Peprony Pizza",
            pizzaimageList,
            mapOf(
                "Big" to 200.0,
                "Medium" to 150.0,
                "Small" to 100.0
            ),
            listOf(
                "Big Fries",
                "Big Coleslaw",
                "Mozzarella Sticks6"
            )
        ),
        FoodItem(
            2,
            "Chicken Pizza",
            pizzaimageList,
            mapOf(
                "Big" to 190.0,
                "Medium" to 140.0,
                "Small" to 90.0
            ),
            listOf(
                "Medium Fries",
                "Medium Coleslaw",
                "Mozzarella Sticks4"
            )
        ),
        FoodItem(
            3,
            "Beef Pizza",
            pizzaimageList,
            mapOf(
                "Big" to 210.0,
                "Medium" to 160.0,
                "Small" to 110.0
            ),
            listOf(
                "Small Fries",
                "Small Coleslaw",
                "Mozzarella Sticks2"
            )
        ),
    )


    private val chickenmenu = listOf(
        FoodItem(
            4,
            "10 Pieces Of Chicken",
            chickenimageList,
            mapOf("Small" to 380.0),
            listOf(
                "Big Fries",
                "Big Coleslaw",
                "Mozzarella Sticks6"
            )
        ),
        FoodItem(
            5,
            "5 Pieces Of Chicken",
            chickenimageList,
            mapOf("Small" to 210.0),
            listOf(
                "Medium Fries",
                "Medium Coleslaw",
                "Mozzarella Sticks4"
            )
        ),
        FoodItem(
            6,
            "3 Pieces Of Chicken",
            chickenimageList,
            mapOf("Small" to 130.0),
            listOf(
                "Small Fries",
                "Small Coleslaw",
                "Mozzarella Sticks2"
            )
        )
    )

    fun allMenu(): List<FoodItem>{
        return pizzamenu + chickenmenu
    }
    fun pizzaMenu(): List<FoodItem>{
        return pizzamenu
    }
    fun chickenMenu(): List<FoodItem>{
        return chickenmenu
    }

}

object Snakes {
    private var snakesMenu = mutableListOf(
        Snake(
            12,
            "Frinch Fries",
            R.drawable.frenchfries,
            mapOf(
                "Big" to 50.0,
                "Medium" to 40.0,
                "Small" to 30.0
            )
        ),
        Snake(
            13,
            "Coleslaw",
            R.drawable.coleslaw,
            mapOf(
                "Big" to 60.0,
                "Medium" to 45.0,
                "Small" to 30.0
            )
        ),
        Snake(
            14,
            "Mozzarella Sticks",
            R.drawable.mozzarellasticks,
            mapOf(
                "6 Pieces" to 70.0,
                "4 Pieces" to 50.0,
                "2 Pieces" to 30.0
            )
        )
    )
    private val snaksForItems = mapOf<String, Snake>(
        "Big Fries" to Snake(
            51,
            "Frinch Fries",
            R.drawable.frenchfries,
            mapOf("Big" to 50.0)),
        "Medium Fries" to Snake(
            52, "Frinch Fries",
            R.drawable.frenchfries,
            mapOf("Medium" to 40.0)),
        "Small Fries" to Snake(
            53,
            "Frinch Fries",
            R.drawable.frenchfries,
            mapOf("Small" to 30.0)),
        "Big Coleslaw" to Snake(
            54,
            "Coleslaw",
            R.drawable.coleslaw,
            mapOf("Big" to 60.0)),
        "Medium Coleslaw" to Snake(
            55,
            "Coleslaw",
            R.drawable.coleslaw,
            mapOf("Medium" to 45.0)),
        "Small Coleslaw" to Snake(
            56,
            "Coleslaw",
            R.drawable.coleslaw,
            mapOf("Small" to 30.0)),
        "Mozzarella Sticks6" to Snake(
            57,
            "Mozzarella Sticks",
            R.drawable.mozzarellasticks,
            mapOf("6 Pieces" to 70.0)),
        "Mozzarella Sticks4" to Snake(
            58,
            "Mozzarella Sticks",
            R.drawable.mozzarellasticks,
            mapOf("4 Pieces" to 50.0)),
        "Mozzarella Sticks2" to Snake(
            59,
            "Mozzarella Sticks",
            R.drawable.mozzarellasticks,
            mapOf("2 Pieces" to 30.0))


    )
    fun snakes(): List<Snake>{
        return snakesMenu
    }
    fun snakesItems(): Map<String, Snake>{
        return snaksForItems
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
        )
    )
    fun categoriesList(): List<CategoriesImage>{
        return categorieslist
    }
}

object RestaurantsMenu {
    private val restaurants = listOf(
        Restaurants(
            111,
            "KFC",
            R.drawable.kfc,
            4.5,
            Color.White
        ),
        Restaurants(
            222,
            "McDonald's",
            R.drawable.mcdonalds,
            4.5,
            Color.Red
        ),
        Restaurants(
            333,
            "Bazooka",
            R.drawable.bazooka,
            4.5,
            Color.Black
        ),
        Restaurants(
            444,
            "Burger King",
            R.drawable.burgerking,
            4.5,
            Color.White
        ),
        Restaurants(
            555,
            "Pizza Hut",
            R.drawable.pizzahut,
            4.5,
            Color.White
        ),
        Restaurants(
            666,
            "B.Laban",
            R.drawable.belaban,
            4.5,
            Color.White
        )
    )
    fun restaurantsMenu(): List<Restaurants>{
        return restaurants
    }
}


object ProfileData {
    private val profile = listOf(
        Account("Name","Mohab Rabea",Icons.Default.Person),
        Account("Phone Number","01011223344",Icons.Default.Phone),
        Account("Country","Egypt",Icons.Default.LocationOn)
    )

    val settings = listOf(
        Settings("Notifications", "Disabled", Icons.Default.Notifications),
        Settings("Language", "English", Icons.Default.AccountCircle),
        Settings("Country", "Egypt", Icons.Default.AccountCircle)
    )
    fun profileData(): List<Account>{
        return profile
    }

    fun settingsata(): List<Settings>{
        return settings
    }
}

object OffersData {
    private val offers = listOf(
        Offers(71, "Offer 1", R.drawable.ic_launcher_background),
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
    fun favoriteList(): List<Food>{
        return favoritelist
    }
}