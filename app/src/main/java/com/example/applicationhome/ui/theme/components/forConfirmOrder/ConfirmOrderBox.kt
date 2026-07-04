package com.example.applicationhome.ui.theme.components.forConfirmOrder

import android.R.attr.name
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.models.local.entity.CartItemsClass
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.phoneNumberState
import com.example.applicationhome.ui.theme.LightOrange
import com.example.applicationhome.ui.theme.model.LoginViewModel

@Composable
fun ConfirmOrderBox(
    food: CartItemsClass,
    loginViewModel: LoginViewModel
){
    LaunchedEffect(loginViewModel.userData.collectAsState().value.phonenumber){
        if(loginViewModel.userData.value.phonenumber.isNotEmpty()){
            phoneNumberState.setTextAndPlaceCursorAtEnd(loginViewModel.userData.value.phonenumber)
        }
    }
    val number = food.quantity
    val size = food.size

    val sizeInTitle = if(size.contains("Pieces")) "" else " (${size})"

    Box(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp).
        fillMaxWidth().height(100.dp).
        background(Color.White)
    ){
        Column(modifier = Modifier.fillMaxSize()){
            Row(
                modifier = Modifier.weight(4f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    AsyncImage(
                        modifier = Modifier.fillMaxHeight().weight(1f).padding(10.dp),
                        model = ImageRequest.Builder(LocalContext.current).
                        data(food.image).
                        crossfade(true).
                        size(400, 400).
                        precision(Precision.EXACT).
                        build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(3f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = "${name}${sizeInTitle}",
                            fontSize = 18.sp,
                            color = Color.Black,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "${food.priceOfOne} x ${number}" ,
                            fontSize = 15.sp,
                            color = Color.Red,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Divider(color = Color.LightOrange.copy(alpha = 0.5f), modifier = Modifier.padding(start = 10.dp, end = 10.dp))
        }
    }
}