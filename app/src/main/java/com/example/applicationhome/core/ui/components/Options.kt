package com.example.applicationhome.core.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.applicationhome.core.domain.model.Drawer
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.components.model.DashboardScreenViewModel
import com.example.applicationhome.data.data.model.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Options(
    navigationController : NavHostController,
    dashboardNavController : NavHostController,
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    dashboardScreenViewModel : DashboardScreenViewModel
){
    val isLogIn by dashboardScreenViewModel.isLogin.collectAsState()
    val density = LocalDensity.current
    val fixedWidth = remember(density) { with(density) { 250.dp.roundToPx()} }
    val options1 = Drawer.optionsData1()
    val options2 = Drawer.optionsData2()
    val state by dashboardScreenViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current.applicationContext
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.spacedBy(16.dp)){
            item{Spacer(modifier = Modifier.height(10.dp))}

            items(options1){item ->
                NavigationDrawerItem(
                    label = {
                        if(state) Text(
                            text = item.title,
                            color = Color.DarkOrange,
                            modifier = Modifier.layout { measurable, constraints ->
                                val placeable = measurable.measure(
                                    constraints.copy(
                                        minWidth = fixedWidth,
                                        maxWidth = fixedWidth
                                    )
                                )
                                layout(width = constraints.maxWidth, height = placeable.height) {
                                    placeable.placeRelative(0, 0)
                                }
                            }
                        )
                    },
                    selected = currentRoute == item.screen,
                    icon = {Icon(imageVector = item.icon, contentDescription = item.title, tint = Color.DarkOrange, modifier = Modifier.padding(start = 5.dp))},
                    onClick = {
                        coroutineScope.launch{
                            drawerState.close()
                            navigationController.navigate(item.screen)
                        }
                    }
                )
            }

            item{
                Box(modifier = Modifier.fillMaxWidth()){
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.Center),
                        thickness = DividerDefaults.Thickness,
                        color = Color.LightGray
                    )
                }
            }

            items(options2){item ->
                NavigationDrawerItem(
                    label = {
                        if(state) Text(
                            text = item.title,
                            color = Color.DarkOrange,
                            modifier = Modifier.layout { measurable, constraints ->
                                val placeable = measurable.measure(
                                    constraints.copy(
                                        minWidth = fixedWidth,
                                        maxWidth = fixedWidth
                                    )
                                )
                                layout(width = constraints.maxWidth, height = placeable.height) {
                                    placeable.placeRelative(0, 0)
                                }
                            }
                        )
                    },
                    selected = currentRoute == item.screen,
                    icon = {Icon(imageVector = item.icon, contentDescription = item.title, tint = Color.DarkOrange, modifier = Modifier.padding(start = 5.dp))},
                    onClick = {
                        coroutineScope.launch{
                            drawerState.close()
                            navigationController.navigate(item.screen)
                        }
                    }
                )
            }

            item {
                NavigationDrawerItem(
                    label = {
                        if(state) Text(
                            text = "Settings",
                            color = Color.DarkOrange,
                            modifier = Modifier.layout { measurable, constraints ->
                                val placeable = measurable.measure(
                                    constraints.copy(
                                        minWidth = fixedWidth,
                                        maxWidth = fixedWidth
                                    )
                                )
                                layout(width = constraints.maxWidth, height = placeable.height) {
                                    placeable.placeRelative(0, 0)
                                }
                            }
                        )
                    },
                    selected = currentRoute == Screens.Settings.screen,
                    icon = {Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings", tint = Color.DarkOrange, modifier = Modifier.padding(start = 5.dp))},
                    onClick = {
                        coroutineScope.launch{
                            drawerState.close()
                            dashboardNavController.navigate(Screens.Settings.screen){
                                popUpTo(dashboardNavController.graph.findStartDestination().id){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }

            item{
                Box(modifier = Modifier.fillMaxWidth()){
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.Center),
                        thickness = DividerDefaults.Thickness,
                        color = Color.LightGray
                    )
                }
            }

            item {
                NavigationDrawerItem(
                    label = {
                        if(state) Text(
                            text = if(isLogIn) "Logout" else "Login",
                            color = if(isLogIn) Color.Red else Color.Green,
                            modifier = Modifier.layout { measurable, constraints ->
                            val placeable = measurable.measure(
                                constraints.copy(
                                    minWidth = fixedWidth,
                                    maxWidth = fixedWidth
                                )
                            )
                            layout(width = constraints.maxWidth, height = placeable.height) {
                                placeable.placeRelative(0, 0)
                            }
                            }
                        )
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = if(isLogIn) Icons.AutoMirrored.Filled.Logout else Icons.AutoMirrored.Filled.Login,
                            contentDescription = if(isLogIn) "Logout" else "Login",
                            tint = if(isLogIn) Color.Red else Color.Green,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    },
                    onClick = {
                        coroutineScope.launch{
                            drawerState.close()
                            if(isLogIn){
                                dashboardScreenViewModel.logout()

                                Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
                            }else{
                                navigationController.navigate(Screens.LoginScreen.screen)
                            }
                        }
                    }
                )
            }
        }
    }
}