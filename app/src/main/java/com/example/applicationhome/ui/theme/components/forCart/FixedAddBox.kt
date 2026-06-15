package com.example.applicationhome.ui.theme.components.forCart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.applicationhome.data.models.model.CartItemsClass
import com.example.applicationhome.data.models.model.Food
import com.example.applicationhome.data.models.repository.CartRepository
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.model.AddBoxViewModel

@Composable
fun FixedAddBox(
    addBoxViewModel : AddBoxViewModel,
    food: CartItemsClass,
    count : Int,
    size : String,
    cartkey : String,
    foodItem : Food?
){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier.fillMaxSize().padding(3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier.size(30.dp).clip(CircleShape).background(Color.White),
            contentAlignment = Alignment.Center
        ){
            IconButton(onClick = { addBoxViewModel.minus(food.id, size) }){
                Icon(Icons.Default.Remove, contentDescription = null, tint = Color.DarkOrange)
            }
        }
        Box(
            modifier = Modifier.fillMaxHeight().width(30.dp).padding(top = 4.dp, bottom = 4.dp),contentAlignment = Alignment.Center
        ){
            BasicTextField(
                value = CartRepository.cartItems[cartkey]?.number.toString(),
                onValueChange = { newValue ->
                    if (newValue.isNotEmpty()) {
                        if(newValue.all {it.isDigit()} && newValue.length <= 2){
                            val newCount = newValue.toIntOrNull() ?: count
                            addBoxViewModel.updateCount(food, size, newCount)
                        }
                    }else{
                        val newCount = newValue.toIntOrNull() ?: 0
                        addBoxViewModel.updateCount(food, size, newCount)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true
            )
        }
        Box(
            modifier = Modifier.size(30.dp).clip(CircleShape).background(Color.DarkOrange),
            contentAlignment = Alignment.Center
        ){
            IconButton(onClick = { if(foodItem != null) addBoxViewModel.plus(foodItem, size) }){
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    }
}