package com.example.applicationhome.data.models.remote

import com.example.applicationhome.data.models.model.CartClass
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.FavoriteClass
import com.example.applicationhome.data.models.model.FirebasePostResponse
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Offers
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.model.UserClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @GET("users.json")
    suspend fun getUserData(
        @Query("orderBy") order : String,
        @Query("equalTo") value : String
    ): Response<Map<String, UserClass>>


    @PUT("food_menu/restaurantId/{mealId}.json")
    suspend fun addToMeals(
        @Path("mealId") userId : String,
        @Body data : String = ""
    ): Response<CartClass>

    @PUT("cart/{userId}/{mealKey}.json")
    suspend fun addToCart(
        @Path("userId") userId : String,
        @Path("mealKey") mealKey : String,
        @Body data : CartClass
    ): Response<CartClass>

    @PATCH("cart/{userId}/{mealKey}.json")
    suspend fun updateCart(
        @Path("userId") userId : String,
        @Path("mealKey") mealKey : String,
        @Body updates: Map<String, Int>
    ): Response<Map<String, Int>>

    @GET("cart/{userId}.json")
    suspend fun getCartItems(
        @Path("userId") userId : String,
    ): Response<Map<String, CartClass>>

    @DELETE("cart/{userId}/{mealKey}.json")
    suspend fun deleteItemFromCart(
        @Path("userId") userId : String,
        @Path("mealKey") mealKey : String,
    ): Response<Unit>




    @PUT("favorite/{userId}/{mealKey}.json")
    suspend fun addToFavorite(
        @Path("userId") userId : String,
        @Path("mealKey") mealKey : String,
        @Body data : FavoriteClass
    ): Response<FavoriteClass>

    @DELETE("favorite/{userId}/{mealKey}.json")
    suspend fun deleteFromFavorite(
        @Path("userId") userId : String,
        @Path("mealKey") mealKey : String,
    ): Response<Unit>

    @GET("favorite/{userId}.json")
    suspend fun getFavoriteItems(
        @Path("userId") userId : String,
    ): Response<Map<String, FavoriteClass>>




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