package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.FoodDataSource
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.data.models.VarietiesMenu
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.components.CartButton
import com.example.applicationhome.ui.theme.components.CategoriesBox
import com.example.applicationhome.ui.theme.components.ItemsBox
import com.example.applicationhome.ui.theme.components.MyBottonBar
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.view.model.ItemScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(scrollBehavior: TopAppBarScrollBehavior, drawerState : DrawerState, coroutineScope : CoroutineScope, navigationController : NavHostController, viewModel: ItemScreenViewModel){
    val menu = FoodDataSource.allMenu()
    val categories = VarietiesMenu.categoriesList()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).
        fillMaxSize().
        background(Color.LightBrownForBackground),
        topBar = { MyTopBar(scrollBehavior, {coroutineScope.launch{drawerState.open()}}, {navigationController.navigate(Screens.Notifications.screen)}, Icons.Default.Notifications, {navigationController.navigate(Screens.Search.screen)}, Icons.Default.Search) },
        bottomBar = { MyBottonBar(navigationController) }
        //floatingActionButton = { CartButton() }
    ){innerPadding ->
        Box(modifier = Modifier.background(Color.LightBrownForBackground).padding(innerPadding)){
            LazyVerticalGrid (
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                item(span = { GridItemSpan(2) }){ LazyRow(
                    modifier = Modifier.height(150.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    items(categories) { category -> CategoriesBox(category) } }
                }

                items(menu){ item -> ItemsBox(item, drawerState, coroutineScope, navigationController, viewModel) }
            }
            Box(modifier = Modifier.fillMaxWidth().height(60.dp).align(Alignment.BottomCenter), contentAlignment = Alignment.Center){
                CartButton()
            }
        }
    }
}