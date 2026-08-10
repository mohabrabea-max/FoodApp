package com.example.applicationhome

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.showNetworkSnackBar
import com.example.applicationhome.core.ui.theme.MatteBlack
import com.example.applicationhome.core.ui.theme.model.FinalScreenViewModel
import com.example.applicationhome.core.ui.theme.model.UserImageViewModel
import com.example.applicationhome.core.ui.theme.screens.NoInternetScreen
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.features.Notifications.Notifications
import com.example.applicationhome.features.cart.ui.Cart
import com.example.applicationhome.features.cart.ui.CartViewModel
import com.example.applicationhome.features.confirmorder.ui.ConfirmOrderScreen
import com.example.applicationhome.features.confirmorder.ui.ConfirmOrderScreenViewModel
import com.example.applicationhome.features.login.ui.LoginScreen
import com.example.applicationhome.features.login.ui.LoginViewModel
import com.example.applicationhome.features.orders.ui.OrderScreenViewModel
import com.example.applicationhome.features.orders.ui.lastorders.LastOrdersScreen
import com.example.applicationhome.features.orders.ui.orderscreen.OrderScreen
import com.example.applicationhome.features.profile.ui.Profile
import com.example.applicationhome.features.profile.ui.ProfileViewModel
import com.example.applicationhome.features.restaurantscreen.ui.RestaurantScreen
import com.example.applicationhome.features.restaurantscreen.ui.RestaurantViewModel
import com.example.applicationhome.features.search.ui.Search
import com.example.applicationhome.features.search.ui.SearchViewModel
import com.example.applicationhome.features.signupscreen.ui.SignUpScreen
import com.example.applicationhome.features.signupscreen.ui.SignUpViewModel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalScreen(finalScreenViewModel : FinalScreenViewModel){
    val networkState = finalScreenViewModel.isNetworkAvailable

    val snackBarHostState = remember { SnackbarHostState() }

    val navigationController = rememberNavController()


    Box(
        modifier = Modifier.fillMaxSize().
        background(Color.Black)
    ){
        NavHost(
            navController = navigationController,
            startDestination = Screens.DashboardScreen.screen,
            modifier = Modifier.fillMaxSize(),

            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },

            // 2. خروج الشاشة الحالية لما تفتح شاشة جديدة فوقها (بتتحرك للشمال)
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },

            // 3. عودة الشاشة السابقة لما تدوس Back (بتيجي من الشمال لليمن)
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },

            // 4. خروج الشاشة الحالية عند الضغط على Back (بتسحب لليمين وتختفي)
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ){
            composable(Screens.DashboardScreen.screen){
                val userImageViewModel: UserImageViewModel = viewModel()
                DashboardScreen(
                    navigationController,
                    userImageViewModel
                )
            }

            composable(Screens.Profile.screen){
                val profileViewModel: ProfileViewModel = hiltViewModel()
                Profile(
                    navigationController,
                    profileViewModel
                )
            }

            composable(Screens.Search.screen){
                val searchViewModel : SearchViewModel = hiltViewModel()
                Search(
                    navigationController,
                    searchViewModel
                )
            }

            composable(
                route = Screens.RestaurantScreen.screen,
                arguments = listOf(
                    navArgument("restaurantId"){
                        type = NavType.IntType
                    },
                    navArgument("mealId"){
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ){
                val restaurantViewModel: RestaurantViewModel = hiltViewModel()
                RestaurantScreen(
                    navigationController,
                    restaurantViewModel
                )
            }

            composable(Screens.Notifications.screen){
                Notifications()
            }

            composable(Screens.Cart.screen){
                val cartViewModel: CartViewModel = hiltViewModel()
                Cart(
                    navigationController,
                    cartViewModel
                )
            }

            composable(Screens.LoginScreen.screen){
                val loginViewModel: LoginViewModel = hiltViewModel()
                LoginScreen(navigationController, loginViewModel)
            }

            composable(Screens.SignUpScreen.screen){
                val signUpViewModel : SignUpViewModel = hiltViewModel()
                SignUpScreen(navigationController, signUpViewModel)
            }

            composable(Screens.ConfirmOrderScreen.screen){
                val confirmOrderScreenViewModel : ConfirmOrderScreenViewModel = hiltViewModel()
                ConfirmOrderScreen(
                    navigationController,
                    confirmOrderScreenViewModel
                )
            }

            navigation(startDestination = Screens.LastOrdersScreen.screen, route = "Orders"){
                composable(Screens.LastOrdersScreen.screen){ backStackEntry ->
                    val parentEntry = remember(backStackEntry){
                        navigationController.getBackStackEntry("Orders")
                    }

                    val orderScreenViewModel : OrderScreenViewModel = hiltViewModel(parentEntry)

                    LastOrdersScreen(navigationController, orderScreenViewModel)
                }

                composable(Screens.OrderScreen.screen){ backStackEntry ->
                    val parentEntry = remember(backStackEntry){
                        navigationController.getBackStackEntry("Orders")
                    }

                    val orderScreenViewModel : OrderScreenViewModel = hiltViewModel(parentEntry)

                    OrderScreen(
                        orderScreenViewModel,
                        navigationController
                    )
                }
            }

            composable(Screens.NoInternetScreen.screen){
                NoInternetScreen(navigationController)
            }
        }

        LaunchedEffect(Unit){
            networkState.drop(1).collect { isConnected ->
                val message = if (isConnected) "Connected!" else "Disconnected!"

                launch {
                    snackBarHostState.showNetworkSnackBar(
                        message = message
                    )
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()){
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .padding(top = 50.dp)
                    .width(150.dp)
            ){ data ->
                Snackbar(
                    containerColor = Color.MatteBlack,
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