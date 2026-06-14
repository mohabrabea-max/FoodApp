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
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.repository.MenuRepository
import com.example.applicationhome.data.models.repository.TapRowData.FavoriteTapRow
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.DeepMatteBlack
import com.example.applicationhome.ui.theme.model.CategoriesBoxViewModel

@Composable
fun CategoriesBar(categoriesBoxViewModel : CategoriesBoxViewModel){
    val categories = MenuRepository.categories
    Row(
        modifier = Modifier.fillMaxWidth().
        height(50.dp).
        background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ){
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            item { Spacer(modifier = Modifier.width(4.dp)) }
            items(categories) { category ->
                CategoriesBox(category, categoriesBoxViewModel)
            }
            item { Spacer(modifier = Modifier.width(4.dp)) }
        }
    }
}



@Composable
fun CategoriesBarForRestaurantsScreen(res : Restaurants, categoriesBoxViewModel : CategoriesBoxViewModel){
    val categories = res.typ
    ScrollableTabRow(
        modifier = Modifier.fillMaxWidth().
        height(50.dp),
        selectedTabIndex = categoriesBoxViewModel.selectedTypeIndex,
        containerColor = Color.White,
        contentColor = Color.Black,
        indicator = { tabPositions ->
            if (categoriesBoxViewModel.selectedTypeIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[categoriesBoxViewModel.selectedTypeIndex]),
                    color = Color.DarkOrange
                )
            }
        }
    ){
        categories.forEachIndexed { index, typ ->
            val isSelected = categoriesBoxViewModel.selectedTypeIndex == index
            Tab(
                selected = isSelected,
                onClick = { categoriesBoxViewModel.selectedtype(index, typ) },
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
fun favoriteBar(categoriesBoxViewModel: CategoriesBoxViewModel){
    TabRow(
        modifier = Modifier.fillMaxWidth().
        height(50.dp),
        selectedTabIndex = categoriesBoxViewModel.selectedCategorieInFavoriteScreen,
        containerColor = Color.White,
        contentColor = Color.DeepMatteBlack,
        indicator = { tabPositions ->
            if (categoriesBoxViewModel.selectedCategorieInFavoriteScreen < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[categoriesBoxViewModel.selectedCategorieInFavoriteScreen]),
                    color = Color.DarkOrange
                )
            }
        }
    ){
        FavoriteTapRow.forEachIndexed { index, title ->
            val isSelected = index == categoriesBoxViewModel.selectedCategorieInFavoriteScreen
            Tab(
                selected = true,
                onClick = { categoriesBoxViewModel.selectedFavoriteScreen(index) },
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