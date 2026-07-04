package com.example.applicationhome.ui.theme.components.bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    color : Color,
    modifier : Modifier,
    title : String?,
    titleColor : Color,
    startaction : @Composable BoxScope.() -> Unit = {},
    actions : @Composable RowScope.() -> Unit = {},
    arrangement : Arrangement.Horizontal = Arrangement.Center,
    weight : Float = 1f
){
    Surface(
        modifier = modifier,
        color = color
    ) {
        Row(
            modifier = Modifier.fillMaxSize().statusBarsPadding().padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                Box(content = startaction)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = arrangement,
                modifier = Modifier.weight(1f)
            ){
                Text(
                    text = title ?: "",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.headlineMedium,
                    color = titleColor
                )
            }
            Row(
                modifier = Modifier.weight(weight),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                content = actions
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantScreenTopBar(
    color : Color,
    modifier : Modifier,
    title : String?,
    titleColor : Color,
    startaction : @Composable BoxScope.() -> Unit = {},
    actions : @Composable RowScope.() -> Unit = {}
){
    Surface(
        modifier = modifier,
        color = color
    ) {
        Row(
            modifier = Modifier.fillMaxSize().statusBarsPadding().padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                Box(content = startaction)

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = title ?: "",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.headlineMedium,
                    color = titleColor
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                content = actions
            )
        }
    }
}