package com.example.applicationhome.ui.theme.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationhome.ui.theme.Orange
import com.example.applicationhome.view.model.AddBoxViewModel

@Composable
fun CartButton(modifier: Modifier = Modifier, cartNumber : AddBoxViewModel = viewModel()){
    val context = LocalContext.current
    var cart2 = cartNumber.totalCart.value ?: 0
    if(cart2 > 0){
        Box(
            modifier = modifier.size(150.dp).
            aspectRatio(3f).
            clip(shape = RoundedCornerShape(30.dp)).
            background(Color.Orange).
            clickable{
                cartNumber.addToCart()
                Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show()
            }.
            border(width = 1.dp, color = Color.Black.copy(alpha = 1f), shape = RoundedCornerShape(30.dp)).
            padding(5.dp),
            contentAlignment = Alignment.Center
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.White, modifier = Modifier.weight(1f))
                VerticalDivider(color = Color.Black ,modifier = Modifier.align(Alignment.CenterVertically))
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                    Text(text = cart2.toString(), style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
            }
        }
    }
}