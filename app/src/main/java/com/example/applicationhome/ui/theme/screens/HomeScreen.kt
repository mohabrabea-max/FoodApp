package com.example.applicationhome.ui.theme.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.LightOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.bars.HomeScreenTopBar
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.CategoriesBar
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.RestaurantImageView
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.RestaurantsBoxHomeScreen
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.SearchBox
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.HomeScreenViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel
import com.example.applicationhome.ui.theme.model.RestaurantViewModel
import com.example.applicationhome.ui.theme.model.ViewRestaurantImageViewModel
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    navigationController : NavHostController,
    itemScreenViewModel: ItemScreenViewModel,
    favoriteViewModel : FavoriteViewModel,
    restaurantViewModel: RestaurantViewModel,
    viewRestaurantImageViewModel: ViewRestaurantImageViewModel,
    homeScreenViewModel : HomeScreenViewModel,
    scrollState : LazyListState
){
    val restaurants by homeScreenViewModel.filterRestaurants
    val offers = homeScreenViewModel.offers
    val offersIsLoading by homeScreenViewModel.offersIsLoading.collectAsStateWithLifecycle()
    val restaurantIsLoading by homeScreenViewModel.restaurantsMenuIsLoading.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = {offers.size})

    val context = LocalContext.current as? Activity
    BackHandler(enabled = true) { context?.finishAffinity() } // ده بيمسح الأبلكيشن من الـ Background ويقفله تماماً

    Scaffold(
        modifier = Modifier.navigationBarsPadding().
        fillMaxSize(),
        topBar = {
            HomeScreenTopBar(scrollState, drawerState, coroutineScope, navigationController)
        }
    ){
        Box(modifier = Modifier.background(Color.VeryLightGray)){
            Box{
                LazyColumn (
                    state = scrollState,
                    modifier = Modifier.fillMaxSize()
                ){
                    item{
                        Box(modifier = Modifier.height(170.dp).fillMaxWidth().background(Color.DarkOrange)){
                            Column(
                                modifier = Modifier.align(Alignment.BottomCenter),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
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

                    item{ Box(modifier = Modifier.fillMaxWidth().height(16.dp).background(Color.DarkOrange)) }

                    item{
                        Box(
                            modifier = Modifier.background(Color.White)
                        ){
                            Box(
                                modifier = Modifier.fillMaxWidth().
                                height(25.dp).
                                clip(shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)).
                                background(Color.DarkOrange).
                                align(Alignment.TopCenter)
                            )
                            SearchBox()
                        }
                    }
                    item{ Box(modifier = Modifier.fillMaxWidth().height(16.dp).background(Color.White)) }

                    item{ CategoriesBar(homeScreenViewModel) }

                    item{ Spacer(modifier = Modifier.height(16.dp)) }

                    item{
                        Box(modifier = Modifier.fillMaxWidth().height(120.dp)){
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 10.dp),
                                pageSpacing = 10.dp
                            ) {page ->
                                val currentOffer = offers[page]
                                Box(
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)).background(Color.White).clickable {  }
                                ){
                                    if(!offersIsLoading){
                                        AsyncImage(
                                            modifier = Modifier.fillMaxSize(),
                                            model = ImageRequest.Builder(LocalContext.current).
                                            data(currentOffer.image).
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
                    }

                    item{ Spacer(modifier = Modifier.height(16.dp)) }

                    item{
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(color = Color.LightOrange.copy(alpha = 0.5f), modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp))
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    items(restaurants){ item ->
                        RestaurantsBoxHomeScreen(
                            restaurantIsLoading,
                            item,
                            favoriteViewModel,
                            itemScreenViewModel,
                            navigationController,
                            restaurantViewModel,
                            viewRestaurantImageViewModel,
                            homeScreenViewModel
                        )
                    }

                    item{ Spacer(modifier = Modifier.height(16.dp)) }

                    item{Spacer(modifier = Modifier.height(80.dp))}
                }
            }
        }
        if(viewRestaurantImageViewModel.viewImageState){
            RestaurantImageView(viewRestaurantImageViewModel)
        }
    }
}