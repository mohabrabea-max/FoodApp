package com.example.applicationhome.core.ui.components.forHomeScreenOrMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision

@Composable
fun SnaksBox(
    modifier: Modifier = Modifier,
    name : String,
    image : String,
    price : Double?,
    cardNavigationClickable : () -> Unit = {},
    actions : @Composable ColumnScope.() -> Unit = {}
){
    Box(
        modifier = modifier
            .padding(7.dp)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable{
                cardNavigationClickable()
            }
    ){
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)){
            Box(modifier = Modifier.fillMaxWidth().weight(2f)){
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current).
                    data(image).
                    crossfade(true).
                    precision(Precision.EXACT).
                    build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.fillMaxSize().
                    padding(end = 10.dp, top = 10.dp, bottom = 5.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween,
                    content = actions
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth().weight(1.1f).padding(start = 15.dp, end = 10.dp, top = 5.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = name,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                )
                Text(
                    text = "$price E.G",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}