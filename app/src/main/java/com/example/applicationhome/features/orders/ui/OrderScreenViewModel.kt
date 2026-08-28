package com.example.applicationhome.features.orders.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Schedule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.OrdersHistoryUseCase
import com.example.applicationhome.data.data.model.ActionsStates
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.data.model.OrderStates
import com.example.applicationhome.data.data.model.OrderStatesEnum
import com.example.applicationhome.data.data.model.OrderUiClass
import com.example.applicationhome.data.data.model.OrdersHistoryScreens
import com.example.applicationhome.data.data.model.RepurchaseOrderStates
import com.example.applicationhome.data.data.model.TimelineStep
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class OrderScreenViewModel @Inject constructor(
    private val orderRepository : OrderRepository,
    private val ordersHistoryUseCase : OrdersHistoryUseCase,
    networkObserver : NetworkObserver,
    userRepository : UserRepository
): ViewModel(){

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

    private val _timelineStep = MutableStateFlow(
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

    val correctTimelineStep = _timelineStep.asStateFlow()

    private val _screenState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val screenState = _screenState.asStateFlow()


    fun openOrderScreen(order : OrderUiClass){
        val targetStepIndex = OrderStates.fromEnum(order.state.enumState).index

        _timelineStep.update {
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
                        date = "",
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
            _screenState.value = HomeUiState.Loading

            val id = userData.value.id
            if(id.isNotEmpty()){
                val result = orderRepository.getOrders(id)
                delay(200.milliseconds)
                selectInitialPage()
                _screenState.value = result
            }else{
                _screenState.value = HomeUiState.Success
            }
        }
    }

    private val _initialPage = MutableStateFlow(0)
    val initialPage = _initialPage.asStateFlow()

    private fun selectInitialPage(){
        _initialPage.value = when{
            preparingOrdersHistory.value.isNotEmpty() -> 0
            deliveredOrdersHistory.value.isNotEmpty() -> 1
            cancelledOrdersHistory.value.isNotEmpty() -> 2
            else -> 0
        }
    }


    private val _actionState = MutableStateFlow<ActionsStates>(ActionsStates.Idle)
    val actionState = _actionState.asStateFlow()

    fun cancelOrder(orderId : Long){
        viewModelScope.launch {
            _actionState.value = ActionsStates.Loading

            val result = ordersHistoryUseCase.cancelOrder(orderId)
            _actionState.value = result

            if(result is ActionsStates.Success){
                getOrdersHistory()
                selectInitialPage()
                closeOrderScreen()
            }
        }
    }

    fun resetActionState(){
        _actionState.value = ActionsStates.Idle
    }


    private val _showCheckCancelOrderAlertDialogMessage = MutableStateFlow(false)
    val showCheckCancelOrderAlertDialogMessage = _showCheckCancelOrderAlertDialogMessage.asStateFlow()

    fun openCheckCancelOrderAlertDialogMessage(){
        _showCheckCancelOrderAlertDialogMessage.value = true
    }

    fun closeCheckCancelOrderAlertDialogMessage(){
        _showCheckCancelOrderAlertDialogMessage.value = false
    }



    private val _repurchaseOrderStates = MutableStateFlow<RepurchaseOrderStates?>(null)
    val repurchaseOrderStates = _repurchaseOrderStates.asStateFlow()

    fun repurchaseOrder(order : OrderUiClass){
        viewModelScope.launch {
            _actionState.value = ActionsStates.Loading

            val result = ordersHistoryUseCase.repurchaseOrder(order, order.orderItems)

            _actionState.value = ActionsStates.Idle
            _repurchaseOrderStates.value = result
        }
    }

    fun filterOrderItems(order : OrderUiClass){
        viewModelScope.launch {
            _actionState.value = ActionsStates.Loading

            val result = ordersHistoryUseCase.filterOrderItems(order)

            _actionState.value = ActionsStates.Idle
            _repurchaseOrderStates.value = result
        }
    }

    fun closeRepurchaseOrderStates(){
        _repurchaseOrderStates.value = null
    }


    private val _showCheckRepurchaseAlertDialogMessage = MutableStateFlow(false)
    val showCheckRepurchaseAlertDialogMessage = _showCheckRepurchaseAlertDialogMessage.asStateFlow()

    fun openCheckRepurchaseAlertDialogMessage(){
        _showCheckRepurchaseAlertDialogMessage.value = true
    }
    fun closeCheckRepurchaseAlertDialogMessage(){
        _showCheckRepurchaseAlertDialogMessage.value = false
    }

    init {
        viewModelScope.launch {
            isNetworkAvailable.collect { available ->
                if(available){
                    if(_screenState.value != HomeUiState.Success) getOrdersHistory()
                }else{
                    if(_screenState.value != HomeUiState.Success) _screenState.value = HomeUiState.Offline
                }
            }
        }
    }
}