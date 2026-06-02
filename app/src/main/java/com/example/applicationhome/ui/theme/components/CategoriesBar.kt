package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.applicationhome.data.models.repository.MenuRepository
import com.example.applicationhome.view.model.CategoriesBoxViewModel

@Composable
fun CategoriesBar(categoriesBoxViewModel : CategoriesBoxViewModel){
    val categories = MenuRepository.categories
    Row(
        modifier = Modifier.padding(start = 5.dp, end = 5.dp).fillMaxWidth().
        height(60.dp).
        clip(shape = RoundedCornerShape(30.dp)).
        background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ){
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            item { Spacer(modifier = Modifier.width(4.dp)) }
            items(categories) { category -> CategoriesBox(category, categoriesBoxViewModel) }
            item { Spacer(modifier = Modifier.width(4.dp)) }
        }
    }
}