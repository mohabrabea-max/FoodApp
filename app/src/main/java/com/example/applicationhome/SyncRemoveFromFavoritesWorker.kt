package com.example.applicationhome

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.applicationhome.data.data.local.dao.FavoriteDao
import com.example.applicationhome.data.data.remote.FoodAppAPIs
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@HiltWorker
class SyncRemoveFromFavoritesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val favoriteDao : FavoriteDao,
    private val api : FoodAppAPIs
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
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
                                api.deleteFromFavorite(
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
                                api.deleteFromFavorite(
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
                                api.deleteFromFavorite(
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