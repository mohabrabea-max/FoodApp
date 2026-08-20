package com.example.applicationhome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.model.FinalScreenViewModel
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.data.model.UserUiState

@Composable
fun MySplashScreen(
    viewModel: FinalScreenViewModel,
    navigationController : NavHostController
){
    //val syncState by viewModel.syncDataUiState.collectAsStateWithLifecycle()
    val syncUserUiState by viewModel.syncUserUiState.collectAsStateWithLifecycle()
    val isFirstTime by viewModel.isFirsTimeToOpenApp.collectAsStateWithLifecycle()

    LaunchedEffect(isFirstTime, syncUserUiState){
        if (syncUserUiState !is UserUiState.Starting && isFirstTime != null) {
            val destination = when {
                syncUserUiState is UserUiState.GuestMode && isFirstTime == true -> Screens.WelcomeScreen.screen
                else -> Screens.DashboardScreen.screen
            }

            navigationController.navigate(destination) {
                popUpTo(0) { inclusive = true }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkOrange)
    ){
        Image(
            painter = painterResource(id = R.drawable.splashscreen),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(0.73f)
                .align(Alignment.Center)
        )
    }
}