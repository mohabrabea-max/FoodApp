package com.example.applicationhome.features.homescreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.example.applicationhome.data.local.entity.CategoriesEntity


@Composable
fun CategoriesBar(
    categories: List<CategoriesEntity>,
    selected : Int,
    select : (CategoriesEntity) -> Unit,
    unSelect : () -> Unit,
    drawBehind : DrawScope.() -> Unit = {}
){
    Box(
        modifier = Modifier.fillMaxWidth().
        height(120.dp).
        background(Color.White).drawBehind {
            drawBehind()
        },
        contentAlignment = Alignment.Center
    ){
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ){
            item { Spacer(modifier = Modifier.width(4.dp)) }
            items(categories.toList()) { category ->
                CategoriesBox(
                    category,
                    selected,
                    { select(category) },
                    { unSelect() }
                )
            }
            item { Spacer(modifier = Modifier.width(4.dp)) }
        }
    }
}