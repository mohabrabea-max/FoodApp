package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.applicationhome.data.models.Favorite
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.data.models.Snake
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.AddBox
import com.example.applicationhome.ui.theme.components.Favorite
import com.example.applicationhome.ui.theme.components.ItemsBox
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.ui.theme.components.SnaksBox
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.BottomBarViewModel
import com.example.applicationhome.view.model.FavoriteViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel
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
    viewModel : ItemScreenViewModel,
    addBoxViewModel: AddBoxViewModel,
    favoriteState : FavoriteViewModel
){
    var favorite = Favorite.favoriteList()
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
            MyTopBar(
                Color.DarkOrange,
                modifier = Modifier.
                fillMaxWidth().
                height(100.dp).
                shadow(elevation = 5.dp),
                "Favorite",
                {
                    IconButton(
                        onClick = {coroutineScope.launch{drawerState.open()}},
                        modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                    ) {
                        Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.White)
                    }
                },
                {
                    IconButton(onClick = {
                        navigationController.navigate(Screens.Search.screen)
                    }) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                    }
                }
            )
            Divider(color = Color.LightGray.copy(alpha = 0.5f))
        }
    ){
        Box(modifier = Modifier.background(Color.White)){
            Box(modifier = Modifier.fillMaxSize()){
                if(favorite.size > 0){
                    LazyVerticalGrid (
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ){
                        item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(100.dp))}
                        items(favorite) { item ->
                            when (item) {
                                is FoodItem -> {
                                    ItemsBox(
                                        item,
                                        navigationController,
                                        viewModel,
                                        {
                                            Favorite(
                                                modifier = Modifier.
                                                clip(CircleShape).
                                                size(35.dp).
                                                background(Color.White.copy(alpha = 1f)),
                                                food = item,
                                                favoriteState = favoriteState
                                            )
                                            AddBox(color = Color.White, food = item, ordernumber = addBoxViewModel)
                                        }
                                    )
                                }
                                is Snake -> {
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
                }else{
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                        Spacer(modifier = Modifier.height(300.dp))
                        AsyncImage(
                            modifier = Modifier.size(120.dp),
                            model = ImageRequest.Builder(LocalContext.current).
                            data(R.drawable.favoriteemptyimage).
                            crossfade(true).
                            size(400, 400).
                            precision(Precision.EXACT).
                            build(),
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
