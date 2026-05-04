package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.data.models.Snake
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.components.CartBox
import com.example.applicationhome.ui.theme.components.CartButton
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.BottomBarViewModel
import com.example.applicationhome.view.model.FavoriteViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState",
    "ContextCastToActivity"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(
    navigationController : NavHostController,
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    viewModelForBottomBar: BottomBarViewModel,
    viewModel: ItemScreenViewModel = viewModel(),
    addBoxViewModel : AddBoxViewModel,
    favoriteState : FavoriteViewModel
){
    var cart = remember{Cart.cartMap()}
    val context = LocalContext.current as? Activity
    BackHandler(enabled = true) {
        // ده بيمسح الأبلكيشن من الـ Background ويقفله تماماً
        context?.finishAffinity()
    }
    Scaffold(
        modifier = Modifier.
        fillMaxSize().
        background(Color.LightBrownForBackground),
        topBar = {
            MyTopBar(
                modifier = Modifier.
                fillMaxWidth().
                height(100.dp).
                shadow(elevation = 5.dp),
                "Cart",
                {coroutineScope.launch{drawerState.open()}},
                {Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.Black)},
                {
                    IconButton(onClick = {
                        navigationController.navigate(Screens.Search.screen){
                            popUpTo(navigationController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                    }
                    IconButton(onClick = {
                        navigationController.navigate(Screens.Favorite.screen){
                            popUpTo(navigationController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }
                    }) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Black)
                    }
                }
            )
            Divider(color = Color.LightGray.copy(alpha = 0.5f))
        }
    ){
        Box(modifier = Modifier.background(Color.White)){
            Box(modifier = Modifier.fillMaxSize()){
                if(cart.isNotEmpty()){
                    LazyVerticalGrid (
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ){
                        item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(100.dp))}
                        items(cart.keys.toList(), key = { it.food.id.toString() + it.size}) { item ->
                            when (item.food) {
                                is FoodItem -> {
                                    CartBox(item.food, item.size, navigationController, viewModel, addBoxViewModel, favoriteState)
                                }
                                is Snake -> {
                                    Text(text = item.food.name)
                                    // هنا الـ image نوعها Int (Resource ID)
                                    Image(painter = painterResource(id = item.food.image), contentDescription = null, modifier = Modifier.fillMaxSize())
                                }
                            }
                        }
                    }
                }else{
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                        Spacer(modifier = Modifier.height(150.dp))
                        Text(
                            text = "Your shopping cart",
                            fontSize = 25.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(5.dp)
                        )
                        Text(
                            text = "looks empty!",
                            fontSize = 25.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(5.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "What are you waiting for?",
                            fontSize = 20.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(5.dp)
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                        Box(
                            modifier = Modifier.aspectRatio(2f).
                            padding(5.dp).
                            clip(RoundedCornerShape(30.dp)).
                            clickable{
                                navigationController.navigate(Screens.HomeScreen.screen){
                                    popUpTo(navigationController.graph.findStartDestination().id) {
                                        saveState = true
                                    }

                                    launchSingleTop = true

                                    restoreState = true
                                }
                                viewModelForBottomBar.home()
                            }.
                            border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp))
                        ){
                            Row {
                                Column(modifier = Modifier.weight(1f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                                    Text(
                                        text = "Go",
                                        fontSize = 80.sp,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.DarkOrange,
                                        textAlign = TextAlign.Center,
                                    )
                                    Text(
                                        text = "shopping!",
                                        fontSize = 40.sp,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.Black,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                                VerticalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                                Icon(
                                    Icons.Default.ShoppingCart, contentDescription = "Shopping Cart", tint = Color.Blue,
                                    modifier = Modifier.weight(1f).size(100.dp).align(Alignment.CenterVertically)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Box(
                            modifier = Modifier.aspectRatio(2f).
                            padding(5.dp).
                            clip(RoundedCornerShape(30.dp)).
                            clickable{
                                navigationController.navigate(Screens.Favorite.screen){
                                    popUpTo(navigationController.graph.findStartDestination().id) {
                                        saveState = true
                                    }

                                    launchSingleTop = true

                                    restoreState = true
                                }
                                viewModelForBottomBar.favorite()
                            }.
                            border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp))
                        ){
                            Row {
                                Column(modifier = Modifier.weight(1f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                                    Text(
                                        text = "Favorite",
                                        fontSize = 40.sp,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.DarkOrange,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                                VerticalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                                Icon(
                                    Icons.Default.Favorite, contentDescription = "Shopping Cart", tint = Color.Red,
                                    modifier = Modifier.weight(1f).size(100.dp).align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                }
            }
            Column(modifier = Modifier.align(Alignment.BottomCenter), horizontalAlignment = Alignment.CenterHorizontally){
                if(cart.isNotEmpty()){
                    CartButton(addBoxViewModel)
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}