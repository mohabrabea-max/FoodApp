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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.applicationhome.ui.theme.screens.FinalScreen
import com.example.applicationhome.view.model.APIData
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.BirthdayViewModel
import com.example.applicationhome.view.model.BottomBarViewModel
import com.example.applicationhome.view.model.CategoriesBoxViewModel
import com.example.applicationhome.view.model.DrawerViewModel
import com.example.applicationhome.view.model.FavoriteViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel
import com.example.applicationhome.view.model.LoginViewModel
import com.example.applicationhome.view.model.ProfileViewModel
import com.example.applicationhome.view.model.UserImageViewModel

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
            val categoriesBoxViewModel : CategoriesBoxViewModel = viewModel(
                factory = viewModelFactory{
                    initializer{
                        val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                        val apiData = APIData(application)
                        CategoriesBoxViewModel(apiData)
                    }
                }
            )
            val birthdayViewModel : BirthdayViewModel = viewModel()
            val loginViewModel: LoginViewModel = viewModel()
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
                loginViewModel
            )
        }
    }
}
