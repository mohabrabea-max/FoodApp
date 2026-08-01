package com.example.applicationhome.core.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.applicationhome.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.data.local.entity.CategoriesEntity
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.domain.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeScreenRepository @Inject constructor(
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao,
    @ApplicationScope externalScope: CoroutineScope
) {
    val categoriesFromDatabase : StateFlow<List<CategoriesEntity>> =
        getAllCategoriesFromDatabase()
            .stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


    fun getRestaurantsFromDatabase(type: String): Flow<PagingData<RestaurantWithFavoriteStatus>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                foodAndRestaurantsDao.getRestaurantsFromDatabaseByCategories(type)
            }
        ).flow

    private fun getAllCategoriesFromDatabase() : Flow<List<CategoriesEntity>> =
        foodAndRestaurantsDao.getAllCategoriesFromDatabase()

    fun getAllOffersFromDatabase(): Flow<List<OffersEntity>> =
        foodAndRestaurantsDao.getAllOffersFromDatabase()
}