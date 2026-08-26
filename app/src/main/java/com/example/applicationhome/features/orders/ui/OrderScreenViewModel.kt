package com.example.applicationhome.features.orders.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Schedule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.OrderStates
import com.example.applicationhome.data.data.model.OrderStatesEnum
import com.example.applicationhome.data.data.model.OrderUiClass
import com.example.applicationhome.data.data.model.OrdersHistoryScreens
import com.example.applicationhome.data.data.model.TimelineStep
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    private val _selectedOrder = MutableStateFlow<OrderUiClass?>(null)
    val selectedOrder = _selectedOrder.asStateFlow()


    val preparingOrdersHistory : StateFlow<List<OrderUiClass>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            if(id.isNotEmpty()){
                orderRepository.getOrdersHistoryFromDatabase(
                    id,
                    listOf(
                        OrderStatesEnum.PREPARING,
                        OrderStatesEnum.DELIVERING
                    )
                )
            }else{
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val deliveredOrdersHistory : StateFlow<List<OrderUiClass>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            if(id.isNotEmpty()){
                orderRepository.getOrdersHistoryFromDatabase(id, listOf(OrderStatesEnum.DELIVERED))
            }else{
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val cancelledOrdersHistory : StateFlow<List<OrderUiClass>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            if(id.isNotEmpty()){
                orderRepository.getOrdersHistoryFromDatabase(id, listOf(OrderStatesEnum.CANCELLED))
            }else{
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val statesBar = listOf(
        OrdersHistoryScreens.Preparing,
        OrdersHistoryScreens.Delivered,
        OrdersHistoryScreens.Cancelled
    )

    private val timelineStep = MutableStateFlow(
        listOf(
            TimelineStep(
                enumState = OrderStatesEnum.PREPARING,
                subtitle = "Order Complete",
                date = "",
                icon = Icons.Default.Schedule,
                isCompleted = false,
                isCurrent = false
            ),
            TimelineStep(
                enumState = OrderStatesEnum.DELIVERING,
                subtitle = "Being Sent by Courier",
                date = "",
                icon = Icons.Default.LocalShipping,
                isCompleted = false,
                isCurrent = false
            ),
            TimelineStep(
                enumState = OrderStatesEnum.DELIVERED,
                subtitle = "Delivered",
                date = "",
                icon = Icons.Default.Check,
                isCompleted = false,
                isCurrent = false
            )
        )
    )

    val correctTimelineStep = timelineStep.asStateFlow()


    fun openOrderScreen(order : OrderUiClass){
        val targetStepIndex = OrderStates.fromEnum(order.state.enumState).index

        timelineStep.update {
            it.mapIndexed { index, step ->
                when{
                    index < targetStepIndex -> step.copy(
                        date = order.orderHistory.getOrNull(index)?.date ?: "",
                        isCompleted = true,
                        isCurrent = false
                    )

                    index == targetStepIndex -> step.copy(
                        date = order.orderHistory.getOrNull(index)?.date ?: "",
                        isCompleted = step.enumState == OrderStatesEnum.DELIVERED,
                        isCurrent = true
                    )

                    else -> step.copy(
                        date = order.orderHistory.getOrNull(index)?.date ?: "",
                        isCompleted = false,
                        isCurrent = false
                    )
                }
            }
        }

        _selectedOrder.value = order
    }

    fun closeOrderScreen(){
        _selectedOrder.value = null
    }

    private fun getOrdersHistory(){
        viewModelScope.launch {
            val id = userData.value.id
            if(id.isNotEmpty()){
                orderRepository.getOrders(id)
            }
        }
    }


    init {
        viewModelScope.launch {
            isNetworkAvailable.collect { available ->
                if(available) getOrdersHistory()
            }
        }
    }
}