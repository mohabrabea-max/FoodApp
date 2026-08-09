package com.example.applicationhome.features.confirmorder.ui.pagetow

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.shortBottomSnackBar
import com.example.applicationhome.core.ui.theme.BrandBlue
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.DeepMatteBlack
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.ActionsStates
import com.example.applicationhome.data.data.model.PaymentMethod
import com.example.applicationhome.data.data.model.PaymentState
import com.example.applicationhome.data.local.entity.CartItemsClass
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PageTowConfirmOrder(
    confirmOrderState : ActionsStates,
    snackBarHostState : SnackbarHostState,
    scope : CoroutineScope,
    cart : List<CartItemsClass?>,
    totalPrice : Double,
    locationImage : String,
    city : String,
    streetAndHome : Pair<String, String>,
    phoneNumber : String,
    payMethodState : PaymentMethod = PaymentMethod.CARD,
    paymentState : PaymentState,
    onMethodSelected : (PaymentMethod) -> Unit,
    changePageNumber : (Int) -> Unit,
    startPayment : () -> Unit,
    uploadOrder : (Boolean) -> Unit
){
    val paymentButtonState = (payMethodState == PaymentMethod.CASH || paymentState == PaymentState.Success)


    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),

        containerColor = Color.White,

        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    Snackbar(
                        modifier = Modifier.padding(12.dp).height(50.dp).clip(RoundedCornerShape(10.dp)),
                        containerColor = Color.DeepMatteBlack,
                        contentColor = Color.White
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Error Icon",
                                    tint = Color.White
                                )

                                Text(text = data.visuals.message)
                            }
                        }
                    }
                }
            )
        },

        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .pointerInput(Unit) { detectTapGestures { } },
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                MyButton(
                    confirmOrderState == ActionsStates.Loading,
                    if(paymentButtonState) Color.BrandBlue else Color.VeryLightGray,
                    if(paymentButtonState) Color.White else Color.LightGray,
                    40.dp,
                    "Confirm order"
                ) {
                    uploadOrder(paymentButtonState)
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            item{Spacer(modifier = Modifier.height(130.dp))}

            // --------------------------------------------\\ Location Box //--------------------------------------------
            item{
                LocationBoxForPageTow(
                    locationImage = locationImage,
                    city = city,
                    streetAndHome = streetAndHome,
                    phoneNumber = phoneNumber
                ){
                    changePageNumber(4)
                }
            }

            items(cart) { item ->
                if(item != null) ConfirmOrderBox(
                    item
                )
            }

            // --------------------------------------------\\ Pay Methods //--------------------------------------------
            item {
                PaymentMethodsBox(
                    payMethodState
                ){
                    onMethodSelected(it)
                }
            }

            if(payMethodState != PaymentMethod.CASH) item{
                Column(
                    modifier = Modifier.pointerInput(Unit) { detectTapGestures { } },
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    MyButton(
                        loading = paymentState == PaymentState.Loading,
                        backgroundcolor = if(paymentState == PaymentState.Success) Color.Green else Color.DarkOrange,
                        fontcolor = Color.White,
                        horizontalPadding = 50.dp,
                        title = if(paymentState == PaymentState.Success) "Payment success!" else "Pay now!"
                    ){
                        startPayment()
                        changePageNumber(5)
                    }
                }
            }

            item{
                PaymentSummaryConfirmOrderScreen(
                    totalPrice
                )
            }

            item{Spacer(modifier = Modifier.height(100.dp))}
        }
    }

    when(paymentState){
        PaymentState.Failed -> {
            scope.shortBottomSnackBar(
                snackBarHostState = snackBarHostState,
                message = "Payment failed. Please try again."
            )
        }

        else -> {}
    }
}