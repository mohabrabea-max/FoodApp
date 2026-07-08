package com.example.applicationhome.ui.theme.model

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.OrderItemsClass
import com.example.applicationhome.data.models.model.OrdersClass
import com.example.applicationhome.data.models.model.UserInformationInOrderClass
import com.example.applicationhome.data.models.repository.CartRepository
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.additionalDirectionsState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.addressLabelState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.houseState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.housetextFieldState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.phoneNumberState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.streetState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.streettextFieldState
import com.example.applicationhome.data.models.repository.OrderRepository
import com.example.applicationhome.data.models.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ConfirmOrderScreenViewModel(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val orderRepository : OrderRepository
) : ViewModel() {
    val loading : StateFlow<Boolean> = orderRepository.loading
    var bottonState by mutableStateOf(false)
    var phoneNumbertextFieldState by mutableStateOf(false)
    val address1 = "${houseState.text} - ${streetState.text}"
    val address2 = " - ${additionalDirectionsState.text} - ${addressLabelState.text}"

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

    fun uploadOrder(onSuccess: () -> Unit){
        viewModelScope.launch {
            val currentUser = userRepository.getActiveUserFromDatabase().first()
            val userId = currentUser?.id ?: ""

            val orderInformation = cartRepository.getCartData(userId).first()
            val currentCartItems = cartRepository.getCartItems(userId).first()

            val firstname = currentUser?.firstname ?: ""
            val lastname = currentUser?.lastname ?: ""


            var subtotal = 0.0
            currentCartItems.forEach { item ->
                subtotal += item?.totalPrice ?: 0.0
            }
            val delivery = 55.0
            val service = 8.0
            val totalPrice = subtotal + delivery + service

            var orderItems = listOf<OrderItemsClass>()
            currentCartItems.forEach { item ->
                orderItems += OrderItemsClass(
                    item?.mealId ?: 0,
                    item?.name ?: "",
                    item?.size ?: "",
                    item?.priceOfOne ?: 0.0,
                    item?.quantity ?: 0,
                    item?.image ?: "",
                    item?.type ?: ""
                )
            }
            if(orderInformation != null){
                val order = OrdersClass(
                    "",
                    "Preparing",
                    subtotal,
                    delivery,
                    service,
                    totalPrice,
                    UserInformationInOrderClass(
                        "$firstname $lastname",
                        phoneNumberState.text.toString(),
                        if(additionalDirectionsState.text.isNotEmpty() && addressLabelState.text.isNotEmpty())
                            address1 + address2
                        else address1,
                        "30.0444,31.2357"
                    ),
                    orderItems,
                    orderInformation.restaurantName,
                    orderInformation.restaurantImage,
                    orderInformation.restaurantId
                    )
                orderRepository.uploadOrderRequest(order, userId)
            }
            onSuccess()
            cleanTextField()
        }
    }
}