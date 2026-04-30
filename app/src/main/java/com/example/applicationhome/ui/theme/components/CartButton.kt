package com.example.applicationhome.ui.theme.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.view.model.AddBoxViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun CartButton(
    addBoxViewModel : AddBoxViewModel = viewModel(),
    cart : Map<CartKey, Int>
){
var totalNumber = mutableStateOf(0)
    Cart.cartMap().forEach {(key, value) ->
        totalNumber.value += value
    }
    var totalPrice = addBoxViewModel.totalPrice.value
    Box(
        modifier = Modifier.width(250.dp).
        height(50.dp).
        clip(RoundedCornerShape(50.dp)).
        background(Color.Green).
        clickable{addBoxViewModel.bay()}.
        border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(50.dp)),
        contentAlignment = Alignment.Center
    ){
        Row(verticalAlignment = Alignment.CenterVertically){
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = totalNumber.toString(),
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                VerticalDivider(color = Color.White)
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = Color.White,
                    modifier = Modifier.weight(1f).size(30.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "${totalPrice.toString()} E.G",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                VerticalDivider(color = Color.White)
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = Color.White,
                    modifier = Modifier.weight(1f).size(30.dp)
                )
            }
        }
    }
}