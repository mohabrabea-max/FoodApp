package com.example.applicationhome.core.ui.components.forHomeScreenOrMenu

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState

suspend fun SnackbarHostState.showNetworkSnackBar(
    message: String
){
    currentSnackbarData?.dismiss()

    this.showSnackbar(
        message = message,
        duration = SnackbarDuration.Short
    )
}