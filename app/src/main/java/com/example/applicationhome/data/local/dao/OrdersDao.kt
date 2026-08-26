package com.example.applicationhome.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.applicationhome.data.local.entity.OrdersDatabaseClass
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDao {
    @Upsert
    suspend fun addNewOrders(orders : List<OrdersDatabaseClass>)

    @Query("SELECT * FROM orders_history WHERE userId =:userId AND state IN (:states) ORDER BY orderId DESC")
    fun getOrders(userId : String, states : List<String>) : Flow<List<OrdersDatabaseClass>>
}