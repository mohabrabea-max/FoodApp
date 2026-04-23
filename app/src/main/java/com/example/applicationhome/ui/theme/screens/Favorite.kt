package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.components.MyBottonBar
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.view.model.BottomBarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorite(scrollBehavior: TopAppBarScrollBehavior, drawerState : DrawerState, coroutineScope : CoroutineScope, navigationController : NavHostController, viewModel: BottomBarViewModel){
    Scaffold(
        modifier = Modifier.
        fillMaxSize().
        background(Color.LightBrownForBackground),
        topBar = { MyTopBar(scrollBehavior, {coroutineScope.launch{drawerState.open()}}, {navigationController.navigate(Screens.Notifications.screen)}, Icons.Default.Notifications, {navigationController.navigate(Screens.Search.screen)}, Icons.Default.Search) },
        bottomBar = { MyBottonBar(navigationController, viewModel) }
        //floatingActionButton = { CartButton() }
    ){innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)){
            Column(
                modifier = Modifier.fillMaxSize().align(Alignment.Center).padding(10.dp).statusBarsPadding(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ){
                Text(text = "Favorite", fontSize = 30.sp, color = Color.Black)
            }
        }
    }

}