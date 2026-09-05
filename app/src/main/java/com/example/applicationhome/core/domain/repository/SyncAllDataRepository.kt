package com.example.applicationhome.core.domain.repository

import androidx.paging.PagingData
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.local.entity.CategoriesEntity
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SyncAllDataRepository {
    // *** ---------------------- \\***  Sync Data For Room Database  ***// ---------------------- ***
    suspend fun syncDataParallel()
    suspend fun syncFavoritesInDatabase(userId : String)
    suspend fun syncAddresses(userId : String): HomeUiState


    // *** ---------------------- \\***  Sync Data For ViewModel  ***// ---------------------- ***
    val categoriesFromDatabase : StateFlow<List<CategoriesEntity>>

    fun getRestaurantsFromDatabase(type: String): Flow<PagingData<RestaurantWithFavoriteStatus>>
    fun getAllOffersFromDatabase(): Flow<List<OffersEntity>>
}