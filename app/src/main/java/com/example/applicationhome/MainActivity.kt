package com.example.applicationhome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.applicationhome.core.ui.theme.model.FinalScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 3. الـ Splash Screen لازم تكون أول سطر في الحياة
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val finalScreenViewModel : FinalScreenViewModel = hiltViewModel()
            FinalScreen(finalScreenViewModel)
        }
    }
}