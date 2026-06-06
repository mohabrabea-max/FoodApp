package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.Food
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.repository.CartRepository.addMealToCart
import com.example.applicationhome.data.models.repository.CartRepository.cartItems
import com.example.applicationhome.data.models.repository.CartRepository.cartMeals
import com.example.applicationhome.data.models.repository.CartRepository.cartMealsMenu
import com.example.applicationhome.data.models.repository.CartRepository.cartSnacks
import com.example.applicationhome.data.models.repository.CartRepository.cartSnacksMenu
import com.example.applicationhome.data.models.repository.CartRepository.deleteFromCart
import com.example.applicationhome.data.models.repository.CartRepository.foodMenu
import com.example.applicationhome.data.models.repository.CartRepository.minusFromCart
import com.example.applicationhome.data.models.repository.CartRepository.snacksMenu
import com.example.applicationhome.data.models.repository.CartRepository.updateTotals
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddBoxViewModel : ViewModel(){
    var activId by mutableStateOf<Int?>(null)

    fun plus(food: Food, size : String){
        viewModelScope.launch {
            val mealKey = "${food.id}_$size"
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
            addMealToCart(food.id, size, finalNumber, type)
            cartMealsMenu = async { cartMeals(foodMenu) }.await().filterNotNull()
            cartSnacksMenu = async { cartSnacks(snacksMenu) }.await().filterNotNull()
            updateTotals()
        }
    }

    fun minus(foodId: Int, size : String){
        viewModelScope.launch {
            val mealKey = "${foodId}_$size"
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

    fun updateCount(food: Food, size : String, newCount: Int) {
        viewModelScope.launch {
            val mealKey = "${food.id}_$size"
            val currentItem = cartItems[mealKey]
            if (currentItem != null) cartItems[mealKey] = currentItem.copy(number = newCount)
            val type = when(food){
                is FoodItem -> {"Meal"}
                is Snack -> {"Snack"}
            }
            addMealToCart(food.id, size, newCount, type)
            updateTotals()
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            deleteFromCart(foodId, size)
        }
        updateTotals()
    }

    fun bay(){
        cartItems.clear()
        updateTotals()
    }
    fun active(foodId : Int){
        activId = foodId
    }
}