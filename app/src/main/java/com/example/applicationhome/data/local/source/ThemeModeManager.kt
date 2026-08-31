package com.example.applicationhome.data.local.source

import androidx.appcompat.app.AppCompatDelegate
import com.example.applicationhome.data.data.model.ThemeMode
import com.example.applicationhome.data.datastore.DataStoreManager
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