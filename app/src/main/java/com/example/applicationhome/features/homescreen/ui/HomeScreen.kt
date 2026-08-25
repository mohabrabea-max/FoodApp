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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.core.ui.components.bars.NetworkErrorTopBar
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.RestaurantImageView
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.LightOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.HomeScreenActions
import com.example.applicationhome.data.data.model.HomeScreenParameters
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.data.model.Screens
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

        modifier = Modifier
            .fillMaxSize()
            .background(Color.VeryLightGray),

        state = state,

        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = Color.White,
                color = Color.DarkOrange,
                state = state
            )
        },

        contentAlignment = Alignment.Center
    ){
        Scaffold(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize(),

            containerColor = Color.White,

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
                        modifier = Modifier.height(170.dp).fillMaxWidth()
                            .background(Color.DarkOrange)
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "What would you like to eat?",
                                fontSize = 22.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White.copy(alpha = 1f),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 20.dp)
                            )
                        }
                    }
                }

                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(16.dp)
                            .background(Color.DarkOrange)
                    )
                }

                item {
                    Box(
                        modifier = Modifier.background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(25.dp).clip(
                                shape = RoundedCornerShape(
                                    bottomStart = 40.dp,
                                    bottomEnd = 40.dp
                                )
                            ).background(Color.DarkOrange).align(Alignment.TopCenter)
                        )
                        SearchBox {
                            navigationController.navigate(Screens.Search.screen)
                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(16.dp).background(Color.White)
                    )
                }

                when (syncDataUiState) {
                    HomeUiState.Success, HomeUiState.Offline -> {
                        item {
                            CategoriesBar(
                                parameters.categories,
                                parameters.categorySelected,
                                { category -> onActions.select(category) },
                                { onActions.unSelected() },
                                {
                                    // 1. حدد طول الظل اللي إنت عايزه ينزل تحت البوكس
                                    val shadowHeight = 3.dp.toPx()

                                    // 2. حدد درجة شفافية وغمقان الظل
                                    val shadowColor = Color.Gray.copy(alpha = 0.2f)

                                    // 3. عملنا فرشة تدرج رأسي تبدأ من نهاية البوكس (size.height) وتنتهي بعد طول الظل
                                    val shadowBrush = Brush.verticalGradient(
                                        colors = listOf(shadowColor, Color.Transparent),
                                        startY = size.height,
                                        endY = size.height + shadowHeight
                                    )

                                    // 4. رسمنا مستطيل الظل: بيبدأ من صفر في العرض (x=0) يعني مش هيهرب برا الأطراف
                                    // وبيبدأ من نهاية ارتفاع البوكس (y = size.height) يعني مستحيل يطلع فوق
                                    drawRect(
                                        brush = shadowBrush,
                                        topLeft = Offset(x = 0f, y = size.height),
                                        size = Size(width = size.width, height = shadowHeight)
                                    )
                                }
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
                                            .clip(RoundedCornerShape(10.dp)).background(Color.White)
                                            .clickable { }
                                    ) {
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
        }
    }
}