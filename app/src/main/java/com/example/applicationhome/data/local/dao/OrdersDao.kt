package com.example.applicationhome.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.applicationhome.data.local.entity.OrdersDatabaseClass
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewOrders(orders : List<OrdersDatabaseClass>)

    @Query("SELECT * FROM orders_history WHERE userId =:userId ORDER BY orderId DESC")
    fun getAllOrders(userId : String) : Flow<List<OrdersDatabaseClass>>
}