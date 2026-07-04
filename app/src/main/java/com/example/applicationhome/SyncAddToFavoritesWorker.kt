package com.example.applicationhome

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.applicationhome.data.models.local.db.UsersDatabase
import com.example.applicationhome.data.models.model.FavoriteClass
import com.example.applicationhome.data.models.remote.RetrofitInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SyncAddToFavoritesWorker(
    context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val favoriteDao = UsersDatabase.getDaoInstance(applicationContext).favoriteDao

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
                                    "${item.type}_${item.mealId}",
                                    FavoriteClass(item.mealId, item.type, item.restaurantId)
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


