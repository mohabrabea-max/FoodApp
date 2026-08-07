package com.example.applicationhome.features.confirmorder.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.applicationhome.data.local.entity.CartItemsClass

@Composable
fun PageTowConfirmOrder(
    cart: List<CartItemsClass?>,
    totalprice: Double
){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        item{Spacer(modifier = Modifier.height(100.dp))}
        items(cart) { item ->
            if(item != null) ConfirmOrderBox(
                item
            )
        }
        item{
            PaymentSummaryConfirmOrderScreen(
                totalprice
            )
        }
        item{Spacer(modifier = Modifier.height(100.dp))}
    }
}