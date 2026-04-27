package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.Food

class AddBoxViewModel : ViewModel(){
    var activId by mutableStateOf<Int?>(null)
    var itemsCount = mutableStateMapOf<Food, Int>()
    var totalCart = mutableStateOf(0)
    var cartMap = Cart.cartmap
    fun addBoxNumberPlus(food: Food){
        val id = food.id
        var current = itemsCount[food] ?: 0
        if(current < 99){
            totalCart.value = totalCart.value + 1
            activId = id
            if(cartMap.containsKey(food)){
                itemsCount[food] = (itemsCount[food] ?: 0) + 1
                cartMap[food] = (cartMap[food] ?: 0) + 1
            }else{
                itemsCount[food] = 1
                cartMap[food] = 1
            }
        }
    }
    fun addBoxNumberMinus(food: Food){
        val id = food.id
        var current = itemsCount[food] ?: 0
        if(current > 0){
            totalCart.value = totalCart.value - 1
            if(cartMap[food] == 1){
                itemsCount.remove(food)
                cartMap.remove(food)
            }else{
                itemsCount[food] = (itemsCount[food] ?: 0) - 1
                cartMap[food] = (cartMap[food] ?: 0) - 1
            }
        }
    }
    fun updateCount(food: Food, newCount: Int) {
        val id = food.id
        var itemsCount2 = itemsCount[food] ?: 0
        itemsCount[food] = newCount
        totalCart.value = totalCart.value + newCount - itemsCount2
        if(cartMap.containsKey(food)){
            itemsCount[food] = totalCart.value
            cartMap[food] = totalCart.value
        }else{
            itemsCount[food] = totalCart.value
            cartMap[food] = totalCart.value
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