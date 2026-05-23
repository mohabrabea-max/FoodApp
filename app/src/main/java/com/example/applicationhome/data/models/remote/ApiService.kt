package com.example.applicationhome.data.models.remote

import com.example.applicationhome.data.models.model.CartClass
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.FirebasePostResponse
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Offers
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.model.UserClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodAppAPIs{

    @POST("users.json")
    suspend fun signUp(
        @Body user : UserClass
    ): Response<FirebasePostResponse>

    @GET("{nodeName}.json")
    suspend fun <T> getAnyData(
        @Path("nodeName") nodename : String,
        @Query("orderBy") order : String,
        @Query("equalTo") value : String
    ): Response<Map<String, T>>

    @PUT("cart/{userId}/{mealId}.json")
    suspend fun addToCart(
        @Path("userId") userId : String,
        @Path("mealId") mealId : String,
        @Body data : CartClass
    ): Response<CartClass>

    @PATCH("carts/{userId}/{mealId}.json")
    suspend fun updateQuantity(
        @Path("userId") userId: String,
        @Path("mealId") mealId: String,
        @Body updates: Map<String, Int>
    ): Response<Map<String, Int>>

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