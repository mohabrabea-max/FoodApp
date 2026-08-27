package com.example.applicationhome.features.orders.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Schedule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.model.orderItemsClassToCartItemsClass
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CartUseCase
import com.example.applicationhome.data.data.model.ActionsStates
import com.example.applicationhome.data.data.model.CategoryEnum
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
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
    private val cartUseCase: CartUseCase,
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

    private val _screenState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val screenState = _screenState.asStateFlow()


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
            _screenState.value = HomeUiState.Loading

            val id = userData.value.id
            if(id.isNotEmpty()){
                orderRepository.getOrders(id)
            }
            selectInitialPage()
            _screenState.value = HomeUiState.Success
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

    fun cancelOrder(orderId : Long, index : Int){
        viewModelScope.launch {
            _actionState.value = ActionsStates.Loading

            val userId = userData.firstOrNull()?.id
            if (userId == null) {
                _actionState.value = ActionsStates.Failed("Unexpected error")
                return@launch
            }

            orderRepository.cancelOrder(
                userId = userId,
                orderId = orderId,
                index = index
            ).onSuccess {
                _actionState.value = ActionsStates.Success
                getOrdersHistory()
                closeOrderScreen()
            }.onFailure {
                _actionState.value = ActionsStates.Failed("Unexpected error")
            }
        }
    }

    private val _repurchaseOrderStates = Channel<RepurchaseOrderStates>(Channel.BUFFERED)
    val repurchaseOrderStates = _repurchaseOrderStates.receiveAsFlow()

    fun repurchaseOrder(order : OrderUiClass){
        viewModelScope.launch {
            val isRestaurantIsDeleted = orderRepository.checkIsRestaurantDeleted(order.restaurantId)
            if(!isRestaurantIsDeleted){
                _repurchaseOrderStates.send(RepurchaseOrderStates.RestaurantIsDeleted)
                return@launch
            }

            val orderItems = order.orderItems


            val mealsIds = orderItems
                .filter {
                    it.type != CategoryEnum.SNACKS.rawValue &&
                    it.type != CategoryEnum.DRINK.rawValue
                }.map { it.mealId }

            val snacksIds = orderItems.filter { it.type == CategoryEnum.SNACKS.rawValue }.map { it.mealId }


            val result = orderRepository.checkAreMealsDeleted(mealsIds, snacksIds)
            if(!result){
                _repurchaseOrderStates.send(RepurchaseOrderStates.MealsAreDeleted)
                return@launch
            }

            val resImage = orderRepository.getRestaurantImage(order.restaurantId)
            val mealsImages = orderRepository.getMealsImages(mealsIds)
            val snacksImages = orderRepository.getSnacksImages(snacksIds)

            val items = orderItems.map {
                it.orderItemsClassToCartItemsClass(
                    userId = order.userId,
                    resId = order.restaurantId,
                    image = when{
                        it.type == CategoryEnum.SNACKS.rawValue -> {
                            snacksImages[it.mealId] ?: it.image
                        }
                        else -> {
                            mealsImages[it.mealId] ?: it.image
                        }
                    }
                )
            }

            cartUseCase.clearAllCart(order.userId)

            cartUseCase.addMoreThanOneItem(
                userId = order.userId,
                foods = items,
                resId = order.restaurantId,
                resName = order.restaurantName,
                resImage = resImage
            )

            _repurchaseOrderStates.send(RepurchaseOrderStates.Success)
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