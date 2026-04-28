package com.example.applicationhome.data.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface Food{val id: Int}
    data class FoodItem(
        override val id : Int,
        val name : String,
        val image : List<Int>,
        val priceANDsize : Map<String, Double>,
        val description: List<String>
    ): Food

    data class Snake(
        override val id : Int,
        val name : String,
        var image : Int,
        val priceANDsize : Map<String, Double>
    ): Food

data class CategoriesImage(
    val id : Int,
    val name : String,
    val image : Int,
    val icon : Int
)

data class Offers(
    val id : Int,
    val name : String,
    val image : Int
)

data class Restaurants(
    val id : Int,
    val name : String,
    val image : Int,
    val review : Double,
    val background : Color
)

data class Options(
    val title : String,
    val icon : ImageVector,
    val screen: String
)

data class Account(
    val title : String,
    val value: String,
    val icon : ImageVector
)

data class Settings(
    val title : String,
    val value: String,
    val icon : ImageVector
)

data class CartKey(val food : Food, val size : String)







data class BottomBar(
    val title : String,
    val icon : ImageVector,
    val screens : String
)
