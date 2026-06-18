package com.example.applicationhome.data.models.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.Drink
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Offers
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.RestaurantsCount
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.remote.RetrofitInstance


object MenuRepository {
    var foodMenuList = mutableStateMapOf<String, FoodItem>()
    var foodMenuListisLoading by mutableStateOf(true)


    var restaurantsMenu = mutableStateMapOf<String, Restaurants>()
    var restaurantsMenuisLoading by mutableStateOf(true)


    var categories by mutableStateOf<List<Categories>>(emptyList()); private set
    var categoriesisLoading by mutableStateOf(true)


    var snacks = mutableStateMapOf<String, Snack>()
    var snacksisLoading by mutableStateOf(true)


    var drinkMenu = mutableStateMapOf<String, Drink>()//; private set
    var drinkMenuisLoading by mutableStateOf(true)


    var offers by mutableStateOf<List<Offers>>(emptyList()); private set
    var offersisLoading by mutableStateOf(true)

    var restaurantOffers by mutableStateOf<List<Offers>>(emptyList()); private set
    var restaurantOffersLoading by mutableStateOf(true)

    var isNetworkAvailable by mutableStateOf(true)

    var restaurantcount = mutableMapOf<Int, RestaurantsCount>() ?: null



    suspend fun restaurantCount(): Map<Int, RestaurantsCount>?{
        val count = try {
            val restaurants = RetrofitInstance.api.getRestaurantCount()
            val countList = restaurants.body()
            if(restaurants.isSuccessful && countList != null){
                countList
            }else{
                null
            }
        }catch (e : Exception){
            null
        }
        return count
    }

    suspend fun uploadFoodMenuFromApi(resId : Int): String {
        return try {
            foodMenuListisLoading = true
            // بنجيب الأكل والمطاعم باستخدام الـ RetrofitInstance المطور بتاعنا
            val response = RetrofitInstance.api.foodmenu("\"restaurantId\"", resId)
            val foodMenu = response.body()
            if(response.isSuccessful && foodMenu != null){
                val filteredNewItems = foodMenu.filter { it.key !in foodMenuList.keys }
                foodMenuList += filteredNewItems
                "Success"
            }else{
                "foodMenuList Is empty"
            }
        } catch (e: Exception) {
            // معالجة الخطأ لو النت قطع
            e.printStackTrace()
            "Network error"
        } finally {
            foodMenuListisLoading = false
        }
    }
    suspend fun uploadRestaurantsFromApi(): String {
        return try {
            restaurantsMenuisLoading = true
            val response = RetrofitInstance.api.restaurants()
            val restaurants = response.body()
            if(response.isSuccessful && restaurants != null){
                val filteredNewItems = restaurants.filter { it.key !in restaurantsMenu.keys }
                restaurantsMenu += filteredNewItems
                "Success"
            }else{
                "restaurantsMenu Is empty"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Network error"
        } finally {
            restaurantsMenuisLoading = false
        }
    }
    suspend fun uploadCategorieslistFromApi(): String {
        return try {
            categoriesisLoading = true
            categories = RetrofitInstance.api.categorieslist()
            "Success"
        } catch (e: Exception) {
            e.printStackTrace()
            "Network error"
        } finally {
            categoriesisLoading = false
        }
    }
    suspend fun uploadSnacksMenuFromApi(resId : Int): String {
        return try {
            snacksisLoading = true
            val response = RetrofitInstance.api.snacksMenu("\"restaurantId\"", resId)
            val snacksMenu = response.body()
            if(response.isSuccessful && snacksMenu != null){
                val filteredNewItems = snacksMenu.filter { it.key !in snacks.keys }
                snacks += filteredNewItems
                "Success"
            }else{
                "snacksMenu Is empty"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Network error"
        } finally {
            snacksisLoading = false
        }
    }
    suspend fun uploadOffersFromApi(): String {
        return try {
            offersisLoading = true
            offers = RetrofitInstance.api.offers()
            "Success"
        } catch (e: Exception) {
            e.printStackTrace()
            "Network error"
        } finally {
            offersisLoading = false
        }
    }
    suspend fun uploadRestaurantOffersFromApi(resId : Int): String {
        return try {
            offersisLoading = true
            restaurantOffers = RetrofitInstance.api.restaurantOffers("\"restaurantId\"", resId).values.toList()
            "Success"
        } catch (e: Exception) {
            e.printStackTrace()
            "Network error"
        } finally {
            offersisLoading = false
        }
    }
}