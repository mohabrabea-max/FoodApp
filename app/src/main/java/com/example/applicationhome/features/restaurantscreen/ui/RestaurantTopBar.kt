package com.example.applicationhome.features.restaurantscreen.ui

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.bars.RestaurantScreenTopBar
import com.example.applicationhome.core.ui.components.designsystem.TopBarButtons
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.Favorite
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.local.entity.RestaurantsEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantTopBar(
    searchSize : Float,
    item : RestaurantsEntity,
    isRestaurantInFavorite : Boolean,
    scrollState : LazyListState,
    addToFavorite : () -> Unit,
    removeFromFavorite : () -> Unit,
    popBackStack : () -> Unit,
    navigation : () -> Unit
){
    val alpha by remember {
        derivedStateOf {
            if(scrollState.firstVisibleItemIndex >= 1){
                1f
            }else{
                ((scrollState.firstVisibleItemScrollOffset / 150f) - 1f).coerceIn(0f, 1f)
            }
        }
    }


    Column{
        RestaurantScreenTopBar(
            color = Color.DarkOrange.copy(alpha = alpha),
            modifier = Modifier.fillMaxWidth().height(100.dp),
            title = item.name,
            titleColor = Color.White.copy(alpha = alpha),
            startaction = {
                TopBarButtons(
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = { popBackStack() },
                    border = 0.dp
                )
            },
            actions = {
                Box(
                    modifier = Modifier.animateContentSize().padding(5.dp).shadow(
                        elevation = 7.dp,
                        spotColor = Color.VeryLightGray.copy(0.5f),
                        shape = RoundedCornerShape(30.dp)
                    ).width(if (searchSize > 1) 120.dp else 40.dp).height(40.dp)
                        .background(MaterialTheme.colorScheme.surface).clickable {
                            navigation()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        if (searchSize > 1) Text(
                            text = stringResource(R.string.search),
                            softWrap = false,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }

                Favorite(
                    isRestaurantInFavorite,
                    {
                        addToFavorite()
                    },
                    { removeFromFavorite() },
                    modifier = Modifier.padding(5.dp).shadow(
                        elevation = 7.dp,
                        spotColor = Color.VeryLightGray.copy(0.5f),
                        shape = RoundedCornerShape(30.dp)
                    ).size(40.dp).background(MaterialTheme.colorScheme.surface),
                    modifier2 = Modifier.size(25.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    icon1 = Icons.Default.Bookmark,
                    icon2 = Icons.Default.BookmarkBorder
                )
            }
        )
    }
}