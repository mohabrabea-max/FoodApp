package com.example.applicationhome.data.models.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {            // دا الجزء اللي بينفذ عمليات في الداتا بيز
//    @Query("SELECT * FROM users")
//    suspend fun getAllUsers(): List<UserClass>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getOneUser(userId : String): Flow<UserClass?>

    @Query("SELECT * FROM users WHERE isActive = :isActive")
    fun getActiveUser(isActive : Boolean): Flow<UserClass?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)   // IGNORE دي بتتجاهل اي داتا عايز اضيفها فيها ايميل مطابق لايميل موجود قبل كدا
                                                       // REPLACE  بتستبدل الداتا القديمة بالجديدة لو الايميل متكرر في الداتا بيز
    suspend fun addUser(user : UserClass)

    @Update(entity = UserClass::class)
    suspend fun updateUser(updateState: UpdateAccountState)
}


@Dao
interface CartDao {            // دا الجزء اللي بينفذ عمليات في الداتا بيز
    @Query("SELECT * FROM cart_items WHERE userId = :userid")
    fun getCartItems(userid : String): Flow<List<CartItemsClass?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCartItem(cartItem : CartItemsClass)

    @Query("UPDATE cart_items SET quantity = :newQuantity, totalPrice = :newTotalPrice WHERE userId = :userId AND mealKey = :mealkey")
    suspend fun updateCartItem(newQuantity: Int, newTotalPrice : Double, userId : String, mealkey : String)

    @Query("DELETE FROM cart_items WHERE userId = :userId AND mealKey = :mealkey")
    suspend fun deleteItemFromCart(mealkey : String, userId : String)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun deleteAllItemFromCart(userId : String)

    @Query("SELECT * FROM cart WHERE userId = :userid")
    fun getParentCart(userid : String): Flow<CartClass?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createParentCart(cart : CartClass)

    @Query("DELETE FROM cart WHERE userId = :userid")
    suspend fun deleteParentCart(userid : String)
}