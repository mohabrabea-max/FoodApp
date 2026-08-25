package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.data.data.model.AddToCartStates
import com.example.applicationhome.data.local.entity.CartItemsClass
import javax.inject.Inject

class CartUseCase @Inject constructor(
    private val cartRepository : CartRepository
){
    suspend fun plus(userId : String, food: CartItemsClass, size : String, quantityToAdd : Int = 1): AddToCartStates {
        if(userId.isEmpty()) return AddToCartStates.ErrorInLoginState()

        val cartItems = cartRepository.cartItems.value

        if(cartItems.isEmpty()){
            val cartRestaurant = cartRepository.getCartRestaurantData(food)
            cartRepository.createNewCart(userId, food, size, food.type, food.priceOfOne, cartRestaurant, quantityToAdd)

            return AddToCartStates.Success
        }


        val currentCart = cartRepository.cartInformation.value

        if(food.restaurantId != currentCart?.restaurantId){
            return AddToCartStates.ErrorInCartRestaurant(food = food, size = size)
        }


        val mealKey = "${food.mealId}_${size}"
        val cartItem = cartItems.find { it.mealKey == mealKey }

        if(cartItem == null){
            cartRepository.addMealToCart(userId, food, size, food.type, food.priceOfOne, quantityToAdd)
            return AddToCartStates.Success
        }

        if(cartItem.quantity >= 99) return AddToCartStates.Success

        val finalNumber = (cartItem.quantity + quantityToAdd).coerceAtMost(99)
        cartRepository.updateQuantity(userId, food, size, food.priceOfOne, finalNumber)

        return AddToCartStates.Success
    }


    suspend fun minus(userId : String, food : CartItemsClass, size : String){
        val cartItems = cartRepository.cartItems.value
        val mealKey = "${food.mealId}_${size}"
        val cartItem = cartItems.find { it.mealKey == mealKey } ?: return

        if(cartItem.quantity <= 1){
            cartRepository.deleteFromCart(userId, food.mealId, size)
        }else{
            val finalNumber = cartItem.quantity - 1
            cartRepository.updateQuantity(userId, food, size, food.priceOfOne, finalNumber)
        }
    }


    suspend fun clearAllCart(userId : String){
        cartRepository.deleteAllCart(userId)
        cartRepository.deleteParentCart(userId)
    }


    suspend fun delete(userId : String, foodId: Int, size : String){
        cartRepository.deleteFromCart(userId, foodId, size)
    }
}