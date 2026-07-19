package com.example.applicationhome.core.ui.components.forHomeScreenOrMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.RedOrange
import com.example.applicationhome.data.data.model.Screens

@Composable
fun RestaurantButton(
    navigationController: NavHostController,
    totalNumber : Int,
    totalPrice : Double
){
    Box(
        modifier = Modifier.fillMaxWidth().
        height(100.dp).
        shadow(elevation = 7.dp).
        background(Color.White).
        pointerInput(Unit) {
            detectTapGestures { }
        }.
        padding(horizontal = 15.dp).
        navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier.navigationBarsPadding().fillMaxWidth().
            height(55.dp).
            shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(50.dp)).
            background(Color.DarkOrange).
            clickable{
                navigationController.navigate(Screens.Cart.screen){ launchSingleTop = true }
            }.
            padding(9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier.
                    fillMaxHeight().
                    width(37.dp).
                    clip(CircleShape).
                    background(Color.RedOrange),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "$totalNumber",
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "View cart",
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "EGP $totalPrice",
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }
}