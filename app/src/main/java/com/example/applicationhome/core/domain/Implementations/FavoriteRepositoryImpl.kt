package com.example.applicationhome.core.domain.Implementations

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.applicationhome.SyncAddToFavoritesWorker
import com.example.applicationhome.SyncRemoveFromFavoritesWorker
import com.example.applicationhome.core.domain.module.ApplicationScope
import com.example.applicationhome.core.domain.module.IODispatcher
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.local.dao.FavoriteDao
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteRepositoryImpl @Inject constructor(
    userRepository : UserRepository,
    private val favoriteDao : FavoriteDao,
    private val workManager : WorkManager,
    @ApplicationScope private val externalScope : CoroutineScope,
    @IODispatcher private val dispatcher : CoroutineDispatcher
): FavoriteRepository {

// *** ---------------------- \\***  Favorite Items  ***// ---------------------- ***

    override val favoriteMeals : StateFlow<List<MealWithFavoriteStatus>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            favoriteDao.getFoodFromDatabase(id)
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    override val favoriteSnacks : StateFlow<List<SnackWithFavoriteStatus>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            favoriteDao.getSnacksFromDatabase(id)
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    override val favoriteRestaurantsFromDatabase : StateFlow<List<RestaurantWithFavoriteStatus>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
            favoriteDao.getRestaurantsFromDatabase(id)
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


// *** ---------------------- \\***  Favorite Count  ***// ---------------------- ***

    override val favoriteFoodCount : StateFlow<Int> = favoriteMeals
        .map { it -> it.filter { it.isFavorite }.size }
        .stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    override val favoriteSnacksCount : StateFlow<Int> = favoriteSnacks
        .map { it -> it.filter { it.isFavorite }.size }
        .stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    override val favoriteRestaurantsCount : StateFlow<Int> = favoriteRestaurantsFromDatabase
        .map { it -> it.filter { it.isFavorite }.size }
        .stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    override val totalCountInFavorite : StateFlow<Int> = combine(
        favoriteFoodCount,
        favoriteSnacksCount,
        favoriteRestaurantsCount
    ){ (food, snacks, restaurants) ->
        food + snacks + restaurants
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )


    // *** ---------------------- \\***  Favorite Functions  ***// ---------------------- ***

    override suspend fun addFoodToFavorite(userId : String, foodItem : FavoriteMealEntity){
        favoriteDao.addFoodToFavorite(listOf(foodItem))
        triggerOfflineSyncWorker(userId)
    }

    override suspend fun addSnackToFavorite(userId : String, snackItem : FavoriteSnackEntity){
        favoriteDao.addSnacksToFavorite(listOf(snackItem))
        triggerOfflineSyncWorker(userId)
    }

    override suspend fun addRestaurantToFavorite(userId : String, restaurantItem : FavoriteRestaurantEntity){
        favoriteDao.addRestaurantToFavorite(listOf(restaurantItem))
        triggerOfflineSyncWorker(userId)
    }

    private fun triggerOfflineSyncWorker(userId : String) {
        if(userId.isEmpty()) return

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

    override suspend fun deleteFoodFromFavorite(userId : String, mealId : Int){
        favoriteDao.markFoodAsDeletedOffline(userId, mealId)
        triggerOfflineRemoveWorker(userId)
    }

    override suspend fun deleteSnackFromFavorite(userId : String, snackId : Int){
        favoriteDao.markSnacksAsDeletedOffline(userId, snackId)
        triggerOfflineRemoveWorker(userId)
    }

    override suspend fun deleteRestaurantFromFavorite(userId : String, resId : Int){
        favoriteDao.markRestaurantsAsDeletedOffline(userId, resId)
        triggerOfflineRemoveWorker(userId)
    }

    private fun triggerOfflineRemoveWorker(userId : String) {
        if(userId.isEmpty()) return

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


    override suspend fun addGuestFavoriteToUser(userId : String){
        withContext(dispatcher){
            launch { favoriteDao.addGuestMealsFavoriteToUser(userId) }
            launch { favoriteDao.addGuestSnacksFavoriteToUser(userId) }
            launch { favoriteDao.addGuestRestaurantsFavoriteToUser(userId) }
        }
        triggerOfflineSyncWorker(userId)
        triggerOfflineRemoveWorker(userId)
    }


    override suspend fun deleteAllFromFavorite(){
        favoriteDao.deleteAllFromFavorite()
    }
}