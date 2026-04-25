package com.example.applicationhome.ui.theme.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.applicationhome.data.models.CategoriesImage

@Composable
fun CategoriesBox(category : CategoriesImage){
    val context = LocalContext.current.applicationContext
    Box(
        modifier = Modifier.
        size(75.dp).
        clip(CircleShape).
        background(Color.LightGray.copy(alpha = 0.5f)).
        clickable{ Toast.makeText(context, category.name, Toast.LENGTH_SHORT).show()},
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = category.icon),
            contentDescription = "${category.name} Logo",
            modifier = Modifier.
            fillMaxSize().
            padding(20.dp),
            contentScale = ContentScale.Crop
        )
        //Favorite(modifier = Modifier.align(alignment = TopEnd).padding(10.dp).clip(CircleShape).size(35.dp).background(Color.LightBrownForBackground.copy(alpha = 0.8f)), id = category.id)
    }
}
//border(width = 0.5.dp, color = Color.Black, shape = RoundedCornerShape(100.dp))