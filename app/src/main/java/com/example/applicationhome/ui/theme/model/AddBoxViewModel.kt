package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.Food
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.repository.CartRepository.addMealToCart
import com.example.applicationhome.data.models.repository.CartRepository.allCart
import com.example.applicationhome.data.models.repository.CartRepository.cartItems
import com.example.applicationhome.data.models.repository.CartRepository.cartMeals
import com.example.applicationhome.data.models.repository.CartRepository.cartMealsMenu
import com.example.applicationhome.data.models.repository.CartRepository.cartSnacks
import com.example.applicationhome.data.models.repository.CartRepository.cartSnacksMenu
import com.example.applicationhome.data.models.repository.CartRepository.createNewCart
import com.example.applicationhome.data.models.repository.CartRepository.deleteAllCart
import com.example.applicationhome.data.models.repository.CartRepository.deleteFromCart
import com.example.applicationhome.data.models.repository.CartRepository.getCartRestaurantData
import com.example.applicationhome.data.models.repository.CartRepository.minusFromCart
import com.example.applicationhome.data.models.repository.CartRepository.totalNumber
import com.example.applicationhome.data.models.repository.CartRepository.totalPrice
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddBoxViewModel : ViewModel(){
    var activId by mutableStateOf<Int?>(null)
    var errorInCart by mutableStateOf(false)
    var newFoodInCart by mutableStateOf<Food?>(null)
    var newFoodInCartSize by mutableStateOf<String?>(null)
    var newCount by mutableStateOf(0)


    init {
        viewModelScope.launch {
            snapshotFlow { Triple(cartItems.size, cartMealsMenu.size, cartSnacksMenu.size) } //هنا خلينا البتاعة دي تنفذ الابديت لما ال3 حاجات دول حاجة فيهم تتغير
                .collect { updateTotals() }
        }
    }

    fun plus(food: Food, size : String){
        viewModelScope.launch {
            val mealKey = "${food.id}_${size}"
            val currentItem = cartItems[mealKey]
            val finalNumber = if (currentItem != null){
                if(currentItem.number == 99){
                    99
                }else{
                    currentItem.number + 1
                }
            }else{
                1
            }
            val type = when(food){
                is FoodItem -> {"Meal"}
                is Snack -> {"Snack"}
            }
            if(cartItems.isNotEmpty()){
                if(food.restaurantId == allCart.value.restaurantId){
                    addMealToCart(food.id, size, finalNumber, type)
                }else{
                    alertDialogTrue()
                    newFoodInCart = food
                    newFoodInCartSize = size
                }
            }else{
                getCartRestaurantData(food)
                createNewCart(food, size, type)
            }

            val mealsDeferred = async { cartMeals() }
            val snacksDeferred = async { cartSnacks() }
            cartMealsMenu += mealsDeferred.await()
            cartSnacksMenu += snacksDeferred.await()

            updateTotals()
        }
    }

    fun clearAndStartNewCart() {
        viewModelScope.launch {
            deleteAllCart()
            totalPrice = 0.0
            totalNumber.value = 0
            val newFood = newFoodInCart
            val newSize = newFoodInCartSize
            if(newFood != null && newSize != null){
                plus(newFood, newSize)
            }
            newFoodInCart = null
            newFoodInCartSize = null
        }
    }

    fun minus(foodId: Int, size : String){
        viewModelScope.launch {
            val mealKey = "${foodId}_${size}"
            var finalNumber by mutableStateOf(0)
            val cartItem = cartItems[mealKey]
            if(cartItem != null){
                if(cartItem.number == 1){
                    deleteFromCart(foodId, size)
                }else{
                    finalNumber = cartItem.number - 1
                    minusFromCart(foodId, size, finalNumber)
                }
                updateTotals()
            }
        }
    }

    fun updateCount(food : Food, size : String, newCount : Int) {
        viewModelScope.launch {
            val mealKey = "${food.id}_${size}"
            val currentItem = cartItems[mealKey]
            val finalNumber = if (currentItem != null){
                if(currentItem.number == 99){
                    99
                }else{
                    currentItem.number + newCount
                }
            }else{
                1
            }
            val type = when(food){
                is FoodItem -> {"Meal"}
                is Snack -> {"Snack"}
            }
            if(cartItems.isNotEmpty()){
                if(food.restaurantId == allCart.value.restaurantId){
                    addMealToCart(food.id, size, finalNumber, type)
                }else{
                    alertDialogTrue()
                    newFoodInCart = food
                    newFoodInCartSize = size
                }
            }else{
                getCartRestaurantData(food)
                createNewCart(food, size, type, newCount)
            }
            val mealsDeferred = async { cartMeals() }
            val snacksDeferred = async { cartSnacks() }
            cartMealsMenu += mealsDeferred.await()
            cartSnacksMenu += snacksDeferred.await()
            updateTotals()
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            deleteFromCart(foodId, size)
        }
        updateTotals()
    }

    fun updateTotals() {
        totalNumber.value = 0
        totalPrice = 0.0
        cartItems.forEach { (key, value) ->
            if(value.type == "Meal"){
                val meal = cartMealsMenu["Meal_${value.id}"]
                val number = value.number
                if(meal != null){
                    totalPrice += (meal.sizeOptions.find { it.size == value.size }?.price ?: 0.0) * number
                }
                totalNumber.value += number
            }else if(value.type == "Snack"){
                val snack = cartSnacksMenu["Snack_${value.id}"]
                val number = value.number
                if(snack != null){
                    totalPrice += (snack.priceANDsize[value.size] ?: 0.0) * number
                }
                totalNumber.value += number
            }
        }
    }

    fun active(foodId : Int){
        activId = foodId
    }

    fun alertDialogTrue(){
        errorInCart = true
    }

    fun alertDialogFalse(){
        errorInCart = false
    }

    fun plusnewCount(){
        newCount += 1
    }

    fun minusnewCount(){
        newCount -= 1
    }

    fun deletenewCount(){
        newCount = 0
    }
}