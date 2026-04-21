package com.example.applicationhome.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.FoodDataSource
import com.example.applicationhome.data.models.VarietiesMenu
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.components.CartButton
import com.example.applicationhome.ui.theme.components.CategoriesBox
import com.example.applicationhome.ui.theme.components.ItemsBox
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun HomeScreen(drawerState : DrawerState, coroutineScope : CoroutineScope, navigationController : NavHostController, cartNumber : AddBoxViewModel = viewModel(), viewModel: ItemScreenViewModel){
    val menu = FoodDataSource.allMenu()
    val categories = VarietiesMenu.categoriesList()
    Box(modifier = Modifier.fillMaxSize().background(Color.LightBrownForBackground)){
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
        Box(modifier = Modifier.fillMaxWidth().height(60.dp).align(Alignment.BottomCenter).background(Color.BrownForFont.copy(alpha = 0.1f)), contentAlignment = Alignment.Center){
            CartButton()
        }
    }
}