package com.example.applicationhome.features.favorite.ui

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
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.core.domain.model.snacksEntityToCartItemsClass
import com.example.applicationhome.core.ui.components.forCart.AlertDialogMessage
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.AddBox
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.Favorite
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.MealBoxIcon
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.RestaurantImageView
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.SnaksBox
import com.example.applicationhome.core.ui.theme.BrownForFont
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.Screens
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorite(
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    navigationController : NavHostController,
    favoriteViewModel : FavoriteViewModel,
    favoriteListState : LazyGridState
){
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current as? Activity
    BackHandler(enabled = true) {
        // ده بيمسح الأبلكيشن من الـ Background ويقفله تماماً
        context?.finishAffinity()
    }

    var viewImageState by remember { mutableStateOf(false) }
    var imageToView by remember { mutableStateOf("") }

    val networkState by favoriteViewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    var activeId by remember { mutableStateOf(0) }

    val userData by favoriteViewModel.userData.collectAsStateWithLifecycle()

    val selectedCategorieInFavoriteScreen by favoriteViewModel.selectedCategorieInFavoriteScreen.collectAsStateWithLifecycle()

    val cartItems by favoriteViewModel.cartItems.collectAsStateWithLifecycle()

    val errorInCart by favoriteViewModel.errorInCart.collectAsStateWithLifecycle()
    val cartInformation by favoriteViewModel.cartInformation.collectAsStateWithLifecycle()
    val restaurantName = cartInformation?.restaurantName

    val favoriteMeals by favoriteViewModel.favoriteMeals.collectAsStateWithLifecycle()
    val favoriteSnacks by favoriteViewModel.favoriteSnacks.collectAsStateWithLifecycle()
    val favoriteRestaurants by favoriteViewModel.favoriteRestaurantsFromDatabase.collectAsStateWithLifecycle()

    val favoriteFoodCount by favoriteViewModel.favoriteFoodCount.collectAsStateWithLifecycle()
    val favoriteRestaurantsCount by favoriteViewModel.favoriteRestaurantsCount.collectAsStateWithLifecycle()
    val favoriteSnacksCount by favoriteViewModel.favoriteSnacksCount.collectAsStateWithLifecycle()
    val count = favoriteFoodCount + favoriteRestaurantsCount + favoriteSnacksCount

    val selectedCategoryInFavoriteScreen by favoriteViewModel.selectedCategorieInFavoriteScreen.collectAsStateWithLifecycle()


    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(
            modifier = Modifier.navigationBarsPadding().
            fillMaxSize(),
            containerColor = Color.White,
            topBar = {
                FavoriteScreenTopBar(
                    drawerState,
                    coroutineScope,
                    navigationController,
                    selectedCategoryInFavoriteScreen,
                    { item -> favoriteViewModel.selectedFavoriteScreen(item) }
                )
            }
        ){
            if(count > 0){
                LazyVerticalGrid (
                    state = favoriteListState,
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ){
                    item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(146.dp))}
                    if(selectedCategorieInFavoriteScreen == 2 && favoriteRestaurants.isNotEmpty()) {
                        items(favoriteRestaurants) { item ->
                            val isRestaurantInFavorite = item.isFavorite

                            RestaurantsBox(
                                false,
                                item.restaurant,
                                isRestaurantInFavorite,
                                {
                                    imageToView = item.restaurant.image
                                    viewImageState = true
                                },
                                {
                                    if (networkState) {
                                        navigationController.navigate(Screens.RestaurantScreen.createRoute(restaurantId = item.restaurant.id))
                                    }else{
                                        navigationController.navigate(Screens.NoInternetScreen.screen)
                                    }
                                },
                                {
                                    val favRestaurant = item.favoriteInfo
                                    if(favRestaurant!= null)
                                        favoriteViewModel.addRestaurantsFavorite(favRestaurant)
                                },
                                { favoriteViewModel.removeRestaurantsFavorite(item.restaurant.id) }
                            )
                            }
                    }
                    if(selectedCategorieInFavoriteScreen == 1) {
                        item(span = { GridItemSpan(2) }) { Spacer(modifier = Modifier.height(15.dp)) }
                        items(favoriteSnacks) { item ->
                            val isSnackInFavorite = item.isFavorite

                            val size = item.snack.priceANDsize.keys.last()
                            val price = item.snack.priceANDsize.values.last()

                            SnaksBox(
                                modifier = Modifier.size(200.dp),
                                item.snack.name,
                                item.snack.image,
                                item.snack.priceANDsize[size],
                                {
                                    if (networkState) {
                                        navigationController.navigate(Screens.RestaurantScreen.createRouteWithSnack(restaurantId = item.snack.restaurantId, snackId = item.snack.id))
                                        favoriteViewModel.deletenewCount()
                                    } else {
                                        navigationController.navigate(Screens.NoInternetScreen.screen)
                                    }
                                },
                                {
                                    Favorite(
                                        isSnackInFavorite,
                                        {
                                            val favSnack = item.favoriteInfo
                                            if(favSnack!= null)
                                                favoriteViewModel.addSnackFavorite(favSnack)
                                        },
                                        { favoriteViewModel.removeSnackFavorite(item.snack.id) },
                                        modifier = Modifier.padding(5.dp).clip(CircleShape).border(
                                            width = 0.5.dp,
                                            color = Color.Gray.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(30.dp)
                                        ).size(35.dp).background(Color.VeryLightGray),
                                        color = Color.DarkOrange,
                                        icon1 = Icons.Default.Favorite,
                                        icon2 = Icons.Default.FavoriteBorder
                                    )
                                    AddBox(
                                        item.snack.id,
                                        {
                                            val quantity = cartItems.find { it?.mealKey == "${item.snack.id}_${size}" }?.quantity ?: 0

                                            val snack = item.snack.snacksEntityToCartItemsClass(userData.id, quantity)

                                            favoriteViewModel.plus(snack, size)
                                        },
                                        {
                                            val quantity = cartItems.find { it?.mealKey == "${item.snack.id}_${size}" }?.quantity ?: 0

                                            val snack = item.snack.snacksEntityToCartItemsClass(userData.id, quantity)

                                            favoriteViewModel.minus(snack, size)
                                        },
                                        { activeId = item.snack.id },
                                        activeId,
                                        cartItems.find { it?.mealKey == "${item.snack.id}_${size}" }?.quantity ?: 0
                                    )
                                }
                            )
                        }
                    }
                    if(selectedCategorieInFavoriteScreen == 0) {
                        item(span = { GridItemSpan(2) }) { Spacer(modifier = Modifier.height(15.dp)) }
                        items(favoriteMeals) { item ->
                            val isMealInFavorite = item.isFavorite

                            val size = item.meal.sizeOptions.last().size

                            MealsBoxForFavoriteScreen(
                                false,
                                item.meal.name,
                                item.meal.image,
                                item.meal.sizeOptions.find { it.size == "Small" || it.size.contains("Pieces") },
                                {
                                    if (networkState) {
                                        navigationController.navigate(Screens.RestaurantScreen.createRouteWithMeal(restaurantId = item.meal.restaurantId, mealId = item.meal.id))
                                        favoriteViewModel.deletenewCount()
                                    }else{
                                        navigationController.navigate(Screens.NoInternetScreen.screen)
                                    }
                                },
                                {
                                    Favorite(
                                        isMealInFavorite,
                                        {
                                            val favMeal = item.favoriteInfo
                                            if(favMeal!= null)
                                            favoriteViewModel.addMealFavorite(favMeal)
                                        },
                                        { favoriteViewModel.removeMealFavorite(item.meal.id) },
                                        modifier = Modifier.padding(5.dp).clip(CircleShape)
                                            .size(35.dp),
                                        color = Color.DarkOrange,
                                        icon1 = Icons.Default.Favorite,
                                        icon2 = Icons.Default.FavoriteBorder
                                    )
                                    MealBoxIcon()
                                }
                            )
                        }
                    }
                    item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(100.dp))}
                }
            }else{
                EmptyFavoriteScreen(navigationController)
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


        if(errorInCart.first && errorInCart.second == "Error In Restaurant"){
            AlertDialogMessage(
                "Start a new cart?",
                "A new order will clear your cart with '${restaurantName ?: ""}'",
                "Start",
                {
                    favoriteViewModel.clearAndStartNewCart(1)
                    favoriteViewModel.alertDialogFalse()
                },
                "Cancel",
                { favoriteViewModel.alertDialogFalse() }
            )
        }else if(errorInCart.first && errorInCart.second == "User Id Is Empty"){
            AlertDialogMessage(
                "Sign in required!",
                "Please sign in or create an account to add items to your cart and proceed with your order.",
                "Sign in",
                {
                    navigationController.navigate(Screens.LoginScreen.screen)
                    favoriteViewModel.alertDialogFalse()
                },
                "Cancel",
                { favoriteViewModel.alertDialogFalse() }
            )
        }


        if(viewImageState){
            RestaurantImageView(
                imageToView
            ) {
                imageToView = ""
                viewImageState = false
            }
        }
    }
}







@Composable
fun EmptyFavoriteScreen(
    navigationController : NavHostController
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