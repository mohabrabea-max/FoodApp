package com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.showAddToCartSnackbar(
    snackbarHostState: SnackbarHostState,
    onActionClicked: () -> Unit,
    message: String = "Item added to cart successfully!",
    actionLabel: String = "View cart"

) {
    this.launch {
        val result = snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = SnackbarDuration.Short // المدة (حوالي 4 ثواني وتختفي)
        )
        if (result == SnackbarResult.ActionPerformed) {
            onActionClicked()
        }
    }
}