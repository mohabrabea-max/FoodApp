package com.example.applicationhome.ui.theme.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.applicationhome.data.models.Drawer
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.view.model.BottomBarViewModel
import com.example.applicationhome.view.model.DrawerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Options(
    navigationController: NavHostController,
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    bottomBarViewModel: BottomBarViewModel,
    drawerViewModel: DrawerViewModel
){
    val options = Drawer.optionsData()
    val menuOptions = Drawer.menuOptionsData()
    var state = drawerViewModel.state
    val context = LocalContext.current.applicationContext
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.spacedBy(16.dp)){
            item{Spacer(modifier = Modifier.height(10.dp))}
            items(options){item ->
                NavigationDrawerItem(label = {if(state) Text(text = item.title, color = Color.DarkOrange)},
                    selected = currentRoute == item.screen,
                    icon = {Icon(imageVector = item.icon, contentDescription = item.title, tint = Color.DarkOrange)},
                    onClick = {
                        if(item.title == "Home") bottomBarViewModel.home()
                        else if(item.title == "Settings") bottomBarViewModel.settings()
                        coroutineScope.launch{drawerState.close()}
                        navigationController.navigate(item.screen)
                    }
                )
            }
            item{
                Box(modifier = Modifier.fillMaxWidth()){
                    Divider(color = Color.LightGray, modifier = Modifier.width(100.dp).align(Alignment.Center))
                }
            }
            items(menuOptions){item ->
                NavigationDrawerItem(label = {if(state) Text(text =  item.title, color = Color.DarkOrange)},
                    selected = currentRoute == item.screen,
                    icon = {Icon(imageVector = item.icon, contentDescription = item.title, tint = Color.DarkOrange)},
                    onClick = {
                        coroutineScope.launch{drawerState.close()}
                        navigationController.navigate(item.screen)
                    }
                )
            }
            item { NavigationDrawerItem(label = {if(state) Text(text = "Logout", color = Color.Red)},
                selected = false,
                icon = {Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.Red)},
                onClick = {
                    coroutineScope.launch{drawerState.close()}
                    Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
                }
            )}
        }
    }
}