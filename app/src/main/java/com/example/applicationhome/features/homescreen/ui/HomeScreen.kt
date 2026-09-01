package com.example.applicationhome.features.homescreen.ui

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.StartingBottomSheet
import com.example.applicationhome.core.ui.components.bars.NetworkErrorTopBar
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.RestaurantImageView
import com.example.applicationhome.core.ui.theme.LightOrange
import com.example.applicationhome.data.data.model.HomeScreenActions
import com.example.applicationhome.data.data.model.HomeScreenParameters
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.data.model.StartBottomSheets
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.features.shimmers.screens.HomeScreenShimmer
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    navigationController : NavHostController,
    onActions : HomeScreenActions,
    parameters : HomeScreenParameters,
    scrollState : LazyListState,
    syncDataUiState : HomeUiState,
    startBottomSheets : StartBottomSheets,
    isRefreshing : Boolean,
    onRefresh : () -> Unit
){
    val state = rememberPullToRefreshState()

    val isOnline = parameters.isNetworkAvailable || syncDataUiState != HomeUiState.Offline

    val pagerState = rememberPagerState(pageCount = {parameters.offers.size})

    var viewImageState by remember { mutableStateOf(false) }
    var imageToView by remember { mutableStateOf("") }

    val context = LocalContext.current as? Activity
    BackHandler(enabled = true) { context?.finishAffinity() } // ده بيمسح الأبلكيشن من الـ Background ويقفله تماماً



    PullToRefreshBox(
        isRefreshing = isRefreshing,

        onRefresh = { onRefresh() },

        modifier = Modifier.fillMaxSize(),

        state = state,

        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = MaterialTheme.colorScheme.surface,
                color = MaterialTheme.colorScheme.primary,
                state = state
            )
        },

        contentAlignment = Alignment.Center
    ){
        Scaffold(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize(),

            topBar = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    HomeScreenTopBar(
                        scrollState,
                        drawerState,
                        coroutineScope,
                        navigationController
                    )

                    NetworkErrorTopBar(isNetworkAvailable = isOnline)
                }
            }
        ){

            LazyColumn (
                state = scrollState,
                modifier = Modifier.fillMaxSize()
            ){
                item {
                    Box(
                        modifier = Modifier
                            .height(170.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.BottomCenter
                    ){
                        Text(
                            text = stringResource(R.string.what_would_you_like_to_eat),
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }

                item {
                    Box(
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(25.dp)
                                .clip(
                                    shape = RoundedCornerShape(
                                        bottomStart = 40.dp,
                                        bottomEnd = 40.dp
                                    )
                                )
                                .background(MaterialTheme.colorScheme.primary).align(Alignment.TopCenter)
                        )
                        SearchBox {
                            navigationController.navigate(Screens.Search.screen)
                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(16.dp).background(MaterialTheme.colorScheme.surface)
                    )
                }

                when (syncDataUiState) {
                    HomeUiState.Success, HomeUiState.Offline -> {
                        item {
                            CategoriesBar(
                                categories = parameters.categories,
                                selected = parameters.categorySelected,
                                select = { category -> onActions.select(category) },
                                unSelect = { onActions.unSelected() }
                            )
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }

                        item {
                            Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(horizontal = 32.dp),
                                    pageSpacing = 10.dp
                                ) { page ->
                                    val currentOffer = parameters.offers[page]
                                    Box(
                                        modifier = Modifier.fillMaxSize()
                                            .clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.surface)
                                            .clickable { }
                                    ){
                                        AsyncImage(
                                            modifier = Modifier.fillMaxSize(),
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(currentOffer.image).crossfade(true)
                                                .size(400, 400).precision(Precision.EXACT).build(),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }

                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                            Divider(
                                color = Color.LightOrange.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        if(parameters.restaurants != null) items(
                            count = parameters.restaurants.itemCount,
                            key = parameters.restaurants.itemKey { it.restaurant.id }
                        ) { index ->
                            val item = parameters.restaurants[index]

                            item?.let {
                                RestaurantsBoxHomeScreen(
                                    item,
                                    item.isFavorite,
                                    {
                                        imageToView = item.restaurant.image
                                        viewImageState = true
                                    },
                                    {
                                        navigationController.navigate(
                                            Screens.RestaurantScreen.createRoute(restaurantId = item.restaurant.id)
                                        )
                                    },
                                    {
                                        val favoriteRestaurantDatabase = FavoriteRestaurantEntity(
                                            item.restaurant.id,
                                            parameters.userData.id,
                                            false,
                                            false
                                        )
                                        onActions.addRestaurantsFavorite(
                                            favoriteRestaurantDatabase
                                        )
                                    },
                                    { onActions.removeRestaurantsFavorite(item.restaurant.id) }
                                )
                            }
                        }

                        item { Spacer(modifier = Modifier.height(95.dp)) }
                    }

                    HomeUiState.Loading -> {
                        item { HomeScreenShimmer() }

                        item { Spacer(modifier = Modifier.height(95.dp)) }
                    }
                }
            }

            if(viewImageState){
                RestaurantImageView(
                    imageToView,
                    {
                        imageToView = ""
                        viewImageState = false
                    }
                )
            }

            when(startBottomSheets){
                StartBottomSheets.None -> {}

                is StartBottomSheets.LoginBottomSheet -> {
                    StartingBottomSheet(
                        title = stringResource(startBottomSheets.title),
                        message = stringResource(startBottomSheets.message),
                        buttonTitle = stringResource(R.string.login),
                        onClickable = {
                            onActions.closeBottomSheet()
                            navigationController.navigate(Screens.LoginScreen.screen){ launchSingleTop = true }
                        },
                        onDismissRequest = { onActions.closeBottomSheet() }
                    )
                }

                is StartBottomSheets.OrdersBottomSheet -> {
                    StartingBottomSheet(
                        title = stringResource(startBottomSheets.title),
                        message = stringResource(startBottomSheets.message),
                        buttonTitle = stringResource(R.string.view_cart),
                        onClickable = {
                            onActions.closeBottomSheet()
                            navigationController.navigate(Screens.Cart.screen){ launchSingleTop = true }
                        },
                        onDismissRequest = { onActions.closeBottomSheet() }
                    )
                }
            }
        }
    }
}

//fun Modifier.bottomBorderOnly(
//    color: Color = Color.DarkOrange,
//    strokeWidth: Dp = 1.dp,
//    cornerRadius: Dp = 40.dp
//) = this.drawBehind {
//    val strokePx = strokeWidth.toPx()
//    val radiusPx = cornerRadius.toPx()
//    val halfStroke = strokePx / 2f
//    val w = size.width
//    val h = size.height
//
//    val path = Path().apply {
//        // 1. البداية عند نقطة بداية الدوران من الجنب الشمال (فوق القاع بـ radiusPx)
//        moveTo(halfStroke, h - radiusPx)
//
//        // 2. دوران الزاوية الشمال تحت
//        arcTo(
//            rect = Rect(
//                left = halfStroke,
//                top = h - (2 * radiusPx) + halfStroke,
//                right = (2 * radiusPx) - halfStroke,
//                bottom = h - halfStroke
//            ),
//            startAngleDegrees = 180f,
//            sweepAngleDegrees = -90f,
//            forceMoveTo = false
//        )
//
//        // 3. الخط المستقيم في قاع البوكس
//        lineTo(w - radiusPx, h - halfStroke)
//
//        // 4. دوران الزاوية اليمين تحت والوقوف تماماً عند نهايتها
//        arcTo(
//            rect = Rect(
//                left = w - (2 * radiusPx) + halfStroke,
//                top = h - (2 * radiusPx) + halfStroke,
//                right = w - halfStroke,
//                bottom = h - halfStroke
//            ),
//            startAngleDegrees = 90f,
//            sweepAngleDegrees = -90f,
//            forceMoveTo = false
//        )
//    }
//
//    drawPath(
//        path = path,
//        color = color,
//        style = Stroke(width = strokePx)
//    )
//}