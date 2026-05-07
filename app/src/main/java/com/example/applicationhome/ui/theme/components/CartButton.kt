package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.view.model.AddBoxViewModel

@Composable
fun CartButton(
    addBoxViewModel : AddBoxViewModel
){
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Box(
            modifier = Modifier.width(240.dp).
            height(30.dp).
            shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)).
            border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)).
            background(Color.VeryLightGray),
            contentAlignment = Alignment.Center
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = addBoxViewModel.totalNumber.value.toString(),
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }
                VerticalDivider(color = Color.Black, modifier = Modifier.padding(top = 4.dp, bottom = 4.dp))
                Row(modifier = Modifier.weight(2f), verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = "${addBoxViewModel.totalPrice.value} E.G",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Box(
            modifier = Modifier.width(300.dp).
            height(50.dp).
            shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(50.dp)).
            background(Color.Blue).
            clickable{addBoxViewModel.bay()},
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Confirm order",
                fontSize = 20.sp,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }
}