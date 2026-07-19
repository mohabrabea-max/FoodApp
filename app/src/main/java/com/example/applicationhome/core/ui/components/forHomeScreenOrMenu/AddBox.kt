package com.example.applicationhome.core.ui.components.forHomeScreenOrMenu

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AddBox(
    color : Color,
    foodId : Int,
    plus : () -> Unit = {},
    minus : () -> Unit = {},
    delete : () -> Unit = {},
    active : () -> Unit = {},
    activeId : Int,
    count : Int,
    modifier: Modifier = Modifier
){

    val context = LocalContext.current

    val isActive = activeId == foodId
    var isExpanded by remember { mutableStateOf(false) }

    //  بيراقب الـ isExpanded
    LaunchedEffect(key1 = count, key2 = activeId) {
        if (isExpanded && activeId == foodId) {
            delay(1000.milliseconds)
            isExpanded = false
        }
    }
    val cartColor = if (!isActive || !isExpanded || count == 0) Color.VeryLightGray else Color.Red
    val targetWidth = if (!isActive || !isExpanded) 35.dp else 160.dp
    Box(
        modifier = modifier.
        padding(5.dp).
        shadow(elevation = 5.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(30.dp)).
        animateContentSize().
        height(35.dp).
        width(targetWidth).
        clip(CircleShape).
        background(cartColor).
        clickable {
            delete()
            if( count > 0 ) Toast.makeText(context, "Removed From Cart", Toast.LENGTH_SHORT).show()
        },
        contentAlignment = Alignment.Center
    ){
        if(count == 0 && !isActive || count == 0 && !isExpanded) {
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
        }else if(count > 0 && !isActive || !isExpanded){
            Box(
                modifier = modifier.
                animateContentSize().
                height(35.dp).
                width(targetWidth).
                clip(CircleShape).
                background(Color.DarkOrange).
                clickable {
                    isExpanded = true
                    if(count > 0){
                        active()
                    }else{
                        active()
                        plus()
                    }
                },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "$count",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }else{
            Row(verticalAlignment = Alignment.CenterVertically){
            Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Cart", tint = if( count > 0 ) Color.White else Color.Black,
                    modifier = Modifier.weight(1f).padding(5.dp)
                )
                Box(
                    modifier = modifier.
                    animateContentSize().
                    weight(3f).
                    clip(CircleShape).
                    background(color).
                    clickable {
                        plus()
                    },
                    contentAlignment = Alignment.Center
                ){
                    Row(
                        modifier = Modifier.
                        fillMaxSize().
                        background(color),
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
                                tint = if(count > 0) Color.DarkOrange else Color.Gray,
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
                                color = Color.Black,
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
                                tint = if(count < 99) Color.DarkOrange else Color.Gray,
                                modifier = Modifier.fillMaxSize().padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}