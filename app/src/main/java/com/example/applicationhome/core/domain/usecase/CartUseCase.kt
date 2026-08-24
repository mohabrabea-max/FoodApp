package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.data.data.model.AddToCartStates
import com.example.applicationhome.data.local.entity.CartItemsClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository
){
    suspend fun plus(userid : String, food: CartItemsClass, size : String): AddToCartStates {
        if(userid.isEmpty()) return AddToCartStates.ErrorInLoginState()

        val cartItems = cartRepository.cartItems.value

        if(cartItems.isEmpty()){
            val cartRestaurant = cartRepository.getCartRestaurantData(food)
            cartRepository.createNewCart(userid, food, size, food.type, food.priceOfOne, cartRestaurant, 1)
            return AddToCartStates.Success
        }


        val currentCart = cartRepository.cartInformation.value

        if(food.restaurantId != currentCart?.restaurantId){
            return AddToCartStates.ErrorInCartRestaurant(food = food, size = size)
        }


        val mealKey = "${food.mealId}_${size}"
        val currentItem = cartItems.find { it.mealKey == mealKey }

        if(currentItem == null) {
            cartRepository.addMealToCart(userid, food, size, food.type, food.priceOfOne, 1)
            return AddToCartStates.Success
        }

        if(currentItem.quantity >= 99) return AddToCartStates.Success

        val finalNumber = currentItem.quantity + 1
        cartRepository.updateQuantity(userid, food, size, food.priceOfOne, finalNumber)

        return AddToCartStates.Success
    }


    suspend fun updateCount(userid : String, food : CartItemsClass, size : String, quantityToAdd : Int): AddToCartStates {
        if(userid.isEmpty()) return AddToCartStates.ErrorInLoginState()

        val cartItems = cartRepository.cartItems.value

        if(cartItems.isEmpty()){
            val cartRestaurant = cartRepository.getCartRestaurantData(food)
            cartRepository.createNewCart(userid, food, size, food.type, food.priceOfOne, cartRestaurant, quantityToAdd)

            return AddToCartStates.Success
        }


        val currentCart = cartRepository.cartInformation.value

        if(food.restaurantId != currentCart?.restaurantId){
            return AddToCartStates.ErrorInCartRestaurant(food = food, size = size)
        }


        val mealKey = "${food.mealId}_${size}"
        val cartItem = cartItems.find { it.mealKey == mealKey }

        if(cartItem == null){
            cartRepository.addMealToCart(userid, food, size, food.type, food.priceOfOne, quantityToAdd)
            return AddToCartStates.Success
        }

        if(cartItem.quantity >= 99){
            return AddToCartStates.Success
        }

        val finalNumber =
            if(cartItem.quantity + quantityToAdd >= 99){
                99
            }else{
                cartItem.quantity + quantityToAdd
            }

        cartRepository.updateQuantity(userid, food, size, food.priceOfOne, finalNumber)

        return AddToCartStates.Success
    }

    suspend fun minus(userid : String, food: CartItemsClass, size : String){
        val cartItems = cartRepository.cartItems.value
        val mealKey = "${food.mealId}_${size}"
        val cartItem = cartItems.find { it.mealKey == mealKey } ?: return

        if(cartItem.quantity <= 1){
            cartRepository.deleteFromCart(userid, food.mealId, size)
        }else{
            val finalNumber = cartItem.quantity - 1
            cartRepository.updateQuantity(userid, food, size, food.priceOfOne, finalNumber)
        }
    }

    suspend fun clearAndStartNewCart(userid : String, newFoodInCart : CartItemsClass?, newFoodInCartSize : String?): Boolean{
        cartRepository.deleteAllCart(userid)
        cartRepository.deleteParentCart(userid)

        return newFoodInCart != null && newFoodInCartSize != null
    }

    suspend fun clearAllCart(userid : String){
        cartRepository.deleteAllCart(userid)
        cartRepository.deleteParentCart(userid)
    }

    suspend fun delete(userid : String, foodId: Int, size : String){
        cartRepository.deleteFromCart(userid, foodId, size)
    }
}