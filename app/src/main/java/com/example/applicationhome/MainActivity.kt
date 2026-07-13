package com.example.applicationhome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationhome.ui.theme.model.ConfirmOrderScreenViewModel
import com.example.applicationhome.ui.theme.model.DrawerViewModel
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.HomeScreenViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel
import com.example.applicationhome.ui.theme.model.LoginViewModel
import com.example.applicationhome.ui.theme.model.OrderScreenViewModel
import com.example.applicationhome.ui.theme.model.RestaurantViewModel
import com.example.applicationhome.ui.theme.model.SignUpViewModel
import com.example.applicationhome.ui.theme.model.UserImageViewModel
import com.example.applicationhome.ui.theme.model.ViewRestaurantImageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        // 3. الـ Splash Screen لازم تكون أول سطر في الحياة
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val loginViewModel: LoginViewModel = hiltViewModel()
            val signUpViewModel: SignUpViewModel = hiltViewModel()
            val confirmOrderScreenViewModel: ConfirmOrderScreenViewModel = hiltViewModel()
            val orderScreenViewModel: OrderScreenViewModel = hiltViewModel()
            val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
            val restaurantViewModel: RestaurantViewModel = hiltViewModel()
            val favoriteViewModel: FavoriteViewModel = hiltViewModel()
            val itemScreenViewModel: ItemScreenViewModel = hiltViewModel()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val userImageViewModel: UserImageViewModel = viewModel()
            val drawerViewModel: DrawerViewModel = viewModel()
            val viewRestaurantImageViewModel: ViewRestaurantImageViewModel = hiltViewModel()

            FinalScreen(
                drawerState,
                itemScreenViewModel,
                userImageViewModel,
                favoriteViewModel,
                drawerViewModel,
                homeScreenViewModel,
                loginViewModel,
                restaurantViewModel,
                confirmOrderScreenViewModel,
                orderScreenViewModel,
                viewRestaurantImageViewModel,
                signUpViewModel
            )
        }
    }
}