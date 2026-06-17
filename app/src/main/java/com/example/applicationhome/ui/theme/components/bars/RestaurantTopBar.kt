package com.example.applicationhome.ui.theme.components.bars

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.Favorite2
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantTopBar(
    searchSize : Float,
    item : Restaurants,
    scrollState : LazyGridState,
    navigationController : NavHostController,
    restaurantViewModel: RestaurantViewModel,
    favoriteViewModel: FavoriteViewModel
){
    val alpha by remember {
        derivedStateOf {
            if(scrollState.firstVisibleItemIndex >= 1){
                1f
            }else{
                ((scrollState.firstVisibleItemScrollOffset / 300f) - 1f).coerceIn(0f, 1f)
            }
        }
    }
    Column{
        MyTopBar(
            Color.DarkOrange.copy(alpha = alpha),
            modifier = Modifier.
            fillMaxWidth().
            height(100.dp),
            item.name,
            Color.White,
            {
                IconButton(
                    onClick = {
                        if (navigationController.previousBackStackEntry != null) { navigationController.popBackStack() }
                        restaurantViewModel.resid = 0
                    },
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
                    favoriteState = favoriteViewModel
                )
            },
            Arrangement.Start,
            2f
        )
    }
}