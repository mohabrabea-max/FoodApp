package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.R
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.components.CartBox
import com.example.applicationhome.ui.theme.components.CartButton
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.ui.theme.components.PaymentSummary
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.BottomBarViewModel
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
    addBoxViewModel : AddBoxViewModel
){
    var cart = Cart.cartMap()
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
                Color.DarkOrange,
                modifier = Modifier.
                fillMaxWidth().
                height(100.dp).
                shadow(elevation = 5.dp),
                "Cart",
                {
                    IconButton(
                        onClick = {coroutineScope.launch{drawerState.open()}},
                        modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                    ) {
                        Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.White)
                    }
                },
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
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                    }
                }
            )
            Divider(color = Color.LightGray.copy(alpha = 0.5f))
        }
    ){
        Box(modifier = Modifier.background(Color.White)){
            Box(modifier = Modifier.fillMaxSize()){
                if(cart.isNotEmpty()){
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ){
                        item{Spacer(modifier = Modifier.height(100.dp))}
                        items(cart.keys.toList(), key = { it.food.id.toString() + it.size}) { item ->
                            CartBox(item.food, item.size, navigationController, viewModel, addBoxViewModel)
                        }
                        item{
                            PaymentSummary(addBoxViewModel)
                        }
                        item{Spacer(modifier = Modifier.height(150.dp))}
                    }
                }else{
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                        Spacer(modifier = Modifier.height(300.dp))
                        AsyncImage(
                            modifier = Modifier.size(120.dp),
                            model = ImageRequest.Builder(LocalContext.current).
                            data(R.drawable.cartemptyimage).
                            crossfade(true).
                            size(400, 400).
                            precision(Precision.EXACT).
                            build(),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "There's nothing in your cart yet",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(
                            text = "Ready to order?",
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Box(
                            modifier = Modifier.width(100.dp).
                            height(40.dp).
                            clip(CircleShape).
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
                            border(width = 1.dp, color = Color.BrownForFont, shape = RoundedCornerShape(40.dp)).
                            padding(7.dp).align(Alignment.CenterHorizontally)
                        ){
                            Text(
                                text = "Add items",
                                fontSize = 15.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.BrownForFont,
                                modifier = Modifier.align(Alignment.Center)
                            )
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