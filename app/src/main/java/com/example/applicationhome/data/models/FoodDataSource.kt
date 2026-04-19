package com.example.applicationhome.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.graphics.Color
import com.example.applicationhome.R

object FoodDataSource {

    private val pizzamenu = listOf(
        FoodItem(1, "Big Pizza", 200.0, R.drawable.pezzapng, "B", "Pizza - Potato - Drink"),
        FoodItem(2, "Medium Pizza", 150.0, R.drawable.pezzapng, "M", "Pizza - Potato - Drink"),
        FoodItem(3, "Small Pizza", 100.0, R.drawable.pezzapng, "S", "Pizza - Potato - Drink"),
    )
    private val chickenmenu = listOf(
        FoodItem(4, "10 Pieces Of Chicken", 380.0, R.drawable.chickenpng, "10 Pieces", "10 Pieces Chicken - Potato - Drink"),
        FoodItem(5, "5 Pieces Of Chicken", 210.0, R.drawable.chickenpng, "5 Pieces", "5 Pieces Chicken - Potato - Drink"),
        FoodItem(6, "3 Pieces Of Chicken", 130.0, R.drawable.chickenpng, "3 Pieces", "3 Pieces Chicken - Potato - Drink")
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

object VarietiesMenu {
    private val categorieslist = listOf(
        CategoriesImage(1,"Burger", R.drawable.burgerpng),
        CategoriesImage(2,"Pizza", R.drawable.pezzapng),
        CategoriesImage(3,"Chicken", R.drawable.chickenpng)
    )
    fun categoriesList(): List<CategoriesImage>{
        return categorieslist
    }
}

object RestaurantsMenu {
    private val restaurants = listOf(
        Restaurants(1, "KFC", R.drawable.kfc, 4.5, Color.White),
        Restaurants(2, "McDonald's", R.drawable.mcdonalds, 4.5, Color.Red),
        Restaurants(3, "Bazooka", R.drawable.bazooka, 4.5, Color.Black),
        Restaurants(4, "Burger King", R.drawable.burgerking, 4.5, Color.White),
        Restaurants(5, "Pizza Hut", R.drawable.pizzahut, 4.5, Color.White),
        Restaurants(6, "B.Laban", R.drawable.belaban, 4.5, Color.White)
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
    fun profileData(): List<Account>{
        return profile
    }
}