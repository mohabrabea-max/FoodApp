package com.example.applicationhome.features.orders.ui.orderscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.TimelineStep

@Composable
fun TimelineItem(
    step : TimelineStep,
    isLast : Boolean,
    isCancelled : Boolean
){
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            val circleBg = if (step.isCompleted && !isCancelled) Color.DarkOrange else Color(0xFFF1F3F5)
            val iconTint = if (step.isCompleted && !isCancelled) Color.White else if(isCancelled) Color.Red else if(step.isCurrent) Color.DarkOrange else Color.Gray

            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .border(
                        width = if(step.isCurrent || isCancelled) 2.dp else 0.dp,
                        color =
                            if(step.isCurrent) Color.DarkOrange
                            else if(isCancelled) Color.Red
                            else Color.White.copy(alpha = 0f),
                        shape = CircleShape
                    )
                    .background(circleBg),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = step.icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ){
                Text(
                    text = step.subtitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = step.date,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        if(!isLast){
            val lineColor = if (step.isCompleted) Color.DarkOrange.copy(alpha = 0.7f) else Color.LightGray

            Canvas(
                modifier = Modifier
                    .padding(start = 26.5.dp)
                    .height(30.dp)
                    .width(2.dp)
                    .padding(vertical = 4.dp)
            ){
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                drawLine(
                    color = lineColor,
                    start = Offset(x = size.width / 2, y = 0f),
                    end = Offset(x = size.width / 2, y = size.height),
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = pathEffect
                )
            }
        }
    }
}