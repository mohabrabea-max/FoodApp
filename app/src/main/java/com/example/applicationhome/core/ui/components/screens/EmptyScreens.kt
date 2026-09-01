package com.example.applicationhome.core.ui.components.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyScreen(
    title : String,
    image : Painter
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Spacer(modifier = Modifier.height(200.dp))

        Image(
            modifier = Modifier.size(120.dp),
            painter = image,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = title,
            fontSize = 20.sp,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EmptyScreenWhithButton(
    title : String,
    subTitle : String? = null,
    buttonTitle : String,
    image : Painter,
    onClickable : () -> Unit
){
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.height(300.dp))

        Image(
            modifier = Modifier.size(120.dp),
            painter = image,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = title,
            fontSize = 22.sp,
            lineHeight = 30.sp,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        if(subTitle != null){
            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = subTitle,
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier.width(100.dp).
            height(40.dp).
            clip(CircleShape).
            clickable{
                onClickable()
            }.
            border(width = 1.dp, color = MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(40.dp)).
            padding(7.dp).align(Alignment.CenterHorizontally)
        ){
            Text(
                text = buttonTitle,
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}