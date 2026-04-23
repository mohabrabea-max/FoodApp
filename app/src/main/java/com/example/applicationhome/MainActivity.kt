package com.example.applicationhome

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationhome.ui.theme.screens.FinalScreen
import com.example.applicationhome.view.model.BottomBarViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val viewModel: ItemScreenViewModel = viewModel()
            val viewModelForBottomBar : BottomBarViewModel = viewModel()
            FinalScreen(scrollBehavior, drawerState, viewModel, viewModelForBottomBar)
        }
    }
}
