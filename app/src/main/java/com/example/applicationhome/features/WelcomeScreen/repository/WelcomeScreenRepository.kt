package com.example.applicationhome.features.WelcomeScreen.repository

import com.example.applicationhome.data.datastore.DataStoreManager
import com.example.applicationhome.domain.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WelcomeScreenRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    @ApplicationScope private val externalScope: CoroutineScope
){
    val isFirsTimeToOpenApp : StateFlow<Boolean?> =
        dataStoreManager.isFirstTimeToOpenApp
            .stateIn(
                scope = externalScope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )

    suspend fun updateFirstTimeToOpenApp(){
        dataStoreManager.updateFirstTimeToOpenApp()
    }
}