package com.example.applicationhome.data.models.remote

import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.FavoriteClass
import com.example.applicationhome.data.models.model.FirebasePostResponse
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Offers
import com.example.applicationhome.data.models.model.OrdersClass
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.RestaurantsCount
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.model.UserClassFireBase
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodAppAPIs{

    @POST("users.json")
    suspend fun signUp(
        @Body user : UserClassFireBase
    ): Response<FirebasePostResponse>

    @GET("users.json")
    suspend fun getUserData(
        @Query("orderBy") order : String,
        @Query("equalTo") value : String
    ): Response<Map<String, UserClassFireBase>>


//    @PATCH("snacks/{mealId}.json")
//    suspend fun addToMeals(
//        @Path("mealId") mealId : String,
//        @Body data : Map<String, Int>
//    ): Response<CartClass>


    @GET("meals/{mealKey}.json")
    suspend fun getCartMeal(
        @Path("mealKey") mealKey : String
    ): Response<Map<String, FoodItem>>

    @GET("restaurants.json")
    suspend fun getCarRestaurant(
        @Query("orderBy") order : String,
        @Query("equalTo") value : Int
    ): Response<Map<String, Restaurants>>

    @GET("meals.json")
    suspend fun getFavoriteMeals(
        @Query("orderBy") order : String,
        @Query("equalTo") value : Int
    ): Response<Map<String, FoodItem>>

    @GET("snacks.json")
    suspend fun getFavoriteSnacks(
        @Query("orderBy") order : String,
        @Query("equalTo") value : Int
    ): Response<Map<String, Snack>>

    @PUT("favorite/{userId}/{mealKey}.json")
    suspend fun addToFavorite(
        @Path("userId") userId : String,
        @Path("mealKey") mealKey : String,
        @Body data : FavoriteClass
    ): Response<Unit>

    @DELETE("favorite/{userId}/{mealKey}.json")
    suspend fun deleteFromFavorite(
        @Path("userId") userId : String,
        @Path("mealKey") mealKey : String,
    ): Response<Unit>

    @GET("favorite/{userId}.json")
    suspend fun getFavoriteItems(
        @Path("userId") userId : String,
    ): Response<Map<String, FavoriteClass>>

    @GET("restaurants.json")
    suspend fun getFavoriteRestaurants(
        @Query("orderBy") order : String,
        @Query("equalTo") value : Int
    ): Response<Map<String, Restaurants>>




    @GET("restaurants_count.json")
    suspend fun getRestaurantCount(): Response<Map<Int, RestaurantsCount>>

    @GET("meals.json")
    suspend fun foodmenu(
        @Query("orderBy") order : String,
        @Query("equalTo") value : Int
    ): Response<Map<String, FoodItem>>

    @GET("snacks.json")
    suspend fun snacksMenu(
        @Query("orderBy") order : String,
        @Query("equalTo") value : Int
    ): Response<Map<String, Snack>>

    @GET("categories.json")
    suspend fun categorieslist(): List<Categories>

    @GET("restaurants.json")
    suspend fun restaurants(): Response<Map<String, Restaurants>>

    @GET("offers.json")
    suspend fun restaurantOffers(
        @Query("orderBy") order : String,
        @Query("equalTo") value : Int
    ): Response<Map<String, Offers>>

    @GET("offers.json")
    suspend fun offers(): Response<List<Offers>>




    @PUT("orders/{userId}/{orderId}.json")
    suspend fun putNewOrder(
        @Path("userId") userId : String,
        @Path("orderId") orderId : String,
        @Body order : OrdersClass
    ): Response<OrdersClass>

    @GET("orders/{userId}.json")
    suspend fun getLastOrders(
        @Path("userId") userId : String
    ): Response<Map<String, OrdersClass>>
}