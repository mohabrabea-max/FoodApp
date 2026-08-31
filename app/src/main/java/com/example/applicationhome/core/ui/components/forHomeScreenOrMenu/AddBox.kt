package com.example.applicationhome.core.ui.components.forHomeScreenOrMenu

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.ui.theme.DarkOrange
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AddBox(
    foodId : Int,
    plus : () -> Unit = {},
    minus : () -> Unit = {},
    active : () -> Unit = {},
    activeId : Int,
    count : Int,
    modifier: Modifier = Modifier
){
    val interactionSource = remember { MutableInteractionSource() }

    val isActive = activeId == foodId
    var isExpanded by remember { mutableStateOf(false) }

    val isExpandedState = isActive && isExpanded

    LaunchedEffect(key1 = count, key2 = isExpandedState) {
        if (isExpandedState) {
            delay(1000.milliseconds)
            isExpanded = false
        }
    }

    val boxColor by animateColorAsState(
        targetValue = if (count > 0 && !isExpandedState) Color.DarkOrange else MaterialTheme.colorScheme.surface,
        label = "BoxColorAnimation"
    )

    val targetWidth = if (!isExpandedState) 40.dp else 120.dp

    Box(
        modifier = modifier
            .animateContentSize()
            .padding(5.dp)
            .shadow(elevation = 5.dp, spotColor = Color.LightGray, shape = CircleShape)
            .height(40.dp)
            .width(targetWidth)
            .background(boxColor)
            .clickable(
                enabled = !isExpandedState,
                interactionSource = interactionSource,
                indication = null
            ){
                isExpanded = true
                active()
            },
        contentAlignment = Alignment.Center
    ){
        when{
            isExpandedState ->{
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                ){
                    IconButton(
                        onClick = {
                            minus()
                        },
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    ){
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = null,
                            tint = if(count > 0) Color.DarkOrange else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxSize().padding(5.dp)
                        )
                    }
                    Box(
                        modifier = Modifier.
                        weight(1f).
                        fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "$count",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                    IconButton(
                        onClick = {
                            plus()
                        },
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    ){
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = if(count < 99) Color.DarkOrange else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxSize().padding(5.dp)
                        )
                    }
                }
            }

            count > 0 -> {
                Text(
                    text = "$count",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                IconButton(
                    onClick = {
                        isExpanded = true
                        active()
                        plus()
                    },
                    modifier = Modifier.fillMaxSize()
                ){
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.DarkOrange,
                        modifier = Modifier.fillMaxSize().padding(5.dp)
                    )
                }
            }
        }
    }
}