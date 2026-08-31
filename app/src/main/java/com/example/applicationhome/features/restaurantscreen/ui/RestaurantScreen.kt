package com.example.applicationhome.features.restaurantscreen.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.R
import com.example.applicationhome.core.domain.model.snacksEntityToCartItemsClass
import com.example.applicationhome.core.ui.components.bars.NetworkErrorTopBar
import com.example.applicationhome.core.ui.components.forCart.AlertDialogMessage
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.AddBox
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.Favorite
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.MealBoxIcon
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.RestaurantButton
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.RestaurantImageView
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.bottomSnackBarWithAction
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.AddToCartStates
import com.example.applicationhome.data.data.model.BottomSheetActions
import com.example.applicationhome.data.data.model.CategoryEnum
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.data.model.ShowSnackBarEvent
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.features.itemscreen.ui.ItemsFullBottomSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RestaurantScreen(
    navigationController : NavHostController,
    restaurantViewModel : RestaurantViewModel
){
    val snackBarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val interactionSource = remember { MutableInteractionSource() }

    var activeId by rememberSaveable { mutableStateOf(0) }

    val scrollState = rememberLazyListState()

    val searchSize by remember {
        derivedStateOf {
            if(scrollState.firstVisibleItemIndex >= 1){
                3f
            }else{
                ((scrollState.firstVisibleItemScrollOffset / 150f) - 1f).coerceIn(1f, 3f)
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

    val isNetworkAvailable by restaurantViewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val errorInCart by restaurantViewModel.errorInCart.collectAsStateWithLifecycle()
    val snackBarChannel = restaurantViewModel.snackBarChannel

    val userData by restaurantViewModel.userData.collectAsStateWithLifecycle()

    val menu = restaurantViewModel.foodMenuList.collectAsLazyPagingItems()
    val snacks = restaurantViewModel.snackMenuList.collectAsLazyPagingItems()
    val offers by restaurantViewModel.restaurantOffersMenuList.collectAsStateWithLifecycle()
    val uiState by restaurantViewModel.uiState.collectAsStateWithLifecycle()

    val totalNumber by restaurantViewModel.totalNumber.collectAsStateWithLifecycle()
    val totalPrice by restaurantViewModel.totalPrice.collectAsStateWithLifecycle()

    val newCount by restaurantViewModel.newCount.collectAsStateWithLifecycle()

    val cartItems by restaurantViewModel.cartItems.collectAsStateWithLifecycle()
    val cartInformation by restaurantViewModel.cartInformation.collectAsStateWithLifecycle()
    val restaurantId = cartInformation?.restaurantId

    val typeInRestaurantScreen by restaurantViewModel.typeInRestaurantScreen.collectAsStateWithLifecycle()
    val selectedTypeIndex by restaurantViewModel.selectedTypeIndex.collectAsStateWithLifecycle()

    val mealSize by restaurantViewModel.mealSize.collectAsStateWithLifecycle()


    val animDuration = 300
    val animateIn = remember(uiState.bottomSheetItem) {
        MutableTransitionState(false).apply {
            targetState = uiState.bottomSheetItem != null
        }
    }
    val scope = rememberCoroutineScope()

    val bottomSheetActions =
        BottomSheetActions(
            navigation = { screenItem ->
                if(screenItem == Screens.Search){
                    navigationController.navigate(screenItem.screen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }else{
                    navigationController.navigate(screenItem.screen) {
                        launchSingleTop = true
                    }
                }
            },
            addFavorite = {
                restaurantViewModel.addItemInBottomSheetToFavorite()
            },
            removeFavorite = {
                restaurantViewModel.removeItemInBottomSheetToFavorite()
            },
            selectSize = {
                restaurantViewModel.selectSize(it)
            },
            updateCount = { food , size , newCount ->
                restaurantViewModel.updateCount(
                    food = food,
                    size = size,
                    newCount = newCount,
                    cartNavigation = {
                        navigationController.navigate(Screens.Cart.screen){ launchSingleTop = true }
                    },
                    onCloseItemScreen = {
                        scope.launch {
                            animateIn.targetState = false
                            delay(animDuration.toLong().milliseconds)
                            restaurantViewModel.closeItemScreen()
                        }
                    }
                )
            },
            clearAndStartNewCart = {
                restaurantViewModel.clearAndStartNewCart(
                    count = it,
                    cartNavigation = { navigationController.navigate(Screens.Cart.screen){ launchSingleTop = true } },
                    onCloseItemScreen = {
                        scope.launch {
                            animateIn.targetState = false
                            delay(animDuration.toLong().milliseconds)
                            restaurantViewModel.closeItemScreen()
                        }
                    }
                )
            },
            minusnewCount = { restaurantViewModel.minusnewCount() },
            plusnewCount = { restaurantViewModel.plusnewCount() },
            deletenewCount = { restaurantViewModel.deletenewCount() },
            alertDialogFalse = { restaurantViewModel.alertDialogFalse() },
            closeBottomSheet = {
                scope.launch {
                    animateIn.targetState = false
                    delay(animDuration.toLong().milliseconds)
                    restaurantViewModel.closeItemScreen()
                }
            }
        )


    Scaffold(
        modifier = Modifier.navigationBarsPadding().fillMaxSize(),

        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    Snackbar(
                        modifier = Modifier.padding(12.dp).height(50.dp).clip(RoundedCornerShape(10.dp)),
                        containerColor = MaterialTheme.colorScheme.onSurface,
                        contentColor = Color.DarkOrange
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(text = data.visuals.message, color = MaterialTheme.colorScheme.surface)

                            Box(
                                modifier = Modifier.clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ){
                                    data.performAction()
                                }
                            ){
                                Text(text = data.visuals.actionLabel.toString())
                            }
                        }
                    }
                }
            )
        },

        topBar = {
            RestaurantTopBar(
                searchSize = searchSize,
                item = uiState.restaurantData.restaurant,
                isRestaurantInFavorite = uiState.restaurantData.isFavorite,
                scrollState = scrollState,
                addToFavorite = {
                    val restaurantsEntity = FavoriteRestaurantEntity(
                        uiState.restaurantData.restaurant.id,
                        userData.id,
                        false,
                        false
                    )
                    restaurantViewModel.addRestaurantsFavorite(restaurantsEntity)
                },
                removeFromFavorite = { restaurantViewModel.removeRestaurantsFavorite(uiState.restaurantData.restaurant.id) },
                popBackStack = {
                    if (navigationController.previousBackStackEntry != null) {
                        navigationController.popBackStack()
                    }
                },
                navigation = {
                    navigationController.navigate(Screens.Search.screen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },

        bottomBar = {
            if(restaurantId == uiState.restaurantData.restaurant.id && cartItems.isNotEmpty()){
                RestaurantButton(
                    totalNumber,
                    totalPrice
                ){
                    navigationController.navigate(Screens.Cart.screen){ launchSingleTop = true }
                }
            }
        }
    ){

        LazyColumn (
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ){
            item {
                RestaurantHeader(
                    uiState.restaurantData
                ){
                    imageToView = uiState.restaurantData.restaurant.image
                    viewImageState = true
                }
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    item { Spacer(modifier = Modifier.width(15.dp)) }

                    items(offers) { item ->
                        AsyncImage(
                            modifier = Modifier.width(300.dp).padding(vertical = 10.dp)
                                .clip(RoundedCornerShape(10.dp)).clickable { },
                            model = ImageRequest.Builder(LocalContext.current).data(item.image)
                                .crossfade(true).size(400, 400).precision(Precision.EXACT).build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            stickyHeader(key = "categories_header") {
                Box(
                    modifier = Modifier.height(47.dp).fillMaxWidth().graphicsLayer {
                        val currentInfo = itemInfo
                        if (currentInfo != null) {
                            if (currentInfo.offset < topBarHeightPx) {
                                translationY = topBarHeightPx - currentInfo.offset
                            }
                        }
                    }.shadow(
                        elevation =
                            if (itemInfo != null && itemInfo!!.offset < topBarHeightPx) {
                                3.dp
                            } else {
                                0.dp
                            }
                    )
                ) {
                    CategoriesBarForRestaurantsScreen(
                        uiState.restaurantData.restaurant.typ.sortedBy { it.index },
                        selectedTypeIndex
                    ) { index, category ->
                        restaurantViewModel.selectedtype(index, category)
                    }

                }
            }

            item {  NetworkErrorTopBar(isNetworkAvailable = isNetworkAvailable) }

            item { Spacer(modifier = Modifier.height(10.dp)) }

            when(typeInRestaurantScreen.category){

                CategoryEnum.SNACKS.name -> {
                    items(
                        count = snacks.itemCount,
                        key = snacks.itemKey { it.snack.id }
                    ){ index ->
                        val item = snacks[index]

                        item?.let {
                            val isSnackInFavorite = item.isFavorite

                            val size = item.snack.priceANDsize.keys.last()
                            val price = item.snack.priceANDsize.values.last()

                            MealsBoxForRestaurantScreen(
                                price,
                                null,
                                item.snack.name,
                                item.snack.image,
                                2.5f,
                                {
                                    restaurantViewModel.selectSnack(item.snack.id, size)
                                },
                                {
                                    Favorite(
                                        isSnackInFavorite,
                                        {
                                            val favoriteSnacksDatabase =
                                                FavoriteSnackEntity(
                                                    item.snack.id,
                                                    userData.id,
                                                    item.snack.restaurantId,
                                                    false,
                                                    false
                                                )
                                            restaurantViewModel.addSnackFavorite(favoriteSnacksDatabase)
                                        },
                                        { restaurantViewModel.removeSnackFavorite(item.snack.id) },
                                        modifier = Modifier.padding(5.dp).size(40.dp),
                                        color = Color.DarkOrange,
                                        icon1 = Icons.Default.Favorite,
                                        icon2 = Icons.Default.FavoriteBorder
                                    )

                                    AddBox(
                                        item.snack.id,
                                        {
                                            val quantity = cartItems.find { it.mealKey == "${item.snack.id}_${size}" }?.quantity ?: 0

                                            val snack = item.snack.snacksEntityToCartItemsClass(userData.id, quantity)

                                            restaurantViewModel.plus(snack, size){
                                                navigationController.navigate(Screens.Cart.screen){ launchSingleTop = true }
                                            }
                                        },
                                        {
                                            val quantity = cartItems.find { it.mealKey == "${item.snack.id}_${size}" }?.quantity ?: 0

                                            val snack = item.snack.snacksEntityToCartItemsClass(userData.id, quantity)

                                            restaurantViewModel.minus(snack, size)
                                        },
                                        { activeId = item.snack.id },
                                        activeId,
                                        cartItems.find { it.mealKey == "${item.snack.id}_${size}" }?.quantity
                                            ?: 0
                                    )
                                }
                            )
                        }
                    }
                }

                CategoryEnum.DRINK.name -> {  }

                else -> {
                    items(
                        count = menu.itemCount
                    ){ index ->
                        val item = menu[index]

                        item?.let {
                            val isMealInFavorite = item.isFavorite

                            val size = item.meal.sizeOptions.last().size

                            val sizeOptions = item.meal.sizeOptions.find { it.size == "Small" || it.size.contains("Pieces") }
                            val details = sizeOptions?.snack?.values?.map { it.size + " " + it.name }

                            MealsBoxForRestaurantScreen(
                                sizeOptions?.price ?: 0.0,
                                details,
                                item.meal.name,
                                item.meal.image,
                                2.2f,
                                { restaurantViewModel.selectMeal(item.meal.id, size) },
                                {
                                    Favorite(
                                        isMealInFavorite,
                                        {
                                            val favoriteFoodDatabase =
                                                FavoriteMealEntity(
                                                    item.meal.id,
                                                    userData.id,
                                                    item.meal.restaurantId,
                                                    false,
                                                    false
                                                )
                                            restaurantViewModel.addMealFavorite(favoriteFoodDatabase)
                                        },
                                        { restaurantViewModel.removeMealFavorite(item.meal.id) },
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

                    items(
                        count = menu.itemCount
                    ){ index ->
                        val item = menu[index]

                        item?.let {
                            val isMealInFavorite = item.isFavorite

                            val size = item.meal.sizeOptions.last().size

                            val sizeOptions = item.meal.sizeOptions.find { it.size == "Small" || it.size.contains("Pieces") }
                            val details = sizeOptions?.snack?.values?.map { it.size + " " + it.name }

                            MealsBoxForRestaurantScreen(
                                sizeOptions?.price ?: 0.0,
                                details,
                                item.meal.name,
                                item.meal.image,
                                2.2f,
                                { restaurantViewModel.selectMeal(item.meal.id, size) },
                                {
                                    Favorite(
                                        isMealInFavorite,
                                        {
                                            val favoriteFoodDatabase =
                                                FavoriteMealEntity(
                                                    item.meal.id,
                                                    userData.id,
                                                    item.meal.restaurantId,
                                                    false,
                                                    false
                                                )
                                            restaurantViewModel.addMealFavorite(favoriteFoodDatabase)
                                        },
                                        { restaurantViewModel.removeMealFavorite(item.meal.id) },
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
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }


        when(errorInCart){
            is AddToCartStates.ErrorInCartRestaurant -> {
                AlertDialogMessage(
                    title = stringResource((errorInCart as AddToCartStates.ErrorInCartRestaurant).title),
                    content = stringResource((errorInCart as AddToCartStates.ErrorInCartRestaurant).message) +
                            (errorInCart as AddToCartStates.ErrorInCartRestaurant).restaurantName,
                    confirmButtonText = stringResource(R.string.start),
                    confirmButton = {
                        restaurantViewModel.clearAndStartNewCart(
                            count = newCount,
                            cartNavigation = { navigationController.navigate(Screens.Cart.screen){ launchSingleTop = true } },
                            onCloseItemScreen = {
                                scope.launch {
                                    animateIn.targetState = false
                                    delay(animDuration.toLong().milliseconds)
                                    restaurantViewModel.closeItemScreen()
                                }
                            }
                        )
                        restaurantViewModel.alertDialogFalse()
                    },
                    dismissButtonText = stringResource(R.string.cancel),
                    dismissButton = { restaurantViewModel.alertDialogFalse() }
                )
            }

            is AddToCartStates.ErrorInLoginState -> {
                AlertDialogMessage(
                    title = stringResource((errorInCart as AddToCartStates.ErrorInLoginState).title),
                    content = stringResource((errorInCart as AddToCartStates.ErrorInLoginState).message),
                    confirmButtonText = stringResource(R.string.sign_in),
                    confirmButton = {
                        navigationController.navigate(Screens.LoginScreen.screen)
                        restaurantViewModel.alertDialogFalse()
                    },
                    dismissButtonText = stringResource(R.string.cancel),
                    dismissButton = { restaurantViewModel.alertDialogFalse() }
                )
            }

            else -> {}
        }

        LaunchedEffect(snackBarChannel){
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                snackBarChannel.collect { item ->
                    when(item){
                        is ShowSnackBarEvent.AddedToCart -> {
                            launch {
                                snackBarHostState.bottomSnackBarWithAction(
                                    message = item.message,
                                    actionLabel = item.actionLabel,
                                    onActionClicked = { item.action() }
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }
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

    uiState.bottomSheetItem?.let { item ->
        ItemsFullBottomSheet(
            bottomSheetItem = item,
            size = mealSize,
            actions = bottomSheetActions,
            userData = userData,
            newCount = newCount,
            animDuration = animDuration,
            animateIn = animateIn
        )
    }
}