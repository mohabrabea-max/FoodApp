package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.models.repository.CartRepository
import com.example.applicationhome.ui.theme.model.AddBoxViewModel

@Composable
fun PaymentSummary(
    addBoxViewModel : AddBoxViewModel
){
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(20.dp)
    ){
        Text(
            text = "Payment summary",
            fontSize = 20.sp,
            color = Color.Black,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Subtotal",
                fontSize = 14.sp,
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "EGP ${CartRepository.totalPrice.value}",
                fontSize = 14.sp,
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Delivery fee",
                fontSize = 14.sp,
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "EGP 51.00",
                fontSize = 14.sp,
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Service fee",
                fontSize = 14.sp,
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "EGP 8.00",
                fontSize = 14.sp,
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Total amount",
                fontSize = 17.sp,
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "EGP ${CartRepository.totalPrice.value}",
                fontSize = 17.sp,
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}