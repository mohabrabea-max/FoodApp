package com.example.applicationhome.core.domain.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.applicationhome.SyncAddToFavoritesWorker
import com.example.applicationhome.SyncRemoveFromFavoritesWorker
import com.example.applicationhome.data.data.model.FavoriteClass
import com.example.applicationhome.data.datastore.DataStoreManager
import com.example.applicationhome.data.local.dao.FavoriteDao
import com.example.applicationhome.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantsEntity
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import com.example.applicationhome.data.remote.FoodAppAPIs
import com.example.applicationhome.domain.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteRepository @Inject constructor(
    userRepository: UserRepository,
    private val favoriteDao : FavoriteDao,
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao,
    private val dataStoreManager: DataStoreManager,
    private val api : FoodAppAPIs,
    @ApplicationContext private val context: Context,
    @ApplicationScope private val externalScope: CoroutineScope
){

// *** ---------------------- \\***  Favorite Items  ***// ---------------------- ***

    val favoriteMeals : StateFlow<List<MealWithFavoriteStatus>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            if(id.isNotEmpty()){
                getFoodFavoriteFromDatabase(id)
            }else{
                flowOf(emptyList())
            }
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favoriteMealsIds = favoriteMeals.map { list ->
        list.map { it.meal.id }.toSet()
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet()
    )


    val favoriteSnacks : StateFlow<List<SnackWithFavoriteStatus>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            if (id.isNotEmpty()) {
                getSnacksFavoriteFromDatabase(id)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favoriteSnacksIds = favoriteSnacks.map { list ->
        list.map { it.snack.id }.toSet()
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet()
    )


    val favoriteRestaurantsFromDatabase : StateFlow<List<RestaurantWithFavoriteStatus>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            if (id.isNotEmpty()){
                getRestaurantsFavoriteFromDatabase(id)
            }else{
                flowOf(emptyList())
            }
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favoriteRestaurantsIds = favoriteRestaurantsFromDatabase.map { list ->
        list.map { it.restaurant.id }.toSet()
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet()
    )


// *** ---------------------- \\***  Favorite Count  ***// ---------------------- ***

    val favoriteFoodCount : StateFlow<Int> = favoriteMeals
        .map { it -> it.filter { it.isFavorite }.size }
        .stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val favoriteSnacksCount : StateFlow<Int> = favoriteSnacks
        .map { it -> it.filter { it.isFavorite }.size }
        .stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val favoriteRestaurantsCount : StateFlow<Int> = favoriteRestaurantsFromDatabase
        .map { it -> it.filter { it.isFavorite }.size }
        .stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val totalCountInFavorite : StateFlow<Int> = combine(
        favoriteFoodCount,
        favoriteSnacksCount,
        favoriteRestaurantsCount
    ){ (food , snacks, restaurants) ->
        food + snacks + restaurants
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )


    // *** ---------------------- \\***  Favorite Functions  ***// ---------------------- ***

    val favoriteLastSyncTime : Flow<Long?> = dataStoreManager.favoriteLastSyncTimeFlow

    fun getFoodFavoriteFromDatabase(userId : String)
    : Flow<List<MealWithFavoriteStatus>> = favoriteDao.getFoodFromDatabase(userId)

    fun getSnacksFavoriteFromDatabase(userId : String)
            : Flow<List<SnackWithFavoriteStatus>> = favoriteDao.getSnacksFromDatabase(userId)

    fun getRestaurantsFavoriteFromDatabase(userId : String)
    : Flow<List<RestaurantWithFavoriteStatus>> = favoriteDao.getRestaurantsFromDatabase(userId)


    suspend fun syncFavoritesInDatabase(userId : String) : String{
        val favoriteList : Map<String, FavoriteClass>
        return try {
            val response = api.getFavoriteItems(userId)
            val favorite = response.body()
            if(response.isSuccessful && favorite != null){
                favoriteList = favorite
                val mealsFavorite = favoriteList.filter { it.value.typ == "Meal" }.values.map { item ->
                    FavoriteMealEntity(
                        item.id,
                        userId,
                        item.restaurants,
                        true,
                        false
                    )
                }
                val snacksFavorite = favoriteList.filter { it.value.typ == "Snack" }.values.map { item ->
                    FavoriteSnackEntity(
                        item.id,
                        userId,
                        item.restaurants,
                        true,
                        false
                    )
                }
                val restaurantsFavorite = favoriteList.filter { it.value.typ == "Restaurant" }.values.map { item ->
                    FavoriteRestaurantEntity(
                        item.id,
                        userId,
                        true,
                        false
                    )
                }

                favoriteDao.addFoodToFavorite(mealsFavorite)

                favoriteDao.addSnacksToFavorite(snacksFavorite)

                favoriteDao.addRestaurantToFavorite(restaurantsFavorite)

            }
            "Success"
        }catch (e : Exception){
            "Network error"
        }
    }

    suspend fun addFoodToFavorite(foodItem : FavoriteMealEntity){
        favoriteDao.addFoodToFavorite(listOf(foodItem))
        triggerOfflineSyncWorker()
    }

    suspend fun addSnackToFavorite(snackItem : FavoriteSnackEntity){
        favoriteDao.addSnacksToFavorite(listOf(snackItem))
        triggerOfflineSyncWorker()
    }

    suspend fun addRestaurantToFavorite(restaurantItem : FavoriteRestaurantEntity){
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

    suspend fun getRestaurantToView(resId : Int): RestaurantsEntity =
        foodAndRestaurantsDao.getOneRestaurantFromDatabase(resId)
}