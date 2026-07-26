package com.example.applicationhome.features.restaurantscreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision

@Composable
fun MealsBoxForRestaurantScreen(
    price : Double,
    details : List<String>?,
    name : String,
    image : String,
    aspectRatio : Float,
    cardNavigationClickable : () -> Unit = {},
    actions : @Composable ColumnScope.() -> Unit = {}
){
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .padding(7.dp)
            .shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(30.dp))
            .background(Color.White)
            .aspectRatio(aspectRatio)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ){
                cardNavigationClickable()
            }
            .padding(vertical = 15.dp, horizontal = 10.dp)
    ){
        Row(modifier = Modifier.fillMaxSize().background(Color.White)){
            Column(
                modifier = Modifier.
                padding(7.dp).
                fillMaxHeight().
                weight(4f),
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Column(
                    horizontalAlignment = Alignment.Start
                ){
                    Text(
                        text = name,
                        fontSize = 15.sp,
                        color = Color.Black,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    if(details != null){
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = details.joinToString(separator = " + "),
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
                Text(
                    text = "$price E.G",
                    fontSize = 16.sp,
                    color = Color.Black,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier.
                fillMaxHeight().
                weight(2.5f),
                contentAlignment = Alignment.Center
            ){
                AsyncImage(
                    modifier = Modifier.
                    padding(15.dp).
                    fillMaxSize(0.9f).
                    clip(RoundedCornerShape(10.dp)).
                    align(Alignment.CenterEnd),
                    model = ImageRequest.Builder(LocalContext.current).
                    data(image).
                    crossfade(true).
                    size(400, 400).
                    precision(Precision.EXACT).
                    build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween,
                    content = actions
                )
            }
        }
    }
}