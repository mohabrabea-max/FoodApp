package com.example.applicationhome.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.applicationhome.data.local.entity.AddressesEntity
import com.example.applicationhome.data.local.entity.UserClass
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {            // دا الجزء اللي بينفذ عمليات في الداتا بيز
//    @Query("SELECT * FROM users")
//    suspend fun getAllUsers(): List<UserClass>

    @Query("SELECT * FROM users WHERE isActive = 1")
    fun getActiveUser(): Flow<UserClass?>

    @Upsert
    suspend fun addUser(user : UserClass)

    @Query("UPDATE users SET phonenumber = :newNumber WHERE id = :userId")
    suspend fun updatePhoneNumber(userId : String, newNumber : String)

    @Query("DELETE FROM users")
    suspend fun deleteUserFromDatabase()



    @Query("SELECT * FROM addresses WHERE userId = :userId ORDER BY lastUse DESC")
    fun getAddress(userId: String) : Flow<List<AddressesEntity>>

    @Upsert
    suspend fun addNewAddresses(addresses : List<AddressesEntity>)

    @Query("UPDATE addresses SET lastUse = :lastUse WHERE userId = :userId AND addressId = :addressId")
    suspend fun updateLastUseAddress(lastUse : Long, userId: String, addressId : Long)

    @Query("DELETE FROM addresses WHERE userId = :userId AND addressId = :addressId")
    suspend fun deleteAddress(userId : String, addressId : Long)
}