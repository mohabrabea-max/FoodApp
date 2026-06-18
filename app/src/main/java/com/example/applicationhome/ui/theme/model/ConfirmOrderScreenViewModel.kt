package com.example.applicationhome.ui.theme.model

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.OrderItemsClass
import com.example.applicationhome.data.models.repository.CartRepository.cartItems
import com.example.applicationhome.data.models.repository.CartRepository.cartMealsMenu
import com.example.applicationhome.data.models.repository.CartRepository.cartSnacksMenu
import com.example.applicationhome.data.models.repository.CartRepository.deleteAllCart
import com.example.applicationhome.data.models.repository.CartRepository.totalNumber
import com.example.applicationhome.data.models.repository.CartRepository.totalPrice
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.additionalDirectionsState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.addressLabelState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.houseState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.housetextFieldState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.phoneNumberState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.streetState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.streettextFieldState
import com.example.applicationhome.data.models.repository.MenuRepository.restaurantsMenu
import com.example.applicationhome.data.models.repository.OrderRepository.getOrders
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

    fun cleanTextField(){
        houseState.clearText()
        streetState.clearText()
        phoneNumberState.clearText()
        additionalDirectionsState.clearText()
        addressLabelState.clearText()
        housetextFieldState = false
        streettextFieldState = false
        phoneNumbertextFieldState = false
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
        val finalOrderItems = cartItems.values.mapNotNull { item ->
            if(item.type == "Meal"){
                val meal = cartMealsMenu["${item.id}_${item.size}"] ?: return@mapNotNull null
                val price = meal.sizeOptions.find { it.size == item.size }?.price ?: return@mapNotNull null
                restaurantId = meal.restaurantId
                OrderItemsClass(
                    item.id,
                    meal.name,
                    item.size,
                    price,
                    item.number,
                    meal.image[0],
                    "Meal"
                )
            }else{
                val snack = cartSnacksMenu["${item.id}_${item.size}"] ?: return@mapNotNull null
                val price = snack.priceANDsize[item.size]?: return@mapNotNull null
                restaurantId = snack.restaurantId ?: 0
                OrderItemsClass(
                    item.id,
                    snack.name,
                    item.size,
                    price,
                    item.number,
                    snack.image[0],
                    "Snack"
                )
            }
        }
        orderItems += finalOrderItems
        restaurantName = restaurantsMenu.values.find { it.id == restaurantId }?.name ?: ""
        restaurantImage = restaurantsMenu.values.find { it.id == restaurantId }?.image ?: ""
    }

    fun uploadOrder(){
        viewModelScope.launch {
            val result = uploadOrderRequest()

            if(result == "Success"){
                getOrders()
                deleteAllCart()

                cartItems.clear()
                cartMealsMenu.clear()
                cartSnacksMenu.clear()
                totalPrice = 0.0
                totalNumber.value = 0
                orderItems = emptyList()
                restaurantName = ""
                restaurantImage = ""
            }
        }
    }
}