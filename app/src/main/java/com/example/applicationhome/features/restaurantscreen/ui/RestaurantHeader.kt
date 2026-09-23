package com.example.applicationhome.features.restaurantscreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.core.ui.model.RestaurantsUIClass

@Composable
fun RestaurantHeader(
    item : RestaurantsUIClass?,
    view : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    val background = item?.image2 ?:""
    val type = item?.categories
    val logo = item?.image ?: ""

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
            .background(MaterialTheme.colorScheme.surface),
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
            height(230.dp),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(15.dp))
                .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.BottomCenter)
                .padding(15.dp),

            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ){ view() },
            ){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).
                    data(logo).
                    crossfade(true).
                    precision(Precision.EXACT).
                    build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .height(70.dp)
                    .padding(start = 13.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = item?.name ?: "",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = type?.joinToString(separator = " & ") ?: "",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier
                        .height(20.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 3.dp),

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
                        text = "${item?.review?.first ?: 0.0}${item?.review?.second?: "0"}",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}