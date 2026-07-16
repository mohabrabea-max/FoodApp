package com.example.applicationhome.ui.theme.components.forItemScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu.Ratings

@Composable
fun RatingsAndReviews(
    navigationController : NavHostController,
    review : Double
){
    Column(
        modifier = Modifier.
        shadow(elevation = 10.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(20.dp)).
        fillMaxWidth().
        background(Color.White).
        padding(15.dp),
        horizontalAlignment = Alignment.Start
    ){
        Text(
            text = "Ratings & Reviews",
            fontSize = 16.sp,
            color = Color.Black,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Text(
                text = review.toString(),
                fontSize = 25.sp,
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            for(x in 0 .. review.toInt()){
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.DarkOrange,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "All reviews (5)",
            fontSize = 18.sp,
            color = Color.Black,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Ratings()
    }
}