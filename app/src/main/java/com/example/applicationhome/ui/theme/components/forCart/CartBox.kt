package com.example.applicationhome.ui.theme.components.forCart

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.models.local.CartItemsClass
import com.example.applicationhome.data.models.model.CartClassForCalculations
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.ui.theme.LightOrange
import com.example.applicationhome.ui.theme.model.CartViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel

@Composable
fun CartBox(
    food: CartItemsClass,
    navigationController : NavHostController,
    viewModel: ItemScreenViewModel,
    cartViewModel: CartViewModel,
){
    val item = cartViewModel.meal
    val size = food.size
    val meal = CartClassForCalculations(
        item?.id ?: 0,
        item?.name ?: "",
        item?.image?.first() ?: "",
        food.priceOfOne,
        size,
        food.type,
        item?.restaurantId ?: 0
    )
    val sizeInTitle = if(size.contains("Pieces")) "" else " (${size})"

    val cartkey = "${food.mealId}_${size}"

    Box(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp).
        fillMaxWidth().height(100.dp).
        background(Color.White).
        clickable {
            cartViewModel.getMeal(food.mealId)
            viewModel.selectItem(item, size)
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
                        data(food.image).
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
                            text = "${food.name}${sizeInTitle}",
                            fontSize = 18.sp,
                            color = Color.Black,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = food.priceOfOne.toString(),
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
                    IconButton(onClick = { cartViewModel.delete(food.mealId, size) }, modifier = Modifier.weight(1f)){
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                    }
                    Box(
                        modifier = Modifier.height(60.dp).
                        weight(6f).
                        padding(10.dp).
                        clip(CircleShape).
                        background(Color.LightOrange.copy(alpha = 0.7f))
                    ){
                        if(meal != null) FixedAddBox(cartViewModel,size, cartkey, meal)
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Divider(color = Color.LightOrange.copy(alpha = 0.5f), modifier = Modifier.padding(start = 10.dp, end = 10.dp))
        }
    }
}