package com.example.applicationhome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationhome.ui.theme.model.APIData
import com.example.applicationhome.ui.theme.model.BottomBarViewModel
import com.example.applicationhome.ui.theme.model.CartViewModel
import com.example.applicationhome.ui.theme.model.CategoriesBoxViewModel
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
import com.example.applicationhome.ui.theme.screens.FinalScreen

class MainActivity : ComponentActivity() {
    val app = application as MyFoodApp
    val cartRepo = app.cartRepository
    val orderRepo = app.orderRepository
    val userRepo = app.userRepository
    private val loginViewModel : LoginViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(userRepo, app) as T
            }
        }
    }

    private val signUpViewModel : SignUpViewModel by viewModels {
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SignUpViewModel(userRepo, app) as T
            }
        }
    }

    private val cartViewModel : CartViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CartViewModel(userRepo, cartRepo, orderRepo) as T
            }
        }
    }

    private val confirmOrderScreenViewModel : ConfirmOrderScreenViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ConfirmOrderScreenViewModel(userRepo, cartRepo, orderRepo) as T
            }
        }
    }

    private val orderScreenViewModel : OrderScreenViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OrderScreenViewModel(orderRepo) as T
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {

            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val itemScreenViewModel : ItemScreenViewModel = viewModel()
            val viewModelForBottomBar : BottomBarViewModel = viewModel()
            val userImageViewModel : UserImageViewModel = viewModel()
            val favoriteViewModel : FavoriteViewModel = viewModel()
            val drawerViewModel : DrawerViewModel = viewModel()
            val apiData : APIData = viewModel()
            val categoriesBoxViewModel : CategoriesBoxViewModel = viewModel()
            val restaurantViewModel : RestaurantViewModel = viewModel()
            val homeScreenViewModel : HomeScreenViewModel = viewModel()
            FinalScreen(
                scrollBehavior,
                drawerState,
                itemScreenViewModel,
                viewModelForBottomBar,
                cartViewModel,
                userImageViewModel,
                favoriteViewModel,
                drawerViewModel,
                categoriesBoxViewModel,
                apiData,
                loginViewModel,
                restaurantViewModel,
                confirmOrderScreenViewModel,
                orderScreenViewModel,
                homeScreenViewModel,
                signUpViewModel
            )
        }
    }
}
