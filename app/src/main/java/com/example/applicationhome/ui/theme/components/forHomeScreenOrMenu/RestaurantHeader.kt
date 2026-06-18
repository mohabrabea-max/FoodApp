package com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.model.HomeScreenViewModel

@Composable
fun RestaurantHeader(item : Restaurants, homeScreenViewModel: HomeScreenViewModel){
    val background = item.image2
    val type = item.typ.toList()
    val logo = item.image

    Box(
        modifier = Modifier.fillMaxWidth().height(270.dp),
        contentAlignment = Alignment.TopCenter
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).
            data(background).
            crossfade(true).
            precision(Precision.EXACT).
            build(),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().
            height(230.dp).
            clickable { homeScreenViewModel.view(item.image) },
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier.padding(horizontal = 15.dp).
            fillMaxWidth().
            height(120.dp).
            clip(RoundedCornerShape(15.dp)).
            border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp)).
            background(Color.White).
            align(Alignment.BottomCenter)
        ){
            Row(
                modifier = Modifier.fillMaxSize().padding(13.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row{
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).
                        data(logo).
                        crossfade(true).
                        precision(Precision.EXACT).
                        build(),
                        contentDescription = null,
                        modifier = Modifier.size(70.dp).
                        clip(RoundedCornerShape(10.dp)).
                        border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier.padding(start = 13.dp)
                    ){
                        Text(
                            text = item.name,
                            fontSize = 18.sp,
                            color = Color.Black,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                        Text(
                            text = type.joinToString(separator = " ", prefix = "", postfix = "")?: "",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Row(
                            modifier = Modifier.width(80.dp).
                            height(20.dp).
                            clip(RoundedCornerShape(5.dp)).
                            background(Color.VeryLightGray).padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700) ,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = item.review.toString(),
                                color = Color.Black,
                                fontSize = 14.sp,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier
                            )
                            Text(
                                text = "(1k+)",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                style = TextStyle(letterSpacing = (-0.7).sp),
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}