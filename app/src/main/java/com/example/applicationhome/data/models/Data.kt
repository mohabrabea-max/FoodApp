package com.example.applicationhome.data.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class FoodItem(val id : Int, val name : String, val image : List<Int>, val priceANDsize : Map<String, Double>, val description: List<String>)
data class Snake(val id : Int, val name : String, var image : Int, val priceANDsize : Map<String, Double>)
data class CategoriesImage(val id : Int, val name : String, val image : Int)

data class Restaurants(val id : Int, val name : String, val image : Int, val review : Double, val background : Color)

data class Options(val title : String, val icon : ImageVector, val screen: String)

data class BottomBar(val title : String, val icon : ImageVector, val screens : String)

data class Account(val title : String, val value: String, val icon : ImageVector)

data class Settings(val title : String, val value: String, val icon : ImageVector)