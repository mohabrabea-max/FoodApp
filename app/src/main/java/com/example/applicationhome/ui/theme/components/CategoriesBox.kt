package com.example.applicationhome.ui.theme.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.models.CategoriesImage
import com.example.applicationhome.ui.theme.BackgroundForCards
import com.example.applicationhome.ui.theme.BrownForFont

@Composable
fun CategoriesBox(category : CategoriesImage){
    val context = LocalContext.current.applicationContext
    Box(
        modifier = Modifier.
        aspectRatio(1f).
        clickable{ Toast.makeText(context, category.name, Toast.LENGTH_SHORT).show()}.
        padding(5.dp).
        shadow(elevation = 3.dp, shape = RoundedCornerShape(10.dp))
    ){
        Column(
            modifier = Modifier.background(Color.BackgroundForCards),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = category.image),
                contentDescription = "${category.name} Logo",
                modifier = Modifier.
                fillMaxWidth().
                weight(2f),
                contentScale = ContentScale.Crop
            )
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleLarge,
                color = Color.BrownForFont,
                fontSize = 18.sp,
                modifier = Modifier.
                weight(1f).
                padding(8.dp)
            )
        }
        Favorite(modifier = Modifier.align(alignment = TopEnd))
    }
}