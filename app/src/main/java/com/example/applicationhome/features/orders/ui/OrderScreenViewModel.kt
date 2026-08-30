package com.example.applicationhome.features.orders.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Schedule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CancelOrderUseCase
import com.example.applicationhome.core.domain.usecase.RepurchaseOrderUseCase
import com.example.applicationhome.data.data.model.ActionsStates
import com.example.applicationhome.data.data.model.ActiveOrderDialog
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.data.model.OrderStates
import com.example.applicationhome.data.data.model.OrderStatesEnum
import com.example.applicationhome.data.data.model.OrderUiClass
import com.example.applicationhome.data.data.model.OrdersHistoryScreens
import com.example.applicationhome.data.data.model.TimelineStep
import com.example.applicationhome.data.data.model.UiEventOrderCancelled
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class OrderScreenViewModel @Inject constructor(
    private val orderRepository : OrderRepository,
    private val repurchaseOrderUseCase : RepurchaseOrderUseCase,
    private val cancelOrderUseCase : CancelOrderUseCase,
    private val userRepository : UserRepository,
    networkObserver : NetworkObserver
): ViewModel(){

    // --------------------------------------------\\ Basic Data //--------------------------------------------

    val isNetworkAvailable = networkObserver.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

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


    private val _selectedOrder = MutableStateFlow<OrderUiClass?>(null)
    val selectedOrder = _selectedOrder.asStateFlow()

    private val _screenState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val screenState = _screenState.asStateFlow()


    // --------------------------------------------\\ Basic Functions //--------------------------------------------

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

    private suspend fun getOrdersHistory(){
        _screenState.value = HomeUiState.Loading

        val id = userRepository.userData.value.id
        if(id.isNotEmpty()){
            val result = orderRepository.getOrders(id)
            _screenState.value = result
        }else{
            _screenState.value = HomeUiState.Success
        }
    }


    // --------------------------------------------\\ Initial Page //--------------------------------------------

    val initialPage : StateFlow<Int> =
        combine(
            preparingOrdersHistory,
            deliveredOrdersHistory,
            cancelledOrdersHistory
        ){ preparing, delivered, cancelled ->
            when{
                preparing.isNotEmpty() -> 0
                delivered.isNotEmpty() -> 1
                cancelled.isNotEmpty() -> 2
                else -> 0
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )


    // --------------------------------------------\\ Dialogs //--------------------------------------------

    private val _activeDialog = MutableStateFlow<ActiveOrderDialog>(ActiveOrderDialog.None)
    val activeDialog = _activeDialog.asStateFlow()


    fun openCheckRepurchaseAlertDialogMessage(){
        _activeDialog.value = ActiveOrderDialog.ConfirmRepurchase
    }

    fun openCheckCancelOrderAlertDialogMessage(){
        _activeDialog.value = ActiveOrderDialog.ConfirmCancel
    }

    fun closeAlertDialogMessage(){
        _activeDialog.value = ActiveOrderDialog.None
    }


    // --------------------------------------------\\ Cancel Order //--------------------------------------------

    private val _actionState = MutableStateFlow<ActionsStates>(ActionsStates.Idle)
    val actionState = _actionState.asStateFlow()

    private val _uiEventOrderCancelled = Channel<UiEventOrderCancelled>(Channel.BUFFERED)
    val uiEventOrderCancelled = _uiEventOrderCancelled.receiveAsFlow()

    fun cancelOrder(orderId : Long){
        viewModelScope.launch {
            _actionState.value = ActionsStates.Loading

            val result = cancelOrderUseCase(orderId)
            _actionState.value = result

            when(result){
                ActionsStates.Success -> {
                    getOrdersHistory()

                    closeOrderScreen()

                    _uiEventOrderCancelled.send(UiEventOrderCancelled.OrderCancelledSuccessfully)
                }

                is ActionsStates.Failed -> {
                    _activeDialog.value = ActiveOrderDialog.CancelFailed
                }

                else -> {}
            }
        }
    }

    fun resetActionState(){
        _actionState.value = ActionsStates.Idle
    }


    // --------------------------------------------\\ Repurchase Order //--------------------------------------------

    fun repurchaseOrder(order : OrderUiClass){
        viewModelScope.launch {
            _actionState.value = ActionsStates.Loading

            val result = repurchaseOrderUseCase.repurchaseOrder(order, order.orderItems)

            _actionState.value = ActionsStates.Idle

            _activeDialog.value = ActiveOrderDialog.RepurchaseResult(result)
        }
    }

    fun filterOrderItems(order : OrderUiClass){
        viewModelScope.launch {
            _actionState.value = ActionsStates.Loading

            val result = repurchaseOrderUseCase.filterOrderItems(order)

            _actionState.value = ActionsStates.Idle

            _activeDialog.value = ActiveOrderDialog.RepurchaseResult(result)
        }
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