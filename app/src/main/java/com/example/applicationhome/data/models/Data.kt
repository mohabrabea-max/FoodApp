package com.example.applicationhome.data.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class FoodItem(val id : Int, val name : String, val price : Double, val image : Int, val size : String, val description: String)

data class CategoriesImage(val name : String, val image : Int)
data class Restaurants(val name : String, val image : Int, val review : Double, val background : Color)

data class Options(val title : String, val icon : ImageVector, val screen: String)

data class Account(val title : String, val value: String, val icon : ImageVector)