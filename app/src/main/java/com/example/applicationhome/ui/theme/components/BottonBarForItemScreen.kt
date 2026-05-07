package com.example.applicationhome.ui.theme.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.data.models.Food
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Snake
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
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var cartkey = CartKey(food, size)
    var count = ordernumber.cartMap[cartkey] ?: 0
    var color : Color
    var fontColor : Color
    if(count == 0){
        color = Color.LightGray
        fontColor = Color.Black
    }else{
        color = Color.Blue
        fontColor = Color.White
    }

    Box(
        modifier = Modifier.fillMaxWidth().
        height(100.dp).
        pointerInput(Unit) { detectTapGestures { } },
        contentAlignment = Alignment.Center
    ){
        if(cart.containsKey(key)){
            Box(
                modifier = Modifier.width(200.dp).
                height(70.dp).
                clip(RoundedCornerShape(50.dp)).
                background(Color.Yellow).align(Alignment.TopCenter).
                border(width = 0.3.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(50.dp)).
                pointerInput(Unit) {
                    detectTapGestures { }
                }
            ){
                Text(
                    text = "${cart[key]} added in your cart",
                    modifier = Modifier.padding(5.dp).align(Alignment.TopCenter)
                )
            }
        }
        Box(
            modifier = Modifier.width(220.dp).
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
                    border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(50.dp))
                ){
                    Row(
                        modifier = Modifier.
                        fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                    ){
                        Box(
                            modifier = Modifier.
                            weight(1f).
                            fillMaxHeight().
                            clickable {ordernumber.addBoxNumberPlus(food, size)},
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "+",
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                        VerticalDivider(color = Color.Black, modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier.
                            weight(1f).
                            fillMaxHeight().
                            clickable {count},
                            contentAlignment = Alignment.Center
                        ){
                            BasicTextField(
                                value = count.toString(),
                                onValueChange = { newValue ->
                                    if (newValue.isNotEmpty()) {
                                        if(newValue.all {it.isDigit()} && newValue.length <= 2){
                                            val newCount = newValue.toIntOrNull() ?: count
                                            ordernumber.updateCount(food, size, newCount)
                                        }
                                    }else{
                                        val newCount = newValue.toIntOrNull() ?: 0
                                        ordernumber.updateCount(food, size, newCount)
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                    }
                                ),
                                singleLine = true,
                                textStyle = TextStyle(
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    color = Color.Black
                                )
                            )
                        }
                        VerticalDivider(color = Color.Black, modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier.
                            weight(1f).
                            fillMaxHeight().
                            animateContentSize().
                            clickable {ordernumber.addBoxNumberMinus(food, size)},
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "-",
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Box(modifier = Modifier.weight(1f)){
                    IconButton(onClick = {

                        ordernumber.delete(food, size)
                        Toast.makeText(context, "Removed from cart", Toast.LENGTH_SHORT).show()}){
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
