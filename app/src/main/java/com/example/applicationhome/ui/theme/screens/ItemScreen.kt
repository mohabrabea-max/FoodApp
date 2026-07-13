package com.example.applicationhome.ui.theme.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.DeepMatteBlack
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.bars.ItemScreenTopBar
import com.example.applicationhome.ui.theme.components.forCart.AlertDialogMessage
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.BottomBarForItemScreen
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.ItemSize
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.SnaksBoxForItemScreen
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.showAddToCartSnackbar
import com.example.applicationhome.ui.theme.components.forItemScreen.ItemScreenImage
import com.example.applicationhome.ui.theme.components.forItemScreen.RatingsAndReviews
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    navigationController : NavHostController,
    itemScreenViewModel : ItemScreenViewModel,
    favoriteViewModel : FavoriteViewModel
){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val userData by itemScreenViewModel.userData.collectAsStateWithLifecycle()

    val cartInformation by itemScreenViewModel.cartInformation.collectAsStateWithLifecycle()

    val newCount = itemScreenViewModel.newCount

    val scrollState = rememberLazyListState()

    val item = itemScreenViewModel.selectedItem
    val size = itemScreenViewModel.selectedSize

    val price = item?.sizeOptions?.find { it.size == itemScreenViewModel.selectedSize }?.price ?: 0.0


    if(item != null){
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
                ItemScreenTopBar(navigationController, scrollState, item, favoriteViewModel)
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
                            ItemScreenImage(scrollState, item.image)
                        }
                    }
                    item {
                        //Spacer(modifier = Modifier.height(20.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth().
                            clip(RoundedCornerShape(20.dp)).
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
                            modifier = Modifier.fillMaxWidth().
                            clip(RoundedCornerShape(20.dp)).
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
                            modifier = Modifier.fillMaxWidth().
                            clip(RoundedCornerShape(20.dp)).
                            background(Color.White).
                            padding(10.dp)
                        ){
                            ItemSize(itemScreenViewModel)
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        RatingsAndReviews(navigationController, item.review)
                    }
                    item{Spacer(modifier = Modifier.height(150.dp))}
                }
            }
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom){
                val price = item.sizeOptions.find { it.size == size }?.price ?: 0.0
                val totalPrice = newCount * price
                val meal = CartItemsClass(
                    userData.id,
                    "${item.mealId}_${size}",
                    item.mealId,
                    item.name,
                    item.type,
                    size,
                    newCount,
                    price,
                    totalPrice,
                    item.image,
                    item.restaurantId
                )
                BottomBarForItemScreen(
                    item,
                    size,
                    newCount,
                    { itemScreenViewModel.minusnewCount() },
                    { itemScreenViewModel.plusnewCount() },
                    {
                        itemScreenViewModel.updateCount(meal, size, newCount)
                        if(item.restaurantId == cartInformation?.restaurantId || cartInformation == null){
                            itemScreenViewModel.deletenewCount()
                            scope.showAddToCartSnackbar(
                                snackbarHostState,
                                {
                                    navigationController.navigate(Screens.Cart.screen){ launchSingleTop = true }
                                }

                            )
                        }
                    }
                )
            }

            if(itemScreenViewModel.errorInCart){
                AlertDialogMessage(
                    cartInformation?.restaurantName ?: "",
                    "Start",
                    {
                        itemScreenViewModel.alertDialogFalse()
                        itemScreenViewModel.clearAndStartNewCart(itemScreenViewModel.newCount)
                        itemScreenViewModel.deletenewCount()
                        scope.showAddToCartSnackbar(
                            snackbarHostState,
                            {
                                navigationController.navigate(Screens.Cart.screen){ launchSingleTop = true }
                            }

                        )
                    },
                    "Cancel",
                    {itemScreenViewModel.alertDialogFalse()}
                )
            }
        }
    }
}