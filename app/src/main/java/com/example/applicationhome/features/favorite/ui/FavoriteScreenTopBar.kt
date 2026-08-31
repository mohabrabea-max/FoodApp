package com.example.applicationhome.features.favorite.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreenTopBar(
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    selectedCategoryInFavoriteScreen : Int,
    navigation : () -> Unit,
    selectedFavoriteScreen : (Int) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(146.dp)
            .shadow(elevation = 3.dp)
    ){
        MyTopBar(
            MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth().height(100.dp).shadow(elevation = 5.dp),
            stringResource(R.string.favorite),
            MaterialTheme.colorScheme.onSurface,
            {
                IconButton(
                    onClick = { coroutineScope.launch { drawerState.open() } },
                    modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                ) {
                    Icon(
                        painterResource(id = R.drawable.custom_menu),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            {
                IconButton(onClick = { navigation() }){
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )
        favoriteCategoriesBar(
            selectedCategoryInFavoriteScreen,
            { item -> selectedFavoriteScreen(item) }
        )
    }
}