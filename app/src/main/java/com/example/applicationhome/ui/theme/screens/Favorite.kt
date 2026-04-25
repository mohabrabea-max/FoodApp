package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.data.models.Snake
import com.example.applicationhome.ui.theme.components.MyBottonBar2
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.view.model.BottomBarViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorite(scrollBehavior: TopAppBarScrollBehavior, drawerState : DrawerState, coroutineScope : CoroutineScope, navigationController : NavHostController, viewModelForBottomBar: BottomBarViewModel, viewModel : ItemScreenViewModel){
    var cart = Cart.cartList()
    Scaffold(
        modifier = Modifier.
        fillMaxSize().
        background(Color.White)
    ){
        Box(modifier = Modifier.background(Color.White)){
            Column{
                Box{
                    LazyVerticalGrid (
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ){
                        item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(100.dp))}
                        items(cart) { item ->
                            when (item) {
                                is FoodItem -> {
                                    CartBox(item, navigationController, viewModel)
                                }
                                is Snake -> {
                                    // هنا كوتلن فهم إن العنصر Snake
                                    // تقدر تستخدم: item.name, item.image (الـ Int)
                                    Text(text = "سناك: ${item.name}")
                                    // هنا الـ image نوعها Int (Resource ID)
                                    Image(painter = painterResource(id = item.image), contentDescription = null)
                                }
                            }
                        }
                    }
                    MyTopBar(
                        { navigationController.popBackStack() },
                        {Icon(Icons.Default.ArrowBack,
                            contentDescription = "Back", tint = Color.Black)},
                        {navigationController.navigate(Screens.Search.screen)},
                        Icons.Default.Search,
                        {navigationController.navigate(Screens.Notifications.screen)},
                        Icons.Default.Notifications
                    )

                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                }
            }
            Column(modifier = Modifier.align(Alignment.BottomCenter)){
                Box(modifier = Modifier.fillMaxWidth().height(60.dp).pointerInput(Unit) { detectTapGestures { } }, contentAlignment = Alignment.Center){
                    MyBottonBar2(navigationController, viewModelForBottomBar)
                }
                Box(modifier = Modifier.fillMaxWidth().height(15.dp).pointerInput(Unit) { detectTapGestures { } }, contentAlignment = Alignment.Center){}
            }
        }
    }
}
