package com.example.applicationhome.data.models.repository

import com.example.applicationhome.data.models.model.OrdersClass
import com.example.applicationhome.data.models.remote.RetrofitInstance

class OrderRepository() {
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

    suspend fun getOrders(userId: String) : Map<String, OrdersClass>{
        return try {
            val response = RetrofitInstance.api.getLastOrders(userId)
            val orders = response.body()
            if(response.isSuccessful && orders != null){
                orders
            }else{
                emptyMap()
            }
        } catch (E : Exception){
            E.printStackTrace()
            emptyMap()
        }
    }
}