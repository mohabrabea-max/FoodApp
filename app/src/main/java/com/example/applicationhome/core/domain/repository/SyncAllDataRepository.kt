package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.core.domain.model.foodItemToMealsEntity
import com.example.applicationhome.core.domain.model.restaurantsToRestaurantsEntity
import com.example.applicationhome.core.domain.model.snackToSnacksEntity
import com.example.applicationhome.data.datastore.DataStoreManager
import com.example.applicationhome.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.data.remote.FoodAppAPIs
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


//ServerValue.TIMESTAMP
@Singleton
class SyncAllDataRepository @Inject constructor(
    private val api : FoodAppAPIs,
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao,
    private val dataStoreManager : DataStoreManager
) {
    val mealsLastSyncTime : Flow<Long> = dataStoreManager.mealsLastSyncTimeFlow

    val snacksLastSyncTime : Flow<Long> = dataStoreManager.snacksLastSyncTimeFlow

    val restaurantsLastSyncTime : Flow<Long> = dataStoreManager.restaurantsLastSyncTimeFlow



    suspend fun syncAllMealsToDatabase(mealsLastSyncTime : Long){
        try {
            val response = api.getMealsByLastUpdate(lastSyncTimestamp = mealsLastSyncTime)
            val meals = response.body()
            if(response.isSuccessful && meals != null){
                try {
                    foodAndRestaurantsDao.syncMealsToDatabase(meals.values.map { it.foodItemToMealsEntity() })
                    val newestTimestamp = meals.values.maxOfOrNull { it.updatedAt } ?: mealsLastSyncTime
                    dataStoreManager.updateMealsSyncTime(newestTimestamp)
                }catch (e: Exception){

                }
            }
        } catch(e: Exception){

        }
    }

    suspend fun syncAllSnacksToDatabase(snacksLastSyncTime : Long){
        try {
            val response = api.getSnacksByLastUpdate(lastSyncTimestamp = snacksLastSyncTime)
            val snacks = response.body()
            if(response.isSuccessful && snacks != null){
                try {
                    foodAndRestaurantsDao.syncSnacksToDatabase(snacks.values.map { it.snackToSnacksEntity() })
                    val newestTimestamp = snacks.values.maxOfOrNull { it.updatedAt } ?: snacksLastSyncTime
                    dataStoreManager.updateSnacksSyncTime(newestTimestamp)
                }catch (e: Exception){

                }
            }
        } catch(e: Exception){

        }
    }

    suspend fun syncAllRestaurantsToDatabase(restaurantsLastSyncTime : Long){
        try {
            val response = api.getRestaurantsByLastUpdate(lastSyncTimestamp = restaurantsLastSyncTime)
            val restaurants = response.body()
            if(response.isSuccessful && restaurants != null){
                try {
                    foodAndRestaurantsDao.syncRestaurantsToDatabase(restaurants.values.map { it.restaurantsToRestaurantsEntity() })
                    val newestTimestamp = restaurants.values.maxOfOrNull { it.updatedAt } ?: restaurantsLastSyncTime
                    dataStoreManager.updateRestaurantsSyncTime(newestTimestamp)
                }catch (e: Exception){

                }
            }
        } catch(e: Exception){

        }
    }
}