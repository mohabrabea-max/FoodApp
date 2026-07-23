package com.example.applicationhome.features.favorite.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.Favorite
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.local.entity.RestaurantsEntity

@Composable
fun RestaurantsBox(
    loading : Boolean,
    item : RestaurantsEntity,
    isRestaurantInFavorite : Boolean,
    view : () -> Unit,
    clickable : () -> Unit,
    addRestaurantsFavorite : () -> Unit,
    removeRestaurantsFavorite : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // دايرة التحميل الافتراضية في أندرويد
        }
    }else{
        Box(
            modifier = Modifier.width(194.dp).
            height(230.dp).
            padding(10.dp).
            shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(30.dp))
        ){
            Box(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ){
                    clickable()
                }
            ){
                Box(modifier = Modifier.fillMaxSize().background(Color.VeryLightGray)){
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current).
                        data(item.image2).
                        crossfade(true).
                        size(400, 400).
                        precision(Precision.EXACT).
                        build(),
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop
                    )

                    Row(
                        modifier = Modifier.
                        fillMaxWidth().
                        background(Color.Black.copy(alpha = 0f)).
                        padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ){
                        Favorite(
                            isRestaurantInFavorite,
                            { addRestaurantsFavorite() },
                            { removeRestaurantsFavorite() },
                            modifier = Modifier.clip(CircleShape).size(35.dp)
                                .background(Color.Black.copy(alpha = 0.2f)),
                            color = Color.White,
                            icon1 = Icons.Default.Bookmark,
                            icon2 = Icons.Default.BookmarkBorder
                        )
                    }
                }
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
                Row(
                    modifier = Modifier.fillMaxWidth().
                    height(70.dp).
                    background(Color.Black.copy(alpha = 0.5f)).
                    align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = item.name,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                Spacer(modifier = Modifier.height(70.dp))
                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier.size(50.dp).
                        clip(CircleShape).
                        background(Color.White).
                        clickable { view() }.
                        shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(40.dp)).
                        border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(40.dp))
                    ){
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = ImageRequest.Builder(LocalContext.current).
                            data(item.image).
                            crossfade(true).
                            size(400, 400).
                            precision(Precision.EXACT).
                            build(),
                            contentDescription = item.name,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}