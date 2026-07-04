package com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.showNetworkSnackBar(
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String

){
    this.launch {
        snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short,
            actionLabel = actionLabel
        )
    }
}