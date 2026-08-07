package com.example.applicationhome.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.applicationhome.data.datastore.DataStoreManager.DataStoreKeys.CATEGORIES_LAST_SYNC
import com.example.applicationhome.data.datastore.DataStoreManager.DataStoreKeys.FAVORITE_LAST_SYNC
import com.example.applicationhome.data.datastore.DataStoreManager.DataStoreKeys.MEALS_LAST_SYNC
import com.example.applicationhome.data.datastore.DataStoreManager.DataStoreKeys.OFFERS_LAST_SYNC
import com.example.applicationhome.data.datastore.DataStoreManager.DataStoreKeys.RESTAURANTS_LAST_SYNC
import com.example.applicationhome.data.datastore.DataStoreManager.DataStoreKeys.SNACKS_LAST_SYNC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object DataStoreKeys {
        val MEALS_LAST_SYNC = longPreferencesKey("meals_last_sync")
        val SNACKS_LAST_SYNC = longPreferencesKey("snacks_last_sync")
        val RESTAURANTS_LAST_SYNC = longPreferencesKey("restaurants_last_sync")
        val CATEGORIES_LAST_SYNC = longPreferencesKey("categories_last_sync")
        val OFFERS_LAST_SYNC = longPreferencesKey("offers_last_sync")
        val FAVORITE_LAST_SYNC = longPreferencesKey("favorite_last_sync")
    }


    val mealsLastSyncTimeFlow : Flow<Long?> = getSyncTime(MEALS_LAST_SYNC)
    suspend fun updateMealsSyncTime(timestamp: Long) {
        saveLastSyncTime(MEALS_LAST_SYNC, timestamp)
    }

    val snacksLastSyncTimeFlow : Flow<Long?> = getSyncTime(SNACKS_LAST_SYNC)
    suspend fun updateSnacksSyncTime(timestamp: Long) {
        saveLastSyncTime(SNACKS_LAST_SYNC, timestamp)
    }

    val restaurantsLastSyncTimeFlow : Flow<Long?> = getSyncTime(RESTAURANTS_LAST_SYNC)
    suspend fun updateRestaurantsSyncTime(timestamp: Long) {
        saveLastSyncTime(RESTAURANTS_LAST_SYNC, timestamp)
    }

    val categoriesLastSyncTimeFlow : Flow<Long?> = getSyncTime(CATEGORIES_LAST_SYNC)
    suspend fun updateCategoriesSyncTime(timestamp: Long){
        saveLastSyncTime(CATEGORIES_LAST_SYNC, timestamp)
    }

    val offersLastSyncTimeFlow : Flow<Long?> = getSyncTime(OFFERS_LAST_SYNC)
    suspend fun updateOffersSyncTime(timestamp: Long){
        saveLastSyncTime(OFFERS_LAST_SYNC, timestamp)
    }

    val favoriteLastSyncTimeFlow : Flow<Long?> = getSyncTime(FAVORITE_LAST_SYNC)
    suspend fun updateFavoriteSyncTime(timestamp: Long) {
        saveLastSyncTime(FAVORITE_LAST_SYNC, timestamp)
    }


    private fun getSyncTime(key: Preferences.Key<Long>): Flow<Long>{
        return dataStore.data
            .catch { exception ->
                if(exception is IOException){
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }.map{ preferences ->
                preferences[key] ?: 0L
            }
    }

    private suspend fun saveLastSyncTime(key: Preferences.Key<Long>, timestamp: Long){
        dataStore.edit { preferences ->
            preferences[key] = timestamp
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_data_store")

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }
}