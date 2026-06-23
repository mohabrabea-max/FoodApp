package com.example.applicationhome.data.models.repository

import com.example.applicationhome.data.models.model.OrdersClass
import com.example.applicationhome.data.models.remote.RetrofitInstance

class OrderRepository() {
    var userId: String = ""
        private set

    fun setUserId(userid: String) {
        userId = userid
    }




    suspend fun uploadOrderRequest(orderClass : OrdersClass): String {
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
        } finally {
            ""
        }
    }

    suspend fun getOrders() : Map<String, OrdersClass>{
        return try {
            val response = RetrofitInstance.api.getLastOrders(userId)
            val orders = response.body()
            if(response.isSuccessful && orders != null){
                orders
            }else{
                emptyMap()
            }
        } catch (E : Exception){
            emptyMap()
        }
    }
}