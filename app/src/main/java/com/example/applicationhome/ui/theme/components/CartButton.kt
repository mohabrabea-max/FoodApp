package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.model.AddBoxViewModel

@Composable
fun CartButton(
    addBoxViewModel : AddBoxViewModel
){
    Box(
        modifier = Modifier.width(300.dp).
        height(50.dp).
        shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(50.dp)).
        background(Color.DarkOrange).
        clickable{addBoxViewModel.bay()},
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "Confirm order",
            fontSize = 20.sp,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}