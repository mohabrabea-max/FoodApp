package com.example.applicationhome.data.models.repository

import com.example.applicationhome.data.models.local.CartClass
import com.example.applicationhome.data.models.local.CartDao
import com.example.applicationhome.data.models.local.CartItemsClass
import com.example.applicationhome.data.models.model.Food
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.remote.RetrofitInstance
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuList
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartdao: CartDao) {

    var userId: String = ""
        private set

    fun setUserId(userid: String) {
        userId = userid
    }
    fun getCartItems(): Flow<List<CartItemsClass?>> = cartdao.getCartItems(userId)

    fun getCartData() : Flow<CartClass?> = cartdao.getParentCart(userId)

    suspend fun getCartRestaurantData(food : Food) : Restaurants?{
        return try {
            val response = RetrofitInstance.api.getCarRestaurant("\"id\"", food.restaurantId)
            val resData = response.body()?.values?.first()
            if(response.isSuccessful && resData != null){
                resData
            }else{
                null
            }
        } catch (e : Exception){
            null
        }
    }

    suspend fun createNewCart(food: Food, size : String, type : String, priceOfOne : Double, res : Restaurants, number: Int = 1) : String{
        val mealKey = "${food.id}_$size"
        val cartObject = CartClass(userId, res.id, res.name, res.image)
        return try {
            val cartItemsObject = CartItemsClass(
                userId,
                mealKey,
                food.id,
                food.name,
                type,
                size,
                number,
                priceOfOne,
                priceOfOne * number,
                food.image.first()
            )
            cartdao.createParentCart(cartObject)
            cartdao.addCartItem(cartItemsObject)
            "Success"
        } catch (e : Exception){
            "Error"
        } finally {
            ""
        }
    }

    suspend fun addMealToCart(food: Food, size : String, type : String, priceOfOne : Double, number: Int = 1): String{
        val mealKey = "${food.id}_$size"
        val cartItemsObject = CartItemsClass(
            userId,
            mealKey,
            food.id,
            food.name,
            type,
            size,
            number,
            priceOfOne,
            priceOfOne * number,
            food.image.first()
        )
        return try {
            cartdao.addCartItem(cartItemsObject)
            "Success"
        }catch (e : Exception){
            "Error"
        }
    }

    suspend fun updateQuantity(food: Food, size : String, type : String, priceOfOne : Double, number: Int): String{
        val mealKey = "${food.id}_$size"
        val cartItemsObject = CartItemsClass(
            userId,
            mealKey,
            food.id,
            food.name,
            type,
            size,
            number,
            priceOfOne,
            priceOfOne * number,
            food.image.first()
        )
        return try {
            cartdao.updateCartItem(cartItemsObject)
            "Success"
        }catch (e : Exception){
            "Error"
        }
    }

    suspend fun deleteFromCart(foodId: Int, size : String): String{
        val mealKey = "${foodId}_${size}"
        return try {
            cartdao.deleteItemFromCart(mealKey, userId)
            "Success"
        }catch (e : Exception){
            "Error"
        }
    }

    suspend fun deleteAllCart(): String{
        return try {
            cartdao.deleteParentCart(userId)
            "Success"
        }catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun getMeal(mealId : Int): FoodItem? {
        val mealKey = "Meal_${mealId}"
        if(foodMenuList[mealKey] != null){
            return foodMenuList["Meal_${mealId}"]
        }else{
            return try {
                val response = RetrofitInstance.api.getCartMeal(mealKey)
                if(response.isSuccessful){
                    return response.body()
                }else{
                    null
                }
            }catch (e : Exception){
                null
            }
        }
    }
}