package com.example.applicationhome

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
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.components.ItemsBox

@Composable
fun Menu(){
    val menuList = listOf(
        FoodItem(1, "Big Pizza", 200.0, R.drawable.pezzapng, "B", "Pizza - Potato - Drink"),
        FoodItem(2, "Medium Pizza", 150.0, R.drawable.pezzapng, "M", "Pizza - Potato - Drink"),
        FoodItem(3, "Small Pizza", 100.0, R.drawable.pezzapng, "S", "Pizza - Potato - Drink"),
        FoodItem(4, "10 Pieces Of Chicken", 380.0, R.drawable.chickenpng, "10 Pieces", "10 Pieces Chicken - Potato - Drink"),
        FoodItem(5, "5 Pieces Of Chicken", 210.0, R.drawable.chickenpng, "5 Pieces", "5 Pieces Chicken - Potato - Drink"),
        FoodItem(6, "3 Pieces Of Chicken", 130.0, R.drawable.chickenpng, "3 Pieces", "3 Pieces Chicken - Potato - Drink")
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightBrownForBackground
    ){
        LazyVerticalGrid (
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            items(menuList){ item -> ItemsBox(item) }
        }
    }
}