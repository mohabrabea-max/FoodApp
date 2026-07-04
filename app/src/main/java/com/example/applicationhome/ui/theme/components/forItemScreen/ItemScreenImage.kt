package com.example.applicationhome.ui.theme.components.forItemScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision

@Composable
fun ItemScreenImage(
    scrollState : LazyListState,
    image : String
){
    Box(
        modifier = Modifier.size(350.dp).
        clip(RoundedCornerShape(10.dp)).
        graphicsLayer {
            // بنخلي الصورة تتحرك بنص سرعة السكرول (Parallax)
            // وبنخليها تنزل لتحت شوية عشان اللي تحتها يغطيها
            translationY = scrollState.firstVisibleItemScrollOffset * 1f
            val scale = 1f - (scrollState.firstVisibleItemScrollOffset.toFloat() / 4000f).coerceIn(0f, 0.2f)  // تأثير التصغير (Scale)
            scaleX = scale
            scaleY = scale
            1f - (scrollState.firstVisibleItemScrollOffset.toFloat() / 1000f).coerceIn(0f, 1f) // بنخلي الصورة دايماً "ورا" الحاجات التانية
        }
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).
            data(image).
            crossfade(true).
            precision(Precision.EXACT).
            build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
    }
}