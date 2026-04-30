package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.data.models.Food
import com.example.applicationhome.data.models.FoodItem

class AddBoxViewModel : ViewModel(){
    var activId by mutableStateOf<Int?>(null)
    var itemsCount = mutableStateMapOf<CartKey, Int>()
    var totalCart = mutableStateOf(0)
    var cartMap = Cart.cartmap
    var totalPrice = mutableDoubleStateOf(0.0)
    fun addBoxNumberPlus(food: Food, size : String){
        val key = CartKey(food, size)
        if((itemsCount[key] ?: 0) < 99){
            totalCart.value = totalCart.value + 1
            activId = food.id
            if(itemsCount.containsKey(key)){
                itemsCount[key] = (itemsCount[key] ?: 0) + 1
            }else{
                itemsCount[key] = 1
            }
        }
    }
    fun addBoxNumberMinus(food: Food, size : String){
        val key = CartKey(food, size)
        if((itemsCount[key] ?: 0) > 0){
            if(itemsCount[key] == 1){
                itemsCount.remove(key)
            }else{
                itemsCount[key] = (itemsCount[key] ?: 0) - 1
            }
        }
    }
    fun updateCount(food: Food, size : String, newCount: Int) {
        val key = CartKey(food, size)
        itemsCount[key] = newCount
    }
    fun addToCart(food : Food, size: String){
        var key = CartKey(food, size)
        if(food is FoodItem){
            val priceForSize = food.priceANDsize[size] ?: 0.0
            totalPrice.value = totalPrice.value + (priceForSize * (itemsCount[key] ?: 0))
        }
        cartMap[key] = (cartMap[key] ?: 0) + (itemsCount[key] ?: 0)
        itemsCount.remove(key)
        totalCart.value = 0
    }
    fun bay(){
        cartMap.clear()
        itemsCount.clear()
        totalCart.value = 0
    }

    fun activ(food : Food){
        activId = food.id
    }

    fun cartPlus(food: Food, size : String){
        val key = CartKey(food, size)
        if((cartMap[key] ?: 0) < 99){
            activId = food.id
            if(cartMap.containsKey(key)){
                cartMap[key] = (cartMap[key] ?: 0) + 1
            }else{
                cartMap[key] = 1
            }
        }
    }
    fun cartMinus(food: Food, size : String){
        val key = CartKey(food, size)
        if(cartMap[key] == 1){
            cartMap.remove(key)
        }else{
            cartMap[key] = (cartMap[key] ?: 0) - 1
        }
    }
    fun updatecart(food: Food, size : String, newCount: Int) {
        val key = CartKey(food, size)
        cartMap[key] = newCount
    }
}