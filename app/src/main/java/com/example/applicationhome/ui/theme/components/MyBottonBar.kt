package com.example.applicationhome.ui.theme.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.Orange
import com.example.applicationhome.view.model.BottomBarViewModel

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottonBar(navController : NavController, viewModel: BottomBarViewModel){
    var selected = viewModel.selected.value
    BottomAppBar(
        contentColor = Color.White,
        containerColor = Color.Black
    ){
        IconButton(
            onClick = {
                viewModel.home()
                navController.navigate(Screens.HomeScreen.screen)
            },
            modifier = Modifier.weight(1f)
        ){
            Icon(
                Icons.Default.Home,
                contentDescription = "Home",
                modifier = Modifier.size(26.dp),
                tint = if(selected == "Home") Color.Orange else Color.White
            )
        }

        IconButton(
            onClick = {
                viewModel.favorit()
                navController.navigate(Screens.Favorite.screen)
            },
            modifier = Modifier.weight(1f)
        ){
            Icon(
                Icons.Default.Favorite,
                contentDescription = "Favorite",
                modifier = Modifier.size(26.dp),
                tint = if(selected == "Favorite") Color.Orange else Color.White
            )
        }

        IconButton(
            onClick = {
                viewModel.cart()
                navController.navigate(Screens.Cart.screen)
            },
            modifier = Modifier.weight(1f)
        ){
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = "Cart",
                modifier = Modifier.size(26.dp),
                tint = if(selected == "Cart") Color.Orange else Color.White
            )
        }

        IconButton(
            onClick = {
                viewModel.profile()
                navController.navigate(Screens.Profile.screen)
            },
            modifier = Modifier.weight(1f)
        ){
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile",
                modifier = Modifier.size(26.dp),
                tint = if(selected == "Profile") Color.Orange else Color.White
            )
        }
    }
}