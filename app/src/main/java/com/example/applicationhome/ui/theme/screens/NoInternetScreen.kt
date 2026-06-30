package com.example.applicationhome.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.ui.theme.BrownForFont

@Composable
fun NoInternetScreen(
    navigationController : NavHostController
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        ,horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(300.dp))
        Image(
            modifier = Modifier.size(120.dp),
            painter = painterResource(R.drawable.offlinescreen),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "You seem to be offline",
            fontSize = 20.sp,
            style = MaterialTheme.typography.labelLarge,
            color = Color.BrownForFont,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Check your internet connection and try again.",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier.width(70.dp).
            height(40.dp).
            clip(CircleShape).
            clickable{
                if (navigationController.previousBackStackEntry != null) { navigationController.popBackStack() }
            }.
            border(width = 1.dp, color = Color.BrownForFont, shape = RoundedCornerShape(40.dp)).
            padding(7.dp).align(Alignment.CenterHorizontally)
        ){
            Text(
                text = "Retry",
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelLarge,
                color = Color.BrownForFont,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}