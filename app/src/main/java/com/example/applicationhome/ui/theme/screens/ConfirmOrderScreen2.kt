package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.ui.theme.BrandBlue
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.components.bars.MyTopBar
import com.example.applicationhome.ui.theme.components.forCart.CartButton
import com.example.applicationhome.ui.theme.components.forCart.PaymentSummary
import com.example.applicationhome.ui.theme.components.forConfirmOrder.ConfirmOrderBox
import com.example.applicationhome.ui.theme.model.CartViewModel
import com.example.applicationhome.ui.theme.model.ConfirmOrderScreenViewModel
import com.example.applicationhome.ui.theme.model.LoginViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter",
    "UnrememberedMutableState",
    "ContextCastToActivity"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmOrderScreen2(
    navigationController : NavHostController,
    confirmOrderScreenViewModel : ConfirmOrderScreenViewModel,
    cartViewModel: CartViewModel,
    loginViewModel: LoginViewModel
){
    val cart by cartViewModel.cartItems.collectAsStateWithLifecycle()

    val clickState = remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.navigationBarsPadding().fillMaxSize(),
        topBar = {
            MyTopBar(
                Color.DarkOrange,
                modifier = Modifier.
                fillMaxWidth().
                height(100.dp).
                shadow(elevation = 5.dp),
                "Checkout",
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
                ){
                    item{Spacer(modifier = Modifier.height(100.dp))}
                    items(cart) { item ->
                        if(item != null)ConfirmOrderBox(item, loginViewModel)
                    }
                    item{
                        PaymentSummary(cartViewModel)
                    }
                    item{Spacer(modifier = Modifier.height(100.dp))}
                }
            }
            Column(modifier = Modifier.align(Alignment.BottomCenter), horizontalAlignment = Alignment.CenterHorizontally){
                if(cart.isNotEmpty()){
                    CartButton(
                        confirmOrderScreenViewModel,
                        Color.BrandBlue,
                        Color.White,
                        "Confirm order"
                    ){
                        if(clickState.value){
                            clickState.value = false
                            confirmOrderScreenViewModel.uploadOrder(
                                onSuccess = {
                                    cartViewModel.clearAllCart()
                                    navigationController.navigate(Screens.DashboardScreen.screen){
                                        popUpTo(Screens.DashboardScreen.screen){
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}