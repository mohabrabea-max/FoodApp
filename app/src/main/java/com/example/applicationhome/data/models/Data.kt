package com.example.applicationhome.data.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

enum class CategoryType { PIZZA, BURGER, CHICKEN, SWEET, ALL }


interface FoodAppAPIs{

    @POST("users.json")
    suspend fun signUp(
        @Body user : UserClass
    ): Response<FirebasePostResponse>

    @GET("users.json")
    suspend fun getUserEmail(
        @Query("orderBy") orderBy : String = "\"email\"",
        @Query("equalTo") email : String,
    ): Response<Map<String, UserClass>>

    @GET("food_menu.json")
    suspend fun foodmenu():List<FoodItem>

    @GET("snacks.json")
    suspend fun snacksMenu():List<Snack>

    @GET("categories.json")
    suspend fun categorieslist():List<Categories>

    @GET("restaurants.json")
    suspend fun restaurants():List<Restaurants>

    @GET("offers.json")
    suspend fun offers():List<Offers>
}


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

data class CartKey(val food : Food, val size : String)

data class UserClass(
    val firstname : String?,
    val lastname : String?,
    val email : String?,
    val password : String?,
    val phonenumber : String?,
    val address : String?
)

data class Birthday(val day : Int, val month : Int, val year : Int)

data class FirebasePostResponse(val name : String)

data class SignupCompose(
    val stateText : String,
    val icon : ImageVector
)