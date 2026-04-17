package com.example.applicationhome.ui.theme.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.applicationhome.data.models.Options
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.BrownForFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Options(navigationController: NavHostController, drawerState : DrawerState, coroutineScope : CoroutineScope){
    val options = listOf(
        Options("Home", Icons.Default.Home, Screens.HomeScreen.screen),
        Options("Profile", Icons.Default.Person, Screens.Profile.screen),
        Options("Settings", Icons.Default.Settings, Screens.Settings.screen)
    )
    val menuOptions = listOf(
        Options("Menu", Icons.Default.Home, Screens.Menu.screen),
        Options("Varieties", Icons.Default.Settings, Screens.Varieties.screen),
        Options("Restaurants", Icons.Default.Person, Screens.Restaurants.screen)

    )
    val context = LocalContext.current.applicationContext
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    LazyColumn(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.spacedBy(16.dp)){
        items(options){item ->
            NavigationDrawerItem(label = {Text(text = item.title, color = Color.BrownForFont)},
                selected = currentRoute == item.screen,
                icon = {Icon(imageVector = item.icon, contentDescription = item.title, tint = Color.BrownForFont)},
                onClick = {
                    coroutineScope.launch{drawerState.close()}
                    navigationController.navigate(item.screen){popUpTo(0)}
                }
            )
        }
        item{Divider()}
        items(menuOptions){item ->
            NavigationDrawerItem(label = {Text(text = item.title, color = Color.BrownForFont)},
                selected = currentRoute == item.screen,
                icon = {Icon(imageVector = item.icon, contentDescription = item.title, tint = Color.BrownForFont)},
                onClick = {
                    coroutineScope.launch{drawerState.close()}
                    navigationController.navigate(item.screen){popUpTo(0)}
                }
            )
        }
        item { NavigationDrawerItem(label = {Text(text = "Logout", color = Color.Red)},
            selected = false,
            icon = {Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.Red)},
            onClick = {
                coroutineScope.launch{drawerState.close()}
                Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
            }
        )}
    }
}