package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.core.domain.model.Address
import com.example.applicationhome.core.data.local.entity.AddressesEntity
import kotlinx.coroutines.flow.Flow

interface AddressesRepository {
    fun getAddresses(userId : String): Flow<List<AddressesEntity>>
    suspend fun addAddress(userId : String, addressId : Long, address : Address): Result<Unit>
    suspend fun updateAddressesLastUse(userId : String, addressId : Long): Result<Unit>
    suspend fun deleteAddress(userId : String, addressId : Long): Result<Unit>
}