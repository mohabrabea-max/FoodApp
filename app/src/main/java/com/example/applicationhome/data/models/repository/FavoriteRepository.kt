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

    var mealsFavorite = mutableStateMapOf<String, FoodItem>()
    var mealsFavoriteIsLoading by mutableStateOf(true)

    var snacksFavorite = mutableStateMapOf<String, Snack>()
    var snacksFavoriteIsLoading by mutableStateOf(true)

    var restaurantsFavorite = mutableStateMapOf<String, Restaurants>()
    var restaurantsFavoriteIsLoading by mutableStateOf(true)


    suspend fun favoriteMeals(): Map<String, FoodItem> {
        val finalMealsList = mutableStateMapOf<String, FoodItem>()
        val missingItems = mutableListOf<Int>()
        mealsFavoriteMenu.forEach { item ->
            val mealKey = "Meal_${item.id}"
            val cachedMeal = foodMenuList[mealKey]
            if(cachedMeal != null){
                finalMealsList += (mealKey to cachedMeal)
            }else{
                missingItems.add(item.id)
            }
        }
        if(missingItems.isNotEmpty()){
            mealsFavoriteIsLoading = true
            try {
                coroutineScope {
                    val deferredRequests = missingItems.map { item ->
                        async {
                            try {
                                val response = RetrofitInstance.api.getCartMeals("\"id\"", item)
                                val resultMap = response.body()
                                if(response.isSuccessful && resultMap != null){
                                    foodMenuList += resultMap
                                    resultMap
                                }else{ null }
                            } catch (e : Exception){ null }
                        }
                    }
                    deferredRequests.awaitAll().filterNotNull().forEach { item ->
                        finalMealsList += item
                    }
                }
            } finally {
                mealsFavoriteIsLoading = false
            }
        }else{
            mealsFavoriteIsLoading = false
        }
        return finalMealsList
    }

    suspend fun favoriteSnacks(): Map<String, Snack> {
        val finalSnacksList = mutableStateMapOf<String, Snack>()
        val missingItems = mutableListOf<Int>()
        snacksFavoriteMenu.forEach { item ->
            val snackKey = "Snack_${item.id}"
            val cachedMeal = snacks[snackKey]
            if(cachedMeal != null){
                finalSnacksList += (snackKey to cachedMeal)
            }else{
                missingItems.add(item.id)
            }
        }
        if(missingItems.isNotEmpty()){
            snacksFavoriteIsLoading = true
            try {
                coroutineScope {
                    val deferredRequests = missingItems.map { item ->
                        async {
                            try {
                                val response = RetrofitInstance.api.getCartSnacks("\"id\"", item)
                                val resultMap = response.body()
                                if(response.isSuccessful && resultMap != null){
                                    snacks += resultMap
                                    resultMap
                                }else{null}
                            } catch (e : Exception){ null }
                        }
                    }
                    deferredRequests.awaitAll().filterNotNull().forEach { item ->
                        finalSnacksList += item
                    }
                }
            } finally {
                snacksFavoriteIsLoading = false
            }
        }else{
            snacksFavoriteIsLoading = false
        }
        return finalSnacksList
    }
    suspend fun favoriteRestaurants(): Map<String, Restaurants> {
        val finalRestaurantsList = mutableStateMapOf<String, Restaurants>()
        val missingItems = mutableListOf<Int>()
        restaurantsFavoriteMenu.forEach { item ->
            val restaurantKey = "Restaurant_${item.id}"
            val cachedMeal = restaurantsMenu[restaurantKey]
            if(cachedMeal != null){
                finalRestaurantsList += (restaurantKey to cachedMeal)
            }else{
                missingItems.add(item.id)
            }
        }
        if(missingItems.isNotEmpty()){
            restaurantsFavoriteIsLoading = true
            try {
                coroutineScope {
                    val deferredRequests = missingItems.map { item ->
                        async {
                            try {
                                val response = RetrofitInstance.api.getFavoriteRestaurants("\"id\"", item)
                                val resultMap = response.body()
                                if(response.isSuccessful && resultMap != null){
                                    restaurantsMenu += resultMap
                                    resultMap
                                }else{ null }
                            } catch (e : Exception){ null }
                        }
                    }
                    deferredRequests.awaitAll().filterNotNull().forEach { item ->
                        finalRestaurantsList += item
                    }
                }
            } finally {
                restaurantsFavoriteIsLoading = false
            }
        }else{
            restaurantsFavoriteIsLoading = false
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