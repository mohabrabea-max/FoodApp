package com.example.applicationhome.features.homescreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R

@Composable
fun SearchBox(
    action : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier.width(340.dp).
        height(45.dp).
        clip(CircleShape).
        background(Color.White).
        border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp)).
        clickable(
            interactionSource = interactionSource,
            indication = null
        ){ action() }.
        padding(5.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.padding(start = 15.dp)
            )
            Text(
                text = stringResource(R.string.search_food),
                softWrap = false,
                color = Color.Gray,
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}