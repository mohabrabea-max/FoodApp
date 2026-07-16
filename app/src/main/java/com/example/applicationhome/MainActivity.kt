package com.example.applicationhome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationhome.ui.theme.model.UserImageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 3. الـ Splash Screen لازم تكون أول سطر في الحياة
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val userImageViewModel: UserImageViewModel = viewModel()

            FinalScreen(
                userImageViewModel
            )
        }
    }
}