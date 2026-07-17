package com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.data.model.Categories
import com.example.applicationhome.data.data.repository.TapRowData.FavoriteTapRow
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.DeepMatteBlack


@Composable
fun CategoriesBar(
    categories: List<Categories>,
    categoriesIsLoading : Boolean,
    selected : Int,
    select : (Categories) -> Unit,
    unSelect : () -> Unit,
){
    Row(
        modifier = Modifier.fillMaxWidth().
        height(120.dp).
        background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ){
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ){
            item { Spacer(modifier = Modifier.width(4.dp)) }
            items(categories.toList()) { category ->
                CategoriesBox(
                    category,
                    categoriesIsLoading,
                    selected,
                    { select(category) },
                    { unSelect() }
                )
            }
            item { Spacer(modifier = Modifier.width(4.dp)) }
        }
    }
}



@Composable
fun CategoriesBarForRestaurantsScreen(
    typ : List<String>,
    selectedTypeIndex : Int = 0,
    selectedType : (Int, String) -> Unit
){
    ScrollableTabRow(
        modifier = Modifier.fillMaxWidth().
        height(50.dp),
        selectedTabIndex = selectedTypeIndex,
        containerColor = Color.White,
        contentColor = Color.Black,
        indicator = { tabPositions ->
            if (selectedTypeIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTypeIndex]),
                    color = Color.DarkOrange
                )
            }
        }
    ){
        typ.forEachIndexed { index, typ ->
            val isSelected = selectedTypeIndex == index
            Tab(
                selected = isSelected,
                onClick = { selectedType(index, typ) },
                text = {
                    Text(
                        text = typ,
                        fontSize = 15.sp,
                        style = if(isSelected) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodySmall,
                        color = if(isSelected) Color.Black else Color.Gray,
                        textAlign = TextAlign.Center
                    )
                },
                selectedContentColor = Color.DarkOrange
            )
        }
    }
}


@Composable
fun favoriteBar(
    selectedCategoryInFavoriteScreen : Int,
    selectedFavoriteScreen : (Int) -> Unit
){
    TabRow(
        modifier = Modifier.fillMaxWidth().
        height(50.dp),
        selectedTabIndex = selectedCategoryInFavoriteScreen,
        containerColor = Color.White,
        contentColor = Color.DeepMatteBlack,
        indicator = { tabPositions ->
            if (selectedCategoryInFavoriteScreen < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedCategoryInFavoriteScreen]),
                    color = Color.DarkOrange
                )
            }
        }
    ){
        FavoriteTapRow.forEachIndexed { index, title ->
            val isSelected = index == selectedCategoryInFavoriteScreen
            Tab(
                selected = true,
                onClick = { selectedFavoriteScreen(index) },
                text = {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        style = if(isSelected) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodySmall,
                        color = if(isSelected) Color.Black else Color.Gray,
                        textAlign = TextAlign.Center
                    )
                },
                selectedContentColor = Color.DarkOrange
            )
        }
    }
}