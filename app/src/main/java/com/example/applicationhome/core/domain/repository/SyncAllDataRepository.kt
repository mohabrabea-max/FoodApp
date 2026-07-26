package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.core.domain.model.foodItemToMealsEntity
import com.example.applicationhome.core.domain.model.restaurantsToRestaurantsEntity
import com.example.applicationhome.core.domain.model.snackToSnacksEntity
import com.example.applicationhome.data.datastore.DataStoreManager
import com.example.applicationhome.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.data.local.entity.CategoriesEntity
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.remote.FoodAppAPIs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


//ServerValue.TIMESTAMP
@Singleton
class SyncAllDataRepository @Inject constructor(
    private val api : FoodAppAPIs,
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao,
    private val dataStoreManager : DataStoreManager
) {
    private suspend fun syncAllMealsToDatabase(){
        try {
            val lastSyncTime = dataStoreManager.mealsLastSyncTimeFlow.filterNotNull().first()
            val response = api.getMealsByLastUpdate(lastSyncTimestamp = lastSyncTime + 1)
            val meals = response.body()
            if(response.isSuccessful && meals != null){
                try {
                    foodAndRestaurantsDao.syncMealsToDatabase(meals.values.map { it.foodItemToMealsEntity() })
                    val newestTimestamp = meals.values.maxOfOrNull { it.updatedAt } ?: lastSyncTime
                    dataStoreManager.updateMealsSyncTime(newestTimestamp)
                }catch (e: Exception){

                }
            }else{
                val errorCode = response.code()

                when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
            }
        } catch(e: Exception){

        }
    }

    private suspend fun syncAllSnacksToDatabase(){
        try {
            val lastSyncTime = dataStoreManager.snacksLastSyncTimeFlow.filterNotNull().first()
            val response = api.getSnacksByLastUpdate(lastSyncTimestamp = lastSyncTime + 1)
            val snacks = response.body()
            if(response.isSuccessful && snacks != null){
                try {
                    foodAndRestaurantsDao.syncSnacksToDatabase(snacks.values.map { it.snackToSnacksEntity() })
                    val newestTimestamp = snacks.values.maxOfOrNull { it.updatedAt } ?: lastSyncTime
                    dataStoreManager.updateSnacksSyncTime(newestTimestamp)
                }catch (e: Exception){

                }
            }else{
                val errorCode = response.code()

                when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
            }
        } catch(e: Exception){

        }
    }

    private suspend fun syncAllRestaurantsToDatabase(){
        try {
            val lastSyncTime = dataStoreManager.restaurantsLastSyncTimeFlow.filterNotNull().first()
            val response = api.getRestaurantsByLastUpdate(lastSyncTimestamp = lastSyncTime + 1)
            val restaurants = response.body()
            if(response.isSuccessful && restaurants != null){
                try {
                    foodAndRestaurantsDao.syncRestaurantsToDatabase(restaurants.values.map { it.restaurantsToRestaurantsEntity() })
                    val newestTimestamp = restaurants.values.maxOfOrNull { it.updatedAt } ?: lastSyncTime
                    dataStoreManager.updateRestaurantsSyncTime(newestTimestamp)
                }catch (e: Exception){

                }
            }else{
                val errorCode = response.code()

                when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
            }
        } catch(e: Exception){

        }
    }

    private suspend fun syncCategoriesToDatabase(){
        try {
            val lastSyncTime = dataStoreManager.categoriesLastSyncTimeFlow.filterNotNull().first()
            val response = api.categorieslist(lastSyncTimestamp = 0)
            val categories = response.body()
            if(response.isSuccessful && categories != null){
                val categoriesEntity = categories.values.map { item ->
                    CategoriesEntity(
                        item.id,
                        item.name,
                        item.type,
                        item.image,
                        item.icon,
                        item.updatedAt
                    )
                }

                foodAndRestaurantsDao.syncCategoriesToDatabase(categoriesEntity)

                val newestTimestamp = categories.maxOfOrNull { it.value.updatedAt }?: lastSyncTime
                dataStoreManager.updateCategoriesSyncTime(newestTimestamp)
            }else{
                val errorCode = response.code()

                when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
            }
        }catch (e: Exception){

        }
    }

    private suspend fun syncOffersToDatabase(){
        try {
            val lastSyncTime = dataStoreManager.offersLastSyncTimeFlow.filterNotNull().first()
            val response = api.offers(lastSyncTimestamp = lastSyncTime)
            val offers = response.body()
            if(response.isSuccessful && offers != null){
                val offersEntity = offers.values.map { item ->
                    OffersEntity(
                        item.restaurantId,
                        item.id,
                        item.name,
                        item.image,
                        item.updatedAt
                    )
                }

                foodAndRestaurantsDao.syncOffersToDatabase(offersEntity)

                val newestTimestamp = offers.maxOfOrNull { it.value.updatedAt } ?: lastSyncTime
                dataStoreManager.updateOffersSyncTime(newestTimestamp)
            }else{
                val errorCode = response.code()

                when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
            }
        }catch (e: Exception){

        }
    }

    suspend fun syncDataParallel() {
        withContext(Dispatchers.IO){
            coroutineScope {
                launch { syncAllMealsToDatabase() }
                launch { syncAllSnacksToDatabase() }
                launch { syncAllRestaurantsToDatabase() }
                launch { syncCategoriesToDatabase() }
                launch { syncOffersToDatabase() }
            }
        }
    }
}