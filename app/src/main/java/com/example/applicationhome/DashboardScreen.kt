package com.example.applicationhome

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.Options
import com.example.applicationhome.ui.theme.components.bars.MyBottonBar
import com.example.applicationhome.ui.theme.components.profileAndSetting.UserImage
import com.example.applicationhome.ui.theme.model.CartViewModel
import com.example.applicationhome.ui.theme.model.DrawerViewModel
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.HomeScreenViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel
import com.example.applicationhome.ui.theme.model.LoginViewModel
import com.example.applicationhome.ui.theme.model.RestaurantViewModel
import com.example.applicationhome.ui.theme.model.UserImageViewModel
import com.example.applicationhome.ui.theme.model.ViewRestaurantImageViewModel
import com.example.applicationhome.ui.theme.screens.Favorite
import com.example.applicationhome.ui.theme.screens.HomeScreen
import com.example.applicationhome.ui.theme.screens.Settings
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navigationController: NavHostController,
    drawerState : DrawerState,
    itemScreenViewModel: ItemScreenViewModel,
    cartViewModel : CartViewModel,
    userImageViewModel : UserImageViewModel,
    favoriteViewModel : FavoriteViewModel,
    drawerViewModel: DrawerViewModel,
    homeScreenViewModel : HomeScreenViewModel,
    loginViewModel: LoginViewModel,
    restaurantViewModel: RestaurantViewModel,
    viewRestaurantImageViewModel: ViewRestaurantImageViewModel
){

    val dashboardNavController = rememberNavController()

    val coroutineScope = rememberCoroutineScope()

    val homeListState = rememberLazyListState()
    val favoriteListState = rememberLazyGridState()
    val settingsListState = rememberLazyGridState()

    val navBackStackEntry by dashboardNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isLogin by loginViewModel.isLogin.collectAsStateWithLifecycle()

    val userState by loginViewModel.userData.collectAsStateWithLifecycle()
    val density = LocalDensity.current
    val fixedWidth = remember(density) { with(density) { 250.dp.roundToPx()} }

    val stat = drawerViewModel.state
    val drawerWidth by animateDpAsState(
        targetValue = if (stat) 250.dp else 70.dp,
        animationSpec = spring(1F), // تقدر تتحكم في السرعة من هنا
        label = "DrawerAnimation"
    )

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
                                    text = userState.firstname + " " + userState.lastname.ifEmpty { "" },
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = userState.email.ifEmpty { "Login" },
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
                Options(navigationController, drawerState, coroutineScope, drawerViewModel, loginViewModel)
            }
        }
    ){
        Scaffold(
            containerColor = Color.Black,
            modifier = Modifier.
            fillMaxSize(),
            bottomBar = {
                Box(
                    modifier = Modifier.navigationBarsPadding().fillMaxWidth().
                    pointerInput(Unit) { detectTapGestures { } },
                    contentAlignment = Alignment.BottomCenter
                ){
                    MyBottonBar(
                        navigationController,
                        dashboardNavController,
                        currentRoute,
                        cartViewModel,
                        favoriteViewModel,
                        loginViewModel,
                        homeListState,
                        favoriteListState,
                        settingsListState,
                        coroutineScope
                    )
                }
            }
        ){
            NavHost(
                navController = dashboardNavController,
                modifier = Modifier.fillMaxSize(),
                startDestination = Screens.HomeScreen.screen,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ){
                composable(Screens.HomeScreen.screen){
                    HomeScreen(
                        drawerState,
                        coroutineScope,
                        navigationController,
                        itemScreenViewModel,
                        favoriteViewModel,
                        restaurantViewModel,
                        viewRestaurantImageViewModel,
                        homeScreenViewModel,
                        homeListState
                    )
                }

                composable(Screens.Favorite.screen){
                    Favorite(
                        drawerState,
                        coroutineScope,
                        navigationController,
                        itemScreenViewModel,
                        cartViewModel,
                        favoriteViewModel,
                        restaurantViewModel,
                        loginViewModel,
                        viewRestaurantImageViewModel,
                        homeScreenViewModel,
                        favoriteListState
                    )
                }

                composable(Screens.Settings.screen){
                    Settings(
                        drawerState,
                        coroutineScope,
                        navigationController,
                        userImageViewModel,
                        settingsListState
                    )
                }
            }
        }
    }
}