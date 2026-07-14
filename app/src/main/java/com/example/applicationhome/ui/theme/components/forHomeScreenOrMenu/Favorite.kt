package com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.applicationhome.ui.theme.DarkOrange
import kotlinx.coroutines.launch

@Composable
fun Favorite(
    isMealInFavorite : Boolean,
    addMealFavorite : () -> Unit,
    removeMealFavorite : () -> Unit,
    modifier: Modifier = Modifier,
    modifier2 : Modifier = Modifier
){
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    fun favorite1(){
        if(isMealInFavorite){
            removeMealFavorite()
        }else{
            addMealFavorite()
        }

        scope.launch {
            // يكبر بسرعة لـ 1.3x في 100 مللي ثانية
            scale.animateTo(1.3f, animationSpec = tween(100))
            // يرجع لحجمه الطبيعي 1x بسرعة برضه
            scale.animateTo(1f, animationSpec = tween(100))
        }
    }

    IconButton(modifier = modifier, onClick = {favorite1()}){
        if(!isMealInFavorite) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "More",
                tint = Color.DarkOrange,
                modifier = modifier2.size(20.dp).scale(scale.value)
            )
        }else{
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "More",
                tint = Color.DarkOrange,
                modifier = modifier2.size(20.dp).scale(scale.value)
            )
        }
    }
}



@Composable
fun FavoriteSnacks(
    isSnackInFavorite : Boolean,
    addSnackFavorite : () -> Unit,
    removeSnackFavorite : () -> Unit,
    modifier : Modifier = Modifier,
    modifier2 : Modifier = Modifier,
){
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    fun favorite1(){
        if(isSnackInFavorite){
            removeSnackFavorite()
        }else{
            addSnackFavorite()
        }

        scope.launch {
            // يكبر بسرعة لـ 1.3x في 100 مللي ثانية
            scale.animateTo(1.3f, animationSpec = tween(100))
            // يرجع لحجمه الطبيعي 1x بسرعة برضه
            scale.animateTo(1f, animationSpec = tween(100))
        }
    }

    IconButton(modifier = modifier, onClick = {favorite1()}){
        if(!isSnackInFavorite) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "More",
                tint = Color.DarkOrange,
                modifier = modifier2.size(20.dp).scale(scale.value)
            )
        }else{
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "More",
                tint = Color.DarkOrange,
                modifier = modifier2.size(20.dp).scale(scale.value)
            )
        }
    }
}



@Composable
fun FavoriteRestaurant(
    isRestaurantInFavorite : Boolean,
    addRestaurantsFavorite : () -> Unit,
    removeRestaurantsFavorite : () -> Unit,
    modifier: Modifier = Modifier,
    modifier2 : Modifier = Modifier,
    color : Color = Color.Black,
){
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    fun favorite1(){
        if(isRestaurantInFavorite){
            removeRestaurantsFavorite()
        }else{
            addRestaurantsFavorite()
        }

        scope.launch {
            // يكبر بسرعة لـ 1.3x في 100 مللي ثانية
            scale.animateTo(1.3f, animationSpec = tween(100))
            // يرجع لحجمه الطبيعي 1x بسرعة برضه
            scale.animateTo(1f, animationSpec = tween(100))
        }
    }

    IconButton(modifier = modifier, onClick = {favorite1()}){
        if(!isRestaurantInFavorite) {
            Icon(
                imageVector = Icons.Default.BookmarkBorder,
                contentDescription = "More",
                tint = color,
                modifier = modifier2.size(20.dp).scale(scale.value)
            )
        }else{
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = "More",
                tint = Color.DarkOrange,
                modifier = modifier2.size(20.dp).scale(scale.value)
            )
        }
    }
}