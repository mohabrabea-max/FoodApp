package com.example.applicationhome.core.domain.Implementations

import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.OrdersClass
import com.example.applicationhome.data.local.dao.OrdersDao
import com.example.applicationhome.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.remote.FoodAppAPIs
import com.example.applicationhome.domain.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class OrderRepositoryImpl @Inject constructor(
    userRepository: UserRepository,
    private val ordersDao : OrdersDao,
    private val api : FoodAppAPIs,
    @ApplicationScope private val externalScope: CoroutineScope
): OrderRepository {
    override val ordersHistory : StateFlow<List<OrdersDatabaseClass>> =
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


    override fun getOrdersHistoryFromDatabase(userId : String)
    : Flow<List<OrdersDatabaseClass>> = ordersDao.getAllOrders(userId)


    override suspend fun uploadOrderRequest(orderClass : OrdersClass, userId: String): Result<Unit> {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = current.format(formatter)

        val orderId = System.currentTimeMillis()

        return try {
            val response = api.putNewOrder(
                userId,
                orderId,
                orderClass.copy(date = date)
            )
            if(response.isSuccessful){
                Result.success(Unit)
            }else{
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
                Result.failure(Exception(errorMessage))
            }

        } catch (e : Exception){
            Result.failure(e)
        }
    }

    override suspend fun getOrders(userId: String) : String{
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