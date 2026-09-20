package com.example.applicationhome.core.data.remote.dto

import androidx.annotation.Keep
import com.example.applicationhome.core.domain.model.CategoriesInWithTitle
import com.example.applicationhome.core.domain.model.MealSnacks
import com.google.gson.annotations.SerializedName

@Keep
data class Meal(
    val id : Int = 0,
    val category : String = "ALL",
    val name : String = "",
    val details : String = "",
    val image : String = "",
    @SerializedName("sizes")
    val sizeOptions : List<MealSizeDetailDto> = listOf(),
    var restaurantId : Int = 0,
    @SerializedName("rating")
    val review : Double = 0.0,
    val updatedAt : Long = 0L
)

@Keep
data class Snack(
    val id : Int = 0,
    val name : String = "",
    val details : String = "",
    val image : String = "",
    @SerializedName("prices")
    val priceANDsize : Map<String, Double> = emptyMap(),
    var restaurantId : Int = 0,
    @SerializedName("rating")
    val review : Double = 0.0,
    val updatedAt : Long = 0L
)

@Keep
data class Drink(
    val id : Int = 0,
    val name : String = "",
    val image : List<String> = emptyList(),
    val priceANDsize : Map<String, Double> = emptyMap(),
    var restaurantId : Int = 0,
    val updatedAt : Long = 0L
)

@Keep
data class MealSizeDetailDto(
    val size : String = "",
    val price : Double = 0.0,
    @SerializedName("details")
    val snack : Map<String, MealSnacks> = emptyMap()
)

@Keep
data class Categories(
    val id : Int = 0,
    val name : String = "",
    val type : String = "ALL",
    val image : String = "",
    val icon : String = "",
    val updatedAt : Long = 0L
)

@Keep
data class Offers(
    val restaurantId : Int = 0,
    val id : Int = 0,
    @SerializedName("title")
    val name : String = "",
    val image : String = "",
    val updatedAt : Long = 0L
)

@Keep
data class Restaurants(
    val id : Int = 0,
    @SerializedName("types")
    val typ : Map<String, CategoriesInWithTitle> = emptyMap(),
    val categories : Map<String, String> = emptyMap(),
    val name : String = "",
    @SerializedName("logo")
    val image : String = "",
    @SerializedName("main_image")
    val image2 : String = "",
    @SerializedName("rating")
    val review : Double = 0.0,
    val background : String = "",
    val searchKeywords: String = "",
    val topFiveMeals : String = "",
    val updatedAt : Long = 0L
)



@Keep
data class FavoriteClass(
    val id : Int,
    val typ : String,
    val restaurants : Int
)

@Keep
data class Discounts(
    val discount : Int = 0,
    val mealId : Int = 0,
    val restaurantId : Int = 0,
    val startDiscount : Long = 0L,
    val endDiscount : Long = 0L
)