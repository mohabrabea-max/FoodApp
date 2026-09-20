package com.example.applicationhome.core.data.local.source

import androidx.appcompat.app.AppCompatDelegate
import com.example.applicationhome.core.data.datastore.DataStoreManager
import com.example.applicationhome.core.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeModeManager @Inject constructor(
    private val dataStoreManager: DataStoreManager
){
    suspend fun updateAppTheme(mode: ThemeMode){
        val nightMode = when(mode){
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)

        dataStoreManager.saveThemeMode(mode)
    }

    fun getCurrentThemeMode(): Flow<ThemeMode> {
        return dataStoreManager.themeMode
    }
}