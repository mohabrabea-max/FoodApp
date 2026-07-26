package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.local.entity.CartItemsClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository
){
    suspend fun plus(userid : String, food: CartItemsClass, size : String): Pair<String, CartItemsClass>?{
        if(userid.isEmpty()) return Pair("User Id Is Empty", CartItemsClass())

        val cartItems = cartRepository.cartItems.value
        val mealKey = "${food.mealId}_${size}"
        val currentItem = cartItems.find { it?.mealKey == mealKey }
        val finalNumber = if (currentItem != null){
            if(currentItem.quantity == 99){
                99
            }else{
                currentItem.quantity + 1
            }
        }else{
            1
        }
        if(cartItems.isNotEmpty()){
            val currentCart = cartRepository.cartInformation.filterNotNull().first()
            if(food.restaurantId == currentCart.restaurantId){
                if(cartItems.find { it?.mealKey == mealKey } != null){
                    cartRepository.updateQuantity(userid, food, size, food.priceOfOne, finalNumber)
                }else{
                    cartRepository.addMealToCart(userid, food, size, food.type, food.priceOfOne, finalNumber)
                }
                return null
            }else{
                return Pair(size, food)
            }
        }else{
            val cartRestaurant = cartRepository.getCartRestaurantData(food)?: Restaurants()
            cartRepository.createNewCart(userid, food, size, food.type, food.priceOfOne, cartRestaurant, finalNumber)
            return null
        }
    }

    suspend fun updateCount(userid : String, food : CartItemsClass, size : String, newCount : Int): Pair<String, CartItemsClass>? {
        if(userid.isEmpty()) return Pair("User Id Is Empty", CartItemsClass())

        val cartItems = cartRepository.cartItems.value
        val mealKey = "${food.mealId}_${size}"
        val cartItem = cartItems.find { it?.mealKey == mealKey }

        if(cartItems.isNotEmpty()){
            val currentCart = cartRepository.cartInformation.filterNotNull().first()
            if(food.restaurantId == currentCart.restaurantId){
                if(cartItem != null){
                    val finalNumber =
                        if(cartItem.quantity + newCount > 99){
                            99
                        }else{
                            cartItem.quantity + newCount
                        }
                    cartRepository.updateQuantity(userid, food, size, food.priceOfOne, finalNumber)
                }else{

                    cartRepository.addMealToCart(userid, food, size, food.type, food.priceOfOne, newCount)
                }
                return null
            }else{
                return Pair(size, food)
            }
        }else{
            val cartRestaurant = cartRepository.getCartRestaurantData(food)?: Restaurants()
            cartRepository.createNewCart(userid, food, size, food.type, food.priceOfOne, cartRestaurant, newCount)
            return null
        }
    }

    suspend fun minus(userid : String, food: CartItemsClass, size : String){
        val cartItems = cartRepository.cartItems.value
        val mealKey = "${food.mealId}_${size}"
        val cartItem = cartItems.find { it?.mealKey == mealKey }
        if(cartItem != null){
            if(cartItem.quantity == 1){
                cartRepository.deleteFromCart(userid, food.mealId, size)
            }else{
                val finalNumber = cartItem.quantity - 1
                cartRepository.updateQuantity(userid, food, size, food.priceOfOne, finalNumber)
            }
        }
    }

    suspend fun clearAndStartNewCart(userid : String, newFoodInCart : CartItemsClass?, newFoodInCartSize : String?): Boolean{
        cartRepository.deleteAllCart(userid)
        cartRepository.deleteParentCart(userid)
        if(newFoodInCart != null && newFoodInCartSize != null){
            return true
        }else{
            return false
        }
    }

    suspend fun clearAllCart(userid : String){
        cartRepository.deleteAllCart(userid)
        cartRepository.deleteParentCart(userid)
    }

    suspend fun delete(userid : String, foodId: Int, size : String){
        cartRepository.deleteFromCart(userid, foodId, size)
    }
}