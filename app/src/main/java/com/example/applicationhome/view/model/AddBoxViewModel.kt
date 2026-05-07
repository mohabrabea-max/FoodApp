package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.data.models.Food
import com.example.applicationhome.data.models.FoodItem

class AddBoxViewModel : ViewModel(){
    var activId by mutableStateOf<Int?>(null)

    var totalCart by mutableStateOf(0)
    var cartMap = Cart.cartmap
    var totalPrice = mutableDoubleStateOf(0.0)
    var totalNumber = mutableStateOf(0)
    fun updateTotals() {
        totalNumber.value = 0
        totalPrice.value = 0.0
        // بنلف على كل العناصر اللي في السلة ونحسبها من الصفر
        for ((key, value) in cartMap) {
            val food = key.food
            val size = key.size
            if (food is FoodItem) {
                totalNumber.value += value
                val priceForSize = food.priceANDsize[size] ?: 0.0
                totalPrice.value += priceForSize * value
            }
        }
    }
    fun addBoxNumberPlus(food: Food, size : String){
        val key = CartKey(food, size)
        if((cartMap[key] ?: 0) < 99){
            totalCart = totalCart + 1
            activId = food.id
            if(cartMap.containsKey(key)){
                cartMap[key] = (cartMap[key] ?: 0) + 1
            }else{
                cartMap[key] = 1
            }
            updateTotals()
        }
    }

    fun addBoxNumberMinus(food: Food, size : String){
        val key = CartKey(food, size)
        if((cartMap[key] ?: 0) > 0){
            if(cartMap[key] == 1){
                cartMap.remove(key)
            }else{
                cartMap[key] = (cartMap[key] ?: 0) - 1
            }
            updateTotals()
        }
    }
    fun updateCount(food: Food, size : String, newCount: Int) {
        val key = CartKey(food, size)
        cartMap[key] = newCount
        updateTotals()
    }
    fun delete(food : Food, size: String){
        var key = CartKey(food, size)
        cartMap.remove(key)
        totalCart = 0
        updateTotals()
    }
    fun bay(){
        totalPrice.value = 0.0
        cartMap.clear()
        totalCart = 0
    }
    fun activ(food : Food){
        activId = food.id
    }
}