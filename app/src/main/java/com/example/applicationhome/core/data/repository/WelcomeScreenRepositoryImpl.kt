package com.example.applicationhome.core.data.repository

import com.example.applicationhome.core.data.datastore.DataStoreManager
import com.example.applicationhome.core.domain.module.ApplicationScope
import com.example.applicationhome.core.domain.repository.WelcomeScreenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class WelcomeScreenRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    @ApplicationScope private val externalScope: CoroutineScope
): WelcomeScreenRepository {
    override val isFirsTimeToOpenApp : StateFlow<Boolean?> =
        dataStoreManager.isFirstTimeToOpenApp
            .stateIn(
                scope = externalScope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )

    override suspend fun updateFirstTimeToOpenApp(){
        dataStoreManager.updateFirstTimeToOpenApp()
    }
}