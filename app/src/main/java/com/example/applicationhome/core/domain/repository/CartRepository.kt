package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.core.data.local.entity.CartClass
import com.example.applicationhome.core.data.local.entity.CartItemWithDiscount
import com.example.applicationhome.core.data.remote.dto.Restaurants
import com.example.applicationhome.core.domain.model.CartItemsDomainClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val cartInformation : StateFlow<CartClass?>
    val cartItems : StateFlow<List<CartItemsDomainClass>>

    val totalNumber : StateFlow<Int>
    val totalPrice : StateFlow<Double>


    fun getCartItems(id : String): Flow<List<CartItemsDomainClass?>>

    suspend fun getOneCartItem(mealKey : String, userId : String): CartItemWithDiscount?

    fun getCartData(id : String) : Flow<CartClass?>

    suspend fun getCartRestaurantData(resId : Int) : Restaurants

    suspend fun createNewCart(
        userId : String,
        food: CartItemsDomainClass,
        size : String,
        type : String,
        priceOfOne : Double,
        res : Restaurants,
        number: Int
    ) : String

    suspend fun createNewCartForMoreThanOneItem(
        userId : String,
        foods : List<CartItemsDomainClass>,
        resId : Int,
        resName : String,
        resImage : String
    ): String

    suspend fun addMealToCart(
        userId : String,
        food: CartItemsDomainClass,
        size : String,
        type : String,
        priceOfOne : Double,
        number: Int
    ): String

    suspend fun updateQuantity(
        userId : String,
        mealKey : String,
        size : String,
        priceOfOne : Double,
        number: Int
    ): String

    suspend fun deleteFromCart(userId : String, foodId: Int, size : String): String

    suspend fun deleteParentCart(userId : String): String

    suspend fun deleteAllCart(userId : String): String
}