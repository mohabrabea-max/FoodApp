package com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.models.local.CartItemsClass
import com.example.applicationhome.data.models.model.FoodItemToCalculate
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.model.CartViewModel
import com.example.applicationhome.ui.theme.model.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun AddBox(
    loginViewModel: LoginViewModel,
    color : Color,
    food : FoodItemToCalculate,
    cartViewModel: CartViewModel,
    message : () -> Unit = {},
    modifier: Modifier = Modifier
){

    val context = LocalContext.current
    val id = food.id
    val selectedSize = food.size
    val price = food.price
    val type = food.type



    val cartkey = "${food.id}_${selectedSize}"
    val count = cartViewModel.cartItems.collectAsState().value.find { it?.mealKey == cartkey }?.quantity ?:0

    val meal = CartItemsClass(
        loginViewModel.userData.collectAsState().value.id,
        cartkey,
        id,
        food.name,
        type,
        selectedSize,
        count,
        price,
        price * count,
        food.image.first(),
        food.restaurantId
    )

    val activid = cartViewModel.activId == id
    var isExpanded by remember { mutableStateOf(false) }
    val active = cartViewModel.activId

    //  بيراقب الـ isExpanded
    LaunchedEffect(key1 = count, key2 = active) {
        if (isExpanded && active == id) {
            delay(1000)
            isExpanded = false
        }
    }
    var cartColor = if (activid == false || isExpanded == false || count == 0) Color.VeryLightGray else Color.Red
    var targetWidth = if (activid == false || isExpanded == false) 35.dp else 160.dp
    Box(
        modifier = modifier.
        animateContentSize().
        height(35.dp).
        width(targetWidth).
        clip(CircleShape).
        background(cartColor).
        clickable {
            cartViewModel.delete(food.id, selectedSize)
            if( count > 0 ) Toast.makeText(context, "Removed From Cart", Toast.LENGTH_SHORT).show()
        }.
        border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.Center
    ){
        if(count == 0 && activid == false || count == 0 && isExpanded == false) {
            IconButton(
                onClick = {
                    isExpanded = true
                    cartViewModel.active(id)
                    cartViewModel.plus(meal, selectedSize)
                    if(food.restaurantId == cartViewModel.cartInformation.value?.restaurantId) message()
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
        }else if(count > 0 && activid == false || isExpanded == false){
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
                        cartViewModel.active(id)
                    }else{
                        cartViewModel.active(id)
                        cartViewModel.plus(meal, selectedSize)
                    }
                },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = count.toString(),
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
                        cartViewModel.plus(meal, selectedSize)
                    },
                    contentAlignment = Alignment.Center
                ){
                    Row(
                        modifier = Modifier.
                        fillMaxSize().
                        background(color),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                    ){
                        IconButton(onClick = {cartViewModel.minus(meal, selectedSize)}, modifier = Modifier.weight(1f).fillMaxHeight()){
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
                        IconButton(
                            onClick = {
                                cartViewModel.plus(meal, selectedSize)
                                if(food.restaurantId == cartViewModel.cartInformation.value?.restaurantId) message()
                            },
                            modifier = Modifier.weight(1f).fillMaxHeight()){
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