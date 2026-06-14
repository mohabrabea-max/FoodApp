package com.example.applicationhome.data.models.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.applicationhome.data.models.model.CartClass
import com.example.applicationhome.data.models.model.CartItemsClass
import com.example.applicationhome.data.models.model.Food
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.remote.RetrofitInstance
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuList
import com.example.applicationhome.data.models.repository.MenuRepository.snacks
import com.example.applicationhome.data.models.repository.UserRepository.userId
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

object CartRepository {
    var totalPrice by mutableDoubleStateOf(0.0)
    var totalNumber = mutableStateOf(0)
    var allCart = mutableStateOf(CartClass())
    var cartItems = mutableStateMapOf<String, CartItemsClass>()
    var cartRestaurant by mutableStateOf(Restaurants())
    val foodMenu get() = cartItems.filter { it.value.type == "Meal" }
    val snacksMenu get() = cartItems.filter { it.value.type == "Snack" }
    var cartMealsMenu by mutableStateOf<List<FoodItem>>(emptyList())
    var cartSnacksMenu by mutableStateOf<List<Snack>>(emptyList())



    fun updateTotals() {
        totalNumber.value = 0
        totalPrice = 0.0
        cartItems.forEach { (key, value) ->
            if(value.type == "Meal"){
                val meal = cartMealsMenu.find { it.id == value.id }
                val number = foodMenu.values.find { it.id == value.id }?.number ?: 0
                if(meal != null){
                    totalPrice = totalPrice + (meal.sizeOptions.find { it.size == value.size }?.price ?: 0.0) * number
                }
                totalNumber.value += 1
            }else if(value.type == "Snack"){
                val snack = cartSnacksMenu.find { it.id == value.id }?.priceANDsize
                val number = snacksMenu.values.find { it.id == value.id }?.number ?: 0
                if(snack != null){
                    totalPrice = totalPrice + (snack[value.size] ?: 0.0) * number
                }
                totalNumber.value += 1
            }
        }

    }

