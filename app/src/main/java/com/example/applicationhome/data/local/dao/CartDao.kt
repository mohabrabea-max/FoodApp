package com.example.applicationhome.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.applicationhome.data.local.entity.CartClass
import com.example.applicationhome.data.local.entity.CartItemsClass
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {            // دا الجزء اللي بينفذ عمليات في الداتا بيز

    //               --------------------------------------   Cart Items    -------------------------------------

    @Query("SELECT * FROM cart_items WHERE userId = :userid")
    fun getCartItems(userid : String): Flow<List<CartItemsClass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCartItem(cartItem : CartItemsClass)

    @Query("UPDATE cart_items SET quantity = :newQuantity, totalPrice = :newTotalPrice WHERE userId = :userId AND mealKey = :mealkey")
    suspend fun updateCartItem(newQuantity: Int, newTotalPrice : Double, userId : String, mealkey : String)

    @Upsert
    suspend fun updateMoreThanOneCartItem(foods : List<CartItemsClass>)

    @Query("DELETE FROM cart_items WHERE userId = :userId AND mealKey = :mealkey")
    suspend fun deleteItemFromCart(mealkey : String, userId : String)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun deleteAllItemFromCart(userId : String)


    //             --------------------------------------   Parent Cart    -------------------------------------


    @Query("SELECT * FROM cart WHERE userId = :userid")
    fun getParentCart(userid : String): Flow<CartClass?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createParentCart(cart : CartClass)

    @Query("DELETE FROM cart WHERE userId = :userid")
    suspend fun deleteParentCart(userid : String)
}