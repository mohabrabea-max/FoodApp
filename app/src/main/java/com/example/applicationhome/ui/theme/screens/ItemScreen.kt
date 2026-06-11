package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.data.models.repository.MenuRepository
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuListisLoading
import com.example.applicationhome.data.models.repository.MenuRepository.snacksisLoading
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.AddBox
import com.example.applicationhome.ui.theme.components.BottonBarForItemScreen
import com.example.applicationhome.ui.theme.components.Favorite
import com.example.applicationhome.ui.theme.components.ItemSize
import com.example.applicationhome.ui.theme.components.ItemsBox
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.ui.theme.components.Ratings
import com.example.applicationhome.ui.theme.components.SnaksBox
import com.example.applicationhome.ui.theme.model.APIData
import com.example.applicationhome.ui.theme.model.AddBoxViewModel
import com.example.applicationhome.ui.theme.model.CategoriesBoxViewModel
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    navigationController : NavHostController,
    viewModel: ItemScreenViewModel,
    addBoxViewModel : AddBoxViewModel,
    favoriteState : FavoriteViewModel,
    categoriesBoxViewModel : CategoriesBoxViewModel,
    apiData : APIData
){
    val scrollState = rememberLazyListState()
    val alpha by remember {
        derivedStateOf {
            if(scrollState.firstVisibleItemIndex >= 1){
                1f
            }else{
                ((scrollState.firstVisibleItemScrollOffset / 300f) - 1f).coerceIn(0f, 1f)
            }
        }
    }
    val searchSize by remember {
        derivedStateOf {
            if(scrollState.firstVisibleItemIndex >= 1){
                3f
            }else{
                ((scrollState.firstVisibleItemScrollOffset / 300f) - 1f).coerceIn(1f, 3f)
            }
        }
    }
    val menu = categoriesBoxViewModel.filterMenu.toList()
    val snacks = MenuRepository.snacks.toList()
    val item = viewModel.selectedItem
    val size = viewModel.selectedSize
    val images = item?.image?.size ?: 0
    val pagerState = rememberPagerState(pageCount = {images})

    if(item != null){
        Scaffold(
            modifier = Modifier.fillMaxSize().
            background(Color.White),
            topBar = {
                Column{
                    MyTopBar(
                        Color.DarkOrange.copy(alpha = alpha),
                        modifier = Modifier.
                        fillMaxWidth().
                        height(100.dp),
                        null,
                        Color.White,
                        {
                            IconButton(
                                onClick = {if (navigationController.previousBackStackEntry != null) { navigationController.popBackStack() } },
                                modifier = Modifier.padding(5.dp).
                                border(width = 1.dp, color = Color.LightGray.copy(alpha = 0.25f), shape = RoundedCornerShape(30.dp)).
                                shadow(elevation = if(searchSize < 1) 7.dp else 0.dp, spotColor = Color.LightGray, shape = CircleShape).clip(CircleShape).size(40.dp).
                                background(Color.White)
                            ){
                                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
                            }
                        },
                        {
                            Box(
                                modifier = Modifier.animateContentSize().padding(5.dp).
                                border(width = 1.dp, color = Color.LightGray.copy(alpha = 0.25f), shape = RoundedCornerShape(30.dp)).
                                shadow(elevation = if(searchSize < 1) 7.dp else 0.dp, spotColor = Color.LightGray, shape = CircleShape).clip(CircleShape).
                                width(if(searchSize > 1) 120.dp else 40.dp).height(40.dp).
                                background(Color.White).
                                clickable {
                                    navigationController.navigate(Screens.Search.screen){
                                        popUpTo(navigationController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                            ){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                ){
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                    if(searchSize > 1) Text(
                                        text = "Search",
                                        softWrap = false,
                                        color = Color.Black,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                }
                            }
                            Favorite(
                                modifier = Modifier.padding(5.dp).
                                border(width = 1.dp, color = Color.LightGray.copy(alpha = 0.25f), shape = RoundedCornerShape(30.dp)).
                                shadow(elevation = if(searchSize < 1) 7.dp else 0.dp, spotColor = Color.LightGray, shape = CircleShape).clip(CircleShape).size(40.dp).
                                background(Color.White),
                                modifier2 = Modifier.size(25.dp),
                                food = item,
                                favoriteState = favoriteState
                            )
                        },
                        weight = 2f
                    )
                }
            }
        ){
            Box(modifier = Modifier.background(Color.VeryLightGray)){
                Column{
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        state = scrollState,
                        modifier = Modifier.fillMaxSize()
                    ){
                        item{
                            Column{
                                Spacer(modifier = Modifier.height(50.dp))
                                Box(
                                    modifier = Modifier.size(350.dp).
                                    clip(RoundedCornerShape(10.dp)).
                                    graphicsLayer {
                                        // بنخلي الصورة تتحرك بنص سرعة السكرول (Parallax)
                                        // وبنخليها تنزل لتحت شوية عشان اللي تحتها يغطيها
                                        translationY = scrollState.firstVisibleItemScrollOffset * 1f
                                        val scale = 1f - (scrollState.firstVisibleItemScrollOffset.toFloat() / 4000f).coerceIn(0f, 0.2f)  // تأثير التصغير (Scale)
                                        scaleX = scale
                                        scaleY = scale
                                        1f - (scrollState.firstVisibleItemScrollOffset.toFloat() / 1000f).coerceIn(0f, 1f) // بنخلي الصورة دايماً "ورا" الحاجات التانية
                                    }
                                ){
                                    HorizontalPager(
                                        state = pagerState,
                                        modifier = Modifier.fillMaxSize()
                                    ) { page ->
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current).
                                            data(item.image[page]).
                                            crossfade(true).
                                            precision(Precision.EXACT).
                                            build(),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    Row(
                                        Modifier.height(50.dp).fillMaxWidth().align(Alignment.BottomCenter),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier.clip(CircleShape).
                                            background(Color.VeryLightGray.copy(alpha = 0.5f)).
                                            padding(3.dp)
                                        ){
                                            repeat(item.image.size) { iteration ->
                                                val color = if (pagerState.currentPage == iteration) Color.DarkOrange else Color.Black
                                                Box(
                                                    modifier = Modifier.padding(4.dp).
                                                    clip(CircleShape).
                                                    background(color).
                                                    size(5.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Column(modifier = Modifier.fillMaxSize().background(Color.VeryLightGray).padding(start = 10.dp, end = 10.dp)){
                                //Spacer(modifier = Modifier.height(20.dp))
                                Column(
                                    modifier = Modifier.fillMaxWidth().
                                    clip(RoundedCornerShape(20.dp)).
                                    background(Color.White)
                                ){
                                    Column(
                                        modifier = Modifier.padding(15.dp)
                                    ){
                                        Text(
                                            text = item.name,
                                            fontSize = 20.sp,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color.BrownForFont,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        val selectedDetail = item.sizeOptions.find { it.size == size }
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start){
                                            val text = mutableListOf<String>()
                                                selectedDetail?.snack?.forEach { (snakeId, snakeSize) ->
                                                val snake2 = snacks.find { it.id == snakeId }
                                                snake2?.let { safeSnake ->
                                                    text.add(snakeSize + " " + safeSnake.name)
                                                }
                                            }
                                            Text(
                                                text = text.joinToString(" - "),
                                                color = Color.MediumBrownForTitle
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "${item.sizeOptions.find { it.size == size }?.price} L.E",
                                        fontSize = 30.sp,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.BrownForFont,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 15.dp, bottom = 15.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier.fillMaxWidth().
                                    clip(RoundedCornerShape(20.dp)).
                                    background(Color.White)
                                ){
                                    Column{
                                        Spacer(modifier = Modifier.height(15.dp))
                                        Text(
                                            text = "Meal snacks",
                                            fontSize = 16.sp,
                                            color = Color.Black,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(start = 15.dp)
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        LazyRow {
                                            item{Spacer(modifier = Modifier.width(7.dp))}
                                            item{
                                                val selectedDetail = item.sizeOptions.find { it.size == size }
                                                selectedDetail?.snack?.forEach { (snakeId, snakeSize) ->
                                                    val snake2 = snacks.find { it.id == snakeId }
                                                    snake2?.let { safeSnake ->
                                                        SnaksBox(
                                                            snacksisLoading,
                                                            modifier = Modifier.size(170.dp),
                                                            true,
                                                            safeSnake,
                                                            snakeSize,
                                                            navigationController,
                                                            viewModel
                                                        )
                                                    }
                                                }
                                            }
                                            item{Spacer(modifier = Modifier.width(7.dp))}
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier.fillMaxWidth().
                                    clip(RoundedCornerShape(20.dp)).
                                    background(Color.White).
                                    padding(10.dp)
                                ){
                                    ItemSize(viewModel)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Column(
                                    modifier = Modifier.fillMaxWidth().
                                    clip(RoundedCornerShape(20.dp)).
                                    background(Color.White).
                                    padding(15.dp),
                                    horizontalAlignment = Alignment.Start
                                ){
                                    Text(
                                        text = "Ratings & Reviews",
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(15.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ){
                                        Text(
                                            text = item.review.toString(),
                                            fontSize = 25.sp,
                                            color = Color.Black,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        for(x in 0 .. item.review.toInt()){
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color.DarkOrange,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(15.dp))
                                    Text(
                                        text = "All reviews (5)",
                                        fontSize = 18.sp,
                                        color = Color.Black,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Ratings(item)
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Box(
                                    modifier = Modifier.fillMaxWidth().
                                    clip(RoundedCornerShape(20.dp)).
                                    background(Color.White)
                                ){
                                    Column{
                                        Text(
                                            text = "Other snacks",
                                            fontSize = 16.sp,
                                            color = Color.Black,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(start = 15.dp, bottom = 15.dp, top = 15.dp)
                                        )
                                        LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)){
                                            item{Spacer(modifier = Modifier.width(7.dp))}
                                            items(snacks){ item ->
                                                SnaksBox(
                                                    snacksisLoading,
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
                                                        AddBox(color = Color.VeryLightGray, food = item, addBoxViewModel)
                                                    }
                                                )
                                            }
                                            item{Spacer(modifier = Modifier.width(7.dp))}
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier.fillMaxWidth().
                                    height(350.dp).
                                    clip(RoundedCornerShape(20.dp)).
                                    background(Color.White)
                                ){
                                    Column{
                                        Text(
                                            text = "Other meals",
                                            fontSize = 16.sp,
                                            color = Color.Black,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(start = 15.dp, bottom = 15.dp, top = 15.dp)
                                        )
                                        LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)){
                                            item{Spacer(modifier = Modifier.width(7.dp))}
                                            items(menu){ item ->
                                                ItemsBox(
                                                    foodMenuListisLoading,
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
                                                        AddBox(color = Color.VeryLightGray, food = item, addBoxViewModel)
                                                    }
                                                )
                                            }
                                            item{Spacer(modifier = Modifier.width(7.dp))}
                                        }
                                    }
                                }
                            }
                        }
                        item{Spacer(modifier = Modifier.height(150.dp))}
                    }
                }
                Column(modifier = Modifier.align(Alignment.BottomCenter)){
                    Box(contentAlignment = Alignment.Center){
                        BottonBarForItemScreen(addBoxViewModel, viewModel, item, size)
                    }
                    Spacer(modifier = Modifier.height(80.dp).pointerInput(Unit) { detectTapGestures { } })
                }
            }
        }
    }
}