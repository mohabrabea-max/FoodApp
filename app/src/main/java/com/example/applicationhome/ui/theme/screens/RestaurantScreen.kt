package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.models.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.models.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.bars.RestaurantTopBar
import com.example.applicationhome.ui.theme.components.forCart.AlertDialogMessage
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.AddBox
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.CategoriesBarForRestaurantsScreen
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.Favorite
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.FavoriteSnacks
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.ItemsBox
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.MealBoxIcon
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.RestaurantButton
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.RestaurantHeader
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.RestaurantImageView
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.SnaksBox
import com.example.applicationhome.ui.theme.model.CartViewModel
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.HomeScreenViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel
import com.example.applicationhome.ui.theme.model.LoginViewModel
import com.example.applicationhome.ui.theme.model.RestaurantViewModel
import com.example.applicationhome.ui.theme.model.ViewRestaurantImageViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RestaurantScreen(
    navigationController : NavHostController,
    itemScreenViewModel: ItemScreenViewModel,
    cartViewModel : CartViewModel,
    favoriteViewModel : FavoriteViewModel,
    restaurantViewModel : RestaurantViewModel,
    loginViewModel : LoginViewModel,
    viewRestaurantImageViewModel: ViewRestaurantImageViewModel,
    homeScreenViewModel : HomeScreenViewModel
){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = restaurantViewModel.resid) {

        if (restaurantViewModel.resid != 0) {
            restaurantViewModel.restaurantData()
        }
    }
    val scrollState = rememberLazyGridState()

    val searchSize by remember {
        derivedStateOf {
            if(scrollState.firstVisibleItemIndex >= 1){
                3f
            }else{
                ((scrollState.firstVisibleItemScrollOffset / 300f) - 1f).coerceIn(1f, 3f)
            }
        }
    }
    val topBarHeightPx = with(LocalDensity.current) { 100.dp.toPx() }
    val itemInfo by remember {
        derivedStateOf {
            scrollState.layoutInfo.visibleItemsInfo.find { it.key == "categories_header" }
        }
    }

    val menu by restaurantViewModel.foodMenuList
    val snacks by restaurantViewModel.snackMenuList
    val offers by restaurantViewModel.restaurantOffersMenuList
    val iteme = itemScreenViewModel.selectedRestaurant

    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartInformation by cartViewModel.cartInformation.collectAsState()
    val restaurantId = cartInformation?.restaurantId
    val restaurantName = cartInformation?.restaurantName

    val foodMenuIsLoading by restaurantViewModel.foodMenuListIsLoading.collectAsState()
    val snacksIsLoading by restaurantViewModel.snacksIsLoading.collectAsState()

    if(iteme != null){
        Scaffold(
            modifier = Modifier.navigationBarsPadding().fillMaxSize(),
            snackbarHost = {
//                SnackbarHost(hostState = snackbarHostState){ data ->
//                    Snackbar(
//                        containerColor = Color.DeepMatteBlack,
//                        actionColor = Color.DarkOrange,
//                        snackbarData = data
//                    )
//                }
                //Spacer(modifier = Modifier.height(150.dp))
            },
            topBar = {
                RestaurantTopBar(searchSize, iteme, scrollState, navigationController, restaurantViewModel, favoriteViewModel)
            }
        ){
            Box(modifier = Modifier.background(Color.VeryLightGray)){
                LazyVerticalGrid (
                    state = scrollState,
                    modifier = Modifier.fillMaxSize().
                    background(Color.White),
                    columns = GridCells.Fixed(2)
                ){
                    item(span = { GridItemSpan(2) }){
                        RestaurantHeader(iteme, viewRestaurantImageViewModel)
                    }
                    item(span = { GridItemSpan(2) }){
                        Box{
                            LazyRow (
                                modifier = Modifier.fillMaxSize(),
                            ){
                                item{ Spacer(modifier = Modifier.width(15.dp)) }

                                items(offers){ item ->
                                    AsyncImage(
                                        modifier = Modifier.fillMaxWidth().height(120.dp).padding(vertical = 10.dp).clip(RoundedCornerShape(10.dp)).clickable {  },
                                        model = ImageRequest.Builder(LocalContext.current).
                                        data(item.image).
                                        crossfade(true).
                                        size(400, 400).
                                        precision(Precision.EXACT).
                                        build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                    stickyHeader(key = "categories_header"){
                        Box(
                            modifier = Modifier.height(47.dp).
                            fillMaxWidth().
                            graphicsLayer {
                                val currentInfo = itemInfo
                                if (currentInfo != null) {
                                    if (currentInfo.offset.y < topBarHeightPx) {
                                        translationY = topBarHeightPx - currentInfo.offset.y
                                    }
                                }
                            }.shadow(
                                elevation =
                                if (itemInfo != null && itemInfo!!.offset.y < topBarHeightPx){
                                    3.dp
                                }else{
                                    0.dp
                                }
                            )
                        ){
                            CategoriesBarForRestaurantsScreen(iteme.typ, restaurantViewModel)

                        }
                    }
                    if(restaurantViewModel.typeInRestaurantScreen == "Snacks"){
                        items(snacks){ item ->
                            val databaseMenu = FavoriteSnacksDatabase(
                                favoriteViewModel.userId,
                                item.id,
                                item.name,
                                item.details,
                                item.image.first(),
                                item.priceANDsize,
                                item.restaurantId,
                                item.review,
                                false,
                                false
                            )
                            SnaksBox(
                                snacksIsLoading,
                                modifier = Modifier.size(200.dp),
                                databaseMenu,
                                item.priceANDsize.keys.last(),
                                navigationController,
                                itemScreenViewModel,
                                cartViewModel,
                                {
                                    FavoriteSnacks(
                                        modifier = Modifier.
                                        clip(CircleShape).
                                        border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(30.dp)).
                                        size(35.dp).
                                        background(Color.VeryLightGray),
                                        snack = databaseMenu,
                                        favoriteViewModel = favoriteViewModel
                                    )
                                    AddBox(
                                        loginViewModel,
                                        color = Color.VeryLightGray,
                                        food = databaseMenu,
                                        cartViewModel
                                    )
                                },
                                homeScreenViewModel
                            )
                        }
                    }else if(restaurantViewModel.typeInRestaurantScreen == "Drink"){

                    }else{
                        items(menu){ item ->
                            val databaseMenu = FavoriteFoodDatabase(
                                favoriteViewModel.userId,
                                item.id,
                                item.category,
                                item.name,
                                item.details,
                                item.image.first(),
                                item.sizeOptions,
                                item.restaurantId,
                                item.review,
                                false,
                                false
                            )
                            ItemsBox(
                                foodMenuIsLoading,
                                databaseMenu,
                                navigationController,
                                itemScreenViewModel,
                                cartViewModel,
                                {
                                    Favorite(
                                        modifier = Modifier.
                                        clip(CircleShape).
                                        size(35.dp),
                                        food = databaseMenu,
                                        favoriteViewModel = favoriteViewModel
                                    )
                                    MealBoxIcon()
                                }
                            )
                        }
                        items(menu){ item ->
                            val databaseMenu = FavoriteFoodDatabase(
                                favoriteViewModel.userId,
                                item.id,
                                item.category,
                                item.name,
                                item.details,
                                item.image.first(),
                                item.sizeOptions,
                                item.restaurantId,
                                item.review,
                                false,
                                false
                            )
                            ItemsBox(
                                foodMenuIsLoading,
                                databaseMenu,
                                navigationController,
                                itemScreenViewModel,
                                cartViewModel,
                                {
                                    Favorite(
                                        modifier = Modifier.
                                        clip(CircleShape).
                                        size(35.dp),
                                        food = databaseMenu,
                                        favoriteViewModel = favoriteViewModel
                                    )
                                    MealBoxIcon()
                                }
                            )
                        }
                    }
                    item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(100.dp))}
                }

                if(restaurantId == restaurantViewModel.resid && cartItems.isNotEmpty()){
                    Column(modifier = Modifier.align(Alignment.BottomCenter)){
                        Box(contentAlignment = Alignment.Center){
                            RestaurantButton(navigationController, cartViewModel)
                        }
                    }
                }

                if(cartViewModel.errorInCart){
                    AlertDialogMessage(
                        restaurantName ?: "",
                        "Start",
                        {
                            cartViewModel.clearAndStartNewCart(1)
                            cartViewModel.alertDialogFalse()
                        },
                        "Cancel",
                        {cartViewModel.alertDialogFalse()}
                    )
                }

                if(viewRestaurantImageViewModel.viewImageState){
                    RestaurantImageView(viewRestaurantImageViewModel)
                }
            }
        }
    }else{
        NoInternetScreen(navigationController)
    }
}