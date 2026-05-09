package com.example.applicationhome.ui.theme.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.Orange
import com.example.applicationhome.view.model.ItemScreenViewModel

@Composable
fun ItemSize(viewModel: ItemScreenViewModel){
    val item = viewModel.selectedItem
    val size = viewModel.selectedSize
    val price = item?.sizeOptions
    Box(
        modifier = Modifier.
        animateContentSize().
        padding(10.dp).
        height(50.dp).
        width(180.dp).
        clip(CircleShape).
        background(Color.White).
        border(width = 0.5.dp, color = Color.DarkGray.copy(alpha = 1f), shape = RoundedCornerShape(50.dp)),
        contentAlignment = Alignment.Center
    ){
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
            price?.forEach{ (size2) ->
                    Box(
                        modifier = Modifier.weight(1f).
                        fillMaxHeight().
                        clickable{
                            viewModel.selectItem(item, size2)
                        },
                        contentAlignment = Alignment.Center
                    ){
                        val isSelected = (size == size2)
                        val color = if(isSelected) Color.Orange else Color.White
                        val fontColor = if(isSelected) Color.Black else Color.DarkOrange

                        Box(modifier = Modifier.fillMaxSize().background(color), contentAlignment = Alignment.Center){
                            Text(text = size2, color = fontColor)
                        }
                    }
                    VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.height(30.dp))
            }
        }
    }
}