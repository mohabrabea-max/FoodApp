package com.example.applicationhome.ui.theme.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.FavoriteViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

@Composable
fun CartBox(
    item: FoodItem,
    size : String,
    navigationController : NavHostController,
    viewModel: ItemScreenViewModel,
    ordernumber : AddBoxViewModel,
    favoriteState: FavoriteViewModel
) {
    var cartnumber = Cart.cartMap()
    var cartkey = CartKey(item, size)
    var count = ordernumber.cartMap[cartkey] ?: 0
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Column{
        ItemsBox(
            item,
            navigationController,
            viewModel,
            size,
            {
                Favorite(
                    modifier = Modifier.padding(10.dp).
                    clip(CircleShape).
                    size(35.dp).
                    background(Color.White.copy(alpha = 1f)),
                    food = item,
                    favoriteState = favoriteState
                )
            }
        )
        //Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier.height(40.dp).
            fillMaxWidth().
            clip(RoundedCornerShape(10.dp)).
            background(Color.White).
            padding(start = 5.dp, end = 5.dp).
            border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(10.dp))

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
                    clickable {ordernumber.addBoxNumberPlus(item, size)},
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
                    clickable {cartnumber[cartkey]},
                    contentAlignment = Alignment.Center
                ){
                    BasicTextField(
                        value = cartnumber[cartkey].toString(),
                        onValueChange = { newValue ->
                            if (newValue.isNotEmpty()) {
                                if(newValue.all {it.isDigit()} && newValue.length <= 2){
                                    val newCount = newValue.toIntOrNull() ?: count
                                    ordernumber.updateCount(item, size, newCount)
                                }
                            }else{
                                val newCount = newValue.toIntOrNull() ?: 0
                                ordernumber.updateCount(item, size, newCount)
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
                    clickable {ordernumber.addBoxNumberMinus(item, size)},
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