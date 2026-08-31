package com.example.applicationhome.core.ui.components.forHomeScreenOrMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.applicationhome.core.ui.theme.DarkOrange

@Composable
fun MealBoxIcon(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier.padding(5.dp).
        shadow(elevation = 5.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(30.dp)).
        size(35.dp).
        background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ){
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.DarkOrange,
            modifier = Modifier.fillMaxSize().padding(7.dp)
        )
    }
}