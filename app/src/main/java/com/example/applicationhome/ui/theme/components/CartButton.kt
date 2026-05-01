package com.example.applicationhome.ui.theme.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.view.model.AddBoxViewModel


@SuppressLint("UnrememberedMutableState")
@Composable
fun CartButton(
    addBoxViewModel : AddBoxViewModel,
    cart : Map<CartKey, Int>
){
    Box(
        modifier = Modifier.width(220.dp).
        height(50.dp).
        clip(RoundedCornerShape(50.dp)).
        background(Color.Green).
        clickable{addBoxViewModel.bay()}.
        border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(50.dp)),
        contentAlignment = Alignment.Center
    ){
        Row(verticalAlignment = Alignment.CenterVertically){
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = addBoxViewModel.totalNumber.toString(),
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
            VerticalDivider(color = Color.White, modifier = Modifier.padding(top = 3.dp, bottom = 3.dp))
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "${addBoxViewModel.totalPrice} E.G",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}