package com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.model.Food
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.repository.CartRepository.allCart
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.model.AddBoxViewModel
import com.example.applicationhome.ui.theme.model.BottomBarViewModel
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBarForItemScreen(
    addBoxViewModel: AddBoxViewModel,
    food : Food,
    size : String,
    snackbarHostState : SnackbarHostState,
    scope : CoroutineScope,
    navigationController : NavHostController,
    bottomBarViewModel: BottomBarViewModel
){
    val price = when(food){
        is FoodItem -> { addBoxViewModel.newCount * (food.sizeOptions.find { it.size == size }?.price ?: 0.0) }
        is Snack -> { addBoxViewModel.newCount * (food.priceANDsize[size] ?: 0.0) }
    }
    var color : Color
    var fontColor : Color
    if(addBoxViewModel.newCount == 0){
        color = Color.VeryLightGray
        fontColor = Color.Gray
    }else{
        color = Color.DarkOrange
        fontColor = Color.White
    }

    Box(
        modifier = Modifier.fillMaxWidth().
        height(100.dp).
        shadow(elevation = 7.dp).
        background(Color.White).
        pointerInput(Unit) {
            detectTapGestures { }
        }.
        padding(horizontal = 15.dp).
        navigationBarsPadding()
    ){
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Box(
                modifier = Modifier.weight(1.2f).
                height(50.dp).
                clip(RoundedCornerShape(50.dp)).
                background(Color.White).
                border(width = 0.5.dp, color = Color.LightGray, shape = CircleShape).padding(4.dp)
            ){
                Row(
                    modifier = Modifier.
                    fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                ){
                    IconButton(onClick = { if(addBoxViewModel.newCount > 0) addBoxViewModel.minusnewCount() }, modifier = Modifier.weight(1f).fillMaxHeight()){
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = null,
                            tint = if(addBoxViewModel.newCount > 0) Color.DarkOrange else Color.Gray,
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
                            text = addBoxViewModel.newCount.toString(),
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = { if(addBoxViewModel.newCount < 99) addBoxViewModel.plusnewCount() }, modifier = Modifier.weight(1f).fillMaxHeight()){
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = if(addBoxViewModel.newCount < 99) Color.DarkOrange else Color.Gray,
                            modifier = Modifier.fillMaxSize().padding(5.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.weight(2f).
                height(50.dp).
                clip(RoundedCornerShape(50.dp)).
                background(color).
                clickable {
                    if(addBoxViewModel.newCount > 0){
                        addBoxViewModel.updateCount(food, size, addBoxViewModel.newCount)
                        if(addBoxViewModel.newCount > 0 && food.restaurantId == allCart.value.restaurantId){
                            scope.showAddToCartSnackbar(
                                snackbarHostState,
                                {
                                    navigationController.navigate(Screens.Cart.screen){ launchSingleTop = true }
                                }

                            )
                            addBoxViewModel.deletenewCount()
                        }
                    }
                }.
                padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Add item",
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = fontColor,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "EGP ${price}",
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = fontColor,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}