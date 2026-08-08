package com.example.applicationhome.core.ui.components.forHomeScreenOrMenu

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.bottomSnackBar(
    snackBarHostState : SnackbarHostState,
    onActionClicked : () -> Unit,
    message : String,
    actionLabel : String

){
    this.launch {
        val result = snackBarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = SnackbarDuration.Indefinite,
        )
        if (result == SnackbarResult.ActionPerformed) {
            onActionClicked()
        }
    }
}

// "Item added to cart successfully!"