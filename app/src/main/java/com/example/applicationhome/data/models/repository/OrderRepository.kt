package com.example.applicationhome.data.models.repository

import com.example.applicationhome.data.models.local.dao.OrdersDao
import com.example.applicationhome.data.models.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.models.model.OrdersClass
import com.example.applicationhome.data.models.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val ordersDao : OrdersDao) {

    fun getOrdersHistoryFromDatabase(userId : String)
    : Flow<List<OrdersDatabaseClass>> = ordersDao.getAllOrders(userId)


    suspend fun uploadOrderRequest(orderClass : OrdersClass, userId: String): String {
        val orderId = System.currentTimeMillis().toString()

        return try {
            val response = RetrofitInstance.api.putNewOrder(
                userId,
                orderId,
                orderClass
            )
            if(response.isSuccessful){
                "Success"
            }else{
                "Network error"
            }
        } catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun getOrders(userId: String) : String{
        return try {
            val response = RetrofitInstance.api.getLastOrders(userId)
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
            }
            "Success"
        } catch (E : Exception){
            E.printStackTrace()
            "Network error"
        }
    }
}