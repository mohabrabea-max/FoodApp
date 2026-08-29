package com.example.applicationhome.features.orders.ui.lastorders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.theme.BrandBlue
import com.example.applicationhome.core.ui.theme.BrownForFont
import com.example.applicationhome.data.data.model.OrderStatesEnum
import com.example.applicationhome.data.data.model.OrderUiClass

@Composable
fun LastOrdersBox(
    order : OrderUiClass,
    onOpenOrderScreen : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    val orderState = order.state.enumState
    val orderStateTitle = stringResource(order.state.title)

    val color =
        when(orderState){
            OrderStatesEnum.PREPARING -> { Color.BrandBlue }
            OrderStatesEnum.DELIVERING -> { Color.BrandBlue }
            OrderStatesEnum.DELIVERED -> { Color.Green }
            OrderStatesEnum.CANCELLED -> { Color.Red }
        }

    val quantity = order.orderItems.sumOf { item -> item.quantity }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ){
                onOpenOrderScreen()
            }
            .background(Color.White),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
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
                    text = stringResource(R.string.order),
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.BrownForFont,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                Text(
                    text = "${order.orderId}",
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
                    text = stringResource(R.string.restaurant),
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
                    text = stringResource(R.string.items),
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.BrownForFont,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = "$quantity",
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
                    text = stringResource(R.string.price),
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.BrownForFont,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = "EGP ${order.totalPrice}",
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
                        text = orderStateTitle,
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