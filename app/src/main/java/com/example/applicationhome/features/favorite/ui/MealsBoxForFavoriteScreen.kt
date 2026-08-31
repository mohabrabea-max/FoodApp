package com.example.applicationhome.features.favorite.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.data.model.MealSizeDetail

@Composable
fun MealsBoxForFavoriteScreen(
    foodMenuIsLoading : Boolean,
    name : String,
    image : String,
    sizeOptions: MealSizeDetail?,
    cardNavigationClickable : () -> Unit = {},
    actions : @Composable ColumnScope.() -> Unit = {}
){
    val interactionSource = remember { MutableInteractionSource() }

    val price = sizeOptions?.price ?: 0.0

    if (foodMenuIsLoading) {
        Box(
            modifier = Modifier
                .padding(7.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.surface)
                .aspectRatio(2f),

            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // دايرة التحميل الافتراضية في أندرويد
        }
    }else{
        Box(
            modifier = Modifier
                .padding(7.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.surface)
                .aspectRatio(0.65f)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ){
                    cardNavigationClickable()
                }.padding(15.dp)
        ){
            Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)){
                Box(modifier = Modifier.fillMaxWidth().weight(1.5f), contentAlignment = Alignment.Center){
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(0.9f).clip(RoundedCornerShape(10.dp)),
                        model = ImageRequest.Builder(LocalContext.current).
                        data(image).
                        crossfade(true).
                        size(400, 400).
                        precision(Precision.EXACT).
                        build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceBetween,
                        content = actions
                    )
                }

                Column(modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween){
                    Text(
                        text = name,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = name,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "$price E.G",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}