    suspend fun getCartRestaurantData(food : Food) : String{
        return try {
            val response = RetrofitInstance.api.getCarRestaurant("\"id\"", food.restaurantId)
            val resData = response.body()?.values?.first()
            if(response.isSuccessful && resData != null){
                cartRestaurant = resData
                "Success"
            }else{
                "Network error"
            }
        } catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        } finally {
            ""
        }
    }

    suspend fun createNewCart(food : Food, size : String, type : String) : String{
        val resName = cartRestaurant.name
        val resImage = cartRestaurant.image
        val mealKey = "${food.id}_$size"
        val cartItemsObject = CartItemsClass(food.id, type, size, 1)
        return try {
            val response = RetrofitInstance.api.createCart(
                userId,
                CartClass(
                    mapOf(mealKey to cartItemsObject),
                    food.restaurantId,
                    resName,
                    resImage
                )
            )
            val finalData = response.body()
            if(response.isSuccessful && finalData != null){
                allCart.value = finalData
                cartItems.putAll(finalData.cartItems)
                "Success"
            }else{
                "Network error"
            }
        } catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        } finally {
            ""
        }
    }

    suspend fun getcart(): String {
        return try {
            val response = RetrofitInstance.api.getCart(userId)
            if(response.isSuccessful){
                val cartItem = response.body()
                if(cartItem != null){
                    allCart.value = cartItem
                    cartItems.clear()
                    cartItems.putAll(cartItem.cartItems)
                    "Success"
                }else{
                    cartItems.clear()
                    "Cart is empty"
                }
            }else{
                "Network error"
            }
        }catch (e: Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun cartMeals(): List<FoodItem> {
        var finalMealsList = mutableListOf<FoodItem>()
        val missingItems = mutableListOf<Int>()
        foodMenu.forEach { item ->
            val cachedMeal = foodMenuList.find { it.id == item.value.id }
            if(cachedMeal != null){
                finalMealsList += cachedMeal
            }else{
                missingItems.add(item.value.id)
            }
        }
        if(missingItems.isNotEmpty()){
            coroutineScope {
                val deferredRequests = missingItems.map { itam ->
                    async {
                        try {
                            val response = RetrofitInstance.api.getCartMeals("\"id\"", itam)
                            if(response.isSuccessful){
                                val resultMap = response.body()
                                resultMap?.values?.firstOrNull()
                            }else{
                                null
                            }
                        } catch (e : Exception){
                            null
                        }
                    }
                }
                finalMealsList += deferredRequests.awaitAll().filterNotNull()
            }
        }
        return finalMealsList
    }

    suspend fun cartSnacks(): List<Snack> {
        var finalSnacksList = mutableListOf<Snack>()
        val missingItems = mutableListOf<Int>()
        snacksMenu.forEach { item ->
            val cachedMeal = snacks.find { it.id == item.value.id }
            if(cachedMeal != null){
                finalSnacksList += cachedMeal
            }else{
                missingItems.add(item.value.id)
            }
        }
        if(missingItems.isNotEmpty()){
            coroutineScope {
                val deferredRequests = missingItems.map { item ->
                    async {
                        try {
                            val response = RetrofitInstance.api.getCartSnacks("\"id\"", item)
                            if(response.isSuccessful){
                                val resultMap = response.body()
                                println("isSuccessful")
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
                        }
                    }
                }
                finalSnacksList += deferredRequests.awaitAll().filterNotNull()
            }
        }
        return finalSnacksList
    }

    suspend fun addMealToCart(food: Food, size : String, number: Int, type : String): String{
        var mealKey by mutableStateOf("${food.id}_$size")
        if(!cartItems.keys.contains(mealKey)){
            val cartItemsObject = CartItemsClass(food.id, type, size, 1)
            return try {
                val response = RetrofitInstance.api.addToCart(userId, mealKey, cartItemsObject)
                val cartItem = response.body()
                if(response.isSuccessful && cartItem != null){
                    cartItems[mealKey] = CartItemsClass(cartItem.id, cartItem.type, cartItem.size, cartItem.number)
                    "Success"
                }else{
                    "Network error"
                }
            }catch (e : Exception){
                "خطأ في الشبكة: ${e.message}"
            }
        }else{
            return try {
                val updatesMap = mapOf("number" to number)
                val response = RetrofitInstance.api.updateCart(userId, mealKey, updatesMap)

                if(response.isSuccessful && response.body()!= null){
                    val currentItem = cartItems[mealKey]
                    if (currentItem != null) {
                        cartItems[mealKey] = currentItem.copy(number = number)
                    }
                    "Success"
                }else{
                    "Network error"
                }
            }catch (e : Exception){
                "خطأ في الشبكة: ${e.message}"
            }
        }
    }

    suspend fun minusFromCart(foodId: Int, size : String, number: Int): String{
        var mealKey by mutableStateOf("${foodId}_$size")
        return try {
            val updatesMap = mapOf("number" to number)
            val response = RetrofitInstance.api.updateCart(userId, mealKey, updatesMap)
            val currentItem = cartItems[mealKey]
            if (response.isSuccessful && currentItem != null){
                cartItems[mealKey] = currentItem.copy(number = number)
                "Success"
            }else{
                "Network error"
            }
        }catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun deleteFromCart(foodId: Int, size : String): String{  //  هنا بنحذف وجبة من السلة
        var mealKey by mutableStateOf("${foodId}_$size")
        return try {
            val response = RetrofitInstance.api.deleteItemFromCart(userId, mealKey)
            if(response.isSuccessful){
                cartItems.keys.remove(mealKey)
                cartMealsMenu = cartMealsMenu.filterNot { it.id == foodId }
                cartSnacksMenu = cartSnacksMenu.filterNot { it.id == foodId }
                "Success"
            }else{
                "Network error"
            }
        }catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun deleteAllCart(): String{ // هنا بنحذف السلة كلها
        return try {
            val response = RetrofitInstance.api.deleteAllCart(userId)
            if(response.isSuccessful){
                allCart.value = CartClass()
                cartItems.clear()
                cartMealsMenu = emptyList()
                cartSnacksMenu = emptyList()
                "Success"
            }else{
                "Network error"
            }
        }catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }
}