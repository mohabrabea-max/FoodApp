package com.example.applicationhome.ui.theme.components

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

@Composable
fun CartBox(
    item: FoodItem,
    size : String,
    number : Int,
    navigationController : NavHostController,
    viewModel: ItemScreenViewModel,
    ordernumber : AddBoxViewModel = viewModel()
) {
    var cartnumber = Cart.cartMap()
    var cartkey = CartKey(item, size)
    val context = LocalContext.current
    var count = ordernumber.cartMap[cartkey] ?: 0
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var total = item.priceANDsize[size] ?: 0
    var totalPrice = total.toInt() * number
    Box(
        modifier = Modifier.
        animateContentSize().
        aspectRatio(0.7f).
        clickable{
            viewModel.selectItem(item, item.priceANDsize.keys.last())
            navigationController.navigate(Screens.ItemScreen.screen)
        }.
        padding(5.dp).
        clip(RoundedCornerShape(10.dp))
    ){
        Column(
            modifier = Modifier.
            background(Color.White)
        ) {
            Box(modifier = Modifier.fillMaxSize().weight(4f)){
                Image(
                    painter = painterResource(id = item.image[0]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier.fillMaxWidth().
                    background(Color.White.copy(alpha = 0.7f)).
                    padding(5.dp).align(Alignment.BottomCenter),
                    contentAlignment = Alignment.CenterStart
                ){
                    Text(
                        text = item.name + " ( $size )",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontSize = 15.sp,
                    )
                }
            }
            Box(
                modifier = Modifier.
                fillMaxWidth().
                clip(RoundedCornerShape(10.dp)).
                background(Color.DarkOrange),
                contentAlignment = Alignment.Center
            ){
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Information",
                        tint = Color.White,
                        modifier = Modifier.
                        clip(CircleShape).
                        alpha(0.8f).
                        clickable {Toast.makeText(context, "Price : ${item.priceANDsize["Small"]}", Toast.LENGTH_SHORT).show()}
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${totalPrice} L.E",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier.height(40.dp).
                fillMaxWidth().
                clip(RoundedCornerShape(10.dp)).
                background(Color.White).
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
}