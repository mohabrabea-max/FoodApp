package com.example.applicationhome.core.ui.components.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NetworkErrorTopBar(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .background(Color(0xFFF9E2DE)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        Icon(
            Icons.Default.Warning,
            contentDescription = "Warning",
            tint = Color(0xFF4A1211),
            modifier = Modifier.padding(start = 15.dp, end = 10.dp).size(18.dp)
        )

        Text(
            text = "You're offline — showing cached data",
            fontSize = 14.sp,
            color = Color(0xFF4A1211)
        )
    }
}