package com.example.applicationhome

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.applicationhome.data.data.local.dao.FavoriteDao
import com.example.applicationhome.data.data.model.FavoriteClass
import com.example.applicationhome.data.data.remote.FoodAppAPIs
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@HiltWorker
class SyncAddToFavoritesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val favoriteDao: FavoriteDao,
    private val api : FoodAppAPIs
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            val unSyncedMeals = favoriteDao.getUnSyncedFood()
            val unSyncedSnacks = favoriteDao.getUnSyncedSnacks()
            val unSyncedRestaurants = favoriteDao.getUnSyncedRestaurants()

            var allSyncSuccess = true

//      =================  1. رفع الوجبات =================

            if(unSyncedMeals.isNotEmpty()){
                coroutineScope {
                    val unSyncedMealsToFirebase = unSyncedMeals.map { item ->
                        async {
                            try {
                                val response = api.addToFavorite(
                                    item.userId,
                                    "Meal_${item.mealId}",
                                    FavoriteClass(item.mealId, "Meal", item.restaurantId)
                                )
                                Pair(item, response.isSuccessful)
                            }catch (e : Exception){
                                Pair(item, false)
                            }
                        }
                    }
                    val results = unSyncedMealsToFirebase.awaitAll()
                    val successfulMeals = results.filter { it.second }.map { it.first }
                    if(successfulMeals.isNotEmpty()){
                        favoriteDao.markMealsAsSynced(successfulMeals.map { it.copy(isSynced = true) })
                    }
                    if(successfulMeals.size < unSyncedMeals.size){
                        allSyncSuccess = false
                    }
                }
            }

//      =================  2. رفع المطاعم =================

            if(unSyncedRestaurants.isNotEmpty()){
                coroutineScope {
                    val unSyncedRestaurantsToFirebase = unSyncedRestaurants.map { item ->
                        async {
                            try {
                                val response = api.addToFavorite(
                                    item.userId,
                                    "Restaurant_${item.restaurantId}",
                                    FavoriteClass(item.restaurantId, "Restaurant", item.restaurantId)
                                )
                                Pair(item, response.isSuccessful)
                            }catch (e : Exception){
                                Pair(item, false)
                            }
                        }
                    }
                    val results = unSyncedRestaurantsToFirebase.awaitAll()
                    val successfulRestaurants = results.filter { it.second }.map { it.first }
                    if(successfulRestaurants.isNotEmpty()){
                        favoriteDao.markRestaurantsAsSynced(successfulRestaurants.map { it.copy(isSynced = true) })
                    }
                    if(successfulRestaurants.size < unSyncedRestaurants.size){
                        allSyncSuccess = false
                    }
                }
            }

            //      =================  3. رفع السناكس =================

            if(unSyncedSnacks.isNotEmpty()){
                coroutineScope {
                    val unSyncedSnacksToFirebase = unSyncedSnacks.map { item ->
                        async {
                            try {
                                val response = api.addToFavorite(
                                    item.userId,
                                    "Snack_${item.snackId}",
                                    FavoriteClass(item.snackId, "Snack", item.restaurantId)
                                )
                                Pair(item, response.isSuccessful)
                            }catch (e : Exception){
                                Pair(item, false)
                            }
                        }
                    }
                    val results = unSyncedSnacksToFirebase.awaitAll()
                    val successfulSnacks = results.filter { it.second }.map { it.first }
                    if(successfulSnacks.isNotEmpty()){
                        favoriteDao.markSnacksAsSynced(successfulSnacks.map { it.copy(isSynced = true) })
                    }
                    if(successfulSnacks.size < unSyncedSnacks.size){
                        allSyncSuccess = false
                    }
                }
            }
            if (allSyncSuccess) {
                Result.success()
            } else {
                Result.retry()
            }
        }catch (e: Exception) {
            Result.retry()
        }
    }
}