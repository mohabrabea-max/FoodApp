package com.example.applicationhome.ui.theme.components.forConfirmOrder

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.models.model.OrdersClass
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.model.OrderScreenViewModel

@Composable
fun LastOrdersBox(navigationController: NavHostController, orderScreenViewModel : OrderScreenViewModel, order : OrdersClass, orderId : String){
    var color by remember { mutableStateOf(Color.Gray) }
    if(order.state == "Preparing" || order.state == "Out for Delivery"){
        color = Color.DarkOrange
    }else if(order.state == "Delivered"){
        color = Color.Green
    }else if(order.state == "Cancelled" || order.state == "Failed"){
        color = Color.Red
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .height(160.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .clickable {
                navigationController.navigate(Screens.OrderScreen.screen)
                orderScreenViewModel.selectorder(order)
            }
            .shadow(elevation = 5.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(15.dp))
            .background(Color.White)
    ){
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(
                modifier = Modifier.weight(4f),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp)),
                    contentAlignment = Alignment.Center
                ){
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).
                        data(order.restaurantImage).
                        crossfade(true).
                        precision(Precision.EXACT).
                        build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier.fillMaxHeight(0.8f),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = "Order",
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                        Text(
                            text = orderId,
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = "Restaurant",
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                        Text(
                            text = order.restaurantName,
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = "Items",
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                        Text(
                            text = order.orderItems.size.toString(),
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Blue,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = "Price",
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                        Text(
                            text = order.totalPrice.toString(),
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }


                    //Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = order.date,
                            fontSize = 13.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Gray,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(25.dp)
                                .clip(shape = RoundedCornerShape(10.dp))
                                .background(color),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = order.state,
                                fontSize = 10.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}