package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.Orange
import com.example.applicationhome.ui.theme.components.MyBottonBar
import com.example.applicationhome.view.model.BottomBarViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(navigationController : NavHostController, viewModel: BottomBarViewModel){
    Scaffold(
        modifier = Modifier.
        fillMaxSize().
        background(Color.LightBrownForBackground),
        topBar = { MyTopBar3(navigationController) },
        bottomBar = { MyBottonBar(navigationController, viewModel) }
        //floatingActionButton = { CartButton() }
    ){innerPadding ->
        Box(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier.fillMaxSize().align(Alignment.Center).padding(10.dp).statusBarsPadding(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ){
                Text(text = "Cart", fontSize = 30.sp, color = Color.Black)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar3(navController: NavHostController){
    Surface(
        modifier = Modifier.
        fillMaxWidth().
        height(100.dp).
        statusBarsPadding(),
        color = Color.Black
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {if (navController.previousBackStackEntry != null) { navController.popBackStack() } } ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Orange)
            }
            Text(
                text = "Cart",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Orange
            )
            Row{
                IconButton(onClick = {navController.navigate(Screens.ItemScreen.screen)}) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Orange)
                }
                IconButton(onClick = {navController.navigate(Screens.Favorite.screen)}) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Orange)
                }
            }
        }
    }
}