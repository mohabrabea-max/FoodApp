package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.data.models.OffersData
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.data.models.Snakes
import com.example.applicationhome.data.models.VarietiesMenu
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.LightOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.AddBox
import com.example.applicationhome.ui.theme.components.CategoriesBox
import com.example.applicationhome.ui.theme.components.Favorite
import com.example.applicationhome.ui.theme.components.ItemsBox
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.ui.theme.components.RestaurantsBox
import com.example.applicationhome.ui.theme.components.SnaksBox
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.CategoriesBoxViewModel
import com.example.applicationhome.view.model.FavoriteViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    navigationController : NavHostController,
    viewModel: ItemScreenViewModel,
    addBoxViewModel: AddBoxViewModel,
    favoriteState : FavoriteViewModel,
    categoriesBoxViewModel : CategoriesBoxViewModel
){
    var showSearchIcon by remember { mutableStateOf(false) }
    var showcategories by remember { mutableStateOf(true) }
    val scrollState = rememberLazyGridState()
    LaunchedEffect(scrollState){
        var previousOffset = 0
        var previousIndex = 0
        snapshotFlow{ scrollState.firstVisibleItemIndex to scrollState.firstVisibleItemScrollOffset }.
        collect{ (currentIndex, currentOffset) ->
            if(currentIndex == previousIndex){
                showcategories = currentOffset <= previousOffset
            }else{
                showcategories = currentIndex <= previousIndex
            }
            if((currentOffset > 440 || scrollState.firstVisibleItemIndex > 0) && showcategories == false){
                showSearchIcon = true

            }else if((currentOffset > 210 || scrollState.firstVisibleItemIndex > 0) && showcategories == true){
                showSearchIcon = true
            }else{
                showSearchIcon = false
            }
            previousIndex = currentIndex
            previousOffset = currentOffset
        }
    }
    var scal by remember { mutableStateOf(false) }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.firstVisibleItemScrollOffset }
            .collect { offset ->
                if ((offset > 440 || scrollState.firstVisibleItemIndex > 0) && showcategories == false) {
                    scal = true
                }else if((offset > 210 || scrollState.firstVisibleItemIndex > 0) && showcategories == true){
                    scal = true
                }else{
                    scal = false
                }
            }
    }
    val snaks = Snakes.snakes()
    val menu = categoriesBoxViewModel.filterMenu
    val restaurants = categoriesBoxViewModel.restaurants
    val offers = OffersData.offersMenu()
    val categories = VarietiesMenu.categoriesList()
    val pagerState = rememberPagerState(pageCount = {offers.size})
    val context = LocalContext.current as? Activity
    BackHandler(enabled = true) {
        // ده بيمسح الأبلكيشن من الـ Background ويقفله تماماً
        context?.finishAffinity()
    }
    Scaffold(
        modifier = Modifier.
        fillMaxSize().
        background(Color.White),
        topBar = {
            Box{
                Column{
                    var height = if(showcategories == true) 100.dp else 0.dp
                    Spacer(modifier = Modifier.animateContentSize().height(height))
                    Row(
                        modifier = Modifier.fillMaxWidth().
                        height(70.dp).
                        shadow(elevation = if(showcategories == true) 5.dp else 0.dp).
                        background(Color.White),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ){
                            item { Spacer(modifier = Modifier.width(4.dp)) }
                            items(categories) { category -> CategoriesBox(category, categoriesBoxViewModel) }
                            item { Spacer(modifier = Modifier.width(4.dp)) }
                        }
                    }
                    Divider(color = Color.LightOrange.copy(alpha = 0.5f))
                }
                Column{
                    MyTopBar(
                        Color.White,
                        modifier = Modifier.
                        fillMaxWidth().
                        height(100.dp),
                        //shadow(elevation = if(showcategories == false) 5.dp else 0.dp),
                        "Home",
                        {
                            IconButton(
                                onClick = {coroutineScope.launch{drawerState.open()}},
                                modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape).background(Color.White.copy(alpha = if(showcategories == false) 1f else 0f)).size(35.dp)
                            ) {
                                Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.Black)
                            }
                        },
                        {
                            AnimatedVisibility(
                                visible = scal,
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut()
                            ){
                                IconButton(onClick = {
                                    navigationController.navigate(Screens.Notifications.screen){
                                        popUpTo(navigationController.graph.findStartDestination().id) {
                                            saveState = true
                                        }

                                        launchSingleTop = true

                                        restoreState = true
                                    }
                                },
                                    modifier = Modifier.
                                    size(50.dp).
                                    padding(5.dp).
                                    clip(CircleShape).background(Color.White).
                                    border(width = 1.dp, color = Color.LightGray.copy(alpha = 0.25f), shape = RoundedCornerShape(30.dp)).
                                    size(35.dp)
                                ){
                                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                                }
                            }

                            IconButton(onClick = {
                                navigationController.navigate(Screens.Search.screen){
                                    popUpTo(navigationController.graph.findStartDestination().id) {
                                        saveState = true
                                    }

                                    launchSingleTop = true

                                    restoreState = true
                                }
                            },
                                modifier = Modifier.size(50.dp).
                                padding(5.dp).
                                clip(CircleShape).background(Color.White).
                                border(width = 1.dp, color = Color.LightGray.copy(alpha = 0.25f), shape = RoundedCornerShape(30.dp)).
                                size(35.dp)
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.Black)
                            }
                        }
                    )
                    if(showcategories == true) Divider(color = Color.VeryLightGray)
                }
            }
        }
    ){
        Box(modifier = Modifier.background(Color.VeryLightGray)){
            Box{
                LazyVerticalGrid (
                    state = scrollState,
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(170.dp))}
                    item(span = { GridItemSpan(2) }){
                        Box(modifier = Modifier.fillMaxSize()){
                            Box(
                                modifier = Modifier.width(300.dp).height(50.dp).align(Alignment.Center).clip(CircleShape).background(Color.Black)
                            )
                        }
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
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)).clickable {  },
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                    item(span = { GridItemSpan(2) }){
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(color = Color.LightOrange.copy(alpha = 0.5f), modifier = Modifier.width(300.dp).padding(start = 20.dp, end = 20.dp))
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item(span = { GridItemSpan(2) }){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = "Restaurants :",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                            TextButton(
                                onClick = {navigationController.navigate(Screens.Menu.screen)},
                                contentPadding = PaddingValues(end = 10.dp)
                            ){
                                Text(
                                    text = "See all",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.DarkOrange,
                                    fontSize = 13.sp
                                )
                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.DarkOrange)
                            }

                        }
                    }
                    item(span = { GridItemSpan(2) }){
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            items(restaurants){item ->
                                RestaurantsBox(item, favoriteState)
                            }
                        }
                    }

                    item(span = { GridItemSpan(2) }){
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(color = Color.LightOrange.copy(alpha = 0.5f), modifier = Modifier.width(300.dp).padding(start = 20.dp, end = 20.dp))
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    item(span = { GridItemSpan(2) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Snacks :",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                            TextButton(
                                onClick = {navigationController.navigate(Screens.Menu.screen)},
                                contentPadding = PaddingValues(end = 10.dp)
                            ){
                                Text(
                                    text = "See all",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.DarkOrange,
                                    fontSize = 13.sp
                                )
                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.DarkOrange)
                            }
                        }
                    }
                    item(span = { GridItemSpan(2) }){
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)){
                            items(snaks){ item ->
                                SnaksBox(
                                    modifier = Modifier.size(200.dp),
                                    false,
                                    item,
                                    null,
                                    navigationController,
                                    viewModel,
                                    {
                                        Favorite(
                                            modifier = Modifier.
                                            clip(CircleShape).
                                            border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(30.dp)).
                                            size(35.dp).
                                            background(Color.VeryLightGray),
                                            food = item,
                                            favoriteState = favoriteState
                                        )
                                        AddBox(color = Color.VeryLightGray, food = item, ordernumber = addBoxViewModel)
                                    }
                                )
                            }
                        }
                    }

                    item(span = { GridItemSpan(2) }){
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(color = Color.LightOrange.copy(alpha = 0.5f), modifier = Modifier.width(300.dp).padding(start = 20.dp, end = 20.dp))
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    items(menu){ item ->
                        ItemsBox(
                            item,
                            navigationController,
                            viewModel,
                            {
                                Favorite(
                                    modifier = Modifier.
                                    clip(CircleShape).
                                    border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(30.dp)).
                                    size(35.dp).
                                    background(Color.VeryLightGray),
                                    food = item,
                                    favoriteState = favoriteState
                                )
                                AddBox(color = Color.VeryLightGray, food = item, ordernumber = addBoxViewModel)
                            }
                        )
                    }
                    item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(80.dp))}
                }
            }
        }
    }
}
//Spacer(modifier = Modifier.height(20.dp))
//Divider(modifier = Modifier.width(300.dp).padding(10.dp))
//Spacer(modifier = Modifier.height(20.dp))