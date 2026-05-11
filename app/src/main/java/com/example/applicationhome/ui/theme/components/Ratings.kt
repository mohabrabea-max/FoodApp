package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.ui.theme.BrandBlue
import com.example.applicationhome.ui.theme.DarkOrange

@Composable
fun Ratings(item: FoodItem){
    Column(                             // البوكس بتاع التقييمات
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ){
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Name",
                fontSize = 14.sp,
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall
            )
            Icon(
                Icons.Default.Stars,
                contentDescription = null,
                tint = Color.BrandBlue,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(verticalAlignment = Alignment.Bottom){
            Icon(Icons.Default.Star, contentDescription = null, tint = Color.DarkOrange)
            Icon(Icons.Default.Star, contentDescription = null, tint = Color.DarkOrange)
            Icon(Icons.Default.Star, contentDescription = null, tint = Color.DarkOrange)
            Icon(Icons.Default.Star, contentDescription = null, tint = Color.DarkOrange)
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Time",
                fontSize = 10.sp,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "It's very good!",
            fontSize = 15.sp,
            color = Color.Black,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = "It's very good!",
            fontSize = 15.sp,
            color = Color.Black,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = "It's very good!",
            fontSize = 15.sp,
            color = Color.Black,
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(10.dp))
        //Divider(color = Color.LightGray.copy(alpha = 0.4f), modifier = Modifier.padding(start = 5.dp, end = 5.dp))
    }
}