package com.example.applicationhome.core.ui.components.forHomeScreenOrMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.applicationhome.core.ui.theme.DarkOrange

@Composable
fun LoadingDialog(isLoading : Boolean){
    if(isLoading){
        Dialog(
            onDismissRequest = {  },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    color = Color.DarkOrange
                )
            }
        }
    }
}