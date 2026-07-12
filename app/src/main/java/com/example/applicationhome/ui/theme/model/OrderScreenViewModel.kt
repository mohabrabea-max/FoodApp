package com.example.applicationhome.ui.theme.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.data.remote.NetworkObserver
import com.example.applicationhome.data.data.repository.OrderRepository
import com.example.applicationhome.data.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class OrderScreenViewModel @Inject constructor(
    application: Application,
    private val orderRepository : OrderRepository,
    userRepository: UserRepository
) : AndroidViewModel(application){

    private val networkObserver = NetworkObserver(application.applicationContext)
    var isNetworkAvailable by mutableStateOf(false)

    var userId by mutableStateOf("")

    var selectedOrder by mutableStateOf(OrdersDatabaseClass())

    val ordersHistory : StateFlow<List<OrdersDatabaseClass>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            if(id.isNotEmpty()){
                userId = id
                orderRepository.getOrdersHistoryFromDatabase(id)
            }else{
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )


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
            orderRepository.getOrders(userId)
        }
    }
}