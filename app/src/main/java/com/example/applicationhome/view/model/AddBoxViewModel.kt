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
    var itemsCount = mutableStateMapOf<Int, Int>()
    var cart = mutableStateMapOf<Int, Int>()
    var totalCart = mutableStateOf(0)
    var itemsCart = mutableListOf<Food>()
    var cartList = Cart.cartlist
    fun addBoxNumberPlus(food: Food){
        val id = food.id
        var current = itemsCount[id] ?: 0
        var cart2 = cart[id] ?: 0
        if(current < 99){
            itemsCount[id] = current + 1
            cart[id] = cart2 + 1
            totalCart.value = totalCart.value + 1
            activId = id
            itemsCart.add(food)
        }
    }
    fun addBoxNumberMinus(food: Food){
        val id = food.id
        var current = itemsCount[id] ?: 0
        var cart2 = cart[id] ?: 0
        if(current > 0){
            itemsCount[id] = current - 1
            cart[id] = cart2 - 1
            totalCart.value = totalCart.value - 1
            itemsCart.remove(food)
        }
    }
    fun updateCount(food: Food, newCount: Int) {
        val id = food.id
        var cart2 = cart[id] ?: 0
        cart[id] = newCount
        itemsCount[id] = newCount
        totalCart.value = totalCart.value + newCount - cart2
    }
    fun addToCart(){
        cartList.addAll(itemsCart)
        itemsCount.clear()
        cart.clear()
        totalCart.value = 0
    }
}