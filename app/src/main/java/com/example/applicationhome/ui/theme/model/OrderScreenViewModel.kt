package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.data.remote.NetworkObserver
import com.example.applicationhome.data.data.repository.OrderRepository
import com.example.applicationhome.data.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class OrderScreenViewModel @Inject constructor(
    private val networkObserver : NetworkObserver,
    private val orderRepository : OrderRepository,
    private val userRepository: UserRepository
) : ViewModel(){

    var isNetworkAvailable by mutableStateOf(false)

    val userData = userRepository.userData

    var selectedOrder by mutableStateOf(OrdersDatabaseClass())

    val ordersHistory = orderRepository.ordersHistory


    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable = available
            }
        }
    }


    fun selectorder(order : OrdersDatabaseClass){
        selectedOrder = order
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