package com.example.applicationhome.features.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R

@Composable
fun SearchHistoryBox(
    text : String,
    textColor : Color = MaterialTheme.colorScheme.onSurface,
    containerHeight : Int = 37,
    containerColor : Color = MaterialTheme.colorScheme.surface,
    containerBorderColor : Color = MaterialTheme.colorScheme.onSurfaceVariant,
    containerBorderWidth : Dp = 1.dp,
    clickable : () -> Unit = {},
    delete : () -> Unit = {}
){
    val interactionSource = remember { MutableInteractionSource() }

    var isMenuExpanded by remember { mutableStateOf(false) }

    val spacer = containerHeight/12.33

    Box(contentAlignment = Alignment.Center){
        Row(
            modifier = Modifier
                .height(containerHeight.dp)
                .clip(CircleShape)
                .background(containerColor)
                .border(containerBorderWidth, containerBorderColor, CircleShape)
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = null,

                    onClick = { clickable() },

                    onLongClick = { isMenuExpanded = true }
                )
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = "History Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
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


        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 10.dp,
            containerColor = MaterialTheme.colorScheme.surface
        ){
            DropdownMenuItem(
                text = { Text(stringResource(R.string.search)) },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                onClick = {
                    isMenuExpanded = false
                    clickable()
                }
            )

            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete), color = Color.Red) },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Clear,
                        contentDescription = null,
                        tint = Color.Red
                    )
                },
                onClick = {
                    isMenuExpanded = false
                    delete()
                }
            )
        }
    }
}