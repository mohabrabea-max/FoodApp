package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.data.models.repository.FavoriteRepository.favoritList
import com.example.applicationhome.data.models.repository.FavoriteRepository.mealsFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.mealsFavoriteIsLoading
import com.example.applicationhome.data.models.repository.FavoriteRepository.restaurantsFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.restaurantsFavoriteIsLoading
import com.example.applicationhome.data.models.repository.FavoriteRepository.snacksFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.snacksFavoriteIsLoading
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DeepMatteBlack
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.AddBox
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.Favorite
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.ItemsBox
import com.example.applicationhome.ui.theme.components.bars.MyBottonBar
import com.example.applicationhome.ui.theme.components.bars.MyTopBar
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.RestaurantsBox
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.SnaksBox
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.favoriteBar
import com.example.applicationhome.ui.theme.model.AddBoxViewModel
import com.example.applicationhome.ui.theme.model.BottomBarViewModel
import com.example.applicationhome.ui.theme.model.CategoriesBoxViewModel
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel
import com.example.applicationhome.ui.theme.model.RestaurantViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorite(
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    navigationController : NavHostController,
    viewModelForBottomBar: BottomBarViewModel,
    itemScreenViewModel : ItemScreenViewModel,
    addBoxViewModel: AddBoxViewModel,
    favoriteViewModel : FavoriteViewModel,
    categoriesBoxViewModel : CategoriesBoxViewModel,
    restaurantViewModel: RestaurantViewModel,
    bottomBarViewModel: BottomBarViewModel
){
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
            Column(
                modifier = Modifier.fillMaxWidth().height(146.dp).
                shadow(elevation = 3.dp)
            ){
                MyTopBar(
                    Color.White,
                    modifier = Modifier.
                    fillMaxWidth().
                    height(100.dp).
                    shadow(elevation = 5.dp),
                    "Favorite",
                    Color.DeepMatteBlack,
                    {
                        IconButton(
                            onClick = {coroutineScope.launch{drawerState.open()}},
                            modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                        ) {
                            Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.DeepMatteBlack)
                        }
                    },
                    {
                        IconButton(onClick = {
                            navigationController.navigate(Screens.Search.screen)
                        }) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.DeepMatteBlack)
                        }
                    }
                )
                favoriteBar(categoriesBoxViewModel)
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier.navigationBarsPadding().fillMaxWidth().
                pointerInput(Unit) { detectTapGestures { } },
                contentAlignment = Alignment.BottomCenter
            ){
                MyBottonBar(navigationController, bottomBarViewModel, addBoxViewModel, favoriteViewModel)
            }
        }
    ){
        Box(modifier = Modifier.background(Color.White)){
            Box(modifier = Modifier.fillMaxSize()){
                if(favoritList.size > 0){
                    LazyVerticalGrid (
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ){
                        item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(146.dp))}
                        if(categoriesBoxViewModel.selectedCategorieInFavoriteScreen == 2) {
                            items(restaurantsFavorite) { item ->
                                RestaurantsBox(
                                    restaurantsFavoriteIsLoading,
                                    item,
                                    favoriteViewModel,
                                    itemScreenViewModel,
                                    navigationController,
                                    categoriesBoxViewModel,
                                    restaurantViewModel
                                )
                            }
                        }
                        if(categoriesBoxViewModel.selectedCategorieInFavoriteScreen == 1) {
                            item(span = { GridItemSpan(2) }) { Spacer(modifier = Modifier.height(15.dp)) }
                            items(snacksFavorite) { item ->
                                SnaksBox(
                                    snacksFavoriteIsLoading,
                                    modifier = Modifier.size(200.dp),
                                    false,
                                    item,
                                    null,
                                    navigationController,
                                    itemScreenViewModel,
                                    {
                                        Favorite(
                                            modifier = Modifier.clip(CircleShape).border(
                                                width = 0.5.dp,
                                                color = Color.Gray.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(30.dp)
                                            ).size(35.dp).background(Color.VeryLightGray),
                                            food = item,
                                            favoriteState = favoriteViewModel
                                        )
                                        AddBox(
                                            color = Color.VeryLightGray,
                                            food = item,
                                            addBoxViewModel
                                        )
                                    }
                                )
                            }
                        }
                        if(categoriesBoxViewModel.selectedCategorieInFavoriteScreen == 0) {
                            item(span = { GridItemSpan(2) }) { Spacer(modifier = Modifier.height(15.dp)) }
                            items(mealsFavorite) { item ->
                                ItemsBox(
                                    mealsFavoriteIsLoading,
                                    item,
                                    navigationController,
                                    itemScreenViewModel,
                                    {
                                        Favorite(
                                            modifier = Modifier.clip(CircleShape).size(35.dp)
                                                .background(Color.White.copy(alpha = 1f)),
                                            food = item,
                                            favoriteState = favoriteViewModel
                                        )
                                        AddBox(color = Color.White, food = item, addBoxViewModel)
                                    }
                                )
                            }
                        }
                        item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(100.dp))}
                    }
                }else{
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                        Spacer(modifier = Modifier.height(300.dp))
                        Image(
                            modifier = Modifier.size(120.dp),
                            painter = painterResource(R.drawable.favoriteemptyimage),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "There's nothing in your wishlist",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Box(
                            modifier = Modifier.width(100.dp).
                            height(40.dp).
                            clip(CircleShape).
                            clickable{
                                navigationController.navigate(Screens.HomeScreen.screen){
                                    popUpTo(navigationController.graph.findStartDestination().id) {
                                        saveState = true
                                    }

                                    launchSingleTop = true

                                    restoreState = true
                                }
                                viewModelForBottomBar.home()
                            }.
                            border(width = 1.dp, color = Color.BrownForFont, shape = RoundedCornerShape(40.dp)).
                            padding(7.dp).align(Alignment.CenterHorizontally)
                        ){
                            Text(
                                text = "Add items",
                                fontSize = 15.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.BrownForFont,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}
