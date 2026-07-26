package com.example.applicationhome.features.itemscreen.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.core.ui.components.forCart.AlertDialogMessage
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.showAddToCartSnackbar
import com.example.applicationhome.core.ui.theme.BrownForFont
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.DeepMatteBlack
import com.example.applicationhome.core.ui.theme.MediumBrownForTitle
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.FavoriteMealEntity

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    navigationController : NavHostController,
    itemScreenViewModel : ItemScreenViewModel
){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val errorInCart by itemScreenViewModel.errorInCart.collectAsStateWithLifecycle()

    val userData by itemScreenViewModel.userData.collectAsStateWithLifecycle()

    val cartInformation by itemScreenViewModel.cartInformation.collectAsStateWithLifecycle()

    val newCount by itemScreenViewModel.newCount.collectAsStateWithLifecycle()

    val favoriteMealsIds by itemScreenViewModel.favoriteMealsIds.collectAsStateWithLifecycle()

    val scrollState = rememberLazyListState()

    val item by itemScreenViewModel.selectedMeal.collectAsStateWithLifecycle()
    val size by itemScreenViewModel.mealSize.collectAsStateWithLifecycle()

    val price = item.sizeOptions.find { it.size == size }?.price ?: 0.0


    Scaffold(
        modifier = Modifier.navigationBarsPadding().fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState){ data ->
                Snackbar(
                    containerColor = Color.DeepMatteBlack,
                    actionColor = Color.DarkOrange,
                    snackbarData = data,
                    shape = RoundedCornerShape(15.dp)
                )
            }
            Spacer(modifier = Modifier.height(160.dp))
        },
        topBar = {
            ItemScreenTopBar(
                scrollState,
                favoriteMealsIds.contains(item.id),
                {
                    if (navigationController.previousBackStackEntry != null) {
                        navigationController.popBackStack()
                    }
                },
                {
                    navigationController.navigate(Screens.Search.screen) {
                        popUpTo(navigationController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                {
                    val favoriteFoodDatabase =
                        FavoriteMealEntity(
                            item.id,
                            userData.id,
                            item.restaurantId,
                            false,
                            false
                        )
                    itemScreenViewModel.addMealFavorite(favoriteFoodDatabase)
                },
                { itemScreenViewModel.removeMealFavorite(item.id) }
            )
        }
    ){
        Box(modifier = Modifier.background(Color.VeryLightGray).padding(10.dp)){
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                state = scrollState,
                modifier = Modifier.fillMaxSize()
            ){
                item{
                    Column{
                        Spacer(modifier = Modifier.height(50.dp))
                        ItemScreenImage(
                            scrollState,
                            item.image
                        )
                    }
                }
                item {
                    //Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier.
                        shadow(elevation = 10.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(20.dp)).
                        fillMaxWidth().
                        background(Color.White).
                        padding(15.dp)
                    ){
                        Text(
                            text = item.name,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = item.details,
                            color = Color.MediumBrownForTitle
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "$price L.E",
                            fontSize = 30.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 15.dp, bottom = 15.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier.
                        shadow(elevation = 10.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(20.dp)).
                        fillMaxWidth().
                        background(Color.White)
                    ){
                        Column{
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = "Meal snacks",
                                fontSize = 16.sp,
                                color = Color.Black,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 15.dp)
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            LazyRow {
                                item{Spacer(modifier = Modifier.width(7.dp))}
                                    item{
                                        val selectedDetail = item.sizeOptions.find { it.size == size }
                                        selectedDetail?.snack?.forEach { (snakeId, value) ->
                                            SnaksBoxForItemScreen(
                                                modifier = Modifier.size(170.dp),
                                                value
                                            )
                                        }
                                    }
                                item{Spacer(modifier = Modifier.width(7.dp))}
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier.
                        shadow(elevation = 10.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(20.dp)).
                        fillMaxWidth().
                        background(Color.White).
                        padding(10.dp)
                    ){
                        ItemSize(
                            item.sizeOptions,
                            size
                        ) { selectedSize ->
                            itemScreenViewModel.selectItem(item, selectedSize)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    RatingsAndReviews(
                        item.review
                    )
                }
                item{Spacer(modifier = Modifier.height(150.dp))}
            }
        }
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom){
            val price = item.sizeOptions.find { it.size == size }?.price ?: 0.0
            val totalPrice = newCount * price
            BottomBarForItemScreen(
                price,
                newCount,
                { itemScreenViewModel.minusnewCount() },
                { itemScreenViewModel.plusnewCount() },
                {
                    val meal = CartItemsClass(
                        userData.id,
                        "${item.id}_${size}",
                        item.id,
                        item.name,
                        item.category,
                        size,
                        newCount,
                        price,
                        totalPrice,
                        item.image,
                        item.restaurantId
                    )

                    itemScreenViewModel.updateCount(
                        meal,
                        size,
                        newCount,
                        cartError = {
                            if ((item.restaurantId == cartInformation?.restaurantId || cartInformation == null)){
                                itemScreenViewModel.deletenewCount()
                                scope.showAddToCartSnackbar(
                                    snackbarHostState,
                                    {
                                        navigationController.navigate(Screens.Cart.screen) {
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                        }
                    )
                }
            )
        }

        if(errorInCart.first && errorInCart.second.isEmpty()){
            AlertDialogMessage(
                "Start a new cart?",
                "A new order will clear your cart with '${cartInformation?.restaurantName ?: ""}'",
                "Start",
                {
                    itemScreenViewModel.alertDialogFalse()
                    itemScreenViewModel.clearAndStartNewCart(newCount)
                    itemScreenViewModel.deletenewCount()
                    scope.showAddToCartSnackbar(
                        snackbarHostState,
                        {
                            navigationController.navigate(Screens.Cart.screen) {
                                launchSingleTop = true
                            }
                        }

                    )
                },
                "Cancel",
                { itemScreenViewModel.alertDialogFalse() }
            )
        }else if(errorInCart.first){
            AlertDialogMessage(
                "Sign in required!",
                "Please sign in or create an account to add items to your cart and proceed with your order.",
                "Sign in",
                {
                    navigationController.navigate(Screens.LoginScreen.screen)
                    itemScreenViewModel.alertDialogFalse()
                },
                "Cancel",
                { itemScreenViewModel.alertDialogFalse() }
            )
        }
    }
}