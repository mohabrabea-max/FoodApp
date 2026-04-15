package com.example.applicationhome.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.applicationhome.R
import com.example.applicationhome.data.models.CategoriesImage
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.components.CategoriesBox
import com.example.applicationhome.ui.theme.components.ItemsBox

@Composable
fun HomeScreen(){
    val menuList = listOf(
        FoodItem(1, "Big Pizza", 200.0, R.drawable.pezzapng, "B", "Pizza - Potato - Drink"),
        FoodItem(2, "Medium Pizza", 150.0, R.drawable.pezzapng, "M", "Pizza - Potato - Drink"),
        FoodItem(3, "Small Pizza", 100.0, R.drawable.pezzapng, "S", "Pizza - Potato - Drink"),
        FoodItem(
            4,
            "10 Pieces Of Chicken",
            380.0,
            R.drawable.chickenpng,
            "10 Pieces",
            "10 Pieces Chicken - Potato - Drink"
        ),
        FoodItem(
            5,
            "5 Pieces Of Chicken",
            210.0,
            R.drawable.chickenpng,
            "5 Pieces",
            "5 Pieces Chicken - Potato - Drink"
        ),
        FoodItem(
            6,
            "3 Pieces Of Chicken",
            130.0,
            R.drawable.chickenpng,
            "3 Pieces",
            "3 Pieces Chicken - Potato - Drink"
        )
    )
    val categoriesList = listOf(
        CategoriesImage("Burger", R.drawable.burgerpng),
        CategoriesImage("Pizza", R.drawable.pezzapng),
        CategoriesImage("Chicken", R.drawable.chickenpng)
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
            item(span = { GridItemSpan(2) }){ LazyRow(
                modifier = Modifier.height(150.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(categoriesList) { category -> CategoriesBox(category) } }
            }

            items(menuList){ item -> ItemsBox(item) }
        }
    }
}