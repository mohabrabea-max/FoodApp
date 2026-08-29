package com.example.applicationhome

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.applicationhome.core.ui.components.model.FinalScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val finalScreenViewModel : FinalScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // 3. الـ Splash Screen لازم تكون أول سطر في الحياة
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            FinalScreen(finalScreenViewModel)
        }
    }
}