package com.example.applicationhome.core.ui.components.forHomeScreenOrMenu

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnackBottomSheet(){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = {

        },
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        sheetState = sheetState,
        containerColor = Color.White
    ){

    }
}