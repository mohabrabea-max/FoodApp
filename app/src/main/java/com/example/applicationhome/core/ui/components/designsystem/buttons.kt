package com.example.applicationhome.core.ui.components.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.ui.theme.VeryLightGray
import kotlinx.coroutines.launch

@Composable
fun TopBarButtons(
    icon : @Composable () -> Unit = {},
    onClick : () -> Unit = {},
    elevation : Dp = 7.dp,
    border : Dp = 1.dp
){
    Box(
        modifier = Modifier
            .padding(5.dp)
            .shadow(elevation = elevation, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(30.dp))
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(
            width = border,
            color = Color.LightGray.copy(alpha = 0.6f),
            shape = CircleShape
        ).
        clickable { onClick() },
        contentAlignment = Alignment.Center
    ){
        icon()
    }
}

@Composable
fun MyButton(loading : Boolean, backgroundcolor : Color, fontcolor : Color, horizontalPadding : Dp, title: String, action: () -> Unit){
    val scope = rememberCoroutineScope()

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier.navigationBarsPadding().padding(horizontal = horizontalPadding).fillMaxWidth().
        height(50.dp).
        shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(50.dp)).
        background(backgroundcolor).
        clickable(
            interactionSource = interactionSource,
            indication = null
        ){
            if(!loading){
                scope.launch {
                    action()
                }
            }
        },
        contentAlignment = Alignment.Center
    ){
        if(loading){
            CircularProgressIndicator(
                color = Color.White
            )
        }else{
            Text(
                text = title,
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelLarge,
                color = fontcolor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SquerButton(modifier : Modifier = Modifier, loading : Boolean, backgroundcolor : Color, fontcolor : Color, horizontalPadding : Dp, title: String, action: () -> Unit){
    val scope = rememberCoroutineScope()

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier.navigationBarsPadding().padding(horizontal = horizontalPadding).
        height(50.dp).
        shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(12.dp)).
        background(backgroundcolor).
        clickable(
            interactionSource = interactionSource,
            indication = null
        ){
            if(!loading){
                scope.launch {
                    action()
                }
            }
        },
        contentAlignment = Alignment.Center
    ){
        if(loading){
            CircularProgressIndicator(
                color = Color.White
            )
        }else{
            Text(
                text = title,
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelLarge,
                color = fontcolor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }
}