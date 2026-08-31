package com.example.applicationhome.core.ui.components.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.core.ui.components.model.DashboardScreenViewModel
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomBar(
    navigationController : NavHostController,
    dashboardNavController : NavHostController,
    currentRoute :  String?,
    dashboardScreenViewModel: DashboardScreenViewModel,
    homeListState : LazyListState,
    favoriteListState : LazyGridState,
    settingsListState : LazyGridState,
    scope : CoroutineScope,
    profileLongClick : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    val totalInFavorite by dashboardScreenViewModel.totalInFavorite.collectAsStateWithLifecycle()
    val cartCount by dashboardScreenViewModel.totalNumberInCart.collectAsStateWithLifecycle()

    val isHomeActive = currentRoute == Screens.HomeScreen.screen
    val isFavoriteActive = currentRoute == Screens.Favorite.screen
    val isSettingsActive = currentRoute == Screens.Settings.screen


    Box(
        modifier = Modifier
            .width(280.dp)
            .height(60.dp)
            .shadow(elevation = 10.dp, spotColor = Color.Black, shape = RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp)
            .pointerInput(Unit) {
                detectTapGestures { }
            }
    ){
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(30.dp))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ){
                        if(!isHomeActive){
                            dashboardNavController.navigate(Screens.HomeScreen.screen){
                                popUpTo(dashboardNavController.graph.findStartDestination().id) {
                                    saveState = true // احفظ حالة الصفحة اللي أنا خارج منها (زي السكرول)
                                }
                                // 2. ميكررش نفس الصفحة لو أنا دوست عليها وأنا واقف فيها
                                launchSingleTop = true

                                restoreState = true  // 3. يرجع الحالة اللي كانت محفوظة لما أرجع للصفحة دي تاني
                            }
                        }else{
                            scope.launch { homeListState.animateScrollToItem(0) }
                        }
                    },
                contentAlignment = Alignment.Center
            ){
                Icon(
                    Icons.Outlined.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(28.dp),
                    tint = if(isHomeActive) Color.DarkOrange else Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(30.dp))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ){
                        if(!isFavoriteActive){
                            dashboardNavController.navigate(Screens.Favorite.screen){
                                popUpTo(dashboardNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true

                                restoreState = true
                            }
                        }else{
                            scope.launch { favoriteListState.animateScrollToItem(0) }
                        }
                    },
                contentAlignment = Alignment.Center
            ){
                BadgedBox(
                    badge = {
                        if(totalInFavorite > 0){
                            Badge(
                                containerColor = Color.DarkOrange,
                                contentColor = Color.White
                            ){
                                Text(text = "$totalInFavorite")
                            }
                        }
                    }
                ){
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        modifier = Modifier.size(28.dp),
                        tint = if(isFavoriteActive) Color.DarkOrange else Color.Gray
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(30.dp))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ){
                        navigationController.navigate(Screens.Cart.screen){
                            launchSingleTop = true
                        }
                    },
                contentAlignment = Alignment.Center
            ){
                BadgedBox(
                    badge = {
                        if(cartCount > 0){
                            Badge(
                                containerColor = Color.DarkOrange,
                                contentColor = Color.White
                            ){
                                if(cartCount <= 99) Text(text = "$cartCount") else Text(text = "+99")
                            }
                        }
                    }
                ){
                    Icon(
                        Icons.Outlined.ShoppingCart,
                        contentDescription = "Cart",
                        modifier = Modifier.size(28.dp),
                        tint = Color.Gray
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(30.dp))
                    .combinedClickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {
                            if(!isSettingsActive){
                                dashboardNavController.navigate(Screens.Settings.screen){
                                    popUpTo(dashboardNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }

                                    launchSingleTop = true

                                    restoreState = true
                                }
                            }else{
                                scope.launch { settingsListState.animateScrollToItem(0) }
                            }
                        },

                        onLongClick = {
                            profileLongClick()
                        }
                    ),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = "Settings",
                    modifier = Modifier.size(28.dp),
                    tint = if(isSettingsActive) Color.DarkOrange else Color.Gray
                )
            }
        }
    }
}
