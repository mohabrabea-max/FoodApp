package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.data.model.OrdersClass
import com.example.applicationhome.data.local.entity.OrdersDatabaseClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface OrderRepository {
    val ordersHistory : StateFlow<List<OrdersDatabaseClass>>

    fun getOrdersHistoryFromDatabase(userId : String): Flow<List<OrdersDatabaseClass>>
    suspend fun uploadOrderRequest(orderClass : OrdersClass, userId: String): Result<Unit>
    suspend fun getOrders(userId: String) : String

}