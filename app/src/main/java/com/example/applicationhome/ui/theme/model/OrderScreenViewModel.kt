package com.example.applicationhome.ui.theme.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.data.remote.NetworkObserver
import com.example.applicationhome.data.data.repository.OrderRepository
import com.example.applicationhome.data.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class OrderScreenViewModel @Inject constructor(
    private val networkObserver : NetworkObserver,
    private val orderRepository : OrderRepository,
    userRepository: UserRepository
) : ViewModel(){

    val isNetworkAvailable = MutableStateFlow(false)

    val userData = userRepository.userData

    val selectedOrder = MutableStateFlow(OrdersDatabaseClass())

    val ordersHistory = orderRepository.ordersHistory


    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable.value = available
            }
        }
    }


    fun selectorder(order : OrdersDatabaseClass){
        selectedOrder.value = order
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