package com.example.applicationhome

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.applicationhome.data.data.local.dao.FavoriteDao
import com.example.applicationhome.data.data.model.FavoriteClass
import com.example.applicationhome.data.data.remote.RetrofitInstance
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@HiltWorker
class SyncAddToFavoritesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val favoriteDao: FavoriteDao
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            val unSyncedMeals = favoriteDao.getUnSyncedFood()
            val unSyncedSnacks = favoriteDao.getUnSyncedSnacks()
            val unSyncedRestaurants = favoriteDao.getUnSyncedRestaurants()

            var mealsSyncSuccess = true
            var snacksSyncSuccess = true
            var restaurantsSyncSuccess = true

//      =================  1. رفع الوجبات =================

            if(unSyncedMeals.isNotEmpty()){
                coroutineScope {
                    val unSyncedMealsToFirebase = unSyncedMeals.map { item ->
                        async {
                            try {
                                RetrofitInstance.api.addToFavorite(
                                    item.userId,
                                    "Meal_${item.mealId}",
                                    FavoriteClass(item.mealId, "Meal", item.restaurantId)
                                ).isSuccessful
                            }catch (e : Exception){ false }
                        }
                    }
                    val results = unSyncedMealsToFirebase.awaitAll()
                    if(results.all { it }){
                        favoriteDao.markMealsAsSynced(unSyncedMeals.map { it.copy(isSynced = true) })
                    }else {
                        mealsSyncSuccess = false
                    }
                }
            }

//      =================  2. رفع المطاعم =================

            if(unSyncedRestaurants.isNotEmpty()){
                coroutineScope {
                    val unSyncedRestaurantsToFirebase = unSyncedRestaurants.map { item ->
                        async {
                            try {
                                RetrofitInstance.api.addToFavorite(
                                    item.userId,
                                    "Restaurant_${item.restaurantId}",
                                    FavoriteClass(item.restaurantId, "Restaurant", item.restaurantId)
                                ).isSuccessful
                            }catch (e : Exception){ false }
                        }
                    }
                    val results = unSyncedRestaurantsToFirebase.awaitAll()
                    if(results.all { it }){
                        favoriteDao.markRestaurantsAsSynced(unSyncedRestaurants.map { it.copy(isSynced = true) })
                    }else {
                        restaurantsSyncSuccess = false
                    }
                }
            }

            //      =================  3. رفع السناكس =================

            if(unSyncedSnacks.isNotEmpty()){
                coroutineScope {
                    val unSyncedSnacksToFirebase = unSyncedSnacks.map { item ->
                        async {
                            try {
                                RetrofitInstance.api.addToFavorite(
                                    item.userId,
                                    "Snack_${item.snackId}",
                                    FavoriteClass(item.snackId, "Snack", item.restaurantId)
                                ).isSuccessful
                            }catch (e : Exception){ false }
                        }
                    }
                    val results = unSyncedSnacksToFirebase.awaitAll()
                    if(results.all { it }){
                        favoriteDao.markSnacksAsSynced(unSyncedSnacks.map { it.copy(isSynced = true) })
                    }else{
                        snacksSyncSuccess = false
                    }
                }
            }

            if (mealsSyncSuccess && snacksSyncSuccess && restaurantsSyncSuccess) {
                Result.success()
            } else {
                Result.retry()
            }
        }catch (e: Exception) {
            Result.retry()
        }
    }
}


