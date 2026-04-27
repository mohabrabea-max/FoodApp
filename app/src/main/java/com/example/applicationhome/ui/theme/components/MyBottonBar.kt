package com.example.applicationhome.ui.theme.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.DeepMatteBlack
import com.example.applicationhome.view.model.BottomBarViewModel

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottonBar2(
    navController : NavController,
    viewModel: BottomBarViewModel
){
    var selected = viewModel.selected.value
    Box(
        modifier = Modifier.width(350.dp).
        height(90.dp).
        clip(RoundedCornerShape(50.dp)).
        background(Color.DeepMatteBlack).
        pointerInput(Unit) {
            detectTapGestures { }
        }
    ){
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(modifier = Modifier.weight(1f)){
                IconButton(
                    onClick = {
                        viewModel.home()
                        navController.navigate(Screens.HomeScreen.screen)
                    }, modifier = Modifier.align(Alignment.Center)
                ){
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Home",
                        modifier = Modifier.size(26.dp),
                        tint = if(selected == "Home") Color.DarkOrange else Color.White
                    )
                }
            }
            Box(modifier = Modifier.weight(1f)){
                IconButton(
                    onClick = {
                        viewModel.favorite()
                        navController.navigate(Screens.Favorite.screen)
                    }, modifier = Modifier.align(Alignment.Center)
                ){
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        modifier = Modifier.size(26.dp),
                        tint = if(selected == "Favorite") Color.DarkOrange else Color.White
                    )
                }
            }
            Box(modifier = Modifier.weight(1f)){
                IconButton(
                    onClick = {
                        viewModel.cart()
                        navController.navigate(Screens.Cart.screen)
                    }, modifier = Modifier.align(Alignment.Center)
                ){
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        modifier = Modifier.size(26.dp),
                        tint = if(selected == "Cart") Color.DarkOrange else Color.White
                    )
                }
            }
            Box(modifier = Modifier.weight(1f)){
                IconButton(
                    onClick = {
                        viewModel.profile()
                        navController.navigate(Screens.Profile.screen)
                    }, modifier = Modifier.align(Alignment.Center)
                ){
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(26.dp),
                        tint = if(selected == "Profile") Color.DarkOrange else Color.White
                    )
                }
            }
        }
    }
}


//@SuppressLint("UnrememberedMutableState")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyBottonBar(navController : NavController, viewModel: BottomBarViewModel){
//    var selected = viewModel.selected.value
//    BottomAppBar(
//        contentColor = Color.DarkOrange,
//        containerColor = Color.White
//    ){
//        IconButton(
//            onClick = {
//                viewModel.home()
//                navController.navigate(Screens.HomeScreen.screen)
//            },
//            modifier = Modifier.weight(1f)
//        ){
//            Icon(
//                Icons.Default.Home,
//                contentDescription = "Home",
//                modifier = Modifier.size(26.dp),
//                tint = if(selected == "Home") Color.Blue else Color.DarkOrange
//            )
//        }
//
//        IconButton(
//            onClick = {
//                viewModel.favorit()
//                navController.navigate(Screens.Favorite.screen)
//            },
//            modifier = Modifier.weight(1f)
//        ){
//            Icon(
//                Icons.Default.Favorite,
//                contentDescription = "Favorite",
//                modifier = Modifier.size(26.dp),
//                tint = if(selected == "Favorite") Color.Blue else Color.DarkOrange
//            )
//        }
//
//        IconButton(
//            onClick = {
//                viewModel.cart()
//                navController.navigate(Screens.Cart.screen)
//            },
//            modifier = Modifier.weight(1f)
//        ){
//            Icon(
//                Icons.Default.ShoppingCart,
//                contentDescription = "Cart",
//                modifier = Modifier.size(26.dp),
//                tint = if(selected == "Cart") Color.Blue else Color.DarkOrange
//            )
//        }
//
//        IconButton(
//            onClick = {
//                viewModel.profile()
//                navController.navigate(Screens.Profile.screen)
//            },
//            modifier = Modifier.weight(1f)
//        ){
//            Icon(
//                Icons.Default.Person,
//                contentDescription = "Profile",
//                modifier = Modifier.size(26.dp),
//                tint = if(selected == "Profile") Color.Blue else Color.DarkOrange
//            )
//        }
//    }
//}