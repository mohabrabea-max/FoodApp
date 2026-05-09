package com.example.applicationhome.data.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface Food{val id: Int}
    data class FoodItem(
        override val id : Int,
        val typ : String,
        val name : String,
        val image : List<Int>,
        val sizeOptions: List<MealSizeDetail>,
        val review : Double
    ): Food

    data class Snake(
        override val id : Int,
        val name : String,
        var image : Int,
        val priceANDsize : Map<String, Double>,
        val review : Double
    ): Food

data class MealSizeDetail(
    val size : String,
    val price : Double,
    val snack : Map<Int, String>
)
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
    val typ : List<String>,
    val name : String,
    val image : Int,
    val image2 : Int,
    val review : Double,
    val background : Color
)

data class Options(
    val title : String,
    val icon : ImageVector,
    val screen: String
)

data class Account(
    val id : Int,
    val title : String,
    val empty : String,
    var value: Any?,
    val icon : ImageVector?
)

data class Settings(
    val title : String,
    val icon : ImageVector
)

data class ProfileOptions(
    val title : String,
    var description : String?,
    val icon: ImageVector,
    val screen : Screens
)

data class CartKey(val food : Food, val size : String)







data class BottomBar(
    val title : String,
    val icon : ImageVector,
    val screens : String
)
