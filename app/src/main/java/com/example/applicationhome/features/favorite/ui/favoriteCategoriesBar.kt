package com.example.applicationhome.features.favorite.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.domain.model.TapRowData.FavoriteTapRow
import com.example.applicationhome.core.ui.theme.DarkOrange

@Composable
fun FavoriteCategoriesBar(
    selectedCategoryInFavoriteScreen : Int,
    selectedFavoriteScreen : (Int) -> Unit
){
    TabRow(
        modifier = Modifier.fillMaxWidth().
        height(50.dp),
        selectedTabIndex = selectedCategoryInFavoriteScreen,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = { tabPositions ->
            if (selectedCategoryInFavoriteScreen < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedCategoryInFavoriteScreen]),
                    color = Color.DarkOrange
                )
            }
        }
    ){
        FavoriteTapRow().forEachIndexed { index, title ->
            val isSelected = index == selectedCategoryInFavoriteScreen
            Tab(
                selected = true,
                onClick = { selectedFavoriteScreen(index) },
                text = {
                    Text(
                        text = stringResource(title),
                        fontSize = 15.sp,
                        style = if(isSelected) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodySmall,
                        color = if(isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                },
                selectedContentColor = Color.DarkOrange
            )
        }
    }
}