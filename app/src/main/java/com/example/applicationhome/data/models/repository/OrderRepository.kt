package com.example.applicationhome.data.models.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.applicationhome.data.models.model.OrderItemsClass
import com.example.applicationhome.data.models.model.OrdersClass
import com.example.applicationhome.data.models.model.UserInformationInOrderClass
import com.example.applicationhome.data.models.remote.RetrofitInstance
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.additionalDirectionsState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.addressLabelState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.houseState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.phoneNumberState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.streetState
import com.example.applicationhome.data.models.repository.UserRepository.userData
import com.example.applicationhome.data.models.repository.UserRepository.userId
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object OrderRepository {
    var orderItems by mutableStateOf<List<OrderItemsClass>>(emptyList())
    var restaurantId by mutableStateOf(0)
    var restaurantName by mutableStateOf("")
    var restaurantImage by mutableStateOf("")
    val address1 = "${houseState.text} - ${streetState.text}"
    val address2 = " - ${additionalDirectionsState.text} - ${addressLabelState.text}"

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadOrderRequest(){
        val orderId = java.util.UUID.randomUUID().toString()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = current.format(formatter)
        try {
            val response = RetrofitInstance.api.putNewOrder(
                userId,
                orderId,
                OrdersClass(
                    date,
                    "Preparing",
                    UserInformationInOrderClass(
                        "${userData.firstname} ${userData.lastname}",
                        phoneNumberState.text.toString(),
                         if(additionalDirectionsState.text.isNotEmpty() && addressLabelState.text.isNotEmpty())
                             address1 + address2
                             else address1,
                        "30.0444,31.2357"
                    ),
                    orderItems,
                    restaurantName,
                    restaurantImage,
                    restaurantId
                )
            )
            if(response.isSuccessful){
                println(response.body())
            }
        } catch (e : Exception){
            null
        } finally {
            null
        }
    }
}