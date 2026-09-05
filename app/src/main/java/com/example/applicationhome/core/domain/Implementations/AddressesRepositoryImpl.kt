package com.example.applicationhome.core.domain.Implementations

import com.example.applicationhome.core.domain.model.addressToAddressesEntity
import com.example.applicationhome.core.domain.repository.AddressesRepository
import com.example.applicationhome.data.data.model.Address
import com.example.applicationhome.data.local.dao.UsersDao
import com.example.applicationhome.data.local.entity.AddressesEntity
import com.example.applicationhome.data.remote.FoodAppAPIs
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class AddressesRepositoryImpl @Inject constructor(
    private val api : FoodAppAPIs,
    private val usersDao : UsersDao
): AddressesRepository {

    override fun getAddresses(userId: String): Flow<List<AddressesEntity>> =
        usersDao.getAddress(userId)


    private suspend fun <T> retryLocally(
        times : Int = 3,
        initialDelay : Long = 1500,
        block : suspend  () -> T
    ): T {
        var currentDelay = initialDelay

        repeat(times - 1){
            try {
                return block()
            } catch (e: Exception) {
                if(e is CancellationException) throw e
                delay(currentDelay.milliseconds)
                currentDelay *= 2
            }
        }
        return block()
    }

    private inline fun <reified T> Response<T>.getOrThrow(): T {
        if (isSuccessful) {
            val body = body()
            if (body == null && Unit is T) {
                @Suppress("UNCHECKED_CAST")
                return Unit as T
            }
            return body ?: throw NullPointerException("Response body was null")
        }
        throw HttpException(this)
    }

    override suspend fun addAddress(userId : String, address : Address): Result<Unit> {
        return runCatching {
            retryLocally{
                val newUpdateTime = System.currentTimeMillis()
                val finalAddress = address.copy(lastUse = newUpdateTime)

                api.putAddresses(
                    userId = userId,
                    addressId = newUpdateTime,
                    address = finalAddress
                ).getOrThrow()

                val daoAddress = finalAddress.addressToAddressesEntity(
                    userId = userId,
                    addressId = newUpdateTime
                )
                usersDao.addNewAddresses(listOf(daoAddress))
            }
        }
    }

    override suspend fun updateAddresses(userId : String, addressId : Long, address : Address): Result<Unit> {
        return runCatching {
            retryLocally {
                val newUpdateTime = System.currentTimeMillis()
                val finalAddress = address.copy(lastUse = newUpdateTime)

                api.putAddresses(
                    userId = userId,
                    addressId = addressId,
                    address = address
                ).getOrThrow()

                val daoAddress = finalAddress.addressToAddressesEntity(
                    userId = userId,
                    addressId = addressId
                )
                usersDao.addNewAddresses(listOf(daoAddress))
            }
        }
    }

    override suspend fun updateAddressesLastUse(userId : String, addressId : Long): Result<Unit> {
        return runCatching {
            retryLocally{
                val newUpdateTime = System.currentTimeMillis()

                api.updateAddressesLastUse(
                    userId = userId,
                    addressId = addressId,
                    lastUse = newUpdateTime
                ).getOrThrow()

                usersDao.updateLastUseAddress(
                    lastUse = newUpdateTime,
                    userId = userId,
                    addressId = addressId
                )
            }
        }
    }

    override suspend fun deleteAddress(userId : String, addressId : Long): Result<Unit> {
        return runCatching {
            retryLocally{
                api.deleteAddress(
                    userId = userId,
                    addressId = addressId
                ).getOrThrow()

                usersDao.deleteAddress(userId, addressId)
            }
        }
    }
}