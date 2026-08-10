package com.example.applicationhome.core.ui.components.forHomeScreenOrMenu

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

suspend fun SnackbarHostState.bottomSnackBarWithAction(
    message : String,
    actionLabel : String,
    duration : SnackbarDuration = SnackbarDuration.Short,
    onActionClicked : () -> Unit
){
    val result = this.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = duration,
        )

    if (result == SnackbarResult.ActionPerformed) {
        onActionClicked()
    }
}

suspend fun SnackbarHostState.bottomSnackBar(
    message : String,
    duration : SnackbarDuration = SnackbarDuration.Short
){
   this.showSnackbar(
        message = message,
        duration = duration,
   )
}

// "Item added to cart successfully!"