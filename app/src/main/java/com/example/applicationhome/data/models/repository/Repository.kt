package com.example.applicationhome.data.models.repository

import androidx.collection.LruCache
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.applicationhome.data.models.model.CartClass
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.Drink
import com.example.applicationhome.data.models.model.FavoriteClass
import com.example.applicationhome.data.models.model.FirebasePostResponse
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Offers
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.model.UserClass
import com.example.applicationhome.data.models.remote.RetrofitInstance
import com.example.applicationhome.data.models.repository.CartRepository.cartSnacksMenu
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuList
import com.example.applicationhome.data.models.repository.MenuRepository.snacks
import com.example.applicationhome.data.models.repository.UserRepository.userId
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
//    suspend fun addToMeals(){
//        try {
//            snacks.forEach { (key, value) ->
//                RetrofitInstance.api.addToMeals(key, mapOf("restaurantId" to 0))
//            }
//        }catch (e : Exception){
//            println("")
//        }
//    }

    suspend fun getUserData(emailstate : String, passwordstate : String?): String {
        return try {
            val formatEmail = "\"$emailstate\""
            val response = RetrofitInstance.api.getUserData(order = "\"email\"", value = formatEmail)
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
    var totalPrice = mutableDoubleStateOf(0.0)
    var totalNumber = mutableStateOf(0)
    var cartItems = mutableStateMapOf<String, CartClass>()
    val foodMenu get() = cartItems.filter { it.value.type == "Meal" }
    val snacksMenu get() = cartItems.filter { it.value.type == "Snack" }
    var cartMealsMenu by mutableStateOf<List<FoodItem>>(emptyList())
    var cartSnacksMenu by mutableStateOf<List<Snack>>(emptyList())



    fun updateTotals() {
        totalNumber.value = 0
        totalPrice.value = 0.0
        cartItems.forEach { (key, value) ->
            if(value.type == "Meal"){
                val meal = cartMealsMenu.find { it.id == value.id }
                val number = foodMenu.values.find { it.id == value.id }?.number ?: 0
                if(meal != null){
                    totalPrice.value = totalPrice.value + (meal.sizeOptions.find { it.size == value.size }?.price ?: 0.0) * number
                }
                totalNumber.value += 1
            }else if(value.type == "Snack"){
                val snack = cartSnacksMenu.find { it.id == value.id }?.priceANDsize
                val number = snacksMenu.values.find { it.id == value.id }?.number ?: 0
                if(snack != null){
                    totalPrice.value = totalPrice.value + (snack[value.size] ?: 0.0) * number
                }
                totalNumber.value += 1
            }
        }

    }
    suspend fun getcart(): String {
        return try {
            val response = RetrofitInstance.api.getCart(userId)
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

    suspend fun cartMeals(): List<FoodItem> {
        var finalMealsList by mutableStateOf<List<FoodItem>>(emptyList())
        val missingItems = mutableListOf<Int>()
        foodMenu.forEach { item ->
            val cachedMeal = foodMenuList.find { it.id == item.value.id }
            if(cachedMeal != null){
                cartMealsMenu += cachedMeal
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
                                if(resultMap != null){
                                    foodMenuList = foodMenuList + resultMap.values
                                }
                            }else{
                                null
                            }
                        } catch (e : Exception){
                            null
                        }
                    }
                }
                finalMealsList += deferredRequests.awaitAll().filterNotNull() as List<FoodItem>
            }
        }
        return finalMealsList
    }

    suspend fun cartSnacks(): List<Snack> {
        var finalSnacksList by mutableStateOf<List<Snack>>(emptyList())
        val missingItems = mutableListOf<Int>()
        snacksMenu.forEach { item ->
            val cachedMeal = snacks.find { it.id == item.value.id }
            if(cachedMeal != null){
                cartSnacksMenu += cachedMeal
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
                                if(resultMap != null){
                                    snacks = snacks + resultMap.values
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
                        }
                    }
                }
                finalSnacksList += deferredRequests.awaitAll().filterNotNull()
            }
        }
        return finalSnacksList
    }

    suspend fun addMealToCart(foodId: Int, size : String, number: Int, type : String): String{
        var mealKey by mutableStateOf("${foodId}_$size")

        if(!cartItems.keys.contains(mealKey)){
            val cartObject = CartClass(foodId, type, size, 1)
            return try {
                val response = RetrofitInstance.api.addToCart(userId, mealKey, cartObject)
                if(response.isSuccessful){
                    val cartItem = response.body()
                    cartItems[mealKey] = CartClass(cartItem!!.id, cartItem.type, cartItem.size, cartItem.number)
                    updateTotals()
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
                        updateTotals()
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
                updateTotals()
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
                cartMealsMenu = cartMealsMenu.filterNot { it.id == foodId }
                cartSnacksMenu = cartSnacksMenu.filterNot { it.id == foodId }
                updateTotals()
                "Success"
            }else{
                "Network error"
            }
        }catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }
}

