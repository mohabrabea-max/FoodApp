package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.data.model.OrderStatesEnum
import com.example.applicationhome.data.data.model.OrderUiClass
import com.example.applicationhome.data.data.model.OrdersClass
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrdersHistoryFromDatabase(userId : String, state : List<OrderStatesEnum>): Flow<List<OrderUiClass>>
    suspend fun uploadOrderRequest(orderClass : OrdersClass, userId: String): Result<Unit>
    suspend fun getOrders(userId: String) : String

}