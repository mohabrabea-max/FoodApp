package com.example.applicationhome.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.applicationhome.data.local.entity.UserClass
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {            // دا الجزء اللي بينفذ عمليات في الداتا بيز
//    @Query("SELECT * FROM users")
//    suspend fun getAllUsers(): List<UserClass>

    @Query("SELECT * FROM users WHERE isActive = 1")
    fun getActiveUser(): Flow<UserClass?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user : UserClass)

    @Query("DELETE FROM users")
    suspend fun deleteUserFromDatabase()
}