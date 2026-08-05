package com.example.applicationhome.features.itemscreen.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.applicationhome.core.ui.theme.DarkOrange

@Composable
fun ItemSize(
    mealSizeDetail : List<String>,
    size : String,
    selectMeal : (String) -> Unit
){
    Box(
        modifier = Modifier
            .animateContentSize()
            .padding(10.dp)
            .height(40.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(15.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            mealSizeDetail.forEach{ item ->
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            width = 1.dp,
                            color = if(size == item) Color.DarkOrange else Color.LightGray,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clickable{
                            selectMeal(item)
                        },
                    contentAlignment = Alignment.Center
                ){
                    val isSelected = (size == item)
                    val color = if(isSelected) Color.DarkOrange else Color.White
                    val fontColor = if(isSelected) Color.White else Color.DarkOrange

                    Box(modifier = Modifier.fillMaxSize().background(color), contentAlignment = Alignment.Center){
                        Text(text = item, color = fontColor)
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}