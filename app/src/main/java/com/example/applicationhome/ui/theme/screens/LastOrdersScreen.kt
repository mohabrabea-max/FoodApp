package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.repository.OrderRepository.lastOrders
import com.example.applicationhome.ui.theme.DeepMatteBlack
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.bars.MyTopBar
import com.example.applicationhome.ui.theme.components.forConfirmOrder.LastOrdersBox
import com.example.applicationhome.ui.theme.model.OrderScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LastOrdersScreen(navigationController : NavHostController, orderScreenViewModel : OrderScreenViewModel){
    val allOrders = lastOrders.entries.toList().sortedByDescending { entry -> entry.key.toLong() }
    Scaffold(
        modifier = Modifier.fillMaxSize().
        background(Color.White),
        topBar = {
            MyTopBar(
                Color.White,
                modifier = Modifier.
                fillMaxWidth().
                height(100.dp).
                shadow(elevation = 5.dp),
                "Orders History",
                Color.DeepMatteBlack,
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
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.DeepMatteBlack)
                    }
                }
            )
        }
    ){
        Box(modifier = Modifier.background(Color.VeryLightGray)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ){
                item{Spacer(modifier = Modifier.height(100.dp))}
                items(allOrders){ item ->
                    Spacer(modifier = Modifier.height(10.dp))
                    LastOrdersBox(navigationController, orderScreenViewModel, item.value, item.key)
                }
                item{Spacer(modifier = Modifier.height(100.dp))}
            }
        }
    }
}