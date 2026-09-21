package com.example.applicationhome.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.applicationhome.core.data.datastore.DataStoreManager
import com.example.applicationhome.core.data.local.dao.FavoriteDao
import com.example.applicationhome.core.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.core.data.local.dao.UsersDao
import com.example.applicationhome.core.data.local.entity.CategoriesEntity
import com.example.applicationhome.core.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.core.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.core.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.core.data.local.entity.OffersEntity
import com.example.applicationhome.core.data.local.entity.RestaurantCategoryCrossRef
import com.example.applicationhome.core.data.mapper.addressToAddressesEntity
import com.example.applicationhome.core.data.mapper.discountsToDiscountsEntity
import com.example.applicationhome.core.data.mapper.foodItemToMealsEntity
import com.example.applicationhome.core.data.mapper.restaurantWithFavoriteStatusToRestaurantDomainClass
import com.example.applicationhome.core.data.mapper.restaurantsToRestaurantsEntity
import com.example.applicationhome.core.data.mapper.snackToSnacksEntity
import com.example.applicationhome.core.data.remote.FoodAppAPIs
import com.example.applicationhome.core.domain.model.RestaurantDomainClass
import com.example.applicationhome.core.domain.module.ApplicationScope
import com.example.applicationhome.core.domain.module.IODispatcher
import com.example.applicationhome.core.domain.repository.SyncAllDataRepository
import com.example.applicationhome.core.ui.model.UiStates
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class SyncAllDataRepositoryImpl @Inject constructor(
    private val api : FoodAppAPIs,
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao,
    private val favoriteDao : FavoriteDao,
    private val usersDao : UsersDao,
    private val dataStoreManager : DataStoreManager,
    @ApplicationScope externalScope: CoroutineScope,
    @IODispatcher private val dispatcher : CoroutineDispatcher
): SyncAllDataRepository {
    // *** ---------------------- \\***  Sync Data For Room Database  ***// ---------------------- ***

    private suspend fun <T> retryLocally(
        times : Int = 3,
        initialDelay : Long = 1500,
        block : suspend  () -> T
    ): T {
        var currentDelay = initialDelay

        repeat(times - 1){
            try {
                return block()
            } catch (e: Exception) {
                if(e is CancellationException) throw e
                delay(currentDelay.milliseconds)
                currentDelay *= 2
            }
        }
        return block()
    }


    private suspend fun syncAllMealsToDatabase(){
        retryLocally{
            val lastSyncTime = dataStoreManager.mealsLastSyncTimeFlow.firstOrNull() ?: 0L
            val response = api.getMealsByLastUpdate(lastSyncTimestamp = lastSyncTime + 1)
            val meals = response.body()
            if(response.isSuccessful && meals != null){
                foodAndRestaurantsDao.syncMealsToDatabase(meals.values.map { it.foodItemToMealsEntity() })

                val newestTimestamp = meals.values.maxOfOrNull { it.updatedAt } ?: lastSyncTime
                dataStoreManager.updateMealsSyncTime(newestTimestamp)
            }else{
                val errorCode = response.code()

                when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }

                throw HttpException(response)
            }
        }
    }

    private suspend fun syncAllSnacksToDatabase(){
        retryLocally{
            val lastSyncTime = dataStoreManager.snacksLastSyncTimeFlow.firstOrNull() ?: 0L
            val response = api.getSnacksByLastUpdate(lastSyncTimestamp = lastSyncTime + 1)
            val snacks = response.body()
            if(response.isSuccessful && snacks != null){
                foodAndRestaurantsDao.syncSnacksToDatabase(snacks.values.map { it.snackToSnacksEntity() })

                val newestTimestamp = snacks.values.maxOfOrNull { it.updatedAt } ?: lastSyncTime
                dataStoreManager.updateSnacksSyncTime(newestTimestamp)
            }else{
                throw HttpException(response)
            }
        }
    }

    private suspend fun syncAllRestaurantsToDatabase(){
        retryLocally{
            val lastSyncTime = dataStoreManager.restaurantsLastSyncTimeFlow.firstOrNull() ?: 0L
            val response = api.getRestaurantsByLastUpdate(lastSyncTimestamp = lastSyncTime + 1)
            val restaurants = response.body()
            if(response.isSuccessful && restaurants != null){
                val restaurantList = restaurants.values.toList()

                val categories = restaurantList.flatMap { item ->
                    item.categories.keys.map {
                        RestaurantCategoryCrossRef(item.id, it.toInt())
                    }
                }

                foodAndRestaurantsDao.syncRestaurantsAndCategoriesTransaction(
                    restaurants = restaurantList.map { it.restaurantsToRestaurantsEntity() },
                    categories = categories
                )

                val newestTimestamp = restaurants.values.maxOfOrNull { it.updatedAt } ?: lastSyncTime
                dataStoreManager.updateRestaurantsSyncTime(newestTimestamp)
            }else{
                throw HttpException(response)
            }
        }
    }

    private suspend fun syncCategoriesToDatabase(){
        retryLocally{
            val lastSyncTime = dataStoreManager.categoriesLastSyncTimeFlow.firstOrNull() ?: 0L
            val response = api.categorieslist(lastSyncTimestamp = lastSyncTime + 1)
            val categories = response.body()
            if(response.isSuccessful && categories != null){
                val categoriesEntity = categories.values.map { item ->
                    CategoriesEntity(
                        item.id,
                        item.name,
                        item.type,
                        item.image,
                        item.icon,
                        item.updatedAt
                    )
                }

                foodAndRestaurantsDao.syncCategoriesToDatabase(categoriesEntity)

                val newestTimestamp = categories.maxOfOrNull { it.value.updatedAt }?: lastSyncTime
                dataStoreManager.updateCategoriesSyncTime(newestTimestamp)
            }else{
                throw HttpException(response)
            }
        }
    }

    private suspend fun syncOffersToDatabase(){
        retryLocally{
            val lastSyncTime = dataStoreManager.offersLastSyncTimeFlow.firstOrNull() ?: 0L
            val response = api.offers(lastSyncTimestamp = lastSyncTime + 1)
            val offers = response.body()
            if(response.isSuccessful && offers != null){
                val offersEntity = offers.values.map { item ->
                    OffersEntity(
                        item.restaurantId,
                        item.id,
                        item.name,
                        item.image,
                        item.updatedAt
                    )
                }

                foodAndRestaurantsDao.syncOffersToDatabase(offersEntity)

                val newestTimestamp = offers.maxOfOrNull { it.value.updatedAt } ?: lastSyncTime
                dataStoreManager.updateOffersSyncTime(newestTimestamp)
            }else{
                throw HttpException(response)
            }
        }
    }

    private suspend fun syncDiscounts(){
        try {
            val lastSyncTime = dataStoreManager.discountsLastSyncTimeFlow.firstOrNull() ?: 0L
            val response = api.getDiscounts(lastSyncTimestamp = lastSyncTime + 1)
            val discounts = response.body()

            if(response.isSuccessful && discounts != null){
                val discountsAfterFiltering = discounts.values.toList()
                    .map {
                        it.discountsToDiscountsEntity()
                    }

                foodAndRestaurantsDao.syncDiscounts(discountsAfterFiltering)

                val newestTimestamp = discounts.maxOfOrNull { it.value.startDiscount } ?: lastSyncTime
                dataStoreManager.updateOffersSyncTime(newestTimestamp)
            }
        }catch (e : Exception){
            if(e is CancellationException) throw e
        }
    }

    private suspend fun deleteDiscountsWasEnd(){
        try {
            val lastSyncTime = getNetworkTime()?: 0L
            val response = api.getDiscountsWasEnded(lastSyncTimestamp = lastSyncTime - 1)
            val discounts = response.body()

            if(response.isSuccessful && discounts != null){
                val discountsAfterFiltering = discounts.values.toList()
                    .map {
                        it.mealId
                    }

                foodAndRestaurantsDao.deleteDiscountsWasEnd(discountsAfterFiltering)
            }
        }catch (e : Exception){
            if(e is CancellationException) throw e
        }
    }

    override suspend fun syncDataParallel() {
        supervisorScope {
            launch { syncAllMealsToDatabase() }
            launch { syncAllSnacksToDatabase() }
            launch { syncAllRestaurantsToDatabase() }
            launch { syncCategoriesToDatabase() }
            launch { syncOffersToDatabase() }
            launch { syncDiscounts() }
            launch { deleteDiscountsWasEnd() }
        }
    }


    override suspend fun syncFavoritesInDatabase(userId : String) = withContext(dispatcher){
        retryLocally{
            val response = api.getFavoriteItems(userId)

            if(response.isSuccessful){
                val mealsIdsInDatabase = foodAndRestaurantsDao.getMealsIdsFromDatabase().toSet()
                val snacksIdsInDatabase = foodAndRestaurantsDao.getSnacksIdsFromDatabase().toSet()
                val restaurantsIdsInDatabase = foodAndRestaurantsDao.getRestaurantsIdsFromDatabase().toSet()

                val favorite = response.body() ?: emptyMap()

                val mealsFavorite = mutableListOf<FavoriteMealEntity>()
                val snacksFavorite = mutableListOf<FavoriteSnackEntity>()
                val restaurantsFavorite = mutableListOf<FavoriteRestaurantEntity>()

                favorite.values.forEach { item ->
                    when(item.typ.lowercase()){
                        "meal" ->
                            if(item.id in mealsIdsInDatabase){
                                mealsFavorite.add(
                                    FavoriteMealEntity(
                                        item.id,
                                        userId,
                                        item.restaurants,
                                        true,
                                        false
                                    )
                                )
                            }

                        "snack" ->
                            if(item.id in snacksIdsInDatabase){
                                snacksFavorite.add(
                                    FavoriteSnackEntity(
                                        item.id,
                                        userId,
                                        item.restaurants,
                                        true,
                                        false
                                    )
                                )
                            }

                        "restaurant" ->
                            if(item.id in restaurantsIdsInDatabase){
                                restaurantsFavorite.add(
                                    FavoriteRestaurantEntity(
                                        item.id,
                                        userId,
                                        true,
                                        false
                                    )
                                )
                            }
                    }
                }
                favoriteDao.addAllToFavorite(
                    foodItems = mealsFavorite,
                    snacksItems = snacksFavorite,
                    restaurant = restaurantsFavorite
                )
            }else{
                throw HttpException(response)
            }
        }
    }



    override suspend fun syncAddresses(userId : String): UiStates {
        return try {
            val lastSyncTime = dataStoreManager.addressesLastSyncTimeFlow.firstOrNull()?: 0L
            val response = api.getAddresses(
                userId = userId,
                lastSyncTimestamp = lastSyncTime
            )
            val addresses = response.body()

            if(response.isSuccessful && addresses != null){
                val finalAddressesList = addresses.map { item ->
                    item.value.addressToAddressesEntity(
                        userId = userId,
                        addressId = item.key
                    )
                }

                usersDao.addNewAddresses(finalAddressesList)

                val newestTimestamp = addresses.values.maxOfOrNull { it.lastUse }?: lastSyncTime
                dataStoreManager.updateAddressesSyncTime(newestTimestamp)

                UiStates.Success
            }else{
                UiStates.Offline
            }
        } catch (e : Exception) {
            UiStates.Offline
        }
    }


    // *** ---------------------- \\***  Sync Data For ViewModel  ***// ---------------------- ***

    override val categoriesFromDatabase : StateFlow<List<CategoriesEntity>> =
        getAllCategoriesFromDatabase()
            .stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


    override fun getRestaurantsFromDatabase(type: String): Flow<PagingData<RestaurantDomainClass>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                foodAndRestaurantsDao.getRestaurantsFromDatabaseByCategories(type)
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.restaurantWithFavoriteStatusToRestaurantDomainClass()
            }
        }

    private fun getAllCategoriesFromDatabase() : Flow<List<CategoriesEntity>> =
        foodAndRestaurantsDao.getAllCategoriesFromDatabase()

    override fun getAllOffersFromDatabase(): Flow<List<OffersEntity>> =
        foodAndRestaurantsDao.getAllOffersFromDatabase()


    private suspend fun getNetworkTime(): Long? = withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url("https://www.google.com")
                .head() // HEAD request برجع الهيدرز فقط بدون داتا ثقيلة
                .build()

            client.newCall(request).execute().use { response ->
                val dateHeader = response.header("Date") // مثلاً: "Sun, 20 Sep 2026 17:30:00 GMT"
                if (dateHeader != null) {
                    val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
                    format.parse(dateHeader)?.time
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }
}