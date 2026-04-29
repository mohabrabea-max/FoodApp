package com.example.applicationhome.ui.theme.components

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.data.models.Food
import com.example.applicationhome.ui.theme.Orange
import com.example.applicationhome.view.model.AddBoxViewModel

@Composable
fun AddBox(
    modifier: Modifier = Modifier,
    color : Color,
    food : Food,
    ordernumber : AddBoxViewModel = viewModel()
){
    val context = LocalContext.current
    var id = food.id
    var selectedSize by remember { mutableStateOf("Small") }
    var cartkey = CartKey(food, selectedSize)
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var count = ordernumber.itemsCount[cartkey] ?: 0
    var activid = ordernumber.activId == id

    var targetWidth = if(count == 0 || activid == false) 35.dp else 160.dp
    Box(
        modifier = modifier.
        animateContentSize().
        padding(10.dp).
        height(35.dp).
        width(targetWidth).
        clip(CircleShape).
        background(Color.Blue).
        clickable {
            ordernumber.addToCart(food, selectedSize)
            Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show()
        }.
        border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.Center
    ){
        if(count == 0) {
            Box(
                modifier = modifier.
                animateContentSize().
                height(35.dp).
                width(targetWidth).
                clip(CircleShape).
                background(color.copy(alpha = 1f)).
                clickable {ordernumber.addBoxNumberPlus(food, selectedSize)}.
                border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp)),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "+",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Orange,
                    textAlign = TextAlign.Center
                )
            }
        }else if(count > 0 && activid == false){
            Box(
                modifier = modifier.
                animateContentSize().
                height(35.dp).
                width(targetWidth).
                clip(CircleShape).
                background(color.copy(alpha = 1f)).
                clickable {
                    if(count == 99){
                        ordernumber.activ(food)
                    }else{
                        ordernumber.addBoxNumberPlus(food, selectedSize)
                    }
                }.
                border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp)),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = count.toString(),
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Orange,
                    textAlign = TextAlign.Center
                )
            }
        }else{
            Row(verticalAlignment = Alignment.CenterVertically){
            Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = "Cart", tint = Color.White,
                    modifier = Modifier.weight(1f).padding(5.dp)
                )
                Box(
                    modifier = modifier.
                    animateContentSize().
                    weight(3f).
                    clip(CircleShape).
                    background(color.copy(alpha = 1f)).
                    clickable {ordernumber.addBoxNumberPlus(food, selectedSize)}.
                    border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Row(
                        modifier = Modifier.
                        fillMaxSize().
                        background(color),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                    ){
                        Box(
                            modifier = Modifier.
                            weight(1f).
                            fillMaxHeight().
                            clickable {ordernumber.addBoxNumberPlus(food, selectedSize)},
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "+",
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.Orange,
                                textAlign = TextAlign.Center
                            )
                        }
                        VerticalDivider(color = Color.Orange, modifier = Modifier.height(20.dp))
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
                                            ordernumber.updateCount(food, selectedSize, newCount)
                                        }
                                    }else{
                                        val newCount = newValue.toIntOrNull() ?: 0
                                        ordernumber.updateCount(food, selectedSize, newCount)
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
                                    color = Color.Orange
                                )
                            )
                        }
                        VerticalDivider(color = Color.Orange, modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier.
                            weight(1f).
                            fillMaxHeight().
                            animateContentSize().
                            clickable {ordernumber.addBoxNumberMinus(food, selectedSize)},
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "-",
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.Orange,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}