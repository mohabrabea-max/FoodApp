package com.example.applicationhome.features.orders.ui.lastorders

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.theme.DeepMatteBlack
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.features.orders.ui.LastOrdersBox
import com.example.applicationhome.features.orders.ui.OrderScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LastOrdersScreen(
    navigationController : NavHostController,
    orderScreenViewModel : OrderScreenViewModel
){
    LaunchedEffect(key1 = orderScreenViewModel.isNetworkAvailable) {
        orderScreenViewModel.getOrdersHistory()
    }

    val ordersHistory by orderScreenViewModel.ordersHistory.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.navigationBarsPadding().fillMaxSize(),
        topBar = {
            MyTopBar(
                Color.White,
                modifier = Modifier.fillMaxWidth().height(100.dp).shadow(elevation = 5.dp),
                "Orders History",
                Color.DeepMatteBlack,
                {
                    IconButton(
                        onClick = {
                            if (navigationController.previousBackStackEntry != null) {
                                navigationController.popBackStack()
                            }
                        },
                        modifier = Modifier.padding(5.dp).clip(CircleShape).size(40.dp)
                            .background(Color.White)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.DeepMatteBlack
                        )
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
                items(ordersHistory){ item ->
                    Spacer(modifier = Modifier.height(10.dp))
                    LastOrdersBox(
                        navigationController,
                        orderScreenViewModel,
                        item
                    )
                }
                item{Spacer(modifier = Modifier.height(100.dp))}
            }
        }
    }
}