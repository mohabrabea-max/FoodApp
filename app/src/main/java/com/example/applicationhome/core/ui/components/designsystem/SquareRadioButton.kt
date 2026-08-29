package com.example.applicationhome.core.ui.components.designsystem

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.applicationhome.core.ui.theme.DarkOrange

@Composable
fun SquareRadioButton(
    selected : Boolean = false,
    modifier : Modifier = Modifier,
    selectedColor : Color = Color.DarkOrange,
    unselectedColor : Color = Color.Gray,
    onClick : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    val backgroundColor by animateColorAsState(
        targetValue = if (selected) selectedColor else Color.Transparent,
        label = "BgColor"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) selectedColor else unselectedColor,
        label = "BorderColor"
    )

    Box(
        modifier = modifier
            .size(22.dp)
            .clip(RoundedCornerShape(5.dp))
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(5.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ){ onClick() },
        contentAlignment = Alignment.Center
    ){
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}