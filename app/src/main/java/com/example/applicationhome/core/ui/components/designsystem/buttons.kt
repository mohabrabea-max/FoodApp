package com.example.applicationhome.core.ui.components.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.applicationhome.core.ui.theme.VeryLightGray

@Composable
fun TopBarButtons(
    icon : @Composable () -> Unit = {},
    onClick : () -> Unit = {}
){
    Box(
        modifier = Modifier
            .padding(5.dp)
            .shadow(elevation = 7.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(30.dp))
            .size(40.dp)
            .background(Color.White)
            .border(
            width = 1.dp,
            color = Color.LightGray,
            shape = CircleShape
        ).
        clickable { onClick() },
        contentAlignment = Alignment.Center
    ){
        icon()
    }
}