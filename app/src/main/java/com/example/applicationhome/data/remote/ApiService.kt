package com.example.applicationhome.data.remote

import com.example.applicationhome.data.data.model.Address
import com.example.applicationhome.data.data.model.Categories
import com.example.applicationhome.data.data.model.FavoriteClass
import com.example.applicationhome.data.data.model.FoodItem
import com.example.applicationhome.data.data.model.Offers
import com.example.applicationhome.data.data.model.OrdersClass
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.data.model.Snack
import com.example.applicationhome.data.data.model.UserClassFireBase
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodAppAPIs{
    @GET("users.json")
    suspend fun getUserData(
        @Query("orderBy") order : String,
        @Query("equalTo") value : String
    ): Response<Map<String, UserClassFireBase>>

    @PATCH("users/{userId}.json")
    suspend fun editeProfile(
        @Path("userId") userId : String,
        @Body newData : UserClassFireBase
    ): Response<Unit>

    @PUT("users/{userId}/phonenumber.json")
    suspend fun editPhoneNumber(
        @Path("userId") userId : String,
        @Body newData : String
    ): Response<Unit>


    //                              فانكشن بتجيب الوجبات اللي بعد اخر ابديت بس
    @GET("meals.json")
    suspend fun getMealsByLastUpdate(
        @Query("orderBy") orderBy: String = "\"updatedAt\"",
        @Query("startAt") lastSyncTimestamp: Long
    ): Response<Map<String, FoodItem>>

    @GET("snacks.json")
    suspend fun getSnacksByLastUpdate(
        @Query("orderBy") orderBy: String = "\"updatedAt\"",
        @Query("startAt") lastSyncTimestamp: Long
    ): Response<Map<String, Snack>>

    @GET("restaurants.json")
    suspend fun getRestaurantsByLastUpdate(
        @Query("orderBy") orderBy: String = "\"updatedAt\"",
        @Query("startAt") lastSyncTimestamp: Long
    ): Response<Map<String, Restaurants>>


//    @PATCH("snacks/{mealId}.json")
//    suspend fun addToMeals(
//        @Path("mealId") mealId : String,
//        @Body data : Map<String, Int>
//    ): Response<CartClass>

    @GET("restaurants.json")
    suspend fun getCarRestaurant(
        @Query("orderBy") order : String,
        @Query("equalTo") value : Int
    ): Response<Map<String, Restaurants>>

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


    @GET("categories.json")
    suspend fun categorieslist(
        @Query("orderBy") orderBy : String = "\"updatedAt\"",
        @Query("startAt") lastSyncTimestamp:  Long
    ): Response<Map<String, Categories>>


    @GET("offers.json")
    suspend fun offers(
        @Query("orderBy") orderBy : String = "\"updatedAt\"",
        @Query("startAt") lastSyncTimestamp:  Long
    ): Response<Map<String, Offers>>




    @PUT("orders/{userId}/{orderId}.json")
    suspend fun putNewOrder(
        @Path("userId") userId : String,
        @Path("orderId") orderId : Long,
        @Body order : OrdersClass
    ): Response<Unit>

    @PATCH("orders/{userId}/{orderId}.json")
    suspend fun cancelOrder(
        @Path("userId") userId: String,
        @Path("orderId") orderId: Long,
        @Body updates : @JvmSuppressWildcards Map<String, Any>
    ): Response<Unit>

    @GET("orders/{userId}.json")
    suspend fun getLastOrders(
        @Path("userId") userId : String,
        @Query("orderBy") orderBy : String = "\"updatedAt\"",
        @Query("startAt") lastSyncTimestamp : Long
    ): Response<Map<Long, OrdersClass>>



    @GET("addresses/{userId}.json")
    suspend fun getAddresses(
        @Path("userId") userId: String,
        @Query("orderBy") orderBy : String = "\"lastUse\"",
        @Query("startAt") lastSyncTimestamp : Long
    ): Response<Map<Long, Address>>

    @PUT("addresses/{userId}/{addressId}.json")
    suspend fun putAddresses(
        @Path("userId") userId: String,
        @Path("addressId") addressId : Long,
        @Body address : Address
    ): Response<Unit>

    @PUT("addresses/{userId}/{addressId}/lastUse.json")
    suspend fun updateAddressesLastUse(
        @Path("userId") userId: String,
        @Path("addressId") addressId : Long,
        @Body lastUse : Long
    ): Response<Unit>

    @DELETE("addresses/{userId}/{addressId}.json")
    suspend fun deleteAddress(
        @Path("userId") userId: String,
        @Path("addressId") addressId : Long
    ): Response<Unit>
}