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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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
import com.example.applicationhome.data.models.repository.MenuRepository.offers
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.Favorite2
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.view.model.APIData
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.CategoriesBoxViewModel
import com.example.applicationhome.view.model.FavoriteViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    navigationController : NavHostController,
    itemScreenViewModel: ItemScreenViewModel,
    addBoxViewModel: AddBoxViewModel,
    favoriteState : FavoriteViewModel,
    categoriesBoxViewModel: CategoriesBoxViewModel,
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
    val snacks = MenuRepository.snacks.values.toList()
    val item = itemScreenViewModel.selectedRestaurant
    val logo = item?.image
    val background = item?.image2
    val type = item?.typ?.toList()

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
                            Favorite2(
                                modifier = Modifier.padding(5.dp).border(
                                    width = 1.dp,
                                    color = Color.LightGray.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(30.dp)
                                ).shadow(
                                    elevation = if (searchSize < 1) 7.dp else 0.dp,
                                    spotColor = Color.LightGray,
                                    shape = CircleShape
                                ).clip(CircleShape).size(40.dp).background(Color.White),
                                modifier2 = Modifier.size(25.dp),
                                restaurants = item,
                                favoriteState = favoriteState
                            )
                        }
                    )
                }
            }
        ){
            LazyVerticalGrid (
                //state = scrollState,
                modifier = Modifier.fillMaxSize().
                background(Color.White),
                columns = GridCells.Fixed(2)
            ){
                item(span = { GridItemSpan(2) }){
                    Box(
                        modifier = Modifier.fillMaxWidth().height(270.dp),
                        contentAlignment = Alignment.TopCenter
                    ){
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).
                            data(background).
                            crossfade(true).
                            precision(Precision.EXACT).
                            build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(230.dp),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier.padding(horizontal = 15.dp).
                            fillMaxWidth().
                            height(120.dp).
                            clip(RoundedCornerShape(15.dp)).
                            border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp)).
                            background(Color.White).
                            align(Alignment.BottomCenter)
                        ){
                            Row(
                                modifier = Modifier.fillMaxSize().padding(13.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Row{
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current).
                                        data(logo).
                                        crossfade(true).
                                        precision(Precision.EXACT).
                                        build(),
                                        contentDescription = null,
                                        modifier = Modifier.size(70.dp).
                                        clip(RoundedCornerShape(10.dp)).
                                        border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Column(
                                        modifier = Modifier.padding(start = 13.dp)
                                    ){
                                        Text(
                                            text = item.name,
                                            fontSize = 18.sp,
                                            color = Color.Black,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 5.dp)
                                        )
                                        Text(
                                            text = type?.joinToString(separator = " ", prefix = "", postfix = "")?: "",
                                            fontSize = 10.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(bottom = 10.dp)
                                        )
                                        Row(
                                            modifier = Modifier.width(80.dp).
                                            height(20.dp).
                                            clip(RoundedCornerShape(5.dp)).
                                            background(Color.VeryLightGray).padding(horizontal = 3.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ){
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color(0xFFFFD700) ,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = item.review.toString(),
                                                color = Color.Black,
                                                fontSize = 14.sp,
                                                style = MaterialTheme.typography.labelLarge,
                                                modifier = Modifier
                                            )
                                            Text(
                                                text = "(1k+)",
                                                color = Color.Gray,
                                                fontSize = 14.sp,
                                                style = TextStyle(letterSpacing = (-0.7).sp),
                                                modifier = Modifier
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                item(span = { GridItemSpan(2) }){
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).height(120.dp)){
                        LazyRow (
                            modifier = Modifier.fillMaxSize(),
                        ){
                            item{ Spacer(modifier = Modifier.width(15.dp)) }
                            item{
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)).clickable {  },
                                    model = ImageRequest.Builder(LocalContext.current).
                                    data(offers).
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







            Box(modifier = Modifier.background(Color.VeryLightGray)){
                Column(modifier = Modifier.align(Alignment.BottomCenter)){
                    Box(contentAlignment = Alignment.Center){
                        //BottonBarForItemScreen(addBoxViewModel, viewModel, item, size)
                    }
                    Spacer(modifier = Modifier.height(80.dp).pointerInput(Unit) { detectTapGestures { } })
                }
            }
        }
    }
}