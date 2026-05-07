package com.example.applicationhome.ui.theme.screens

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
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.MyBottonBar
import com.example.applicationhome.ui.theme.components.Options
import com.example.applicationhome.ui.theme.components.UserImage
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.BottomBarViewModel
import com.example.applicationhome.view.model.CategoriesBoxViewModel
import com.example.applicationhome.view.model.DrawerViewModel
import com.example.applicationhome.view.model.FavoriteViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel
import com.example.applicationhome.view.model.UserImageViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalScreen(
    scrollBehavior : TopAppBarScrollBehavior,
    drawerState : DrawerState,
    viewModel : ItemScreenViewModel,
    viewModelForBottomBar : BottomBarViewModel,
    addBoxViewModel : AddBoxViewModel,
    userImageViewModel : UserImageViewModel,
    favoriteViewModel : FavoriteViewModel,
    drawerViewModel: DrawerViewModel,
    categoriesBoxViewModel : CategoriesBoxViewModel
){
    val density = LocalDensity.current
    val fixedWidth = remember(density) { with(density) { 250.dp.roundToPx()} }
    val navigationController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var stat = drawerViewModel.state
    //var drawer = if(stat) 250.dp else 70.dp
    val drawerWidth by animateDpAsState(
        targetValue = if (stat) 250.dp else 70.dp,
        animationSpec = spring(1F), // تقدر تتحكم في السرعة من هنا
        label = "DrawerAnimation"
    )
    val allScreens = listOf(
        Screens.HomeScreen,
        Screens.Profile,
        Screens.Settings,
        Screens.Menu,
        Screens.Restaurants,
        Screens.Varieties,
        Screens.Search,
        Screens.ItemScreen,
        Screens.Notifications,
        Screens.Favorite,
        Screens.Cart
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute == "homescreen" || currentRoute == "favorite" || currentRoute == "cart" || currentRoute == "settings",
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
                        coroutineScope.launch{drawerState.close()}
                        navigationController.navigate(Screens.Profile.screen)
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
                                    text = "Mohab Rabea",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = "01011223344",
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
                Options(navigationController, drawerState, coroutineScope, viewModelForBottomBar, drawerViewModel)
            }
        }
    ){
        Scaffold(
            modifier = Modifier.
            fillMaxSize().
            background(Color.White),
            bottomBar = {
                Box(
                    modifier = Modifier.navigationBarsPadding().fillMaxWidth().
                    pointerInput(Unit) { detectTapGestures { } },
                    contentAlignment = Alignment.BottomCenter
                ){
                    MyBottonBar(navigationController, viewModelForBottomBar, addBoxViewModel, favoriteViewModel)
                }
            },
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
                            is Screens.HomeScreen -> HomeScreen(drawerState, coroutineScope, navigationController, viewModel, addBoxViewModel, favoriteViewModel, categoriesBoxViewModel)
                            is Screens.Profile -> Profile(drawerState, coroutineScope, navigationController, userImageViewModel)
                            is Screens.Settings -> Settings(drawerState, coroutineScope, navigationController, userImageViewModel)
                            is Screens.Search -> Search()
                            is Screens.Menu -> Menu(navigationController, viewModel, addBoxViewModel, favoriteViewModel)
                            is Screens.Restaurants -> Restaurants(drawerState, coroutineScope, navigationController, favoriteViewModel)
                            is Screens.Varieties -> Varieties(drawerState, coroutineScope, navigationController, favoriteViewModel)
                            is Screens.ItemScreen -> ItemScreen(navigationController, viewModel, addBoxViewModel, favoriteViewModel)
                            is Screens.Notifications -> Notifications()
                            is Screens.Favorite -> Favorite(drawerState, coroutineScope, navigationController, viewModelForBottomBar, viewModel, addBoxViewModel, favoriteViewModel)
                            is Screens.Cart -> Cart(navigationController, drawerState, coroutineScope, viewModelForBottomBar, viewModel, addBoxViewModel, favoriteViewModel)
                        }
                    }
                }
            }
        }
    }
}