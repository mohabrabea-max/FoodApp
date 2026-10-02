package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.data.mapper.mealDomainToCartItemsDomainClass
import com.example.applicationhome.core.data.mapper.snackDomainToCartItemsDomainClass
import com.example.applicationhome.core.domain.model.AddToCartStates
import com.example.applicationhome.core.domain.model.CartItemsDomainClass
import com.example.applicationhome.core.domain.model.CategoryEnum
import com.example.applicationhome.core.domain.repository.CartRepository
import javax.inject.Inject

class CartUseCase @Inject constructor(
    private val cartRepository : CartRepository
){
    suspend fun plus(userId : String, mealId : Int, size : String, type : CategoryEnum, quantityToAdd : Int = 1): AddToCartStates {
        if(userId.isEmpty()) return AddToCartStates.ErrorInLoginState()

        val mealKey = "${mealId}_${size}"
        val cartItems = cartRepository.cartItems.value

        val domainFood = when(type){
            CategoryEnum.SNACKS -> {
                cartRepository.getSnackByIdFromDatabase(mealId)
                    ?.snackDomainToCartItemsDomainClass(userId, size, quantityToAdd)
            }

            else -> {
                cartRepository.getMealByIdFromDatabase(mealId)
                    ?.mealDomainToCartItemsDomainClass(userId, size, quantityToAdd)
            }
        }
        if(domainFood == null){
            return AddToCartStates.ErrorInCartRestaurant(food = CartItemsDomainClass(), size = size)
        }

        if(cartItems.isEmpty()){
            val cartRestaurant = cartRepository.getCartRestaurantData(domainFood.restaurantId)
            cartRepository.createNewCart(
                userId = userId,
                food = domainFood,
                size = size,
                type = domainFood.type,
                priceOfOne = domainFood.priceOfOne,
                res = cartRestaurant,
                number = quantityToAdd
            )

            return AddToCartStates.Success
        }


        val currentCart = cartRepository.cartInformation.value

        if(domainFood.restaurantId != currentCart?.restaurantId){
            return AddToCartStates.ErrorInCartRestaurant(food = domainFood, size = size)
        }

        val cartItem = cartItems.find { it.mealKey == mealKey }

        if(cartItem == null){
            cartRepository.addMealToCart(
                userId,
                domainFood,
                size,
                domainFood.type,
                domainFood.priceOfOne,
                quantityToAdd
            )
            return AddToCartStates.Success
        }

        if(cartItem.quantity >= 99) return AddToCartStates.Success

        val finalNumber = (cartItem.quantity + quantityToAdd).coerceAtMost(99)
        cartRepository.updateQuantity(userId, mealKey, size, domainFood.priceOfOne, finalNumber)

        return AddToCartStates.Success
    }


    suspend fun addMoreThanOneItem(
        userId : String,
        foods : List<CartItemsDomainClass>,
        resId : Int,
        resName : String,
        resImage : String
    ): AddToCartStates {
        if(userId.isEmpty()) return AddToCartStates.ErrorInLoginState()

        cartRepository.createNewCartForMoreThanOneItem(
            userId = userId,
            foods = foods,
            resId = resId,
            resName = resName,
            resImage = resImage
        )

        return AddToCartStates.Success
    }


    suspend fun minus(userId : String, mealId : Int, size : String){
        val cartItems = cartRepository.cartItems.value

        val mealKey = "${mealId}_${size}"
        val cartItem = cartItems.find { it.mealKey == mealKey } ?: return

        if(cartItem.quantity <= 1){
            cartRepository.deleteFromCart(userId, mealId, size)
        }else{
            val finalNumber = cartItem.quantity - 1
            cartRepository.updateQuantity(userId, mealKey, size, cartItem.priceOfOne, finalNumber)
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