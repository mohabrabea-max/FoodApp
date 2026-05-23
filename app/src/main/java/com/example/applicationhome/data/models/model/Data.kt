package com.example.applicationhome.data.models.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.gson.annotations.SerializedName

enum class CategoryType { PIZZA, BURGER, CHICKEN, SWEET, ALL }

sealed interface Food{
    val id: Int
    val name : String
    val image : List<String>
    val review : Double
}

    data class FoodItem(
        override val id : Int = 0,
        @SerializedName("category")
        val typ : CategoryType = CategoryType.ALL,
        override val name : String = "",
        @SerializedName("images")
        override val image : List<String> = listOf(""),
        @SerializedName("sizes")
        val sizeOptions: List<MealSizeDetail>,
        @SerializedName("rating")
        override val review : Double = 0.0
    ): Food

    data class Snack(
        override val id : Int = 0,
        override val name : String = "",
        @SerializedName("images")
        override val image : List<String> = listOf(""),
        @SerializedName("prices")
        val priceANDsize : Map<String, Double>,
        @SerializedName("rating")
        override val review : Double = 0.0
    ): Food

data class MealSizeDetail(
    val size : String = "",
    val price : Double = 0.0,
    @SerializedName("details")
    val snack : Map<Int, String> = mapOf(0 to "")
)
data class Categories(
    val id : Int = 0,
    val name : String = "",
    @SerializedName("type")
    val typ : CategoryType = CategoryType.ALL,
    val image : String = "",
    val icon : String = ""
)

data class Offers(
    val id : Int = 0,
    @SerializedName("title")
    val name : String = "",
    val image : String = ""
)

data class Restaurants(
    val id : Int = 0,
    @SerializedName("types")
    val typ : List<CategoryType>,
    val name : String = "",
    @SerializedName("logo")
    val image : String = "",
    @SerializedName("main_image")
    val image2 : String = "",
    @SerializedName("rating")
    val review : Double = 0.0,
    val background : Color = Color.White
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

data class CartClass(
    val size : String?,
    val number : Int?
)

data class UserClass(
    val firstname : String?,
    val lastname : String?,
    val email : String?,
    val password : String?,
    val phonenumber : String?,
    val address : String?
)

data class FirebasePostResponse(val name : String)
