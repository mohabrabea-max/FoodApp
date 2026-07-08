package com.example.applicationhome

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.showNetworkSnackBar
import com.example.applicationhome.ui.theme.model.CartViewModel
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
import com.example.applicationhome.ui.theme.screens.Cart
import com.example.applicationhome.ui.theme.screens.ConfirmOrderScreen
import com.example.applicationhome.ui.theme.screens.ConfirmOrderScreen2
import com.example.applicationhome.ui.theme.screens.ItemScreen
import com.example.applicationhome.ui.theme.screens.LastOrdersScreen
import com.example.applicationhome.ui.theme.screens.LoginScreen
import com.example.applicationhome.ui.theme.screens.NoInternetScreen
import com.example.applicationhome.ui.theme.screens.Notifications
import com.example.applicationhome.ui.theme.screens.OrderScreen
import com.example.applicationhome.ui.theme.screens.Profile
import com.example.applicationhome.ui.theme.screens.RestaurantScreen
import com.example.applicationhome.ui.theme.screens.Search
import com.example.applicationhome.ui.theme.screens.SignUpScreen
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalScreen(
    drawerState : DrawerState,
    itemScreenViewModel: ItemScreenViewModel,
    cartViewModel : CartViewModel,
    userImageViewModel : UserImageViewModel,
    favoriteViewModel : FavoriteViewModel,
    drawerViewModel: DrawerViewModel,
    homeScreenViewModel : HomeScreenViewModel,
    loginViewModel: LoginViewModel,
    restaurantViewModel: RestaurantViewModel,
    confirmOrderScreenViewModel : ConfirmOrderScreenViewModel,
    orderScreenViewModel : OrderScreenViewModel,
    viewRestaurantImageViewModel: ViewRestaurantImageViewModel,
    signUpViewModel : SignUpViewModel
){
    val snackBarHostState = remember { SnackbarHostState() }
    val networkState  = homeScreenViewModel.isNetworkAvailable

    val navigationController = rememberNavController()

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize().
        background(Color.Black)
    ){
        NavHost(
            navController = navigationController,
            startDestination = Screens.DashboardScreen.screen,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ){
            composable(Screens.DashboardScreen.screen){
                DashboardScreen(
                    navigationController,
                    drawerState,
                    itemScreenViewModel,
                    cartViewModel,
                    userImageViewModel,
                    favoriteViewModel,
                    drawerViewModel,
                    homeScreenViewModel,
                    loginViewModel,
                    restaurantViewModel,
                    viewRestaurantImageViewModel
                )
            }

            composable(Screens.Profile.screen){
                Profile(navigationController, userImageViewModel)
            }

            composable(Screens.Search.screen){
                Search()
            }

            composable(Screens.RestaurantScreen.screen){
                RestaurantScreen(
                    navigationController,
                    itemScreenViewModel,
                    cartViewModel,
                    favoriteViewModel,
                    restaurantViewModel,
                    loginViewModel,
                    viewRestaurantImageViewModel,
                    homeScreenViewModel
                )
            }

            composable(Screens.ItemScreen.screen){
                ItemScreen(
                    navigationController,
                    itemScreenViewModel,
                    cartViewModel,
                    favoriteViewModel,
                    loginViewModel,
                    restaurantViewModel
                )
            }

            composable(Screens.Notifications.screen){
                Notifications()
            }

            composable(Screens.Cart.screen){
                Cart(
                    navigationController,
                    cartViewModel,
                    confirmOrderScreenViewModel
                )
            }

            composable(Screens.LoginScreen.screen){
                LoginScreen(navigationController, loginViewModel)
            }

            composable(Screens.SignUpScreen.screen){
                SignUpScreen(navigationController, signUpViewModel)
            }

            composable(Screens.ConfirmOrderScreen.screen){
                ConfirmOrderScreen(
                    navigationController,
                    confirmOrderScreenViewModel,
                    cartViewModel
                )
            }

            composable(Screens.ConfirmOrderScreen2.screen){
                ConfirmOrderScreen2(
                    navigationController,
                    confirmOrderScreenViewModel,
                    cartViewModel,
                    loginViewModel
                )
            }

            composable(Screens.LastOrdersScreen.screen){
                LastOrdersScreen(navigationController, orderScreenViewModel)
            }

            composable(Screens.OrderScreen.screen){
                OrderScreen(
                    orderScreenViewModel,
                    navigationController,
                    cartViewModel
                )
            }

            composable(Screens.NoInternetScreen.screen){
                NoInternetScreen(navigationController)
            }
        }

        var wasNetworkDisconnected by remember { mutableStateOf(false) }
        LaunchedEffect(networkState){
            delay(1000.milliseconds)
            if(!networkState){
                wasNetworkDisconnected = true
                coroutineScope.showNetworkSnackBar(
                    snackBarHostState,
                    message = "No internet connection. Please try again.",
                    actionLabel = "DISCONNECTED",
                )
            }else{
                if(wasNetworkDisconnected){
                    wasNetworkDisconnected = false
                    coroutineScope.showNetworkSnackBar(
                        snackBarHostState,
                        message = "Connected! Refreshing menu...",
                        actionLabel = "CONNECTED",
                    )

                }
            }
        }
        Box(modifier = Modifier.fillMaxSize()){
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .padding(top = 60.dp)
                    .width(300.dp)
            ){ data ->
                val backgroundColor = if (data.visuals.actionLabel == "DISCONNECTED") {
                    Color.Red
                } else {
                    Color.Green
                }
                Snackbar(
                    containerColor = backgroundColor,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(15.dp),
                    content = {
                        Text(
                            text = data.visuals.message,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }
        }
    }
}