package com.example.applicationhome.features.shimmers.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TextShimmer(
    width : Dp = 150.dp,
    height : Dp = 17.dp
){
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(7.dp))
            .background(Color.LightGray)
    )
}

@Composable
fun SquareShimmer(size : Dp = 90.dp){
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.LightGray)
    )
}

@Composable
fun CircleShimmer(){
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
    )
}