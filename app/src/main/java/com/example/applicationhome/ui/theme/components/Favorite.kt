package com.example.applicationhome.ui.theme.components

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.view.model.FavoriteViewModel
import kotlinx.coroutines.launch

@Composable
fun Favorite(modifier: Modifier = Modifier, id: Int, favoritState : FavoriteViewModel = viewModel()){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    val favorite = favoritState.itemsCount[id] ?: false

    fun favorite1(){

        if(favorite == false){
            favoritState.addFavorite(id)
            Toast.makeText(context, "Add To Favorite", Toast.LENGTH_SHORT).show()
        }else{
            favoritState.removeFavorite(id)
            Toast.makeText(context, "Remove From Favorite", Toast.LENGTH_SHORT).show()
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
                tint = Color.BrownForFont,
                modifier = Modifier.size(20.dp).scale(scale.value)
            )
        }else{
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "More",
                tint = Color.Red,
                modifier = Modifier.size(20.dp).scale(scale.value)
            )
        }
    }
}