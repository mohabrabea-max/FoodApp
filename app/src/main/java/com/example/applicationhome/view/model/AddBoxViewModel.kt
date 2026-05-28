package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.repository.CartRepository.addMealToCart
import com.example.applicationhome.data.models.repository.CartRepository.cartItems
import com.example.applicationhome.data.models.repository.CartRepository.deleteFromCart
import com.example.applicationhome.data.models.repository.CartRepository.minusFromCart
import com.example.applicationhome.data.models.repository.CartRepository.totalCart
import com.example.applicationhome.data.models.repository.CartRepository.totalPrice
import com.example.applicationhome.data.models.repository.CartRepository.updateTotals
import kotlinx.coroutines.launch

class AddBoxViewModel : ViewModel(){
    var activId by mutableStateOf<Int?>(null)

    fun plus(foodId: Int, size : String){
        viewModelScope.launch {
            val mealKey = "${foodId}_$size"
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
            addMealToCart(foodId, size, finalNumber)
            updateTotals()
        }
    }

    fun minus(foodId: Int, size : String){
        viewModelScope.launch {
            val mealKey = "${foodId}_$size"
            var finalNumber by mutableStateOf(0)
            if(cartItems[mealKey]?.number == 1){
                deleteFromCart(foodId, size)
            }else{
                finalNumber = cartItems[mealKey]!!.number - 1
                minusFromCart(foodId, size, finalNumber)
            }
            updateTotals()
        }
    }

    fun updateCount(foodId: Int, size : String, newCount: Int) {
        viewModelScope.launch {
            val mealKey = "${foodId}_$size"
            val currentItem = cartItems[mealKey]
            if (currentItem != null) cartItems[mealKey] = currentItem.copy(number = newCount)
            addMealToCart(foodId, size, newCount)
            updateTotals()
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            deleteFromCart(foodId, size)
        }
        totalCart = 0
        updateTotals()
    }

    fun bay(){
        totalPrice.value = 0.0
        cartItems.clear()
        totalCart = 0
        updateTotals()
    }
    fun active(foodId : Int){
        activId = foodId
    }
}