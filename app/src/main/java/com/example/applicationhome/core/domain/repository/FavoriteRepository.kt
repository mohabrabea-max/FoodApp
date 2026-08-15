package com.example.applicationhome.core.domain.repository

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.applicationhome.SyncAddToFavoritesWorker
import com.example.applicationhome.SyncRemoveFromFavoritesWorker
import com.example.applicationhome.data.local.dao.FavoriteDao
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import com.example.applicationhome.domain.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class FavoriteRepository @Inject constructor(
    userRepository: UserRepository,
    private val favoriteDao : FavoriteDao,
    private val workManager : WorkManager,
    @ApplicationScope private val externalScope: CoroutineScope
){

// *** ---------------------- \\***  Favorite Items  ***// ---------------------- ***

    val favoriteMeals : StateFlow<List<MealWithFavoriteStatus>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            getFoodFavoriteFromDatabase(id)
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val favoriteSnacks : StateFlow<List<SnackWithFavoriteStatus>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            getSnacksFavoriteFromDatabase(id)
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val favoriteRestaurantsFromDatabase : StateFlow<List<RestaurantWithFavoriteStatus>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            getRestaurantsFavoriteFromDatabase(id)
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
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

    fun getFoodFavoriteFromDatabase(userId : String)
    : Flow<List<MealWithFavoriteStatus>> = favoriteDao.getFoodFromDatabase(userId)

    fun getSnacksFavoriteFromDatabase(userId : String)
    : Flow<List<SnackWithFavoriteStatus>> = favoriteDao.getSnacksFromDatabase(userId)

    fun getRestaurantsFavoriteFromDatabase(userId : String)
    : Flow<List<RestaurantWithFavoriteStatus>> = favoriteDao.getRestaurantsFromDatabase(userId)


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
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                10,
                TimeUnit.SECONDS
            )
            .build()

        workManager.enqueueUniqueWork(
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
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                10,
                TimeUnit.SECONDS
            )
            .build()

        workManager.enqueueUniqueWork(
            "delete_favorites_work",
            ExistingWorkPolicy.KEEP,
            syncRequest
        )
    }


    suspend fun addGuestFavoriteToUser(userId : String){
        coroutineScope{
            launch { favoriteDao.addGuestMealsFavoriteToUser(userId) }
            launch { favoriteDao.addGuestSnacksFavoriteToUser(userId) }
            launch { favoriteDao.addGuestRestaurantsFavoriteToUser(userId) }
        }
    }


    suspend fun deleteAllFromFavorite(){
        favoriteDao.deleteAllFromFavorite()
    }
}