object FavoriteRepository {
    var favoritList = mutableStateMapOf<String, FavoriteClass>()

    val mealsFavoriteMenu get() = favoritList.filter { it.value.typ == "Meal" }.values.toList()

    val snacksFavoriteMenu get() = favoritList.filter { it.value.typ == "Snack" }.values.toList()

    val restaurantsFavoriteMenu get() = favoritList.filter { it.value.typ == "Restaurant" }.values.toList()

    var mealsFavorite by mutableStateOf<List<FoodItem>>(emptyList())

    var snacksFavorite by mutableStateOf<List<Snack>>(emptyList())

    var restaurantsFavorite by mutableStateOf<List<Restaurants>>(emptyList())


    suspend fun favoriteMeals(): List<FoodItem> {
        var finalMealsList by mutableStateOf<List<FoodItem>>(emptyList())
        val missingItems = mutableListOf<Int>()
        mealsFavoriteMenu.forEach { item ->
            val cachedMeal = foodMenuList.find { it.id == item.id }
            if(cachedMeal != null){
                mealsFavorite += cachedMeal
            }else{
                missingItems.add(item.id)
            }
        }
        if(missingItems.isNotEmpty()){
            coroutineScope {
                val deferredRequests = missingItems.map { item ->
                    async {
                        try {
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
                        }
                    }
                }
                finalMealsList += deferredRequests.awaitAll().filterNotNull()
            }
        }
        return finalMealsList
    }

    suspend fun favoriteSnacks(): List<Snack> {
        var finalSnacksList by mutableStateOf<List<Snack>>(emptyList())
        val missingItems = mutableListOf<Int>()
        snacksFavoriteMenu.forEach { item ->
            val cachedMeal = snacks.find { it.id == item.id }
            if(cachedMeal != null){
                cartSnacksMenu += cachedMeal
            }else{
                missingItems.add(item.id)
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
                        }
                    }
                }
                finalSnacksList += deferredRequests.awaitAll().filterNotNull()
            }
        }
        return finalSnacksList
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


object MenuRepository {
    var foodMenuList = LruCache<Int, FoodItem>(50)
    var foodMenuListisLoading by mutableStateOf(true)


    var restaurantsMenu by mutableStateOf<List<Restaurants>>(emptyList())
    var restaurantsMenuisLoading by mutableStateOf(true)


    var categories by mutableStateOf<List<Categories>>(emptyList()); private set
    var categoriesisLoading by mutableStateOf(true)


    var snacks by mutableStateOf<List<Snack>>(emptyList())
    var snacksisLoading by mutableStateOf(true)


    var drinkMenu = mutableStateMapOf<String, Drink>()//; private set
    var drinkMenuisLoading by mutableStateOf(true)


    var offers by mutableStateOf<List<Offers>>(emptyList()); private set
    var offersisLoading by mutableStateOf(true)

    var restaurantOffers by mutableStateOf<List<Offers>>(emptyList()); private set
    var restaurantOffersLoading by mutableStateOf(true)

    var isNetworkAvailable by mutableStateOf(true)



    suspend fun uploadFoodMenuFromApi(resId : Int): String {
        return try {
            foodMenuListisLoading = true
            // بنجيب الأكل والمطاعم باستخدام الـ RetrofitInstance المطور بتاعنا
            val response = RetrofitInstance.api.foodmenu("\"restaurantId\"", resId)
            val foodMenu = response.body()
            if(response.isSuccessful && foodMenu != null){
                foodMenuList = foodMenuList + foodMenu.values
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
                restaurantsMenu = restaurantsMenu + restaurants.values
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
                snacks = snacks + snacksMenu.values
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