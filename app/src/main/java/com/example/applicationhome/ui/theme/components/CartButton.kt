package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CartButton(backgroundcolor : Color, fontcolor : Color, title: String, action: () -> Unit){
    Box(
        modifier = Modifier.navigationBarsPadding().padding(horizontal = 20.dp).fillMaxWidth().
        height(50.dp).
        shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(50.dp)).
        background(backgroundcolor).
        clickable{action()},
        contentAlignment = Alignment.Center
    ){
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