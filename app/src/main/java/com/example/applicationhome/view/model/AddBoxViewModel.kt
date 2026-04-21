package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddBoxViewModel : ViewModel(){
    var activId by mutableStateOf<Int?>(null)
    var itemsCount = mutableStateMapOf<Int, Int>()
    var cart = mutableStateMapOf<Int, Int>()
    var totalCart = mutableStateOf(0)
    fun addBoxNumberPlus(id : Int){
        var current = itemsCount[id] ?: 0
        var cart2 = cart[id] ?: 0
        if(current < 99){
            itemsCount[id] = current + 1
            cart[id] = cart2 + 1
            totalCart.value = totalCart.value + 1
            activId = id
        }
    }
    fun addBoxNumberMinus(id : Int){
        var current = itemsCount[id] ?: 0
        var cart2 = cart[id] ?: 0
        if(current > 0){
            itemsCount[id] = current - 1
            cart[id] = cart2 - 1
            totalCart.value = totalCart.value - 1
        }
    }
    fun updateCount(id: Int, newCount: Int) {
        var cart2 = cart[id] ?: 0
        cart[id] = newCount
        itemsCount[id] = newCount
        totalCart.value = totalCart.value + newCount - cart2
    }
    fun addToCart(){
        itemsCount.clear()
        cart.clear()
        totalCart.value = 0
    }
}