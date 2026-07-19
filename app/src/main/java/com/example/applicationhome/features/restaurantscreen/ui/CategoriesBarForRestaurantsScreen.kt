package com.example.applicationhome.features.restaurantscreen.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.ui.theme.DarkOrange

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
