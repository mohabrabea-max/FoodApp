package com.example.applicationhome

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.applicationhome.data.models.local.db.UsersDatabase
import com.example.applicationhome.data.models.remote.RetrofitInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SyncRemoveFromFavoritesWorker(
    context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val favoriteDao = UsersDatabase.getDaoInstance(applicationContext).favoriteDao

        return try {
            favoriteDao.cleanUpLocalOnlyDeletedMeals()
            favoriteDao.cleanUpLocalOnlyDeletedSnacks()
            favoriteDao.cleanUpLocalOnlyDeletedRestaurants()

            val unDeletedOnlineMeals = favoriteDao.getFoodDeletedOffline()
            val unDeletedOnlineSnacks = favoriteDao.getSnacksDeletedOffline()
            val unDeletedOnlineRestaurants = favoriteDao.getRestaurantsDeletedOffline()

            var mealsSyncSuccess = true
            var snacksSyncSuccess = true
            var restaurantsSyncSuccess = true

//      =================  1. حذف الوجبات =================

            if(unDeletedOnlineMeals.isNotEmpty()){
                coroutineScope {
                    val unDeletedOnlineMealsToFirebase = unDeletedOnlineMeals.map { item ->
                        async {
                            try {
                                RetrofitInstance.api.deleteFromFavorite(
                                    item.userId,
                                    "Meal_${item.mealId}"
                                ).isSuccessful
                            }catch (e : Exception){ false }
                        }
                    }
                    val results = unDeletedOnlineMealsToFirebase.awaitAll()
                    if(results.all { it }){
                        unDeletedOnlineMeals.groupBy { it.userId }.forEach { (userId, mealsGroup) ->
                            favoriteDao.deleteFoodFromDatabase(userId, mealsGroup.map { it.mealId })
                        }
                    }else{
                        mealsSyncSuccess = false
                    }
                }
            }

//      =================  2. حذف المطاعم =================

            if(unDeletedOnlineRestaurants.isNotEmpty()){
                coroutineScope {
                    val unDeletedOnlineRestaurantsToFirebase = unDeletedOnlineRestaurants.map { item ->
                        async {
                            try {
                                RetrofitInstance.api.deleteFromFavorite(
                                    item.userId,
                                    "Restaurant_${item.restaurantId}"
                                ).isSuccessful
                            }catch (e : Exception){ false }
                        }
                    }
                    val results = unDeletedOnlineRestaurantsToFirebase.awaitAll()
                    if(results.all { it }){
                        unDeletedOnlineRestaurants.groupBy { it.userId }.forEach { (userId, mealsGroup) ->
                            favoriteDao.deleteRestaurantFromDatabase(userId, mealsGroup.map { it.restaurantId })
                        }
                    }else{
                        restaurantsSyncSuccess = false
                    }
                }
            }

            //      =================  2. حذف السناكس =================

            if(unDeletedOnlineSnacks.isNotEmpty()){
                coroutineScope {
                    val unDeletedOnlineSnacksToFirebase = unDeletedOnlineSnacks.map { item ->
                        async {
                            try {
                                RetrofitInstance.api.deleteFromFavorite(
                                    item.userId,
                                    "Snack_${item.snackId}"
                                ).isSuccessful
                            }catch (e : Exception){ false }
                        }
                    }
                    val results = unDeletedOnlineSnacksToFirebase.awaitAll()
                    if(results.all { it }){
                        unDeletedOnlineSnacks.groupBy { it.userId }.forEach { (userId, mealsGroup) ->
                            favoriteDao.deleteSnacksFromDatabase(userId, mealsGroup.map { it.snackId })
                        }
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