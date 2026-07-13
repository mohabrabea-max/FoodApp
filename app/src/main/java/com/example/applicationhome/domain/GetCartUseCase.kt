package com.example.applicationhome.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.data.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository
){
    suspend fun plus(userid : String, food: CartItemsClass, size : String): Pair<String, CartItemsClass>?{
        val finally = withContext(Dispatchers.IO){
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
                    null
                }else{
                    Pair(size, food)
                }
            }else{
                val cartRestaurant = cartRepository.getCartRestaurantData(food)?: Restaurants()
                cartRepository.createNewCart(userid, food, size, food.type, food.priceOfOne, cartRestaurant, finalNumber)
                null
            }
        }
        return finally
    }

    suspend fun updateCount(userid : String, food : CartItemsClass, size : String, newCount : Int): Pair<String, CartItemsClass>? {
        val finally = withContext(Dispatchers.IO){
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
                    null
                }else{
                    Pair(size, food)
                }
            }else{
                val cartRestaurant = cartRepository.getCartRestaurantData(food)?: Restaurants()
                cartRepository.createNewCart(userid, food, size, food.type, food.priceOfOne, cartRestaurant, newCount)
                null
            }
        }
        return finally
    }

    suspend fun minus(userid : String, food: CartItemsClass, size : String){
        withContext(Dispatchers.IO){
            val cartItems = cartRepository.cartItems.value
            val mealKey = "${food.mealId}_${size}"
            var finalNumber by mutableStateOf(0)
            val cartItem = cartItems.find { it?.mealKey == mealKey }
            if(cartItem != null){
                if(cartItem.quantity == 1){
                    cartRepository.deleteFromCart(userid, food.mealId, size)
                }else{
                    finalNumber = cartItem.quantity - 1
                    cartRepository.updateQuantity(userid, food, size, food.priceOfOne, finalNumber)
                }
            }
        }
    }

    suspend fun clearAndStartNewCart(userid : String, newFoodInCart : CartItemsClass?, newFoodInCartSize : String?): Boolean{
        val state = withContext(Dispatchers.IO){
            cartRepository.deleteAllCart(userid)
            cartRepository.deleteParentCart(userid)
            if(newFoodInCart != null && newFoodInCartSize != null){
                true
            }else{
                false
            }
        }
        return state
    }

    suspend fun clearAllCart(userid : String){
        withContext(Dispatchers.IO){
            cartRepository.deleteAllCart(userid)
        }
    }

    suspend fun delete(userid : String, foodId: Int, size : String){
        withContext(Dispatchers.IO){
            cartRepository.deleteFromCart(userid, foodId, size)
        }
    }
}