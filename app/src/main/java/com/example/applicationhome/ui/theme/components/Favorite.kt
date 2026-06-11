package com.example.applicationhome.ui.theme.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.applicationhome.data.models.model.Food
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun Favorite(
    modifier: Modifier = Modifier,
    modifier2 : Modifier = Modifier,
    food: Food,
    favoriteState : FavoriteViewModel
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    val favorite = favoriteState.isMealInFavorite(food.id)
    val type = when(food){
        is FoodItem -> {"Meal"}
        is Snack -> {"Snack"}
    }
    fun favorite1(){
        if(favorite == true){
            favoriteState.removeFavorite(food, type)
            Toast.makeText(context, "Remove From Favorite", Toast.LENGTH_SHORT).show()
        }else{
            favoriteState.addFavorite(food)
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
        if(favorite == false) {
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