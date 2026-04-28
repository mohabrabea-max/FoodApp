package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.data.models.Food

class AddBoxViewModel : ViewModel(){
    var activId by mutableStateOf<Int?>(null)
    var itemsCount = mutableStateMapOf<CartKey, Int>()
    var totalCart = mutableStateOf(0)
    var cartMap = Cart.cartmap
    fun addBoxNumberPlus(food: Food, size : String){
        val key = CartKey(food, size)
        var current = itemsCount[key] ?: 0
        if(current < 99){
            totalCart.value = totalCart.value + 1
            activId = food.id
            if(cartMap.containsKey(key)){
                itemsCount[key] = (itemsCount[key] ?: 0) + 1
                cartMap[key] = (cartMap[key] ?: 0) + 1
            }else{
                itemsCount[key] = 1
                cartMap[key] = 1
            }
        }
    }
    fun addBoxNumberMinus(food: Food, size : String){
        val key = CartKey(food, size)
        totalCart.value = totalCart.value - 1
        if(cartMap[key] == 1){
            itemsCount.remove(key)
            cartMap.remove(key)
        }else{
            itemsCount[key] = (itemsCount[key] ?: 0) - 1
            cartMap[key] = (cartMap[key] ?: 0) - 1
        }
    }
    fun updateCount(food: Food, size : String, newCount: Int) {
        val key = CartKey(food, size)
        var itemsCount2 = itemsCount[key] ?: 0
        itemsCount[key] = newCount
        totalCart.value = totalCart.value + newCount - itemsCount2
        if(cartMap.containsKey(key)){
            itemsCount[key] = (itemsCount[key] ?: 0) + totalCart.value
            cartMap[key] = (cartMap[key] ?: 0) + totalCart.value
        }else{
            itemsCount[key] = totalCart.value
            cartMap[key] = totalCart.value
        }
    }
    fun addToCart(){
        //cartMap.putAll(itemsCount)
        itemsCount.clear()
        totalCart.value = 0
    }
    fun bay(){
        cartMap.clear()
        itemsCount.clear()
        totalCart.value = 0
    }
}