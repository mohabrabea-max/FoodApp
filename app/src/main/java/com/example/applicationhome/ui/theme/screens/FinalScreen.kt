package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.Options
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.showNetworkSnackBar
import com.example.applicationhome.ui.theme.components.profileAndSetting.UserImage
import com.example.applicationhome.ui.theme.model.BottomBarViewModel
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
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalScreen(
    scrollBehavior : TopAppBarScrollBehavior,
    drawerState : DrawerState,
    itemScreenViewModel: ItemScreenViewModel,
    bottomBarViewModel : BottomBarViewModel,
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


    val userState by loginViewModel.userData.collectAsStateWithLifecycle()
    val density = LocalDensity.current
    val fixedWidth = remember(density) { with(density) { 250.dp.roundToPx()} }
    val navigationController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    var stat = drawerViewModel.state
    val drawerWidth by animateDpAsState(
        targetValue = if (stat) 250.dp else 70.dp,
        animationSpec = spring(1F), // تقدر تتحكم في السرعة من هنا
        label = "DrawerAnimation"
    )
    val allScreens = listOf(
        Screens.HomeScreen,
        Screens.Profile,
        Screens.Settings,
        Screens.RestaurantScreen,
        Screens.Search,
        Screens.ItemScreen,
        Screens.Notifications,
        Screens.Favorite,
        Screens.Cart,
        Screens.LoginScreen,
        Screens.SignUpScreen,
        Screens.ConfirmOrderScreen,
        Screens.ConfirmOrderScreen2,
        Screens.LastOrdersScreen,
        Screens.OrderScreen,
        Screens.NoInternetScreen
    )
    val isLogin by loginViewModel.isLogin.collectAsState()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.White,modifier = Modifier.width(drawerWidth)){
                IconButton(
                    onClick = {if(stat) drawerViewModel.stateFalse() else drawerViewModel.stateTrue()},
                    modifier = Modifier.align(if(stat) Alignment.End else Alignment.CenterHorizontally))
                {
                    Icon(if(stat) Icons.Default.KeyboardArrowLeft else Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Black)
                }
                //Divider(color = Color.LightGray)
                Box(
                    modifier = Modifier.
                    fillMaxWidth().
                    height(80.dp).
                    clip(RoundedCornerShape(40.dp)).
                    background(Color.VeryLightGray).
                    clickable{
                        if(isLogin){
                            coroutineScope.launch{drawerState.close()}
                            navigationController.navigate(Screens.Profile.screen)
                        }else{
                            coroutineScope.launch{drawerState.close()}
                            navigationController.navigate(Screens.LoginScreen.screen)
                        }
                    }
                ){
                    Row(
                        modifier = Modifier.fillMaxSize().layout { measurable, constraints ->
                            val placeable = measurable.measure(
                                constraints.copy(
                                    minWidth = fixedWidth,
                                    maxWidth = fixedWidth
                                )
                            )
                            layout(width = constraints.maxWidth, height = placeable.height) {
                                placeable.placeRelative(0, 0)
                            }
                        }.padding(10.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier.size(50.dp).
                            clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ){
                            UserImage(userImageViewModel)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        if(stat){
                            Column(modifier = Modifier.weight(2.5f),horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center){
                                Text(
                                    text = userState.firstname + " " + if(userState.lastname != null) userState.lastname else "",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = if(userState.email != null) userState.email else "Login",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
//                Box(modifier = Modifier.fillMaxWidth()){
//                    Divider(color = Color.LightGray, modifier = Modifier.width(100.dp).align(Alignment.Center))
//                }
                Options(navigationController, drawerState, coroutineScope, bottomBarViewModel, drawerViewModel, loginViewModel)
            }
        }
    ){
        Box(
            modifier = Modifier.
            fillMaxSize().background(Color.Black),
        ){
            NavHost(navController = navigationController, startDestination = Screens.HomeScreen.screen){
                allScreens.forEach { item ->
                    composable(
                        route = item.screen,
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None },
                        popEnterTransition = { EnterTransition.None },
                        popExitTransition = { ExitTransition.None }
                    ) {
                        when(item){
                            is Screens.HomeScreen -> HomeScreen(drawerState, coroutineScope, navigationController, itemScreenViewModel, cartViewModel, favoriteViewModel, restaurantViewModel, bottomBarViewModel, viewRestaurantImageViewModel, loginViewModel, homeScreenViewModel)
                            is Screens.Profile -> Profile(navigationController, userImageViewModel)
                            is Screens.Settings -> Settings(drawerState, coroutineScope, navigationController, userImageViewModel, bottomBarViewModel, cartViewModel, favoriteViewModel, loginViewModel)
                            is Screens.Search -> Search()
                            is Screens.RestaurantScreen -> RestaurantScreen(navigationController, itemScreenViewModel, cartViewModel, favoriteViewModel, restaurantViewModel, loginViewModel, viewRestaurantImageViewModel, homeScreenViewModel)
                            is Screens.ItemScreen -> ItemScreen(navigationController, itemScreenViewModel, cartViewModel, favoriteViewModel, loginViewModel, restaurantViewModel)
                            is Screens.Notifications -> Notifications()
                            is Screens.Favorite -> Favorite(drawerState, coroutineScope, navigationController, itemScreenViewModel, cartViewModel, favoriteViewModel, restaurantViewModel, bottomBarViewModel, loginViewModel, viewRestaurantImageViewModel, homeScreenViewModel)
                            is Screens.Cart -> Cart(navigationController, drawerState, coroutineScope, bottomBarViewModel, itemScreenViewModel, cartViewModel, bottomBarViewModel, loginViewModel)
                            is Screens.LoginScreen -> LoginScreen(navigationController, loginViewModel)
                            is Screens.SignUpScreen -> SignUpScreen(navigationController, signUpViewModel)
                            is Screens.ConfirmOrderScreen -> ConfirmOrderScreen(navigationController, confirmOrderScreenViewModel, cartViewModel)
                            is Screens.ConfirmOrderScreen2 -> ConfirmOrderScreen2(navigationController, confirmOrderScreenViewModel, bottomBarViewModel, cartViewModel, loginViewModel)
                            is Screens.LastOrdersScreen -> LastOrdersScreen(navigationController, orderScreenViewModel)
                            is Screens.OrderScreen -> OrderScreen(orderScreenViewModel, navigationController, cartViewModel)
                            is Screens.NoInternetScreen -> NoInternetScreen(navigationController)
                        }
                    }
                }

            }
            // TODO: fix snackbar color and racing condition issues
            var wasNetworkDisconnected by remember { mutableStateOf(false) }
            LaunchedEffect(networkState){
                if(!networkState){
                    //delay(2000.milliseconds)
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

            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .padding(top = 150.dp)
                    .width(300.dp)
            ){ data ->
                val backgroundColor = if (data.visuals.actionLabel == "DISCONNECTED") {
                    Color(0xFFD32F2F)
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