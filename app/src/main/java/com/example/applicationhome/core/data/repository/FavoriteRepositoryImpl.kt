package com.example.applicationhome.core.data.repository

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.applicationhome.SyncAddToFavoritesWorker
import com.example.applicationhome.SyncRemoveFromFavoritesWorker
import com.example.applicationhome.core.data.local.dao.FavoriteDao
import com.example.applicationhome.core.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.core.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.core.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.core.data.mapper.mealWithFavoriteStatusToMealDomain
import com.example.applicationhome.core.data.mapper.restaurantWithFavoriteStatusToRestaurantDomainClass
import com.example.applicationhome.core.data.mapper.snackWithFavoriteStatusToSnackDomain
import com.example.applicationhome.core.domain.model.MealDomain
import com.example.applicationhome.core.domain.model.RestaurantDomainClass
import com.example.applicationhome.core.domain.model.SnackDomain
import com.example.applicationhome.core.domain.module.ApplicationScope
import com.example.applicationhome.core.domain.module.IODispatcher
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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

    override fun getFavoriteMeals(userId : String) : Flow<List<MealDomain>> =
        favoriteDao.getFoodFromDatabase(userId).map { meals ->
            meals.map {
                it.mealWithFavoriteStatusToMealDomain()
            }
        }


    override fun getFavoriteSnacks(userId : String) : Flow<List<SnackDomain>> =
        favoriteDao.getSnacksFromDatabase(userId).map { snacks ->
            snacks.map {
                it.snackWithFavoriteStatusToSnackDomain()
            }
        }


    override fun favoriteRestaurantsFromDatabase(userId : String) : Flow<List<RestaurantDomainClass>> =
        favoriteDao.getRestaurantsFromDatabase(userId).map { item ->
            item.map { it.restaurantWithFavoriteStatusToRestaurantDomainClass() }
        }

// *** ---------------------- \\***  Favorite Count  ***// ---------------------- ***

    override fun favoriteFoodCount(userId : String) : Flow<Int> = getFavoriteMeals(userId)
        .map { it -> it.filter { it.isFavorite }.size }
        .stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    override fun favoriteSnacksCount(userId : String) : Flow<Int> = getFavoriteSnacks(userId)
        .map { it -> it.filter { it.isFavorite }.size }
        .stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    override fun favoriteRestaurantsCount(userId : String) : Flow<Int> = favoriteRestaurantsFromDatabase(userId)
        .map { it -> it.filter { it.isFavorite }.size }
        .stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    override fun totalCountInFavorite(userId : String) : Flow<Int> = combine(
        favoriteFoodCount(userId),
        favoriteSnacksCount(userId),
        favoriteRestaurantsCount(userId)
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