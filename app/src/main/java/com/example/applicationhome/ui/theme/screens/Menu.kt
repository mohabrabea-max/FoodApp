package com.example.applicationhome.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationhome.data.models.FoodDataSource
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.components.ItemsBox
import com.example.applicationhome.view.model.AddBoxViewModel

@Composable
fun Menu(viewModel: AddBoxViewModel = viewModel()){
    val menu = FoodDataSource.allMenu()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightBrownForBackground
    ){
        LazyVerticalGrid (
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            items(menu){ item -> ItemsBox(item) }
        }
    }
}