package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.data.local.entity.CategoriesEntity
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.RestaurantsEntity
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

    val offersFromDatabase : StateFlow<List<OffersEntity>> =
        getAllOffersFromDatabase()
            .stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )



    fun getRestaurantsFromDatabase(): Flow<List<RestaurantsEntity>> =
        foodAndRestaurantsDao.getAllRestaurantsFromDatabase()

    private fun getAllCategoriesFromDatabase() : Flow<List<CategoriesEntity>> =
        foodAndRestaurantsDao.getAllCategoriesFromDatabase()

    private fun getAllOffersFromDatabase(): Flow<List<OffersEntity>> =
        foodAndRestaurantsDao.getAllOffersFromDatabase()
}