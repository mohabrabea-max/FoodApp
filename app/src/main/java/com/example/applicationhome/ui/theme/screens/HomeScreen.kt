package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.models.repository.MenuRepository.offers
import com.example.applicationhome.data.models.repository.MenuRepository.restaurantsMenuisLoading
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.LightOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.bars.HomeScreenTopBar
import com.example.applicationhome.ui.theme.components.bars.MyBottonBar
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.CategoriesBar
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.RestaurantImageView
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.RestaurantsBoxHomeScreen
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.SearchBox
import com.example.applicationhome.ui.theme.model.AddBoxViewModel
import com.example.applicationhome.ui.theme.model.BottomBarViewModel
import com.example.applicationhome.ui.theme.model.CategoriesBoxViewModel
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.HomeScreenViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel
import com.example.applicationhome.ui.theme.model.LoginViewModel
import com.example.applicationhome.ui.theme.model.RestaurantViewModel
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    navigationController : NavHostController,
    itemScreenViewModel: ItemScreenViewModel,
    addBoxViewModel: AddBoxViewModel,
    favoriteViewModel : FavoriteViewModel,
    categoriesBoxViewModel : CategoriesBoxViewModel,
    restaurantViewModel: RestaurantViewModel,
    bottomBarViewModel : BottomBarViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    loginViewModel: LoginViewModel
){
    val scrollState = rememberLazyListState()

    val restaurants = categoriesBoxViewModel.filterrestaurants
    val offers = offers
    val pagerState = rememberPagerState(pageCount = {offers.size})

    val context = LocalContext.current as? Activity
    BackHandler(enabled = true) { context?.finishAffinity() } // ده بيمسح الأبلكيشن من الـ Background ويقفله تماماً

    Scaffold(
        modifier = Modifier.navigationBarsPadding().
        fillMaxSize(),
        topBar = {
            HomeScreenTopBar(scrollState, drawerState, coroutineScope, navigationController)
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth().
                pointerInput(Unit) { detectTapGestures { } },
                contentAlignment = Alignment.BottomCenter
            ){
                MyBottonBar(navigationController, bottomBarViewModel, addBoxViewModel, favoriteViewModel)
            }
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

                    item{ CategoriesBar(categoriesBoxViewModel) }

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
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)).clickable {  },
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
                    item{ Spacer(modifier = Modifier.height(16.dp)) }
                    item{
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(color = Color.LightOrange.copy(alpha = 0.5f), modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp))
                        Spacer(modifier = Modifier.height(20.dp))
                    }
//                    item{
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ){
//                            Text(
//                                text = "Restaurants :",
//                                style = MaterialTheme.typography.titleLarge,
//                                color = Color.Black,
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold,
//                                modifier = Modifier.padding(start = 15.dp)
//                            )
//                            TextButton(
//                                onClick = {navigationController.navigate(Screens.Restaurants.screen)},
//                                contentPadding = PaddingValues(end = 15.dp)
//                            ){
//                                Text(
//                                    text = "See all",
//                                    style = MaterialTheme.typography.titleLarge,
//                                    color = Color.DarkOrange,
//                                    fontSize = 13.sp
//                                )
//                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.DarkOrange)
//                            }
//
//                        }
//                    }
                    items(restaurants.values.toList()){ item ->
                        RestaurantsBoxHomeScreen(
                            restaurantsMenuisLoading,
                            item,
                            favoriteViewModel,
                            itemScreenViewModel,
                            navigationController,
                            categoriesBoxViewModel,
                            restaurantViewModel,
                            homeScreenViewModel
                        )
                    }
                    item{ Spacer(modifier = Modifier.height(16.dp)) }
                    item{Spacer(modifier = Modifier.height(80.dp))}
                }
            }
        }
        if(homeScreenViewModel.viewImageState){
            RestaurantImageView(homeScreenViewModel)
        }
    }
}