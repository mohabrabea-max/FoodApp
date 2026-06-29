package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.data.models.model.FoodItemToCalculate
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.bars.FavoriteScreenTopBar
import com.example.applicationhome.ui.theme.components.bars.MyBottonBar
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.AddBox
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.Favorite
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.ItemsBox
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.RestaurantsBox
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.SnaksBox
import com.example.applicationhome.ui.theme.model.BottomBarViewModel
import com.example.applicationhome.ui.theme.model.CartViewModel
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel
import com.example.applicationhome.ui.theme.model.LoginViewModel
import com.example.applicationhome.ui.theme.model.RestaurantViewModel
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorite(
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    navigationController : NavHostController,
    itemScreenViewModel : ItemScreenViewModel,
    cartViewModel : CartViewModel,
    favoriteViewModel : FavoriteViewModel,
    restaurantViewModel: RestaurantViewModel,
    bottomBarViewModel: BottomBarViewModel,
    loginViewModel : LoginViewModel
){
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current as? Activity
    BackHandler(enabled = true) {
        // ده بيمسح الأبلكيشن من الـ Background ويقفله تماماً
        context?.finishAffinity()
    }

    val favoriteMeals = favoriteViewModel.favoriteMeals.collectAsState()
    val favoriteSnacks = favoriteViewModel.favoriteSnacks.collectAsState()
    val favoriteRestaurants = favoriteViewModel.favoriteRestaurantsFromDatabase.collectAsState()

    val count = favoriteViewModel.favoriteFoodCount.collectAsState().value +
            favoriteViewModel.favoriteRestaurantsCount.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(
            modifier = Modifier.navigationBarsPadding().
            fillMaxSize(),
            topBar = { FavoriteScreenTopBar(drawerState, coroutineScope, navigationController, favoriteViewModel) },
            bottomBar = {
                Box(
                    modifier = Modifier.navigationBarsPadding().fillMaxWidth().
                    pointerInput(Unit) { detectTapGestures { } },
                    contentAlignment = Alignment.BottomCenter
                ){
                    MyBottonBar(navigationController, bottomBarViewModel, cartViewModel, favoriteViewModel, loginViewModel)
                }
            }
        ){
            Box(modifier = Modifier.fillMaxSize().background(Color.White)){
                if(count > 0){
                    LazyVerticalGrid (
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ){
                        item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(146.dp))}
                        if(favoriteViewModel.selectedCategorieInFavoriteScreen == 2) {
                            if(favoriteRestaurants.value.isNotEmpty()){
                                items(favoriteRestaurants.value.toList()) { item ->
                                    RestaurantsBox(
                                        false,
                                        item,
                                        favoriteViewModel,
                                        itemScreenViewModel,
                                        navigationController,
                                        restaurantViewModel,
                                        snackBarHostState,
                                        coroutineScope
                                    )
                                }
                            }
                        }
                        if(favoriteViewModel.selectedCategorieInFavoriteScreen == 1) {
                            item(span = { GridItemSpan(2) }) { Spacer(modifier = Modifier.height(15.dp)) }
                            items(favoriteSnacks.value) { item ->
                                val snack = FoodItemToCalculate(
                                    item.mealId,
                                    item.name,
                                    listOf(item.image),
                                    item.size,
                                    item.price,
                                    "Snack",
                                    item.restaurantId,
                                    "ffffffffff sssssssss hhjd ggg",
                                    5.0
                                )
                                SnaksBox(
                                    false,
                                    modifier = Modifier.size(200.dp),
                                    false,
                                    snack,
                                    null,
                                    navigationController,
                                    itemScreenViewModel,
                                    cartViewModel,
                                    {
                                        Favorite(
                                            modifier = Modifier.clip(CircleShape).border(
                                                width = 0.5.dp,
                                                color = Color.Gray.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(30.dp)
                                            ).size(35.dp).background(Color.VeryLightGray),
                                            food = item,
                                            favoriteViewModel = favoriteViewModel
                                        )
                                        AddBox(
                                            loginViewModel,
                                            color = Color.VeryLightGray,
                                            food = snack,
                                            cartViewModel
                                        )
                                    }
                                )
                            }
                        }
                        if(favoriteViewModel.selectedCategorieInFavoriteScreen == 0) {
                            item(span = { GridItemSpan(2) }) { Spacer(modifier = Modifier.height(15.dp)) }
                            items(favoriteMeals.value) { item ->
                                val meal = FoodItemToCalculate(
                                    item.mealId,
                                    item.name,
                                    listOf(item.image),
                                    item.size,
                                    item.price,
                                    "Snack",
                                    item.restaurantId,
                                    "ffffffffff sssssssss hhjd ggg",
                                    5.0
                                )
                                ItemsBox(
                                    false,
                                    meal,
                                    navigationController,
                                    itemScreenViewModel,
                                    cartViewModel,
                                    {
                                        Favorite(
                                            modifier = Modifier.clip(CircleShape).size(35.dp)
                                                .background(Color.White.copy(alpha = 1f)),
                                            food = item,
                                            favoriteViewModel = favoriteViewModel
                                        )
                                        AddBox(loginViewModel, color = Color.White, food = meal, cartViewModel)
                                    }
                                )
                            }
                        }
                        item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(100.dp))}
                    }
                }else{
                    OfflineFavoriteScreen(navigationController, bottomBarViewModel)
                }
            }
        }
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .padding(top = 150.dp)
                .width(300.dp)
        ){ data ->
            Snackbar(
                containerColor = Color(0xFFE53935),
                contentColor = Color.White,
                shape = RoundedCornerShape(15.dp),
                content = {
                    Text(
                        text = data.visuals.message,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
    }
}










@Composable
fun OfflineFavoriteScreen(
    navigationController : NavHostController,
    bottomBarViewModel : BottomBarViewModel
){
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.height(300.dp))
        Image(
            modifier = Modifier.size(120.dp),
            painter = painterResource(R.drawable.favoriteemptyimage),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "There's nothing in your wishlist",
            fontSize = 22.sp,
            style = MaterialTheme.typography.labelLarge,
            color = Color.BrownForFont,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
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
                bottomBarViewModel.home()
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