package com.example.applicationhome.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.applicationhome.data.local.entity.UpdateAccountState
import com.example.applicationhome.data.local.entity.UserClass
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