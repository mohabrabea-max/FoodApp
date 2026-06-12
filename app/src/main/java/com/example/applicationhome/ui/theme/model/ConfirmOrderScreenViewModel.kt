package com.example.applicationhome.ui.theme.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.OrderItemsClass
import com.example.applicationhome.data.models.repository.CartRepository.cartItems
import com.example.applicationhome.data.models.repository.CartRepository.cartMealsMenu
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.houseState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.housetextFieldState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.phoneNumberState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.streetState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.streettextFieldState
import com.example.applicationhome.data.models.repository.MenuRepository.restaurantsMenu
import com.example.applicationhome.data.models.repository.OrderRepository.orderItems
import com.example.applicationhome.data.models.repository.OrderRepository.restaurantId
import com.example.applicationhome.data.models.repository.OrderRepository.restaurantImage
import com.example.applicationhome.data.models.repository.OrderRepository.restaurantName
import com.example.applicationhome.data.models.repository.OrderRepository.uploadOrderRequest
import kotlinx.coroutines.launch

class ConfirmOrderScreenViewModel : ViewModel() {

    var bottonState by mutableStateOf(false)
    var phoneNumbertextFieldState by mutableStateOf(false)

    fun bottonstate(){
        if(
            houseState.text.isNotEmpty()
            && streetState.text.isNotEmpty()
            && phoneNumberState.text.isNotEmpty()
            && phoneNumberState.text.length == 11
        ){
            bottonState = true
        } else {
            bottonState = false
        }
    }

    fun phoneNumbertextFieldtrue(){
        phoneNumbertextFieldState = true
    }
    fun housetextFieldStatetrue(){
        housetextFieldState = true
    }
    fun streettextFieldtrue(){
        streettextFieldState = true
    }

    fun addToOrderItems(){
        val menuMap = cartMealsMenu.associateBy{ it.id }

        val finalOrderItems = cartItems.values.mapNotNull { item ->

            val meal = menuMap[item.id] ?: return@mapNotNull null
            val price = meal.sizeOptions.find { it.size == item.size }?.price ?: return@mapNotNull null
            restaurantId = meal.restaurantId
            OrderItemsClass(
                item.id,
                meal.name,
                item.size,
                price,
                item.number,
                meal.image[0]
            )
        }
        orderItems = orderItems + finalOrderItems
        restaurantName = restaurantsMenu.find { it.id == restaurantId }?.name ?: ""
        restaurantImage = restaurantsMenu.find { it.id == restaurantId }?.image ?: ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadOrder(){
        viewModelScope.launch {
            uploadOrderRequest()
        }
    }
}