package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.data.models.Snakes
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
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.CategoriesBoxViewModel
import com.example.applicationhome.view.model.FavoriteViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    navigationController : NavHostController,
    viewModel: ItemScreenViewModel,
    addBoxViewModel : AddBoxViewModel,
    favoriteState : FavoriteViewModel,
    categoriesBoxViewModel : CategoriesBoxViewModel
){
    val menu = categoriesBoxViewModel.filterMenu
    val snaks = Snakes.snakes()
    val item = viewModel.selectedItem
    val size = viewModel.selectedSize
    val images = item?.image?.size ?: 0
    val pagerState = rememberPagerState(pageCount = {images})

    if(item != null){
        Scaffold(
            modifier = Modifier.fillMaxSize().
            background(Color.White),
            topBar = {
                MyTopBar(
                    modifier = Modifier.
                    fillMaxWidth().
                    height(100.dp).
                    shadow(elevation = 5.dp),
                null,
                {if (navigationController.previousBackStackEntry != null) { navigationController.popBackStack() } },
                {Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)},
                {
                    IconButton(onClick = {
                        navigationController.navigate(Screens.Search.screen){
                            popUpTo(navigationController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                    }
                    Favorite(
                        modifier = Modifier.padding(10.dp).clip(CircleShape).size(35.dp),
                        food = item,
                        favoriteState = favoriteState
                    )
                }
            )
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
            }
        ){
            Box(modifier = Modifier.background(Color.VeryLightGray)){
                Box{
                    LazyColumn(modifier = Modifier.fillMaxSize()){
                        item{Spacer(modifier = Modifier.height(100.dp))}
                        item {
                            Column(modifier = Modifier.fillMaxSize().padding(start = 10.dp, end = 10.dp)){
                                Box(modifier = Modifier.fillMaxWidth().height(400.dp).clip(RoundedCornerShape(10.dp))){
                                    HorizontalPager(
                                        state = pagerState,
                                        modifier = Modifier.fillMaxSize()
                                    ) { page ->
                                        Image(
                                            painter = painterResource(id = item.image[page]),
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
                                            modifier = Modifier.shadow(elevation = 2.dp, spotColor = Color.Black, shape = CircleShape).
                                            background(Color.VeryLightGray).
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
                                Spacer(modifier = Modifier.height(20.dp))
                                Column(
                                    modifier = Modifier.fillMaxWidth().
                                    clip(RoundedCornerShape(20.dp)).
                                    background(Color.White).
                                    padding(10.dp)
                                ){
                                    Text(
                                        text = item.name,
                                        fontSize = 20.sp,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.BrownForFont,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                    Text(
                                        text = item.sizeOptions.find { it.size == size }?.snack.toString(),
                                        color = Color.MediumBrownForTitle,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "${item.sizeOptions.find { it.size == size }?.price} L.E",
                                        fontSize = 30.sp,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.BrownForFont,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier.fillMaxWidth().
                                    clip(RoundedCornerShape(20.dp)).
                                    background(Color.White).
                                    padding(10.dp)
                                ){
                                    LazyRow {
                                        item{
                                            val selectedDetail = item.sizeOptions.find { it.size == size }
                                            selectedDetail?.snack?.forEach { (snakeId, snakeSize) ->
                                                val snake2 = Snakes.snakes().find { it.id == snakeId }
                                                snake2?.let {
                                                    SnaksBox(
                                                        modifier = Modifier.size(170.dp),
                                                        true,
                                                        snake2,
                                                        snakeSize,
                                                        navigationController,
                                                        viewModel
                                                    )
                                                }
                                            }
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
                                Ratings(item)
                            }
                        }
                        item{Spacer(modifier = Modifier.height(30.dp))}
                        item{
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "Other snacks",
                                fontSize = 16.sp,
                                color = Color.Black,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                        item{
                            Box(
                                modifier = Modifier.fillMaxWidth().
                                background(Color.White)
                            ){
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
                        }
                        item{
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "Other meals",
                                fontSize = 16.sp,
                                color = Color.Black,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                        item{
                            Box(
                                modifier = Modifier.fillMaxWidth().height(300.dp).
                                background(Color.White)
                            ){
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)){
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