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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
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
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.view.model.AddBoxViewModel
import kotlinx.coroutines.delay

@Composable
fun AddBox(
    modifier: Modifier = Modifier,
    color : Color,
    food : FoodItem,
    ordernumber : AddBoxViewModel
){

    val context = LocalContext.current
    var id = food.id
    var selectedSize by remember { mutableStateOf(food.priceANDsize.keys.last()) }
    var cartkey = CartKey(food, selectedSize)
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var count = ordernumber.cartMap[cartkey] ?: 0
    var activid = ordernumber.activId == id
    var textValue by remember(count) { mutableStateOf(count.toString()) }
    var isExpanded by remember { mutableStateOf(false) }
    var active by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    //  بيراقب الـ isExpanded
    LaunchedEffect(key1 = count, key2 = active) {
        if (isExpanded && !active && count > 0) {
            delay(2000)
            isExpanded = false
        }
    }
    var cartColor = if (activid == false || isExpanded == false || count == 0) Color.VeryLightGray else Color.Red
    var targetWidth = if (activid == false || isExpanded == false || count == 0) 35.dp else 160.dp
    Box(
        modifier = modifier.
        animateContentSize().
        height(35.dp).
        width(targetWidth).
        clip(CircleShape).
        background(cartColor).
        clickable {
            ordernumber.delete(food, selectedSize)
            Toast.makeText(context, "Removed From Cart", Toast.LENGTH_SHORT).show()
        }.
        border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.Center
    ){
        if(count == 0) {
            Box(
                modifier = modifier.
                animateContentSize().
                height(35.dp).
                width(targetWidth).
                clip(CircleShape).
                background(color).
                clickable {
                    isExpanded = true
                    ordernumber.addBoxNumberPlus(food, selectedSize)
                },
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
        }else if(count > 0 && activid == false || isExpanded == false){
            Box(
                modifier = modifier.
                animateContentSize().
                height(35.dp).
                width(targetWidth).
                clip(CircleShape).
                background(color).
                clickable {
                    isExpanded = true
                    if(count == 99){
                        ordernumber.activ(food)
                    }else{
                        ordernumber.addBoxNumberPlus(food, selectedSize)
                    }
                },
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
        }else{
            Row(verticalAlignment = Alignment.CenterVertically){
            Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Cart", tint = Color.White,
                    modifier = Modifier.weight(1f).padding(5.dp)
                )
                Box(
                    modifier = modifier.
                    animateContentSize().
                    weight(3f).
                    clip(CircleShape).
                    background(color).
                    clickable {
                        ordernumber.addBoxNumberPlus(food, selectedSize)
                    },
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
                            clickable {
                                ordernumber.addBoxNumberPlus(food, selectedSize)
                            },
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
                            clickable {
                                count
                            },
                            contentAlignment = Alignment.Center
                        ){
                            BasicTextField(
                                value = textValue,
                                onValueChange = { newValue ->
                                    if (newValue.isNotEmpty() || (newValue.all { it.isDigit() } && newValue.length <= 2)) {
                                        textValue = newValue
                                        val newCount = newValue.toIntOrNull()
                                        if(newCount != null){
                                            ordernumber.updateCount(food, selectedSize, newCount)
                                        }
                                    }else{
                                        ordernumber.updateCount(food, selectedSize, 0)
                                    }
                                },
                                modifier = Modifier.
                                    focusRequester(focusRequester).
                                    // السطر ده بيلقط أول ما المستخدم يدوس على الـ TextField عشان يكتب
                                    onFocusChanged { focusState ->
                                        active = focusState.isFocused
                                        if (focusState.isFocused) {
                                            isExpanded = true // بنخليه مفرود فوراً أول ما يركز فيه
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
                            clickable {
                                ordernumber.addBoxNumberMinus(food, selectedSize)
                            },
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
            }
        }
    }
}