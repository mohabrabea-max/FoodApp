package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.DeepMatteBlack
import com.example.applicationhome.ui.theme.components.bars.MyTopBar
import com.example.applicationhome.ui.theme.components.forCart.CartBox
import com.example.applicationhome.ui.theme.components.forCart.CartButton
import com.example.applicationhome.ui.theme.components.forCart.PaymentSummaryCartScreen
import com.example.applicationhome.ui.theme.model.BottomBarViewModel
import com.example.applicationhome.ui.theme.model.CartViewModel
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter",
    "UnrememberedMutableState",
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
    cartViewModel: CartViewModel,
    bottomBarViewModel: BottomBarViewModel,
    favoriteViewModel: FavoriteViewModel
){
    val cartItems by cartViewModel.cartItems.collectAsState()
    Scaffold(
        modifier = Modifier.navigationBarsPadding().
        fillMaxSize(),
        topBar = {
            MyTopBar(
                Color.White,
                modifier = Modifier.
                fillMaxWidth().
                height(100.dp).
                shadow(elevation = 5.dp),
                "Cart",
                Color.DeepMatteBlack,
                {
                    IconButton(
                        onClick = {
                            if (navigationController.previousBackStackEntry != null) { navigationController.popBackStack() }
                            bottomBarViewModel.home()
                        },
                        modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                    ){
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
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
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.DeepMatteBlack)
                    }
                }
            )
            Divider(color = Color.LightGray.copy(alpha = 0.5f))
        }
    ){
        Box(modifier = Modifier.background(Color.White)){
            Box(modifier = Modifier.fillMaxSize()){
                if(cartViewModel.cartItems.collectAsState().value.isNotEmpty()){
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ){
                        item{Spacer(modifier = Modifier.height(100.dp))}

                        items(cartItems) { item ->
                            if(item != null) CartBox(item, navigationController, viewModel, cartViewModel)
                        }
                        item{
                            PaymentSummaryCartScreen(cartViewModel)
                        }
                        item{Spacer(modifier = Modifier.height(100.dp))}
                    }
                }else{
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                        Spacer(modifier = Modifier.height(300.dp))
                        Image(
                            modifier = Modifier.size(120.dp),
                            painter = painterResource(R.drawable.cartemptyimage),
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
                if(cartViewModel.cartItems.collectAsState().value.isNotEmpty()){
                    CartButton(
                        Color.DarkOrange,
                        Color.White,
                        "Checkout",
                        { navigationController.navigate(Screens.ConfirmOrderScreen.screen) }
                    )
                }
            }
        }
    }
}