package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.models.model.OrderItemsClass
import com.example.applicationhome.data.models.repository.OrderRepository.lastOrders
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.LightOrange
import com.example.applicationhome.ui.theme.components.bars.MyTopBar
import com.example.applicationhome.ui.theme.components.forCart.PaymentSummary
import com.example.applicationhome.ui.theme.model.OrderScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderScreen(orderScreenViewModel : OrderScreenViewModel, navigationController : NavHostController){
    LaunchedEffect(
        key1 = lastOrders
    ){
        orderScreenViewModel.getOrdersHistory()
    }
    val order = orderScreenViewModel.selectedOrder
    Scaffold(
        modifier = Modifier.fillMaxSize().
        background(Color.White),
        topBar = {
            MyTopBar(
                Color.DarkOrange,
                modifier = Modifier.
                fillMaxWidth().
                height(100.dp).
                shadow(elevation = 5.dp),
                "Order",
                Color.White,
                {
                    IconButton(
                        onClick = {
                            if (navigationController.previousBackStackEntry != null) { navigationController.popBackStack() }
                        },
                        modifier = Modifier.padding(5.dp).
                        border(width = 1.dp, color = Color.LightGray.copy(alpha = 0.25f), shape = RoundedCornerShape(30.dp)).
                        shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = CircleShape).clip(CircleShape).size(40.dp).
                        background(Color.White)
                    ){
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
                    }
                },
            )
        }
    ) {
        Box(modifier = Modifier.background(Color.White)) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(100.dp)) }
                    items(order.orderItems) { item ->
                        OrderDetelseBox(item)
                    }
                    item {
                        PaymentSummary(order.totalPrice)
                    }
                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }
            }
        }
    }
}

@Composable
fun OrderDetelseBox(
    food: OrderItemsClass
){
    val price = "EGP " + food.price.toString()

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
                            text = food.mealName,
                            fontSize = 18.sp,
                            color = Color.Black,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "${price} x ${food.quantity}" ,
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