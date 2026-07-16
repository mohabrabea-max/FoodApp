package com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.applicationhome.ui.theme.DarkOrange
import kotlinx.coroutines.launch

@Composable
fun Favorite(
    isMealInFavorite : Boolean,
    addMealFavorite : () -> Unit,
    removeMealFavorite : () -> Unit,
    modifier: Modifier = Modifier,
    modifier2 : Modifier = Modifier,
    color : Color,
    icon1 : ImageVector,
    icon2 : ImageVector
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
        if(isMealInFavorite) {
            Icon(
                imageVector = icon1,
                contentDescription = "More",
                tint = Color.DarkOrange,
                modifier = modifier2.size(20.dp).scale(scale.value)
            )
        }else{
            Icon(
                imageVector = icon2,
                contentDescription = "More",
                tint = color,
                modifier = modifier2.size(20.dp).scale(scale.value)
            )
        }
    }
}