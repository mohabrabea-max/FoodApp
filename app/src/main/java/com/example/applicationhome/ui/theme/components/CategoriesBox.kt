package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.models.CategoriesImage
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.view.model.CategoriesBoxViewModel

@Composable
fun CategoriesBox(category : CategoriesImage, categoriesBoxViewModel : CategoriesBoxViewModel){
    val selected = categoriesBoxViewModel.selected
    Box(
        modifier = Modifier.
        clip(RoundedCornerShape(30.dp)).
        background(if(selected == category.id) Color.DarkOrange else Color.DarkOrange.copy(alpha = 0.3f)).
        clickable {
            if(selected == 0){
                categoriesBoxViewModel.selected(category)
            }else if(selected != category.id){
                categoriesBoxViewModel.unSelected()
                categoriesBoxViewModel.selected(category)
            }else{
                categoriesBoxViewModel.unSelected()
            }
        }.
        padding(7.dp)
    ){
        Row(verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(id = category.icon),
                contentDescription = "${category.name} Logo",
                modifier = Modifier.
                size(if(selected == category.id) 38.dp else 35.dp).
                padding(7.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = category.name,
                fontSize = if(selected == category.id) 15.sp else 13.sp,
                color = if(selected == category.id) Color.White else Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.
                padding(end = 10.dp)
            )
        }
    }
}