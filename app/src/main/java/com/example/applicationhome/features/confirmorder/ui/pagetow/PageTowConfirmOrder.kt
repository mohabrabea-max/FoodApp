package com.example.applicationhome.features.confirmorder.ui.pagetow

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.PaymentMethod
import com.example.applicationhome.data.data.model.PaymentState
import com.example.applicationhome.data.local.entity.CartItemsClass

@Composable
fun PageTowConfirmOrder(
    cart : List<CartItemsClass?>,
    totalPrice : Double,
    locationImage : String,
    city : String,
    streetAndHome : Pair<String, String>,
    phoneNumber : String,
    payMethodState : PaymentMethod = PaymentMethod.CARD,
    paymentState : PaymentState,
    changeLocation : () -> Unit,
    onMethodSelected : (PaymentMethod) -> Unit
){
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
                streetAndHome = streetAndHome
            ){
                changeLocation()
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
                    // فتح بوابة الدفع
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