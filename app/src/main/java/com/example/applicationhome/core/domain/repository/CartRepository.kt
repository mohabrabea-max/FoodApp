package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.local.entity.CartClass
import com.example.applicationhome.data.local.entity.CartItemsClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val cartInformation: StateFlow<CartClass?>
    val cartItems: StateFlow<List<CartItemsClass>>

    val totalNumber: StateFlow<Int>
    val totalPrice : StateFlow<Double>


    fun getCartItems(id : String): Flow<List<CartItemsClass?>>

    fun getCartData(id : String) : Flow<CartClass?>

    suspend fun getCartRestaurantData(food : CartItemsClass) : Restaurants

    suspend fun createNewCart(
        userId : String,
        food: CartItemsClass,
        size : String,
        type : String,
        priceOfOne : Double,
        res : Restaurants,
        number: Int
    ) : String

    suspend fun addMealToCart(
        userId : String,
        food: CartItemsClass,
        size : String,
        type : String,
        priceOfOne : Double,
        number: Int
    ): String

    suspend fun updateQuantity(
        userId : String,
        food: CartItemsClass,
        size : String,
        priceOfOne : Double,
        number: Int
    ): String

    suspend fun deleteFromCart(userId : String, foodId: Int, size : String): String

    suspend fun deleteParentCart(userId : String): String

    suspend fun deleteAllCart(userId : String): String
}