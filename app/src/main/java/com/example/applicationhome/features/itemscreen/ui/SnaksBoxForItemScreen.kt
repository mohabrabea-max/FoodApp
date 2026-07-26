package com.example.applicationhome.features.itemscreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.data.model.MealSnacks

@Composable
fun SnaksBoxForItemScreen(
    modifier: Modifier = Modifier,
    item: MealSnacks
){
    Card(
        modifier = modifier.padding(7.dp).shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(30.dp)).
        background(Color.White).
        clickable{  }
    ){
        Column(modifier = Modifier.fillMaxSize().background(Color.White)){
            Box(modifier = Modifier.fillMaxWidth().weight(2f)){
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current).
                    data(item.image).
                    crossfade(true).
                    precision(Precision.EXACT).
                    build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth().weight(1.1f).padding(start = 15.dp, end = 10.dp, top = 5.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = item.size + " " + item.name,
                    fontSize = 14.sp,
                    color = Color.Black,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                )
            }
        }
    }
}