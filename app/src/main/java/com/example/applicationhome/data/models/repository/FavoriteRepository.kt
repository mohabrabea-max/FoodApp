package com.example.applicationhome.data.models.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.applicationhome.data.models.model.FavoriteClass
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.remote.RetrofitInstance
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuList
import com.example.applicationhome.data.models.repository.MenuRepository.restaurantsMenu
import com.example.applicationhome.data.models.repository.MenuRepository.snacks
import com.example.applicationhome.data.models.repository.UserRepository.userId
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

object FavoriteRepository {
    var favoritList = mutableStateMapOf<String, FavoriteClass>()

    val mealsFavoriteMenu get() = favoritList.filter { it.value.typ == "Meal" }.values.toList()

    val snacksFavoriteMenu get() = favoritList.filter { it.value.typ == "Snack" }.values.toList()

    val restaurantsFavoriteMenu get() = favoritList.filter { it.value.typ == "Restaurant" }.values.toList()

    var mealsFavorite by mutableStateOf<List<FoodItem>>(emptyList())
    var mealsFavoriteIsLoading by mutableStateOf(true)

    var snacksFavorite by mutableStateOf<List<Snack>>(emptyList())
    var snacksFavoriteIsLoading by mutableStateOf(true)

    var restaurantsFavorite by mutableStateOf<List<Restaurants>>(emptyList())
    var restaurantsFavoriteIsLoading by mutableStateOf(true)


    suspend fun favoriteMeals(): List<FoodItem> {
        var finalMealsList = mutableListOf<FoodItem>()
        val missingItems = mutableListOf<Int>()
        mealsFavoriteMenu.forEach { item ->
            val cachedMeal = foodMenuList.find { it.id == item.id }
            if(cachedMeal != null){
                finalMealsList += cachedMeal
            }else{
                missingItems.add(item.id)
            }
        }
        if(missingItems.isNotEmpty()){
            coroutineScope {
                val deferredRequests = missingItems.map { item ->
                    async {
                        try {
                            mealsFavoriteIsLoading = true
                            val response = RetrofitInstance.api.getCartMeals("\"id\"", item)
                            if(response.isSuccessful){
                                val resultMap = response.body()
                                if(resultMap != null){
                                    foodMenuList = foodMenuList + resultMap.values
                                }
                                resultMap?.values?.firstOrNull()
                            }else{
                                null
                            }
                        } catch (e : Exception){
                            null
                        } finally {
                            mealsFavoriteIsLoading = false
                        }
                    }
                }
                finalMealsList += deferredRequests.awaitAll().filterNotNull()
            }
        }
        return finalMealsList
    }

    suspend fun favoriteSnacks(): List<Snack> {
        var finalSnacksList = mutableListOf<Snack>()
        val missingItems = mutableListOf<Int>()
        snacksFavoriteMenu.forEach { item ->
            val cachedMeal = snacks.find { it.id == item.id }
            if(cachedMeal != null){
                finalSnacksList += cachedMeal
            }else{
                missingItems.add(item.id)
            }
        }
        if(missingItems.isNotEmpty()){
            coroutineScope {
                val deferredRequests = missingItems.map { item ->
                    async {
                        try {
                            snacksFavoriteIsLoading = true
                            val response = RetrofitInstance.api.getCartSnacks("\"id\"", item)
                            if(response.isSuccessful){
                                val resultMap = response.body()
                                println("isSuccessful")
                                if(resultMap != null){
                                    snacks = snacks +resultMap.values
                                }
                                resultMap?.values?.firstOrNull()
                            }else{
                                println("Error in response")
                                println("Firebase Error Code: ${response.code()}")
                                println("Firebase Error Body: ${response.errorBody()?.string()}")
                                null
                            }
                        } catch (e : Exception){
                            println("Error in catch")
                            null
                        } finally {
                            snacksFavoriteIsLoading = false
                        }
                    }
                }
                finalSnacksList += deferredRequests.awaitAll().filterNotNull()
            }
        }
        return finalSnacksList
    }
    suspend fun favoriteRestaurants(): List<Restaurants> {
        var finalRestaurantsList by mutableStateOf<List<Restaurants>>(emptyList())
        val missingItems = mutableListOf<Int>()
        restaurantsFavoriteMenu.forEach { item ->
            val cachedMeal = restaurantsMenu.find { it.id == item.id }
            if(cachedMeal != null){
                finalRestaurantsList += cachedMeal
            }else{
                missingItems.add(item.id)
            }
        }
        if(missingItems.isNotEmpty()){
            coroutineScope {
                val deferredRequests = missingItems.map { item ->
                    async {
                        try {
                            restaurantsFavoriteIsLoading = true
                            val response = RetrofitInstance.api.getCartRestaurants("\"id\"", item)
                            if(response.isSuccessful){
                                val resultMap = response.body()
                                println("isSuccessful")
                                if(resultMap != null){
                                    restaurantsMenu += resultMap.values
                                }
                                resultMap?.values?.firstOrNull()
                            }else{
                                println("Error in response")
                                println("Firebase Error Code: ${response.code()}")
                                println("Firebase Error Body: ${response.errorBody()?.string()}")
                                null
                            }
                        } catch (e : Exception){
                            println("Error in catch")
                            null
                        } finally {
                            restaurantsFavoriteIsLoading = false
                        }
                    }
                }
                finalRestaurantsList += deferredRequests.awaitAll().filterNotNull()
            }
        }
        return finalRestaurantsList
    }

    suspend fun addToFavorite(id : Int, typ : String, restaurants : Int) : String{
        val favoriteObject = FavoriteClass(id, typ, restaurants)
        val mealKey = "${typ}_$id"
        return try {
            val response = RetrofitInstance.api.addToFavorite(userId, mealKey, favoriteObject)
            if(response.isSuccessful && response.body() != null){
                favoritList[mealKey] = favoriteObject
                //viewFavorite()
                "Success"
            }else{
                "Network error"
            }
        } catch (e : Exception){
            println("addToFavorite error")
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun getFavorite() : String {
        return try {
            val response = RetrofitInstance.api.getFavoriteItems(userId)
            if (response.isSuccessful) {
                val favoriteItems = response.body()
                if (favoriteItems != null) {
                    favoritList.clear()
                    favoritList.putAll(favoriteItems)
                    //viewFavorite()
                    "Success"
                } else {
                    favoritList.clear()
                    "Favorite is empty"
                }
            } else {
                "Network error"
            }
        } catch (e : Exception) {
            e.printStackTrace()
            println("🚨 الكراش الحقيقي هو: ${e.localizedMessage}")
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun deleteFavorite(id : Int, type : String): String{
        val mealKey = "${type}_$id"
        return try {
            val response = RetrofitInstance.api.deleteFromFavorite(userId, mealKey)
            if(response.isSuccessful){
                favoritList.keys.remove(mealKey)
                //viewFavorite()
                "Success"
            }else{
                "Network error"
            }
        }catch (e : Exception){
            println("deleteFavorite error")
            "خطأ في الشبكة: ${e.message}"
        }
    }
}