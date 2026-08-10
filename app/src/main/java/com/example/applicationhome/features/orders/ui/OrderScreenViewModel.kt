package com.example.applicationhome.features.orders.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class OrderScreenViewModel @Inject constructor(
    private val orderRepository : OrderRepository,
    networkObserver : NetworkObserver,
    userRepository: UserRepository
) : ViewModel(){

    val isNetworkAvailable = networkObserver.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val userData = userRepository.userData

    private val _selectedOrder = MutableStateFlow(OrdersDatabaseClass())
    val selectedOrder = _selectedOrder.asStateFlow()

    val ordersHistory = orderRepository.ordersHistory


    fun selectorder(order : OrdersDatabaseClass){
        _selectedOrder.value = order
    }

    fun getOrdersHistory(){
        viewModelScope.launch {
            val id = userData.value.id
            if(id.isNotEmpty()){
                orderRepository.getOrders(id)
            }
        }
    }
}