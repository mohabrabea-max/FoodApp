package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.data.repository.ConfirmOrderScreenTextField.textFieldConfirmOrderScreenList1
import com.example.applicationhome.data.data.repository.ConfirmOrderScreenTextField.textFieldConfirmOrderScreenList2
import com.example.applicationhome.ui.theme.BrandBlue
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.components.bars.MyTopBar
import com.example.applicationhome.ui.theme.components.forCart.CartButton
import com.example.applicationhome.ui.theme.components.forCart.PaymentSummary
import com.example.applicationhome.ui.theme.components.forConfirmOrder.ConfirmOrderBox
import com.example.applicationhome.ui.theme.components.forConfirmOrder.ConfirmOrderScreenTextField2
import com.example.applicationhome.ui.theme.components.forConfirmOrder.TextFieldForConfirmOrder
import com.example.applicationhome.ui.theme.model.ConfirmOrderScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter",
    "UnrememberedMutableState",
    "ContextCastToActivity"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmOrderScreen(
    navigationController : NavHostController,
    confirmOrderScreenViewModel : ConfirmOrderScreenViewModel
){
    val loading by confirmOrderScreenViewModel.loading.collectAsStateWithLifecycle()

    val cart by confirmOrderScreenViewModel.cartItems.collectAsStateWithLifecycle()

    val totalprice = confirmOrderScreenViewModel.totalPrice

    val clickState = remember { mutableStateOf(true) }

    var color = if(confirmOrderScreenViewModel.bottonState) Color.DarkOrange else Color.Gray
    var fontcolor = if(confirmOrderScreenViewModel.bottonState) Color.White else Color.Black

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
                            if(confirmOrderScreenViewModel.confirmOrderPages == 1){
                                confirmOrderScreenViewModel.lastPage()
                            }else{
                                if (navigationController.previousBackStackEntry != null) { navigationController.popBackStack() }
                                confirmOrderScreenViewModel.cleanTextField()
                                confirmOrderScreenViewModel.lastPage()
                            }
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
    ){
        Box(modifier = Modifier.background(Color.White)) {
            Box(modifier = Modifier.fillMaxSize()) {
                if(confirmOrderScreenViewModel.confirmOrderPages == 1){
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ){
                        item{Spacer(modifier = Modifier.height(100.dp))}

                        item{
                            Box(
                                modifier = Modifier.padding(10.dp)
                                    .height(130.dp)
                                    .fillMaxWidth()
                                    .clip(shape = RoundedCornerShape(15.dp))
                                    .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(15.dp))
                                    .background(Color.White)
                            )
                        }

                        item{
                            Row(
                                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                                    .height(70.dp)
                                    .fillMaxWidth()
                                    .clip(shape = RoundedCornerShape(15.dp))
                                    .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(15.dp))
                                    .background(Color.White)
                                    .clickable {  }
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ){
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.padding(end = 7.dp)
                                    )
                                    Column(
                                        modifier = Modifier.padding(horizontal = 7.dp).fillMaxHeight(),
                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ){
                                        Text(
                                            text = "Area",
                                            color = Color.Gray,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Hadayek Helwan",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                                Box(contentAlignment = Alignment.CenterEnd){
                                    Text(
                                        text = "Change",
                                        color = Color.DarkOrange,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }

                        item{Spacer(modifier = Modifier.height(15.dp))}

                        items(textFieldConfirmOrderScreenList1){item ->
                            TextFieldForConfirmOrder(confirmOrderScreenViewModel, item.textState, item.title)
                        }

                        item{ ConfirmOrderScreenTextField2(confirmOrderScreenViewModel) }

                        items(textFieldConfirmOrderScreenList2){item ->
                            TextFieldForConfirmOrder(confirmOrderScreenViewModel, item.textState, item.title)
                        }

                        item{Spacer(modifier = Modifier.height(100.dp))}
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        item{Spacer(modifier = Modifier.height(100.dp))}
                        items(cart) { item ->
                            if(item != null)ConfirmOrderBox(item, confirmOrderScreenViewModel)
                        }
                        item{
                            PaymentSummary(totalprice)
                        }
                        item{Spacer(modifier = Modifier.height(100.dp))}
                    }
                }

            }
            Column(modifier = Modifier.align(Alignment.BottomCenter), horizontalAlignment = Alignment.CenterHorizontally){
                if(confirmOrderScreenViewModel.confirmOrderPages == 1){
                    CartButton(
                        loading,
                        color,
                        fontcolor,
                        "Save address"
                    ){
                        if (confirmOrderScreenViewModel.bottonState) {
                            confirmOrderScreenViewModel.nextPage()
                        }
                    }
                }else{
                    CartButton(
                        loading,
                        Color.BrandBlue,
                        Color.White,
                        "Confirm order"
                    ){
                        if(clickState.value){
                            clickState.value = false
                            confirmOrderScreenViewModel.uploadOrder(
                                onSuccess = {
                                    confirmOrderScreenViewModel.clearAllCart()
                                    navigationController.navigate(Screens.DashboardScreen.screen){
                                        popUpTo(Screens.DashboardScreen.screen){
                                            inclusive = true
                                        }
                                        confirmOrderScreenViewModel.lastPage()
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