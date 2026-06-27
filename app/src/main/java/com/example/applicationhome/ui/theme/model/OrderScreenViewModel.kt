package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.OrdersClass
import com.example.applicationhome.data.models.repository.OrderRepository
import com.example.applicationhome.data.models.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OrderScreenViewModel(
    private val orderRepository : OrderRepository,
    private val userRepository: UserRepository
) : ViewModel(){
    var selectedOrder by mutableStateOf(OrdersClass())
    var lastOrders = mutableStateMapOf<String, OrdersClass>()

    private val _sortedOrdersList = MutableStateFlow<Map<String, OrdersClass>>(emptyMap())
    val sortedOrdersList = _sortedOrdersList.asStateFlow()


    fun selectorder(order : OrdersClass){
        selectedOrder = order
    }

    fun getOrdersHistory(){
        viewModelScope.launch {
            try {
                val currentUser = userRepository.getActiveUserFromDatabase().first()
                val userId = currentUser?.id ?: ""
                if(userId.isNotEmpty()){
                    lastOrders = mutableStateMapOf<String, OrdersClass>().apply {
                        clear()
                        putAll(orderRepository.getOrders(userId))
                    }
                    _sortedOrdersList.value = lastOrders.entries
                        .sortedByDescending { it.value.date }
                        .associate { it.key to it.value }
                }
            }catch (e : Exception){
                e.printStackTrace()
            }

        }
    }
}