package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.applicationhome.R
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.BackgroundForCards
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.components.MyBottonBar
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.ui.theme.components.Options
import com.example.applicationhome.view.model.ItemScreenViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalScreen(scrollBehavior: TopAppBarScrollBehavior, drawerState : DrawerState, viewModel : ItemScreenViewModel = viewModel()){
    val navigationController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val allScreens = listOf(
        Screens.HomeScreen,
        Screens.Profile,
        Screens.Settings,
        Screens.Menu,
        Screens.Restaurants,
        Screens.Varieties,
        Screens.Search,
        Screens.ItemScreen
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.LightBrownForBackground,modifier = Modifier.width(250.dp)){
                Box(
                    modifier = Modifier.
                    background(Color.BackgroundForCards).
                    fillMaxWidth().
                    height(80.dp).
                    clickable{
                        coroutineScope.launch{drawerState.close()}
                        navigationController.navigate(Screens.Profile.screen){popUpTo(0)}
                    }
                ){
                    Row(
                        modifier = Modifier.fillMaxSize().padding(10.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier.size(50.dp).
                            clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.myphoto),
                                contentDescription = "Food Logo",
                                modifier = Modifier.
                                fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(2.5f),horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center){
                            Text(
                                text = "Mohab Rabea",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.BrownForFont
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "01011223344",
                                fontSize = 12.sp,
                                color = Color.MediumBrownForTitle
                            )
                        }

                    }

                }
                Divider()
                Options(navigationController, drawerState, coroutineScope)
            }
        }
    ){
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

            topBar = { MyTopBar(scrollBehavior,navigationController, drawerState) },

            bottomBar = { MyBottonBar(navigationController) }

        ){innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)){
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
                                is Screens.HomeScreen -> HomeScreen(drawerState, coroutineScope, navigationController, viewModel = viewModel)
                                is Screens.Profile -> Profile()
                                is Screens.Settings -> Settings(drawerState, coroutineScope, navigationController)
                                is Screens.Search -> Search()
                                is Screens.Menu -> Menu(drawerState, coroutineScope, navigationController)
                                is Screens.Restaurants -> Restaurants()
                                is Screens.Varieties -> Varieties()
                                is Screens.ItemScreen -> ItemScreen(viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}