package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.FoodDataSource
import com.example.applicationhome.data.models.RestaurantsMenu
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.data.models.VarietiesMenu
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.AddBox
import com.example.applicationhome.ui.theme.components.Favorite
import com.example.applicationhome.ui.theme.components.ItemsBox
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.ui.theme.components.RestaurantsBox
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.FavoriteViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    navigationController : NavHostController,
    viewModel: ItemScreenViewModel,
    addBoxViewModel: AddBoxViewModel,
    favoriteState : FavoriteViewModel
){
    val categories = VarietiesMenu.categoriesList()
    val restaurants = RestaurantsMenu.restaurantsMenu()
    val pezza = FoodDataSource.pizzaMenu()
    val chicken = FoodDataSource.chickenMenu()
    val burger = FoodDataSource.burgerMenu()
    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.LightBrownForBackground),
        topBar = {
            MyTopBar(
                modifier = Modifier.
                fillMaxWidth().
                height(100.dp).
                shadow(elevation = 5.dp),
                "Menu",
                {navigationController.popBackStack()},
                {Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)},
                {
                    IconButton(onClick = {
                        navigationController.navigate(Screens.Notifications.screen){
                            popUpTo(navigationController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }
                    }) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.Black)
                    }
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
                }
            )
        }
    ){innerPadding ->
        Box(modifier = Modifier.fillMaxSize().background(Color.White).padding(innerPadding)){
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                item(span = { GridItemSpan(2) }){
                    Text(
                        text = "Pizza",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontSize = 25.sp,
                    )
                }
                item(span = { GridItemSpan(2) }){
                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically){
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            items(restaurants){item ->
                                RestaurantsBox(item, "Pizza", favoriteState)
                            }
                        }
                    }
                }
                items(pezza){ item ->
                    ItemsBox(
                        item,
                        navigationController,
                        viewModel,
                        null,
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
                            AddBox(color = Color.White, food = item, ordernumber = addBoxViewModel)
                        }
                    )
                }
                item(span = { GridItemSpan(2) }){
                    Text(
                        text = "Chicken",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontSize = 25.sp,
                    )
                }
                item(span = { GridItemSpan(2) }){
                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically){
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            items(restaurants){item ->
                                RestaurantsBox(item, "Chicken", favoriteState)
                            }
                        }
                    }
                }
                items(chicken){ item ->
                    ItemsBox(
                        item,
                        navigationController,
                        viewModel,
                        null,
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
                            AddBox(color = Color.White, food = item, ordernumber = addBoxViewModel)
                        }
                    )
                }
                item(span = { GridItemSpan(2) }){
                    Text(
                        text = "Burger",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontSize = 25.sp,
                    )
                }
                item(span = { GridItemSpan(2) }){
                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically){
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            items(restaurants){item ->
                                RestaurantsBox(item, "Burger", favoriteState)
                            }
                        }
                    }
                }
                items(burger){ item ->
                    ItemsBox(
                        item,
                        navigationController,
                        viewModel,
                        null,
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
                            AddBox(color = Color.White, food = item, ordernumber = addBoxViewModel)
                        }
                    )
                }
                item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(80.dp))}
            }
        }
    }
}