package com.example.applicationhome.features.restaurantscreen.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.core.domain.model.snacksEntityToCartItemsClass
import com.example.applicationhome.core.ui.components.forCart.AlertDialogMessage
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.AddBox
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.Favorite
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.MealBoxIcon
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.RestaurantButton
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.RestaurantImageView
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.core.ui.theme.screens.NoInternetScreen
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RestaurantScreen(
    navigationController : NavHostController,
    restaurantViewModel : RestaurantViewModel
){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var activeId by remember { mutableStateOf(0) }

    val resid by restaurantViewModel.resid.collectAsStateWithLifecycle()


    LaunchedEffect(key1 = resid) {
        restaurantViewModel.selectedtype(0, restaurantViewModel.selectedRestaurant.value?.typ?.first() ?: "")
    }

    val scrollState = rememberLazyListState()

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

    var viewImageState by remember { mutableStateOf(false) }
    var imageToView by remember { mutableStateOf("") }

    val networkState by restaurantViewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val errorInCart by restaurantViewModel.errorInCart.collectAsStateWithLifecycle()

    val userData by restaurantViewModel.userData.collectAsStateWithLifecycle()

    val favoriteMealsIds by restaurantViewModel.favoriteMealsIds.collectAsStateWithLifecycle()
    val favoriteSnacksIds by restaurantViewModel.favoriteSnacksIds.collectAsStateWithLifecycle()
    val favoriteRestaurantsIds by restaurantViewModel.favoriteRestaurantsIds.collectAsStateWithLifecycle()

    val menu by restaurantViewModel.foodMenuList.collectAsStateWithLifecycle()
    val snacks by restaurantViewModel.snackMenuList.collectAsStateWithLifecycle()
    val offers by restaurantViewModel.restaurantOffersMenuList.collectAsStateWithLifecycle()
    val item by restaurantViewModel.selectedRestaurant.collectAsStateWithLifecycle()

    val totalNumber by restaurantViewModel.totalNumber.collectAsStateWithLifecycle()
    val totalPrice by restaurantViewModel.totalPrice.collectAsStateWithLifecycle()

    val cartItems by restaurantViewModel.cartItems.collectAsStateWithLifecycle()
    val cartInformation by restaurantViewModel.cartInformation.collectAsStateWithLifecycle()
    val restaurantId = cartInformation?.restaurantId
    val restaurantName = cartInformation?.restaurantName

    val typeInRestaurantScreen by restaurantViewModel.typeInRestaurantScreen.collectAsStateWithLifecycle()
    val selectedTypeIndex by restaurantViewModel.selectedTypeIndex.collectAsStateWithLifecycle()

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
                RestaurantTopBar(
                    searchSize,
                    item!!,
                    scrollState,
                    navigationController,
                    restaurantViewModel,
                    userData.id
                )
            }
        ){
            Box(modifier = Modifier.background(Color.VeryLightGray)){
                LazyColumn (
                    state = scrollState,
                    modifier = Modifier.fillMaxSize().background(Color.White)
                ){
                    item{
                        RestaurantHeader(
                            item
                        ) {
                            imageToView = item!!.image
                            viewImageState = true
                        }
                    }
                    item{
                        LazyRow (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(Color.White)
                        ){
                            item{ Spacer(modifier = Modifier.width(15.dp)) }

                            items(offers){ item ->
                                AsyncImage(
                                    modifier = Modifier.width(300.dp).padding(vertical = 10.dp).clip(RoundedCornerShape(10.dp)).clickable {  },
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
                    stickyHeader(key = "categories_header"){
                        Box(
                            modifier = Modifier.height(47.dp).
                            fillMaxWidth().
                            graphicsLayer {
                                val currentInfo = itemInfo
                                if (currentInfo != null) {
                                    if (currentInfo.offset < topBarHeightPx) {
                                        translationY = topBarHeightPx - currentInfo.offset
                                    }
                                }
                            }.shadow(
                                elevation =
                                if (itemInfo != null && itemInfo!!.offset < topBarHeightPx){
                                    3.dp
                                }else{
                                    0.dp
                                }
                            )
                        ){
                            CategoriesBarForRestaurantsScreen(
                                item!!.typ,
                                selectedTypeIndex
                            ) { index, size ->
                                restaurantViewModel.selectedtype(index, size)
                            }

                        }
                    }
                    item{Spacer(modifier = Modifier.height(10.dp))}
                    if(typeInRestaurantScreen == "Snacks"){
                        items(snacks){ item ->
                            val isSnackInFavorite = favoriteSnacksIds.contains(item.id)

                            val size = item.priceANDsize.keys.last()
                            val price = item.priceANDsize.values.last()

                            MealsBoxForRestaurantScreen(
                                price,
                                null,
                                item.name,
                                item.image,
                                2.5f,
                                {
                                    restaurantViewModel.selectSnack(item, size)

                                },
                                {
                                    Favorite(
                                        isSnackInFavorite,
                                        {
                                            val favoriteSnacksDatabase =
                                                FavoriteSnackEntity(
                                                    item.id,
                                                    userData.id,
                                                    item.restaurantId,
                                                    false,
                                                    false
                                                )
                                            restaurantViewModel.addSnackFavorite(favoriteSnacksDatabase)
                                        },
                                        { restaurantViewModel.removeSnackFavorite(item.id) },
                                        modifier = Modifier.padding(5.dp).size(40.dp),
                                        color = Color.DarkOrange,
                                        icon1 = Icons.Default.Favorite,
                                        icon2 = Icons.Default.FavoriteBorder
                                    )

                                    AddBox(
                                        item.id,
                                        {
                                            val quantity = cartItems.find { it?.mealKey == "${item.id}_${size}" }?.quantity ?: 0

                                            val snack = item.snacksEntityToCartItemsClass(userData.id, quantity)

                                            restaurantViewModel.plus(snack, size)
                                        },
                                        {
                                            val quantity = cartItems.find { it?.mealKey == "${item.id}_${size}" }?.quantity ?: 0

                                            val snack = item.snacksEntityToCartItemsClass(userData.id, quantity)

                                            restaurantViewModel.minus(snack, size)
                                        },
                                        { activeId = item.id },
                                        activeId,
                                        cartItems.find { it?.mealKey == "${item.id}_${size}" }?.quantity
                                            ?: 0
                                    )
                                }
                            )
                        }
                    }else if(typeInRestaurantScreen == "Drink"){

                    }else{
                        items(menu){ item ->
                            val isMealInFavorite = favoriteMealsIds.contains(item.id)

                            val size = item.sizeOptions.last().size

                            val sizeOptions = item.sizeOptions.find { it.size == "Small" || it.size.contains("Pieces") }
                            val details = sizeOptions?.snack?.values?.map { it.size + " " + it.name }

                                MealsBoxForRestaurantScreen(
                                    sizeOptions?.price ?: 0.0,
                                    details,
                                    item.name,
                                    item.image,
                                    2.2f,
                                    {
                                        restaurantViewModel.selectMeal(item, size)
                                        navigationController.navigate(Screens.ItemScreen.screen)
                                    },
                                    {
                                        Favorite(
                                            isMealInFavorite,
                                            {
                                                val favoriteFoodDatabase =
                                                    FavoriteMealEntity(
                                                        item.id,
                                                        userData.id,
                                                        item.restaurantId,
                                                        false,
                                                        false
                                                    )
                                                restaurantViewModel.addMealFavorite(favoriteFoodDatabase)
                                            },
                                            { restaurantViewModel.removeMealFavorite(item.id) },
                                            modifier = Modifier.padding(5.dp).size(40.dp),
                                            color = Color.DarkOrange,
                                            icon1 = Icons.Default.Favorite,
                                            icon2 = Icons.Default.FavoriteBorder
                                        )

                                        MealBoxIcon(
                                            modifier = Modifier.size(50.dp)
                                        )
                                }
                            )
                        }
                        items(menu){ item ->
                            val isMealInFavorite = favoriteMealsIds.contains(item.id)

                            val size = item.sizeOptions.last().size

                            val sizeOptions = item.sizeOptions.find { it.size == "Small" || it.size.contains("Pieces") }
                            val details = sizeOptions?.snack?.values?.map { it.size + " " + it.name }

                            MealsBoxForRestaurantScreen(
                                sizeOptions?.price ?: 0.0,
                                details,
                                item.name,
                                item.image,
                                2.2f,
                                {
                                    restaurantViewModel.selectMeal(item, size)
                                    navigationController.navigate(Screens.ItemScreen.screen)
                                },
                                {
                                    Favorite(
                                        isMealInFavorite,
                                        {
                                            val favoriteFoodDatabase =
                                                FavoriteMealEntity(
                                                    item.id,
                                                    userData.id,
                                                    item.restaurantId,
                                                    false,
                                                    false
                                                )
                                            restaurantViewModel.addMealFavorite(favoriteFoodDatabase)
                                        },
                                        { restaurantViewModel.removeMealFavorite(item.id) },
                                        modifier = Modifier.padding(5.dp).size(40.dp),
                                        color = Color.DarkOrange,
                                        icon1 = Icons.Default.Favorite,
                                        icon2 = Icons.Default.FavoriteBorder
                                    )

                                    MealBoxIcon(
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                            )
                        }
                    }
                    item{Spacer(modifier = Modifier.height(100.dp))}
                }

                if(restaurantId == resid && cartItems.isNotEmpty()){
                    Column(modifier = Modifier.align(Alignment.BottomCenter)){
                        Box(contentAlignment = Alignment.Center){
                            RestaurantButton(
                                navigationController,
                                totalNumber,
                                totalPrice
                            )
                        }
                    }
                }

                if(errorInCart.first && errorInCart.second.isEmpty()){
                    AlertDialogMessage(
                        "Start a new cart?",
                        "A new order will clear your cart with '${restaurantName ?: ""}'",
                        "Start",
                        {
                            restaurantViewModel.clearAndStartNewCart(1)
                            restaurantViewModel.alertDialogFalse()
                        },
                        "Cancel",
                        { restaurantViewModel.alertDialogFalse() }
                    )
                }else if(errorInCart.first){
                    AlertDialogMessage(
                        "Sign in required!",
                        "Please sign in or create an account to add items to your cart and proceed with your order.",
                        "Sign in",
                        {
                            navigationController.navigate(Screens.LoginScreen.screen)
                            restaurantViewModel.alertDialogFalse()
                        },
                        "Cancel",
                        { restaurantViewModel.alertDialogFalse() }
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
    }else{
        NoInternetScreen(navigationController)
    }
}