package com.example.applicationhome.data.models.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.applicationhome.SyncAddToFavoritesWorker
import com.example.applicationhome.SyncRemoveFromFavoritesWorker
import com.example.applicationhome.data.models.local.dao.FavoriteDao
import com.example.applicationhome.data.models.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.models.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.models.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.models.model.FavoriteClass
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.remote.RetrofitInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val context: Context, private val favoriteDao : FavoriteDao) {
    private val _mealsFavoriteObject = mutableMapOf<String, FoodItem>()


    private val _snacksFavoriteObject = mutableMapOf<String, Snack>()


    private val _restaurantsFavoriteObject = java.util.concurrent.ConcurrentHashMap<String, Restaurants>()



    fun getFoodFavoriteFromDatabase(userId : String)
    : Flow<List<FavoriteFoodDatabase>> = favoriteDao.getFoodFromDatabase(userId)

    fun getSnacksFavoriteFromDatabase(userId : String)
            : Flow<List<FavoriteSnacksDatabase>> = favoriteDao.getSnacksFromDatabase(userId)

    fun getRestaurantsFavoriteFromDatabase(userId : String)
    : Flow<List<FavoriteRestaurantDatabase>> = favoriteDao.getRestaurantsFromDatabase(userId)


    suspend fun syncFavoritesInDatabase(userId : String) : String{
        val favoriteList : Map<String, FavoriteClass>
        return try {
            val response = RetrofitInstance.api.getFavoriteItems(userId)
            val favorite = response.body()
            if(response.isSuccessful && favorite != null){
                favoriteList = favorite
                val mealsFavorite = favoriteList.filter { it.value.typ == "Meal" }.values.toList()
                val snacksFavorite = favoriteList.filter { it.value.typ == "Snack" }.values.toList()
                val restaurantsFavorite = favoriteList.filter { it.value.typ == "Restaurant" }.values.toList()
                try {
                    coroutineScope {
                        val deferredMeals = mealsFavorite.map { item ->
                            async {
                                try {
                                    val response = RetrofitInstance.api.getFavoriteMeals("\"id\"", item.id)
                                    val resultMap = response.body()
                                    if(response.isSuccessful && resultMap != null){
                                        _mealsFavoriteObject += resultMap
                                        resultMap
                                    }else{ null }
                                }catch (e : Exception){ null }
                            }
                        }
                        val mealsFavoriteToAddInDatabase : List<FavoriteFoodDatabase>

                        val finalMealsList = mutableMapOf<String, FoodItem>()

                        deferredMeals.awaitAll().filterNotNull().forEach { item ->
                            finalMealsList += item
                        }

                        mealsFavoriteToAddInDatabase = finalMealsList.values.map { item ->
                            FavoriteFoodDatabase(
                                userId,
                                item.id,
                                item.category,
                                item.name,
                                item.details,
                                item.image.first(),
                                item.sizeOptions,
                                item.restaurantId,
                                item.review,
                                true,
                                false
                            )
                        }
                        favoriteDao.addFoodToFavorite(mealsFavoriteToAddInDatabase)


                        val deferredSnacks = snacksFavorite.map { item ->
                            async {
                                try {
                                    val response = RetrofitInstance.api.getFavoriteSnacks("\"id\"", item.id)
                                    val resultMap = response.body()
                                    if(response.isSuccessful && resultMap != null){
                                        _snacksFavoriteObject += resultMap
                                        resultMap
                                    }else{ null }
                                }catch (e : Exception){ null }
                            }
                        }
                        val snacksFavoriteToAddInDatabase : List<FavoriteSnacksDatabase>

                        val finalSnacksList = mutableMapOf<String, Snack>()

                        deferredSnacks.awaitAll().filterNotNull().forEach { item ->
                            finalSnacksList += item
                        }

                        snacksFavoriteToAddInDatabase = finalSnacksList.values.map { item ->
                            FavoriteSnacksDatabase(
                                userId,
                                item.id,
                                item.name,
                                item.details,
                                item.image.first(),
                                item.priceANDsize,
                                item.restaurantId,
                                item.review,
                                true,
                                false
                            )
                        }
                        favoriteDao.addSnacksToFavorite(snacksFavoriteToAddInDatabase)


                        val deferredRestaurants = restaurantsFavorite.map { item ->
                            async {
                                try {
                                    val response = RetrofitInstance.api.getFavoriteRestaurants("\"id\"", item.id)
                                    val resultMap = response.body()
                                    if(response.isSuccessful && resultMap != null){
                                        _restaurantsFavoriteObject.putAll(resultMap)
                                        resultMap
                                    }else{ null }
                                }catch (e : Exception){ null }
                            }
                        }
                        val restaurantsFavoriteToAddInDatabase : List<FavoriteRestaurantDatabase>

                        val finalRestaurantsList = mutableMapOf<String, Restaurants>()

                        deferredRestaurants.awaitAll().filterNotNull().forEach { item ->
                            finalRestaurantsList += item
                        }

                        restaurantsFavoriteToAddInDatabase = finalRestaurantsList.values.map { item ->
                            FavoriteRestaurantDatabase(
                                userId,
                                item.id,
                                item.name,
                                item.image,
                                item.image2,
                                true,
                                false
                            )
                        }
                        favoriteDao.addRestaurantToFavorite(restaurantsFavoriteToAddInDatabase)
                    }
                }finally {
                    null
                }
            }
            "Success"
        }catch (e : Exception){
            "Network error"
        }
    }

    suspend fun addFoodToFavorite(foodItem : FavoriteFoodDatabase){
        favoriteDao.addFoodToFavorite(listOf(foodItem))
        triggerOfflineSyncWorker()
    }

    suspend fun addSnackToFavorite(snackItem : FavoriteSnacksDatabase){
        favoriteDao.addSnacksToFavorite(listOf(snackItem))
        triggerOfflineSyncWorker()
    }

    suspend fun addRestaurantToFavorite(restaurantItem : FavoriteRestaurantDatabase){
        favoriteDao.addRestaurantToFavorite(listOf(restaurantItem))
        triggerOfflineSyncWorker()
    }

    private fun triggerOfflineSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncAddToFavoritesWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "sync_favorites_work",  //  اسم الWorker
            ExistingWorkPolicy.KEEP,   // عشان الWorker ميتعملش منه اكتر من نسخة
            syncRequest
        )
    }

    suspend fun deleteFoodFromFavorite(userId : String, mealId : Int){
        favoriteDao.markFoodAsDeletedOffline(userId, mealId)
        triggerOfflineRemoveWorker()
    }

    suspend fun deleteSnackFromFavorite(userId : String, snackId : Int){
        favoriteDao.markSnacksAsDeletedOffline(userId, snackId)
        triggerOfflineRemoveWorker()
    }

    suspend fun deleteRestaurantFromFavorite(userId : String, resId : Int){
        favoriteDao.markRestaurantsAsDeletedOffline(userId, resId)
        triggerOfflineRemoveWorker()
    }

    private fun triggerOfflineRemoveWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncRemoveFromFavoritesWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "delete_favorites_work",
            ExistingWorkPolicy.KEEP,
            syncRequest
        )
    }

    fun getMealToView(mealKey : String): FoodItem?{
        return _mealsFavoriteObject[mealKey]
    }

    fun getSnackToView(snackKey : String): Snack?{
        return _snacksFavoriteObject[snackKey]
    }

    suspend fun getRestaurantToView(resId : Int): Restaurants? {
        _restaurantsFavoriteObject["Restaurant_${resId}"]?.let { return it }

        return try {
            val response = RetrofitInstance.api.getFavoriteRestaurants("\"id\"", resId)
            val resultMap = response.body()
            if (response.isSuccessful && resultMap != null) {
                _restaurantsFavoriteObject.putAll(resultMap) // دمج آمن جوه الـ ConcurrentHashMap
                resultMap["Restaurant_${resId}"]
            } else { null }
        } catch (e: Exception) { null }
    }
}