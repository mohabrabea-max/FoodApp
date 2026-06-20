package com.example.applicationhome.data.models.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsersDao {            // دا الجزء اللي بينفذ عمليات في الداتا بيز
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserClass>


    @Insert(onConflict = OnConflictStrategy.REPLACE)   // IGNORE دي بتتجاهل اي داتا عايز اضيفها فيها ايميل مطابق لايميل موجود قبل كدا
                                                       // REPLACE  بتستبدل الداتا القديمة بالجديدة لو الايميل متكرر في الداتا بيز
    suspend fun addUser(user : UserClass)
}