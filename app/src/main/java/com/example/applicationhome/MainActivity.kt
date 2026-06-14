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
import com.example.applicationhome.ui.theme.model.APIData
import com.example.applicationhome.ui.theme.model.AddBoxViewModel
import com.example.applicationhome.ui.theme.model.BirthdayViewModel
import com.example.applicationhome.ui.theme.model.BottomBarViewModel
import com.example.applicationhome.ui.theme.model.CategoriesBoxViewModel
import com.example.applicationhome.ui.theme.model.ConfirmOrderScreenViewModel
import com.example.applicationhome.ui.theme.model.DrawerViewModel
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel
import com.example.applicationhome.ui.theme.model.LoginViewModel
import com.example.applicationhome.ui.theme.model.OrderScreenViewModel
import com.example.applicationhome.ui.theme.model.ProfileViewModel
import com.example.applicationhome.ui.theme.model.RestaurantViewModel
import com.example.applicationhome.ui.theme.model.UserImageViewModel
import com.example.applicationhome.ui.theme.screens.FinalScreen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val itemScreenViewModel: ItemScreenViewModel = viewModel()
            val viewModelForBottomBar : BottomBarViewModel = viewModel()
            val addBoxViewModel : AddBoxViewModel = viewModel()
            val userImageViewModel: UserImageViewModel = viewModel()
            val favoriteViewModel : FavoriteViewModel = viewModel()
            val drawerViewModel : DrawerViewModel = viewModel()
            val profileViewModel : ProfileViewModel = viewModel()
            val apiData : APIData = viewModel()
            val categoriesBoxViewModel : CategoriesBoxViewModel = viewModel()
            val birthdayViewModel : BirthdayViewModel = viewModel()
            val loginViewModel: LoginViewModel = viewModel()
            val restaurantViewModel: RestaurantViewModel = viewModel()
            val confirmOrderScreenViewModel : ConfirmOrderScreenViewModel = viewModel()
            val orderScreenViewModel : OrderScreenViewModel = viewModel()
            FinalScreen(
                scrollBehavior,
                drawerState,
                itemScreenViewModel,
                viewModelForBottomBar,
                addBoxViewModel,
                userImageViewModel,
                favoriteViewModel,
                drawerViewModel,
                categoriesBoxViewModel,
                profileViewModel,
                apiData,
                birthdayViewModel,
                loginViewModel,
                restaurantViewModel,
                confirmOrderScreenViewModel,
                orderScreenViewModel
            )
        }
    }
}
