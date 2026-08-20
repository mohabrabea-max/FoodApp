package com.example.applicationhome.core.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface WelcomeScreenRepository {
    val isFirsTimeToOpenApp : StateFlow<Boolean?>

    suspend fun updateFirstTimeToOpenApp()
}