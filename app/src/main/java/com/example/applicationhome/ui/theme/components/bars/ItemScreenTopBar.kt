package com.example.applicationhome.ui.theme.components.bars

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.designsystem.TopBarButtons
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.Favorite

@Composable
fun ItemScreenTopBar(
    scrollState : LazyListState,
    isMealInFavorite : Boolean,
    backStack : () -> Unit,
    navigation : () -> Unit,
    addMealFavorite : () -> Unit,
    removeMealFavorite : () -> Unit,
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
    val searchSize by remember {
        derivedStateOf {
            if(scrollState.firstVisibleItemIndex >= 1){
                3f
            }else{
                ((scrollState.firstVisibleItemScrollOffset / 300f) - 1f).coerceIn(1f, 3f)
            }
        }
    }

    Column{
        MyTopBar(
            Color.DarkOrange.copy(alpha = alpha),
            modifier = Modifier.
            fillMaxWidth().
            height(100.dp),
            null,
            Color.White,
            {
                TopBarButtons(
                    { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black) },
                    { backStack() }
                )
            },
            {
                Box(
                    modifier = Modifier.animateContentSize().padding(5.dp).
                    shadow(elevation = 7.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(30.dp)).
                    width(if(searchSize > 1) 120.dp else 40.dp).
                    height(40.dp).
                    background(Color.White).
                    clickable {
                        navigation()
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
                    isMealInFavorite,
                    { addMealFavorite() },
                    { removeMealFavorite() },
                    modifier = Modifier.padding(5.dp).
                    shadow(elevation = 7.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(30.dp)).
                    size(40.dp).
                    background(Color.White),
                    modifier2 = Modifier.size(25.dp),
                    color = Color.DarkOrange,
                    icon1 = Icons.Default.Favorite,
                    icon2 = Icons.Default.FavoriteBorder
                )
            },
            weight = 2f
        )
    }
}