package com.example.applicationhome.ui.theme.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.data.models.Food
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Snake
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottonBarForItemScreen(
    ordernumber : AddBoxViewModel,
    viewModel: ItemScreenViewModel,
    food : Food,
    size : String
){
    var cart = Cart.cartmap
    val item = viewModel.selectedItem
    var key: CartKey? = null
    when (item) {
        is FoodItem -> {
            key = CartKey(item, size)
        }
        is Snake -> { }
    }
    val context = LocalContext.current
    var cartkey = CartKey(food, size)
    var count = ordernumber.cartMap[cartkey] ?: 0
    var color : Color
    var fontColor : Color
    if(count == 0){
        color = Color.VeryLightGray
        fontColor = Color.Black
    }else{
        color = Color.Red
        fontColor = Color.White
    }
    Column(
        modifier = Modifier.fillMaxWidth().
        height(75.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){
        if(cart.containsKey(key)){
            Box(
                modifier = Modifier.width(170.dp).
                height(25.dp).
                clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)).
                background(Color.Yellow).
                border(width = 0.3.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)).
                pointerInput(Unit) {
                    detectTapGestures { }
                }
            ){
                Text(
                    text = "${cart[key]} added in your cart",
                    modifier = Modifier.padding(top = 5.dp, start = 5.dp, end = 5.dp).align(Alignment.TopCenter)
                )
            }
        }
        Box(
            modifier = Modifier.width(200.dp).
            height(50.dp).
            clip(RoundedCornerShape(50.dp)).
            background(color).
            border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(50.dp)).
            pointerInput(Unit) {
                detectTapGestures { }
            }
        ){
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Box(
                    modifier = Modifier.weight(2.5f).
                    height(50.dp).
                    clip(RoundedCornerShape(50.dp)).
                    background(Color.White).
                    border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = CircleShape).padding(4.dp)
                ){
                    Row(
                        modifier = Modifier.
                        fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                    ){
                        IconButton(onClick = {ordernumber.addBoxNumberMinus(food, size)}, modifier = Modifier.weight(1f).fillMaxHeight()){
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
                                text = count.toString(),
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                        IconButton(onClick = {ordernumber.addBoxNumberPlus(food, size)}, modifier = Modifier.weight(1f).fillMaxHeight()){
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = if(count < 99) Color.DarkOrange else Color.Gray,
                                modifier = Modifier.fillMaxSize().padding(5.dp)
                            )
                        }
                    }
                }
                Box(modifier = Modifier.weight(1f)){
                    IconButton(onClick = {
                        ordernumber.delete(food, size)
                        if(count > 0) Toast.makeText(context, "Removed from cart", Toast.LENGTH_SHORT).show()}){
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Cart",
                            tint = fontColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
