package com.example.applicationhome.features.homescreen.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.data.model.Categories

@Composable
fun CategoriesBox(
    category : Categories,
    categoriesIsLoading : Boolean,
    selected : Int,
    select : () -> Unit,
    unSelect : () -> Unit,
){
    val interactionSource = remember { MutableInteractionSource() }

    val size = if(selected == category.id) 65.dp else 70.dp
    val alpha = if(selected == category.id) 1f else 0f

    if (categoriesIsLoading) {
        Column(
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ){
                if(selected == 0){
                    select()
                }else if(selected != category.id){
                    unSelect()
                    select()
                }else{
                    unSelect()
                }
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Box(
                modifier = Modifier.
                size(70.dp).
                clip(CircleShape).
                background(Color.White).
                border(
                    width = 2.dp,
                    color = Color.Black.copy(alpha = alpha),
                    shape = CircleShape
                ),
                contentAlignment = Alignment.Center
            ){
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }else{
        Column(
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ){
                if(selected == 0){
                    select()
                }else if(selected != category.id){
                    unSelect()
                    select()
                }else{
                    unSelect()
                }
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Box(
                modifier = Modifier.
                size(70.dp).
                clip(CircleShape).
                border(
                    width = 2.dp,
                    color = Color.Black.copy(alpha = alpha),
                    shape = CircleShape
                ),
                contentAlignment = Alignment.Center
            ){
                AsyncImage(
                    modifier = Modifier.animateContentSize(alignment = Alignment.Center).
                    size(size).
                    padding(5.dp).align(Alignment.Center),
                    model = ImageRequest.Builder(LocalContext.current).
                    data(category.image).
                    crossfade(true).
                    size(400, 400).
                    precision(Precision.EXACT).
                    build(),
                    contentDescription = "${category.name} Logo",
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = category.name,
                fontSize = 14.sp,
                style = if(selected == category.id) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodySmall,
                color = if(selected == category.id) Color.Black else Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}