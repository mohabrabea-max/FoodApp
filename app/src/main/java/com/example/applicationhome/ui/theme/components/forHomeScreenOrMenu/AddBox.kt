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
import com.example.applicationhome.data.models.model.Food
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.repository.CartRepository
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.model.AddBoxViewModel
import kotlinx.coroutines.delay

@Composable
fun AddBox(
    color : Color,
    food : Food,
    addBoxViewModel: AddBoxViewModel,
    modifier: Modifier = Modifier
){

    val context = LocalContext.current
    val id = food.id
    val selectedSize = remember {
        when(food){
            is FoodItem -> {
                mutableStateOf(food.sizeOptions.find { it.size == "Small" || it.size.contains("Pieces")}?.size)
            }
            is Snack -> {
                mutableStateOf(food.priceANDsize.keys.last())
            }
        }
    }
    val cartkey = "${food.id}_${selectedSize.value.toString()}"
    val count = CartRepository.cartItems[cartkey]?.number ?: 0
    val activid = addBoxViewModel.activId == id
    var isExpanded by remember { mutableStateOf(false) }
    val active = addBoxViewModel.activId

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
            addBoxViewModel.delete(food.id, selectedSize.value.toString())
            if( count > 0 ) Toast.makeText(context, "Removed From Cart", Toast.LENGTH_SHORT).show()
        }.
        border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.Center
    ){
        if(count == 0 && activid == false || count == 0 && isExpanded == false) {
            IconButton(
                onClick = {
                    isExpanded = true
                    addBoxViewModel.active(id)
                    addBoxViewModel.plus(food, selectedSize.value.toString())
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
                        addBoxViewModel.active(id)
                    }else{
                        addBoxViewModel.active(id)
                        addBoxViewModel.plus(food, selectedSize.value.toString())
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
                        addBoxViewModel.plus(food, selectedSize.value.toString())
                    },
                    contentAlignment = Alignment.Center
                ){
                    Row(
                        modifier = Modifier.
                        fillMaxSize().
                        background(color),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                    ){
                        IconButton(onClick = {addBoxViewModel.minus(food.id, selectedSize.value.toString())}, modifier = Modifier.weight(1f).fillMaxHeight()){
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
                        IconButton(onClick = {addBoxViewModel.plus(food, selectedSize.value.toString())}, modifier = Modifier.weight(1f).fillMaxHeight()){
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

//BasicTextField(
//value = Cart.cartMap()[cartkey].toString(),
//onValueChange = { newValue ->
//    if (newValue.isNotEmpty()) {
//        if((newValue.all { it.isDigit() } && newValue.length <= 2)){
//            val newCount = newValue.toIntOrNull() ?: count
//            ordernumber.updateCount(food, selectedSize.value.toString(), newCount)
//        }
//    }else{
//        val newCount = newValue.toIntOrNull() ?: 0
//        ordernumber.updateCount(food, selectedSize.value.toString(), newCount)
//    }
//},
//modifier = Modifier.
//focusRequester(focusRequester).
//// السطر ده بيلقط أول ما المستخدم يدوس على الـ TextField عشان يكتب
//onFocusChanged { focusState ->
//    active = focusState.isFocused
//    if (focusState.isFocused) {
//        isExpanded = true // بنخليه مفرود فوراً أول ما يركز فيه
//    }
//},
//keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
//keyboardActions = KeyboardActions(
//onDone = {
//    keyboardController?.hide()
//    focusManager.clearFocus()
//}
//),
//singleLine = true,
//textStyle = TextStyle(
//textAlign = TextAlign.Center,
//fontSize = 20.sp,
//color = Color.Black
//)
//)