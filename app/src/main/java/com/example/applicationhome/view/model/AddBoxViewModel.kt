package com.example.applicationhome.view.model

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AddBoxViewModel : ViewModel(){
    var itemsCount = mutableStateMapOf<Int, Int>()
    var cart = mutableStateOf(0)
    fun addBoxNumberPlus(id : Int){
        var current = itemsCount[id] ?: 0
        itemsCount[id] = current + 1
        cart.value = cart.value + 1
    }
    fun addBoxNumberMinus(id : Int){
        var current = itemsCount[id] ?: 0
        if(current > 0){
            itemsCount[id] = current - 1
            cart.value = cart.value - 1
        }
    }
    fun updateCount(id: Int, newCount: Int) {
        cart.value = newCount
        itemsCount[id] = newCount
    }
    fun addToCart(){
        itemsCount.clear()
        cart.value = 0
    }
}