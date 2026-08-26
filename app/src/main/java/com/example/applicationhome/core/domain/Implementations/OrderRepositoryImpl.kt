package com.example.applicationhome.core.domain.Implementations

import com.example.applicationhome.core.domain.model.ordersDatabaseClassToOrderUiClass
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.data.data.model.OrderHistoryClass
import com.example.applicationhome.data.data.model.OrderStatesEnum
import com.example.applicationhome.data.data.model.OrderUiClass
import com.example.applicationhome.data.data.model.OrdersClass
import com.example.applicationhome.data.local.dao.OrdersDao
import com.example.applicationhome.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.remote.FoodAppAPIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class OrderRepositoryImpl @Inject constructor(
    private val ordersDao : OrdersDao,
    private val api : FoodAppAPIs
): OrderRepository {
    override fun getOrdersHistoryFromDatabase(userId : String, state : List<OrderStatesEnum>)
    : Flow<List<OrderUiClass>> =
        ordersDao.getOrders(userId, state.map { it.rawValue })
            .map { item ->
                item.map {
                    it.ordersDatabaseClassToOrderUiClass()
                }
            }


    override suspend fun uploadOrderRequest(orderClass : OrdersClass, userId: String): Result<Unit> {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val orderHistoryFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
        val date = current.format(formatter)
        val orderHistoryDate = current.format(orderHistoryFormatter)

        val orderId = System.currentTimeMillis()

        return try {
            val response = api.putNewOrder(
                userId,
                orderId,
                orderClass.copy(
                    date = date,
                    orderHistory = listOf(
                        OrderHistoryClass(
                            date = orderHistoryDate,
                            state = orderClass.state,
                            details = ""
                        )
                    )
                )
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
                        orderId = key,
                        userId = userId,
                        date = value.date,
                        state = value.state,
                        subtotal = value.subtotal,
                        delivery = value.delivery,
                        service = value.service,
                        totalPrice = value.totalPrice,
                        restaurantName = value.restaurantName,
                        restaurantImage = value.restaurantImage,
                        restaurantId = value.restaurantId,
                        userInformation = value.userInformation,
                        orderItems = value.orderItems,
                        orderHistory = value.orderHistory
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