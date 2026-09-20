package com.example.applicationhome.core.ui.components.forHomeScreenOrMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R

@Composable
fun DiscountsOutlookBox(discount : Int){
    Box(
        modifier = Modifier
            .height(20.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFFD2F84A))
            .padding(horizontal = 5.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "${discount}%" + " " + stringResource(R.string.off_select_items),
            fontSize = 11.sp,
            color = Color.Black,
            style = MaterialTheme.typography.labelLarge
        )
    }
}