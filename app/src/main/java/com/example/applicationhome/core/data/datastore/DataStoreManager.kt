package com.example.applicationhome.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.applicationhome.core.domain.model.ThemeMode
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
        val DISCOUNTS_LAST_SYNC = longPreferencesKey("discounts_last_sync")
        val ORDERS_HISTORY_LAST_SYNC = longPreferencesKey("orders_history_last_sync")
        val ADDRESSES = longPreferencesKey("addresses")
        val IS_FIRST_OPEN = booleanPreferencesKey("is_first_open")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }


    val mealsLastSyncTimeFlow : Flow<Long?> = getSyncTime(DataStoreKeys.MEALS_LAST_SYNC)
    suspend fun updateMealsSyncTime(timestamp: Long) {
        saveLastSyncTime(DataStoreKeys.MEALS_LAST_SYNC, timestamp)
    }

    val snacksLastSyncTimeFlow : Flow<Long?> = getSyncTime(DataStoreKeys.SNACKS_LAST_SYNC)
    suspend fun updateSnacksSyncTime(timestamp: Long) {
        saveLastSyncTime(DataStoreKeys.SNACKS_LAST_SYNC, timestamp)
    }

    val restaurantsLastSyncTimeFlow : Flow<Long?> = getSyncTime(DataStoreKeys.RESTAURANTS_LAST_SYNC)
    suspend fun updateRestaurantsSyncTime(timestamp: Long) {
        saveLastSyncTime(DataStoreKeys.RESTAURANTS_LAST_SYNC, timestamp)
    }

    val categoriesLastSyncTimeFlow : Flow<Long?> = getSyncTime(DataStoreKeys.CATEGORIES_LAST_SYNC)
    suspend fun updateCategoriesSyncTime(timestamp: Long){
        saveLastSyncTime(DataStoreKeys.CATEGORIES_LAST_SYNC, timestamp)
    }

    val offersLastSyncTimeFlow : Flow<Long?> = getSyncTime(DataStoreKeys.OFFERS_LAST_SYNC)
    suspend fun updateOffersSyncTime(timestamp: Long){
        saveLastSyncTime(DataStoreKeys.OFFERS_LAST_SYNC, timestamp)
    }

    val discountsLastSyncTimeFlow : Flow<Long?> = getSyncTime(DataStoreKeys.DISCOUNTS_LAST_SYNC)
    suspend fun updateDiscountsSyncTime(timestamp: Long){
        saveLastSyncTime(DataStoreKeys.DISCOUNTS_LAST_SYNC, timestamp)
    }

    val ordersHistoryLastSyncTimeFlow : Flow<Long?> = getSyncTime(DataStoreKeys.ORDERS_HISTORY_LAST_SYNC)
    suspend fun updateOrdersHistorySyncTime(timestamp: Long){
        saveLastSyncTime(DataStoreKeys.ORDERS_HISTORY_LAST_SYNC, timestamp)
    }

    val addressesLastSyncTimeFlow : Flow<Long?> = getSyncTime(DataStoreKeys.ADDRESSES)
    suspend fun updateAddressesSyncTime(timestamp: Long){
        saveLastSyncTime(DataStoreKeys.ADDRESSES, timestamp)
    }

    val isFirstTimeToOpenApp : Flow<Boolean?> = getBoolean(DataStoreKeys.IS_FIRST_OPEN)
    suspend fun updateFirstTimeToOpenApp() {
        dataStore.edit { preferences ->
            preferences[DataStoreKeys.IS_FIRST_OPEN] = false
        }
    }

    val themeMode : Flow<ThemeMode> = dataStore.data
        .map{ preferences ->
            val savedTheme = preferences[DataStoreKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
            ThemeMode.valueOf(savedTheme)
        }
    suspend fun saveThemeMode(mode : ThemeMode){
        dataStore.edit { preferences ->
            preferences[DataStoreKeys.THEME_MODE] = mode.name
        }
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

    private fun getBoolean(key: Preferences.Key<Boolean>): Flow<Boolean>{
        return dataStore.data
            .catch { exception ->
                if(exception is IOException){
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }.map{ preferences ->
                preferences[key] ?: true
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