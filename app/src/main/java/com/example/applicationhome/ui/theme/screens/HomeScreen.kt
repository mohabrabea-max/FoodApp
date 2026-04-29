package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.data.models.FoodDataSource
import com.example.applicationhome.data.models.OffersData
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.data.models.VarietiesMenu
import com.example.applicationhome.ui.theme.components.CategoriesBox
import com.example.applicationhome.ui.theme.components.ItemsBox
import com.example.applicationhome.ui.theme.components.MyBottonBar2
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.view.model.BottomBarViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(scrollBehavior: TopAppBarScrollBehavior, drawerState : DrawerState, coroutineScope : CoroutineScope, navigationController : NavHostController, viewModel: ItemScreenViewModel, viewModelForBottomBar : BottomBarViewModel){
    val offers = OffersData.offersMenu()
    val menu = FoodDataSource.allMenu()
    val categories = VarietiesMenu.categoriesList()
    val pagerState = rememberPagerState(pageCount = {offers.size})
    val scrollState = rememberLazyGridState()
    Scaffold(
        modifier = Modifier.
        fillMaxSize().
        background(Color.White),
    ){
        Box(modifier = Modifier.background(Color.White)){
            Box{
                LazyVerticalGrid (
                    state = scrollState,
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ){
                    item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(100.dp))}
                    item(span = { GridItemSpan(2) }){
                        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically){
                            LazyRow(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(30.dp)
                            ){
                                items(categories) { category -> CategoriesBox(category) }
                            }
                        }
                    }
                    item(span = { GridItemSpan(2) }){
                        Divider(color = Color.LightGray.copy(alpha = 0.5f), modifier = Modifier.width(300.dp).padding(start = 20.dp, end = 20.dp))
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    item(span = { GridItemSpan(2) }){
                        Box(modifier = Modifier.fillMaxWidth().height(180.dp)){
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 10.dp), // عشان تبين طرف الصورة اللي قبلها واللي بعدها (شكلها بيبقى أشيك)
                                pageSpacing = 10.dp
                            ) {page ->
                                val currentOffer = offers[page]
                                Image(
                                    painter = painterResource(id = currentOffer.image),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                    item(span = { GridItemSpan(2) }){
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(color = Color.LightGray.copy(alpha = 0.5f), modifier = Modifier.width(300.dp).padding(start = 20.dp, end = 20.dp))
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    items(menu){ item -> ItemsBox(item, navigationController, viewModel) }

                    item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(90.dp))}
                }
                MyTopBar(
                    "Home",
                    {coroutineScope.launch{drawerState.open()}},
                    {Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.Black)},
                    {
                        navigationController.navigate(Screens.Search.screen){
                            popUpTo(navigationController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }
                    },
                    Icons.Default.Search,
                    {
                        navigationController.navigate(Screens.Notifications.screen){
                            popUpTo(navigationController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }
                    },
                    Icons.Default.Notifications
                )
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
            }
            Column(modifier = Modifier.align(Alignment.BottomCenter)){
                Box(
                    modifier = Modifier.fillMaxWidth().
                    height(60.dp).
                    pointerInput(Unit) { detectTapGestures { } },
                    contentAlignment = Alignment.Center
                ){
                    MyBottonBar2(navigationController, viewModelForBottomBar)
                }
                Box(modifier = Modifier.fillMaxWidth().height(20.dp).pointerInput(Unit) { detectTapGestures { } }, contentAlignment = Alignment.Center){}
            }
        }
    }
}
//Spacer(modifier = Modifier.height(20.dp))
//Divider(modifier = Modifier.width(300.dp).padding(10.dp))
//Spacer(modifier = Modifier.height(20.dp))