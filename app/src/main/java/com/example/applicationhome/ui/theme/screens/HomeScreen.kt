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
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.R
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.LightOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.AddBox
import com.example.applicationhome.ui.theme.components.CategoriesBar
import com.example.applicationhome.ui.theme.components.Favorite
import com.example.applicationhome.ui.theme.components.ItemsBox
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.ui.theme.components.RestaurantsBox
import com.example.applicationhome.ui.theme.components.SearchBox
import com.example.applicationhome.ui.theme.components.SnaksBox
import com.example.applicationhome.view.model.APIData
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
    categoriesBoxViewModel : CategoriesBoxViewModel,
    apiData: APIData
){
    val density = LocalDensity.current
    val thresholdPx = with(density) { 60.dp.toPx() }
    val minOffsetToShowBox = with(density) { 147.dp.toPx() }
    val minOffsetPx = remember(density) { with(density) { 170.dp.toPx() } }

    var showCategoriesBox by remember { mutableStateOf(false) }
    var showcategories by remember { mutableStateOf(true) }
    val scrollState = rememberLazyGridState()
    val alpha by remember {
        derivedStateOf {
            val targetPx = with(density) { 85.dp.toPx() }
            if(scrollState.firstVisibleItemIndex > 0){
                1f
            }else{
                ((scrollState.firstVisibleItemScrollOffset / targetPx)).coerceIn(0f, 1f)
            }
        }
    }
    var scal by remember { mutableStateOf(false) }
    LaunchedEffect(scrollState){
        var previousOffset = 0
        var previousIndex = 0
        var accumulatedScrollUp = 0f
        snapshotFlow{ scrollState.firstVisibleItemIndex to scrollState.firstVisibleItemScrollOffset }.
        collect{ (currentIndex, currentOffset) ->
            val delta = previousOffset - currentOffset
            accumulatedScrollUp += delta
            if(accumulatedScrollUp > thresholdPx && currentIndex == previousIndex){    //السكرول اللي لفوق
                showcategories = currentOffset <= previousOffset
                accumulatedScrollUp = thresholdPx
            }else if (accumulatedScrollUp <= -thresholdPx && scrollState.firstVisibleItemScrollOffset > minOffsetPx){   //          السكرول اللي لتحت
                showcategories = currentIndex < previousIndex
                accumulatedScrollUp = -thresholdPx
            }
            if((currentOffset > minOffsetToShowBox || scrollState.firstVisibleItemIndex > 0)){   //        الجزء المسؤول عن ظهور بار الاصناف
                showCategoriesBox = true

            }else{
                showCategoriesBox = false
            }
            previousIndex = currentIndex
            previousOffset = currentOffset
            if ((currentOffset > minOffsetToShowBox * 1.7 || scrollState.firstVisibleItemIndex > 0) && showcategories == false) {
                scal = true
            }else if((currentOffset > minOffsetToShowBox * 1.7 / 2 || scrollState.firstVisibleItemIndex > 0) && showcategories == true){
                scal = true
            }else{
                scal = false
            }
        }
    }

    val snacks = apiData.snacks
    val menu = categoriesBoxViewModel.filterMenu
    val restaurants = categoriesBoxViewModel.filterrestaurants
    val offers = apiData.offers
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
                    var height = if(showcategories == true) 105.dp else 0.dp
                    Spacer(modifier = Modifier.animateContentSize().height(height))
                    if(showCategoriesBox == true) CategoriesBar(categoriesBoxViewModel, apiData)
                }
                Column{
                    MyTopBar(
                        Color.DarkOrange.copy(alpha = alpha),
                        modifier = Modifier.
                        fillMaxWidth().
                        height(100.dp).shadow(elevation = if(showcategories == false && scal == true) 5.dp else 0.dp),
                        null,
                        {
                            IconButton(
                                onClick = {coroutineScope.launch{drawerState.open()}},
                                modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape).size(35.dp)
                            ) {
                                Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.White)
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
                                    clip(CircleShape).
                                    size(35.dp)
                                ){
                                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
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
                                clip(CircleShape).
                                size(35.dp)
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                            }
                        }
                    )
                }
            }
        }
    ){
        Box(modifier = Modifier.background(Color.VeryLightGray)){
            Box{
                LazyVerticalGrid (
                    state = scrollState,
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2)
                ){
                    item(span = { GridItemSpan(2) }){
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
                                    color = Color.White.copy(alpha = 1f - alpha),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 20.dp)
                                )
                            }
                        }
                    }
                    item(span = { GridItemSpan(2) }){ Spacer(modifier = Modifier.height(16.dp).background(Color.DarkOrange)) }
                    item(span = { GridItemSpan(2) }){
                        Box{
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
                    item(span = { GridItemSpan(2) }){ Spacer(modifier = Modifier.height(16.dp)) }
                    item(span = { GridItemSpan(2) }){
                        Box(modifier = Modifier.height(60.dp).fillMaxWidth()){
                            if(showCategoriesBox == false)CategoriesBar(categoriesBoxViewModel, apiData)
                        }
                    }
                    item(span = { GridItemSpan(2) }){ Spacer(modifier = Modifier.height(16.dp)) }
                    item(span = { GridItemSpan(2) }){
                        Box(modifier = Modifier.fillMaxWidth().height(180.dp)){
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 10.dp), // عشان تبين طرف الصورة اللي قبلها واللي بعدها (شكلها بيبقى أشيك)
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
                    item(span = { GridItemSpan(2) }){ Spacer(modifier = Modifier.height(16.dp)) }
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
                                modifier = Modifier.padding(start = 15.dp)
                            )
                            TextButton(
                                onClick = {navigationController.navigate(Screens.Restaurants.screen)},
                                contentPadding = PaddingValues(end = 15.dp)
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
                                RestaurantsBox(item, favoriteState, apiData)
                            }
                        }
                    }
                    item(span = { GridItemSpan(2) }){ Spacer(modifier = Modifier.height(16.dp)) }
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
                                modifier = Modifier.padding(start = 15.dp)
                            )
                            TextButton(
                                onClick = {navigationController.navigate(Screens.Menu.screen)},
                                contentPadding = PaddingValues(end = 15.dp)
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
                            items(snacks){ item ->
                                SnaksBox(
                                    modifier = Modifier.size(200.dp),
                                    false,
                                    item,
                                    null,
                                    navigationController,
                                    viewModel,
                                    apiData,
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
                    item(span = { GridItemSpan(2) }){ Spacer(modifier = Modifier.height(16.dp)) }
                    item(span = { GridItemSpan(2) }){
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(color = Color.LightOrange.copy(alpha = 0.5f), modifier = Modifier.width(300.dp).padding(start = 20.dp, end = 20.dp))
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    item(span = { GridItemSpan(2) }){ Spacer(modifier = Modifier.height(16.dp)) }
                    items(menu){ item ->
                        ItemsBox(
                            item,
                            navigationController,
                            viewModel,
                            apiData,
                            {
                                Favorite(
                                    modifier = Modifier.
                                    clip(CircleShape).
                                    size(35.dp),
                                    food = item,
                                    favoriteState = favoriteState
                                )
                                AddBox(color = Color.VeryLightGray, food = item, ordernumber = addBoxViewModel)
                            }
                        )
                    }
                    item(span = { GridItemSpan(2) }){ Spacer(modifier = Modifier.height(16.dp)) }
                    item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(80.dp))}
                }
            }
        }
    }
}