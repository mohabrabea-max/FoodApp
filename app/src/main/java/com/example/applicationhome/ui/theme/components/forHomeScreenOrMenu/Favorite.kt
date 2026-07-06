package com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu

import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.applicationhome.data.models.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.models.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.models.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import kotlinx.coroutines.launch

@Composable
fun Favorite(
    modifier: Modifier = Modifier,
    modifier2 : Modifier = Modifier,
    food: FavoriteFoodDatabase,
    favoriteViewModel : FavoriteViewModel
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    val favorite by favoriteViewModel.isMealInFavorite(food.mealId).collectAsState(initial = false)

    fun favorite1(){
        if(favorite){
            favoriteViewModel.removeMealFavorite(food.mealId)
            Toast.makeText(context, "Remove From Favorite", Toast.LENGTH_SHORT).show()
        }else{
            favoriteViewModel.addMealFavorite(food)
            Toast.makeText(context, "Add To Favorite", Toast.LENGTH_SHORT).show()
        }

        scope.launch {
            // يكبر بسرعة لـ 1.3x في 100 مللي ثانية
            scale.animateTo(1.3f, animationSpec = tween(100))
            // يرجع لحجمه الطبيعي 1x بسرعة برضه
            scale.animateTo(1f, animationSpec = tween(100))
        }
    }

    IconButton(modifier = modifier, onClick = {favorite1()}){
        if(!favorite) {
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
    modifier : Modifier = Modifier,
    modifier2 : Modifier = Modifier,
    snack : FavoriteSnacksDatabase,
    favoriteViewModel : FavoriteViewModel
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    val favorite by favoriteViewModel.isSnackInFavorite(snack.snackId).collectAsState(initial = false)

    fun favorite1(){
        if(favorite){
            favoriteViewModel.removeSnackFavorite(snack.snackId)
            Toast.makeText(context, "Remove From Favorite", Toast.LENGTH_SHORT).show()
        }else{
            favoriteViewModel.addSnackFavorite(snack)
            Toast.makeText(context, "Add To Favorite", Toast.LENGTH_SHORT).show()
        }

        scope.launch {
            // يكبر بسرعة لـ 1.3x في 100 مللي ثانية
            scale.animateTo(1.3f, animationSpec = tween(100))
            // يرجع لحجمه الطبيعي 1x بسرعة برضه
            scale.animateTo(1f, animationSpec = tween(100))
        }
    }

    IconButton(modifier = modifier, onClick = {favorite1()}){
        if(!favorite) {
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
    modifier: Modifier = Modifier,
    modifier2 : Modifier = Modifier,
    color : Color = Color.Black,
    restaurants: FavoriteRestaurantDatabase,
    favoriteViewModel : FavoriteViewModel
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    val favorite by favoriteViewModel.isRestaurantInFavorite(restaurants.restaurantId).collectAsState(initial = false)

    fun favorite1(){
        if(favorite){
            favoriteViewModel.removeRestaurantsFavorite(restaurants.restaurantId)
            Toast.makeText(context, "Remove From Favorite", Toast.LENGTH_SHORT).show()
        }else{
            favoriteViewModel.addRestaurantsFavorite(restaurants)
            Toast.makeText(context, "Add To Favorite", Toast.LENGTH_SHORT).show()
        }

        scope.launch {
            // يكبر بسرعة لـ 1.3x في 100 مللي ثانية
            scale.animateTo(1.3f, animationSpec = tween(100))
            // يرجع لحجمه الطبيعي 1x بسرعة برضه
            scale.animateTo(1f, animationSpec = tween(100))
        }
    }

    IconButton(modifier = modifier, onClick = {favorite1()}){
        if(!favorite) {
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