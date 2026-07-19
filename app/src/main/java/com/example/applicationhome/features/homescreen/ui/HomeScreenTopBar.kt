package com.example.applicationhome.features.homescreen.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreenTopBar(
    scrollState : LazyListState,
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    navigationController : NavHostController
){
    val density = LocalDensity.current
    val minOffsetToShowBox = with(density) { 135.dp.toPx() }

    val alpha by remember {
        derivedStateOf {
            val startPx = with(density) { 50.dp.toPx() }
            val endPx = with(density) { 85.dp.toPx() }
            val currentOffset = scrollState.firstVisibleItemScrollOffset.toFloat()
            if(scrollState.firstVisibleItemIndex > 0){
                1f
            }else if(currentOffset < startPx){
                0f
            }else{
                ((currentOffset - startPx) / ( endPx - startPx )).coerceIn(0f, 1f)
            }
        }
    }

    val scal by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 ||
            scrollState.firstVisibleItemScrollOffset > (minOffsetToShowBox)
        }
    }


    Box{
        Column{
            MyTopBar(
                Color.DarkOrange.copy(alpha = alpha),
                modifier = Modifier.shadow(elevation = if (alpha == 1f) 5.dp else 0.dp)
                    .fillMaxWidth().height(100.dp),
                null,
                Color.White,
                {
                    IconButton(
                        onClick = { coroutineScope.launch { drawerState.open() } },
                        modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape).size(35.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.custom_menu),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                {
                    AnimatedVisibility(
                        visible = scal,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        IconButton(
                            onClick = {
                                navigationController.navigate(Screens.Search.screen) {
                                    popUpTo(navigationController.graph.findStartDestination().id) {
                                        saveState = true
                                    }

                                    launchSingleTop = true

                                    restoreState = true
                                }
                            },
                            modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                                .size(35.dp)
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            navigationController.navigate(Screens.Notifications.screen) {
                                popUpTo(navigationController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true

                                restoreState = true
                            }
                        },
                        modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape).size(35.dp)
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        }
    }
}