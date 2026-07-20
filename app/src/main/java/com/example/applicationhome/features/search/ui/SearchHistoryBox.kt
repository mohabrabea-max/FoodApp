package com.example.applicationhome.features.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchHistoryBox(
    text : String,
    textColor : Color = Color.Black,
    containerHeight : Int = 37,
    containerColor : Color = Color.White,
    containerBorderColor : Color = Color.LightGray,
    containerBorderWidth : Dp = 1.dp,
    clickable : () -> Unit = {}
){
    val interactionSource = remember { MutableInteractionSource() }

    val spacer = containerHeight/12.33

    Row(
        modifier = Modifier
            .height(containerHeight.dp)
            .clip(CircleShape)
            .background(containerColor)
            .border(containerBorderWidth, containerBorderColor, CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { clickable() }
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = "History Icon",
            tint = Color.DarkGray,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(spacer.dp))

        Text(
            text = text,
            fontSize = 13.sp,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}