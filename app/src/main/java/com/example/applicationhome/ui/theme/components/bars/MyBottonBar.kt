package com.example.applicationhome.ui.theme.components.bars

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
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.data.models.repository.CartRepository.totalNumber
import com.example.applicationhome.data.models.repository.FavoriteRepository.favoritList
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.DeepMatteBlack
import com.example.applicationhome.ui.theme.model.AddBoxViewModel
import com.example.applicationhome.ui.theme.model.BottomBarViewModel
import com.example.applicationhome.ui.theme.model.FavoriteViewModel

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottonBar(
    navigationController : NavController,
    viewModel: BottomBarViewModel,
    addBoxViewModel : AddBoxViewModel,
    favoriteViewModel: FavoriteViewModel
){
    var selected = viewModel.selected
    Box(
        modifier = Modifier.width(350.dp).
        height(60.dp).
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
                        if(selected == "Home"){
                            navigationController.navigate(Screens.HomeScreen.screen)
                        }else{
                            viewModel.home()
                            navigationController.navigate(Screens.HomeScreen.screen){
                                popUpTo(navigationController.graph.findStartDestination().id) {
                                    saveState = true // احفظ حالة الصفحة اللي أنا خارج منها (زي السكرول)
                                }

                                // 2. ميكررش نفس الصفحة لو أنا دوست عليها وأنا واقف فيها
                                launchSingleTop = true

                                // 3. يرجع الحالة اللي كانت محفوظة لما أرجع للصفحة دي تاني
                                restoreState = true
                            }
                        }
                    }, modifier = Modifier.fillMaxSize().align(Alignment.Center)
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
                        if(selected == "Favorite"){
                            navigationController.navigate(Screens.Favorite.screen)
                        }else{
                            viewModel.favorite()
                            navigationController.navigate(Screens.Favorite.screen){
                                popUpTo(navigationController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true

                                restoreState = true
                            }
                        }
                    }, modifier = Modifier.fillMaxSize().align(Alignment.Center)
                ){
                    BadgedBox(
                        badge = {
                            if(favoritList.size > 0){
                                Badge(
                                    containerColor = Color.DarkOrange,
                                    contentColor = Color.White
                                ){
                                    Text(text = favoritList.size.toString())
                                }
                            }
                        }
                    ){
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            modifier = Modifier.size(26.dp),
                            tint = if(selected == "Favorite") Color.DarkOrange else Color.White
                        )
                    }
                }
            }
            Box(modifier = Modifier.weight(1f)){
                IconButton(
                    onClick = {
                        if(selected == "Cart"){
                            navigationController.navigate(Screens.Cart.screen)
                        }else{
                            viewModel.cart()
                            navigationController.navigate(Screens.Cart.screen){
                                popUpTo(navigationController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true

                                restoreState = true
                            }
                        }
                    }, modifier = Modifier.fillMaxSize().align(Alignment.Center)
                ){
                    BadgedBox(
                        badge = {
                            if(totalNumber.value > 0){
                                Badge(
                                    containerColor = Color.DarkOrange,
                                    contentColor = Color.White
                                ){
                                    Text(text = totalNumber.value.toString())
                                }
                            }
                        }
                    ){
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            modifier = Modifier.size(26.dp),
                            tint = if(selected == "Cart") Color.DarkOrange else Color.White
                        )
                    }
                }
            }
            Box(modifier = Modifier.weight(1f)){
                IconButton(
                    onClick = {
                        if(selected == "Settings"){
                            navigationController.navigate(Screens.Settings.screen)
                        }else{
                            viewModel.settings()
                            navigationController.navigate(Screens.Settings.screen){
                                popUpTo(navigationController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true

                                restoreState = true
                            }
                        }
                    }, modifier = Modifier.fillMaxSize().align(Alignment.Center)
                ){
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Settings",
                        modifier = Modifier.size(26.dp),
                        tint = if(selected == "Settings") Color.DarkOrange else Color.White
                    )
                }
            }
        }
    }
}
