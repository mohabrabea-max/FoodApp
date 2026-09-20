package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.core.ui.model.UiStates
import com.example.applicationhome.core.domain.model.OrderStatesEnum
import com.example.applicationhome.core.domain.model.OrderUiClass
import com.example.applicationhome.core.domain.model.OrdersClass
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrdersHistoryFromDatabase(userId : String, state : List<OrderStatesEnum>): Flow<List<OrderUiClass>>
    suspend fun uploadOrderRequest(orderClass : OrdersClass, userId: String): Result<Unit>
    suspend fun cancelOrder(userId : String, orderId : Long, index : Int): Result<Unit>
    suspend fun getOrders(userId: String) : UiStates
    suspend fun checkAreMealsDeleted(mealsIds : List<Int>, snacksIds : List<Int>): Boolean
    suspend fun filterOrderItems(
        mealsIds : List<Int>,
        snacksIds : List<Int>
    ): List<Int>
    suspend fun isRestaurantExist(resId : Int): Boolean
    suspend fun getRestaurantImage(resId : Int): String
    suspend fun getMealsImages(ids : List<Int>): Map<Int, String?>
    suspend fun getSnacksImages(ids : List<Int>): Map<Int, String?>
}