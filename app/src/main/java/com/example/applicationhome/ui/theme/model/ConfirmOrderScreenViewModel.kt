package com.example.applicationhome.ui.theme.model

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.local.CartClass
import com.example.applicationhome.data.models.local.CartItemsClass
import com.example.applicationhome.data.models.local.UserClass
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConfirmOrderScreenViewModel(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val orderRepository : OrderRepository
) : ViewModel() {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val date = current.format(formatter)


    val cartInformation : StateFlow<CartClass?> = userRepository.userId
        .flatMapLatest { id ->
            cartRepository.getCartData(id)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val cartItems : StateFlow<List<CartItemsClass?>> =userRepository.userId
        .flatMapLatest { id ->
            cartRepository.getCartItems(id)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val userData : StateFlow<UserClass> =
        userRepository.getActiveUserFromDatabase()
            .map { userInDb ->
                userInDb ?: UserClass(firstname = "Guest")
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UserClass(firstname = "Guest")
            )
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

    fun uploadOrder(){
        viewModelScope.launch {
            val orderInformation = cartInformation.value

            var totalPrice = 0.0
            cartItems.value.forEach { item ->
                totalPrice += item?.totalPrice ?: 0.0
            }

            var orderItems = listOf(OrderItemsClass())
            cartItems.value.forEach { item ->
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
                    date,
                    "Preparing",
                    totalPrice,
                    UserInformationInOrderClass(
                        "${userData.value.firstname} ${userData.value.lastname}",
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
                orderRepository.uploadOrderRequest(order)
            }
        }
    }
}