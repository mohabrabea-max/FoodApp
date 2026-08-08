package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.data.model.ActionsStates
import com.example.applicationhome.data.data.model.OrdersClass
import com.example.applicationhome.data.local.dao.OrdersDao
import com.example.applicationhome.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.remote.FoodAppAPIs
import com.example.applicationhome.domain.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class OrderRepository @Inject constructor(
    userRepository: UserRepository,
    private val ordersDao : OrdersDao,
    private val api : FoodAppAPIs,
    @ApplicationScope private val externalScope: CoroutineScope
) {
    private val _loading = MutableStateFlow(false)
    val loading : StateFlow<Boolean> = _loading

    private val _confirmOrderState = MutableStateFlow<ActionsStates>(ActionsStates.Idle)
    val confirmOrderState = _confirmOrderState.asStateFlow()

    val ordersHistory : StateFlow<List<OrdersDatabaseClass>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            if(id.isNotEmpty()){
                getOrdersHistoryFromDatabase(id)
            }else{
                flowOf(emptyList())
            }
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun getOrdersHistoryFromDatabase(userId : String)
    : Flow<List<OrdersDatabaseClass>> = ordersDao.getAllOrders(userId)


    suspend fun uploadOrderRequest(orderClass : OrdersClass, userId: String){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = current.format(formatter)

        val orderId = System.currentTimeMillis()

        return try {
            _loading.value = true
            _confirmOrderState.value = ActionsStates.Loading
            val response = api.putNewOrder(
                userId,
                orderId,
                orderClass.copy(date = date)
            )
            if(response.isSuccessful){
                _confirmOrderState.value = ActionsStates.Success
            }else{
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
                _confirmOrderState.value = ActionsStates.Failed(errorMessage)
            }
        } catch (e : Exception){
            _confirmOrderState.value = ActionsStates.Failed("Network error")
        } finally {
            _loading.value = false
        }
    }

    suspend fun getOrders(userId: String) : String{
        return try {
            val response = api.getLastOrders(userId)
            val orders = response.body()
            if(response.isSuccessful && orders != null){
                val ordersHistory = orders.map { (key, value) ->
                    OrdersDatabaseClass(
                        key,
                        userId,
                        value.date,
                        value.state,
                        value.subtotal,
                        value.delivery,
                        value.service,
                        value.totalPrice,
                        value.restaurantName,
                        value.restaurantImage,
                        value.restaurantId,
                        value.userInformation,
                        value.orderItems
                    )
                }
                ordersDao.addNewOrders(ordersHistory)
                "Success"
            }else{
                val errorCode = response.code()

                when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
            }
        } catch (E : Exception){
            E.printStackTrace()
            "Network error"
        }
    }
}