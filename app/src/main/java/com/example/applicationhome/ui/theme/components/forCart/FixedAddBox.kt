package com.example.applicationhome.ui.theme.components.forCart

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.models.model.CartClassForCalculations
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.model.CartViewModel

@Composable
fun FixedAddBox(
    cartViewModel : CartViewModel,
    size : String,
    cartkey : String,
    foodItem : CartClassForCalculations
){
    Row(
        modifier = Modifier.fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { }
            }
            .padding(3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier.size(30.dp).clip(CircleShape).background(Color.White),
            contentAlignment = Alignment.Center
        ){
            IconButton(onClick = { cartViewModel.minus(foodItem, size) }){
                Icon(Icons.Default.Remove, contentDescription = null, tint = Color.DarkOrange)
            }
        }
        Box(
            modifier = Modifier.fillMaxHeight().width(30.dp).padding(top = 4.dp, bottom = 4.dp),contentAlignment = Alignment.Center
        ){
            Text(
                text = cartViewModel.cartItems.collectAsState().value.find { it?.mealKey == cartkey }?.quantity.toString(),
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelLarge,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier.size(30.dp).clip(CircleShape).background(Color.DarkOrange),
            contentAlignment = Alignment.Center
        ){
            IconButton(onClick = { cartViewModel.plus(foodItem, size) }){
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    }
}