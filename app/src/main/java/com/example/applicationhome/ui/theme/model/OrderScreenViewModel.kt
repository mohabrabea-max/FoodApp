package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.OrdersClass
import com.example.applicationhome.data.models.repository.OrderRepository.getOrders
import kotlinx.coroutines.launch

class OrderScreenViewModel : ViewModel(){
    var selectedOrder by mutableStateOf(OrdersClass())

    fun selectorder(order : OrdersClass){
        selectedOrder = order
    }

    fun getOrdersHistory(){
        viewModelScope.launch {
            getOrders()
        }
    }
}