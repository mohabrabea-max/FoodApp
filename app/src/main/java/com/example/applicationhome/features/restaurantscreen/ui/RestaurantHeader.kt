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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus

@Composable
fun RestaurantHeader(
    item : RestaurantWithFavoriteStatus?,
    view: () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    val background = item?.restaurant?.image2 ?:""
    val type = item?.categories?.map { it.name }?.toList()
    val logo = item?.restaurant?.image ?: ""

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
        Box(
            modifier = Modifier.padding(horizontal = 15.dp).
            fillMaxWidth().
            height(120.dp).
            clip(RoundedCornerShape(15.dp)).
            border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp)).
            background(MaterialTheme.colorScheme.surface).
            align(Alignment.BottomCenter)
        ){
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(13.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row{
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
                        modifier = Modifier.padding(start = 13.dp)
                    ){
                        Text(
                            text = item?.restaurant?.name ?: "",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                        Text(
                            text = type?.joinToString(separator = " ", prefix = "", postfix = "") ?: "",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Row(
                            modifier = Modifier.width(80.dp).
                            height(20.dp).
                            clip(RoundedCornerShape(5.dp)).
                            background(MaterialTheme.colorScheme.background).padding(horizontal = 3.dp),
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
                                text = "${item?.restaurant?.review ?: 0.0}",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier
                            )
                            Text(
                                text = "(1k+)",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
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