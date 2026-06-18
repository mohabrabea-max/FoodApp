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
import com.example.applicationhome.data.models.repository.CartRepository.allCart
import com.example.applicationhome.data.models.repository.CartRepository.cartItems
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuListisLoading
import com.example.applicationhome.data.models.repository.MenuRepository.restaurantOffers
import com.example.applicationhome.data.models.repository.MenuRepository.snacks
import com.example.applicationhome.data.models.repository.MenuRepository.snacksisLoading
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.bars.RestaurantTopBar
import com.example.applicationhome.ui.theme.components.forCart.AlertDialogMessage
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.AddBox
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.CategoriesBarForRestaurantsScreen
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.Favorite
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.ItemsBox
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.RestaurantButton
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.RestaurantHeader
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.RestaurantImageView
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.SnaksBox
import com.example.applicationhome.ui.theme.model.AddBoxViewModel
import com.example.applicationhome.ui.theme.model.BottomBarViewModel
import com.example.applicationhome.ui.theme.model.CategoriesBoxViewModel
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.HomeScreenViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel
import com.example.applicationhome.ui.theme.model.RestaurantViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RestaurantScreen(
    navigationController : NavHostController,
    itemScreenViewModel: ItemScreenViewModel,
    addBoxViewModel: AddBoxViewModel,
    favoriteViewModel : FavoriteViewModel,
    categoriesBoxViewModel: CategoriesBoxViewModel,
    restaurantViewModel : RestaurantViewModel,
    bottomBarViewModel: BottomBarViewModel,
    homeScreenViewModel: HomeScreenViewModel
){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = restaurantViewModel.isNetworkAvailable, key2 = restaurantViewModel.resid) {
        if(restaurantViewModel.isNetworkAvailable && restaurantViewModel.resid != 0){
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
    val layoutInfo = scrollState.layoutInfo
    val itemInfo = layoutInfo.visibleItemsInfo.find { it.key == "categories_header" }

    val menu = categoriesBoxViewModel.filterMenu.values.filter { it.restaurantId == restaurantViewModel.resid }.toList()
    val snacks = snacks.values.toList().filter { it.restaurantId == restaurantViewModel.resid }
    val item = itemScreenViewModel.selectedRestaurant

    if(item != null){
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
                RestaurantTopBar(searchSize, item, scrollState, navigationController, restaurantViewModel, favoriteViewModel)
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
                        RestaurantHeader(item, homeScreenViewModel)
                    }
                    item(span = { GridItemSpan(2) }){
                        Box{
                            LazyRow (
                                modifier = Modifier.fillMaxSize(),
                            ){
                                item{ Spacer(modifier = Modifier.width(15.dp)) }

                                items(restaurantOffers.toList()){ item ->
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
                                if (itemInfo != null) {
                                    if (itemInfo.offset.y < topBarHeightPx) {
                                        translationY = topBarHeightPx - itemInfo.offset.y
                                    }
                                }
                            }.shadow( elevation =
                                if (itemInfo != null){
                                    if (itemInfo.offset.y < topBarHeightPx) 3.dp else 0.dp
                                }else{
                                    0.dp
                                }
                            )
                        ){
                            CategoriesBarForRestaurantsScreen(item.typ, categoriesBoxViewModel)

                        }
                    }
                    if(categoriesBoxViewModel.typeInRestaurantScreen == "Snacks"){
                        items(snacks){ item ->
                            SnaksBox(
                                snacksisLoading,
                                modifier = Modifier.size(200.dp),
                                false,
                                item,
                                null,
                                navigationController,
                                itemScreenViewModel,
                                addBoxViewModel,
                                {
                                    Favorite(
                                        modifier = Modifier.
                                        clip(CircleShape).
                                        border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(30.dp)).
                                        size(35.dp).
                                        background(Color.VeryLightGray),
                                        food = item,
                                        favoriteState = favoriteViewModel
                                    )
                                    AddBox(
                                        color = Color.VeryLightGray,
                                        food = item,
                                        addBoxViewModel
                                    )
                                }
                            )
                        }
                    }else if(categoriesBoxViewModel.typeInRestaurantScreen == "Drink"){
                        println("")
                    }else{
                        items(menu){ item ->
                            ItemsBox(
                                foodMenuListisLoading,
                                item,
                                navigationController,
                                itemScreenViewModel,
                                addBoxViewModel,
                                {
                                    Favorite(
                                        modifier = Modifier.
                                        clip(CircleShape).
                                        size(35.dp),
                                        food = item,
                                        favoriteState = favoriteViewModel
                                    )
                                    AddBox(
                                        color = Color.VeryLightGray,
                                        food = item,
                                        addBoxViewModel
                                    )
                                }
                            )
                        }
                        items(menu){ item ->
                            ItemsBox(
                                foodMenuListisLoading,
                                item,
                                navigationController,
                                itemScreenViewModel,
                                addBoxViewModel,
                                {
                                    Favorite(
                                        modifier = Modifier.
                                        clip(CircleShape).
                                        size(35.dp),
                                        food = item,
                                        favoriteState = favoriteViewModel
                                    )
                                    AddBox(
                                        color = Color.VeryLightGray,
                                        food = item,
                                        addBoxViewModel
                                    )
                                }
                            )
                        }
                    }
                    item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(100.dp))}
                }

                if(allCart.value.restaurantId == restaurantViewModel.resid && cartItems.isNotEmpty()){
                    Column(modifier = Modifier.align(Alignment.BottomCenter)){
                        Box(contentAlignment = Alignment.Center){
                            RestaurantButton(navigationController)
                        }
                    }
                }

                if(addBoxViewModel.errorInCart){
                    AlertDialogMessage(
                        allCart.value.restaurantName,
                        "Start",
                        {
                            addBoxViewModel.alertDialogFalse()
                            addBoxViewModel.clearAndStartNewCart()
                        },
                        "Cancel",
                        {addBoxViewModel.alertDialogFalse()}
                    )
                }

                if(homeScreenViewModel.viewImageState){
                    RestaurantImageView(homeScreenViewModel)
                }
            }
        }
    }
}