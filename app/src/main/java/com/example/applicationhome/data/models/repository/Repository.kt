package com.example.applicationhome.data.models.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.applicationhome.data.models.model.CartClass
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.FirebasePostResponse
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Offers
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.model.UserClass
import com.example.applicationhome.data.models.remote.RetrofitInstance
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuList
import com.example.applicationhome.data.models.repository.UserRepository.userId
import retrofit2.Response

object UserRepository {
    var isEmailDone by mutableStateOf(false)
    var userId by mutableStateOf("")
    var isLogin by mutableStateOf(false)
    var userData by mutableStateOf(
        UserClass(
            "Guest",
            null,
            null,
            null,
            null,
            null
        )
    )

    suspend fun getUserData(emailstate : String, passwordstate : String?): String {
        return try {
            val formatEmail = "\"$emailstate\""
            val response = RetrofitInstance.api.getUserData(
                order = "\"email\"",
                value = formatEmail
            )
            if(response.isSuccessful && response.body() != null){
                val userMap = response.body()!!
                if(userMap.isNotEmpty()){
                    "Email is true"
                    if(passwordstate == userMap.values.firstOrNull()?.password){
                        val user = userMap.values.firstOrNull()
                        userData = userData.copy(
                            user?.firstname,
                            user?.lastname,
                            user?.email,
                            user?.password,
                            user?.phonenumber,
                            user?.address
                        )
                        userId = userMap.keys.first()
                        "Password is true"
                    }else{
                        "Password is false"
                    }
                }else{
                    "Email is false"
                }
            }else{
                "Network error"
            }
        } catch (e : Exception){
            println("خطأ في الشبكة: ${e.message}")
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun signUp(userRequest : UserClass): String {
        return try {
            val response: Response<FirebasePostResponse> = RetrofitInstance.api.signUp(userRequest)
            // 🟢 الـ if الذكية بتاعتك هنا عشان تتأكد إن العملية نجحت
            if (response.isSuccessful && response.body() != null) {
                userId = response.body()?.name.toString()
                "The operation was successful Account created"
            } else {
                "The operation failed"
            }

        } catch (e: Exception) {
            println("خطأ")
            "خطأ في الشبكة: ${e.message}"
        }
    }
}

object CartRepository {
    var totalCart by mutableStateOf(0)
    var cartItems = mutableStateMapOf<String, CartClass>()

    var totalPrice = mutableDoubleStateOf(0.0)
    var totalNumber = mutableStateOf(0)

    fun updateTotals() {
        totalNumber.value = 0
        totalPrice.value = 0.0
        cartItems.forEach { (key, value) ->
            val foodId = value.id
            val size = value.size
            val foodMenu = foodMenuList.find { it.id == foodId }
            if(foodMenu != null){
                totalNumber.value += value.number
                val priceForSize = foodMenu.sizeOptions.find { it.size == size }
                totalPrice.value += priceForSize?.price!! * value.number
            }
        }
    }
    suspend fun getcartitems(): String {
        return try {
            val response = RetrofitInstance.api.getCartItems(userId)
            if(response.isSuccessful){
                val cartItem = response.body()
                if(cartItem != null){
                    cartItems.clear()
                    cartItems.putAll(cartItem)
                    updateTotals()
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

    suspend fun addMealToCart(foodId: Int, size : String, number: Int): String{
        var mealKey by mutableStateOf("${foodId}_$size")

        if(!cartItems.keys.contains(mealKey)){
            val cartObject = CartClass(foodId, size, 1)
            return try {
                val response = RetrofitInstance.api.addToCart(userId, mealKey, cartObject)
                if(response.isSuccessful){
                    val cartItem = response.body()
                    cartItems[mealKey] = CartClass(cartItem!!.id, cartItem.size, cartItem.number)
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
                val response = RetrofitInstance.api.updateQuantity(userId, mealKey, updatesMap)

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
            val response = RetrofitInstance.api.updateQuantity(userId, mealKey, updatesMap)
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

    suspend fun deleteFromCart(foodId: Int, size : String): String{
        var mealKey by mutableStateOf("${foodId}_$size")
        return try {
            val response = RetrofitInstance.api.deleteItemFromCart(userId, mealKey)
            if(response.isSuccessful){
                cartItems.keys.remove(mealKey)
                "Success"
            }else{
                "Network error"
            }
        }catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }
}


object MenuRepository {
    var foodMenuList by mutableStateOf<List<FoodItem>>(emptyList()); private set
    var foodMenuListisLoading by mutableStateOf(true)


    var restaurantsMenu by mutableStateOf<List<Restaurants>>(emptyList()); private set
    var restaurantsMenuisLoading by mutableStateOf(true)


    var categories by mutableStateOf<List<Categories>>(emptyList()); private set
    var categoriesisLoading by mutableStateOf(true)


    var snacks by mutableStateOf<List<Snack>>(emptyList()); private set
    var snacksisLoading by mutableStateOf(true)


    var offers by mutableStateOf<List<Offers>>(emptyList()); private set
    var offersisLoading by mutableStateOf(true)

    var isNetworkAvailable by mutableStateOf(true)

    suspend fun uploadFoodMenuFromApi(): String {
        return try {
            foodMenuListisLoading = true
            // بنجيب الأكل والمطاعم باستخدام الـ RetrofitInstance المطور بتاعنا
            foodMenuList = RetrofitInstance.api.foodmenu()
            "Success"
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
            restaurantsMenu = RetrofitInstance.api.restaurants()
            "Success"
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
    suspend fun uploadSnacksMenuFromApi(): String {
        return try {
            snacksisLoading = true
            snacks = RetrofitInstance.api.snacksMenu()
            "Success"
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
}