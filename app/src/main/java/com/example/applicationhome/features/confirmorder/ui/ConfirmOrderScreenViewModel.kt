package com.example.applicationhome.features.confirmorder.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CartUseCase
import com.example.applicationhome.data.data.model.OrderItemsClass
import com.example.applicationhome.data.data.model.OrdersClass
import com.example.applicationhome.data.data.model.TextFieldClassFromConfirmOrderScreen
import com.example.applicationhome.data.data.model.UserInformationInOrderClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmOrderScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val orderRepository : OrderRepository,
    private val cartUseCase: CartUseCase,
) : ViewModel() {

    val phoneNumberState = TextFieldState()
    val houseState = TextFieldState()
    val housetextFieldState = MutableStateFlow(false)
    val streetState = TextFieldState()
    val streettextFieldState = MutableStateFlow(false)

    val additionalDirectionsState = TextFieldState()
    val addressLabelState = TextFieldState()

    val textFieldConfirmOrderScreenList1 = listOf(
        TextFieldClassFromConfirmOrderScreen(houseState, "House"),
        TextFieldClassFromConfirmOrderScreen(streetState, "Street"),
    )
    val textFieldConfirmOrderScreenList2 = listOf(
        TextFieldClassFromConfirmOrderScreen(
            additionalDirectionsState,
            "Additional directions (optional)"
        ),
        TextFieldClassFromConfirmOrderScreen(addressLabelState, "Address label (optional)"),
    )


    val confirmOrderPages = MutableStateFlow(1)

    val cartItems = cartRepository.cartItems

    val userData = userRepository.userData

    val totalPrice = cartRepository.totalPrice

    val loading : StateFlow<Boolean> = orderRepository.loading
    val bottonState = MutableStateFlow(false)
    val phoneNumbertextFieldState = MutableStateFlow(false)
    val address1 = "${houseState.text} - ${streetState.text}"
    val address2 = " - ${additionalDirectionsState.text} - ${addressLabelState.text}"


    init {
        viewModelScope.launch(Dispatchers.IO){
            userData.collect { user ->
                phoneNumberState.edit {
                    replace(0, length, user.phonenumber)
                }
            }
        }
    }


    fun bottonstate(){
        if(
            houseState.text.isNotEmpty()
            && streetState.text.isNotEmpty()
            && phoneNumberState.text.isNotEmpty()
            && phoneNumberState.text.length == 11
        ){
            bottonState.value = true
        } else {
            bottonState.value = false
        }
    }

    fun cleanTextField(){
        houseState.clearText()
        streetState.clearText()
        phoneNumberState.clearText()
        additionalDirectionsState.clearText()
        addressLabelState.clearText()
        housetextFieldState.value = false
        streettextFieldState.value = false
        phoneNumbertextFieldState.value = false
    }

    fun phoneNumbertextFieldtrue(){
        phoneNumbertextFieldState.value = true
    }
    fun housetextFieldStatetrue(){
        housetextFieldState.value = true
    }
    fun streettextFieldtrue(){
        streettextFieldState.value = true
    }

    fun uploadOrder(onSuccess: () -> Unit){
        viewModelScope.launch {
            val currentUser = userRepository.userData.first()
            val userId = currentUser.id

            val orderInformation = cartRepository.getCartData(userId).first()
            val currentCartItems = cartRepository.getCartItems(userId).first()

            val firstname = currentUser.firstname
            val lastname = currentUser.lastname


            var subtotal = 0.0
            currentCartItems.forEach { item ->
                subtotal += item?.totalPrice ?: 0.0
            }
            val delivery = 55.0
            val service = 8.0
            val totalPrice = subtotal + delivery + service

            val orderItems = mutableListOf<OrderItemsClass>()
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
                        if (additionalDirectionsState.text.isNotEmpty() && addressLabelState.text.isNotEmpty())
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

    fun clearAllCart(){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            cartUseCase.clearAllCart(userId)
        }
    }

    fun nextPage(){
        confirmOrderPages.value += 1
    }

    fun lastPage(){
        confirmOrderPages.value -= 1
    }
}