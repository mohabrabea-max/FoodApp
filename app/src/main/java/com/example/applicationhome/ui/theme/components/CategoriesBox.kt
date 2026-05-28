package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.repository.MenuRepository
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.view.model.CategoriesBoxViewModel

@Composable
fun CategoriesBox(category : Categories, categoriesBoxViewModel : CategoriesBoxViewModel){
    val selected = categoriesBoxViewModel.selected
    if (MenuRepository.categoriesisLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // دايرة التحميل الافتراضية في أندرويد
        }
    }else{
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
                AsyncImage(
                    modifier = Modifier.
                    size(if(selected == category.id) 38.dp else 35.dp).
                    padding(7.dp),
                    model = ImageRequest.Builder(LocalContext.current).
                    data(category.image).
                    crossfade(true).
                    size(400, 400).
                    precision(Precision.EXACT).
                    build(),
                    contentDescription = "${category.name} Logo",
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
}