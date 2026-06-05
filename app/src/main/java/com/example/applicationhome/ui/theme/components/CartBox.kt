package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.data.models.repository.CartRepository
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuList
import com.example.applicationhome.data.models.repository.MenuRepository.snacks
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.LightOrange
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

@Composable
fun CartBox(
    id: Int,
    size : String,
    navigationController : NavHostController,
    viewModel: ItemScreenViewModel,
    ordernumber : AddBoxViewModel,
){
    val fooditem = foodMenuList.values.find { it.id == id }
    val snackitem = snacks.values.find { it.id == id }
    val cartkey : String
    val count : Int
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val image : String
    val name : String
    val price : String
    if(fooditem != null){
        cartkey = "${id}_${size}"
        count = CartRepository.cartItems[cartkey]?.number ?: 0
        name = fooditem.name
        price = "EGP " + fooditem.sizeOptions.find { it.size == size }?.price.toString()
        image = fooditem.image.first()
    }else{
        cartkey = "${id}_${size}"
        count = CartRepository.cartItems[cartkey]?.number ?: 0
        name = snackitem?.name ?: ""
        price = "EGP " + snackitem?.priceANDsize[size].toString()
        image = snackitem?.image?.first() ?: ""
    }
    Box(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp).
        fillMaxWidth().height(100.dp).
        background(Color.White).
        clickable {
            if(fooditem != null) viewModel.run { selectItem(fooditem, fooditem.sizeOptions.find { it.size == size }?.size.toString()) }
            navigationController.navigate(Screens.ItemScreen.screen)
        }
    ){
        Column(modifier = Modifier.fillMaxSize()){
            Row(
                modifier = Modifier.weight(4f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row(modifier = Modifier.weight(3f), verticalAlignment = Alignment.CenterVertically){
                    AsyncImage(
                        modifier = Modifier.fillMaxHeight().weight(1f).padding(10.dp),
                        model = ImageRequest.Builder(LocalContext.current).
                        data(image).
                        crossfade(true).
                        size(400, 400).
                        precision(Precision.EXACT).
                        build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(2f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = name,
                            fontSize = 18.sp,
                            color = Color.Black,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = price,
                            fontSize = 15.sp,
                            color = Color.Red,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Row(
                    modifier = Modifier.height(60.dp).
                    weight(1.8f),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(onClick = { ordernumber.delete(id, size) }, modifier = Modifier.weight(1f)){
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                    }
                    Box(
                        modifier = Modifier.height(60.dp).
                        weight(6f).
                        padding(10.dp).
                        clip(CircleShape).
                        background(Color.LightOrange.copy(alpha = 0.7f))
                    ){
                        Row(
                            modifier = Modifier.fillMaxSize().padding(3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Box(
                                modifier = Modifier.size(30.dp).clip(CircleShape).background(Color.White),
                                contentAlignment = Alignment.Center
                            ){
                                IconButton(onClick = {ordernumber.minus(id, size)}){
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
                                                ordernumber.updateCount(id, size, newCount as Int)
                                            }
                                        }else{
                                            val newCount = newValue.toIntOrNull() ?: 0
                                            ordernumber.updateCount(id, size, newCount)
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
                                IconButton(onClick = {ordernumber.plus(id, size)}){
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Divider(color = Color.LightOrange.copy(alpha = 0.5f), modifier = Modifier.padding(start = 10.dp, end = 10.dp))
        }
    }
}