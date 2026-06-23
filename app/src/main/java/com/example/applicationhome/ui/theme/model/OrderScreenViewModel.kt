package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.OrdersClass
import com.example.applicationhome.data.models.repository.OrderRepository
import kotlinx.coroutines.launch

class OrderScreenViewModel(private val orderRepository : OrderRepository) : ViewModel(){
    var selectedOrder by mutableStateOf(OrdersClass())
    var lastOrders = mutableStateMapOf<String, OrdersClass>()

    fun selectorder(order : OrdersClass){
        selectedOrder = order
    }

    fun getOrdersHistory(){
        viewModelScope.launch {
            lastOrders = mutableStateMapOf<String, OrdersClass>().apply {
                putAll(orderRepository.getOrders())
            }
        }
    }
}