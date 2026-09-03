package com.example.applicationhome.features.homescreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.Favorite
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus

@Composable
fun RestaurantsBoxHomeScreen(
    item : RestaurantWithFavoriteStatus,
    isRestaurantInFavorite : Boolean,
    view : () -> Unit,
    clickable : () -> Unit,
    addRestaurantsFavorite : () -> Unit,
    removeRestaurantsFavorite : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(130.dp)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ){
                clickable()
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(150.dp)
                .clip(RoundedCornerShape(30.dp))
        ){
            Box(modifier = Modifier.fillMaxSize()){
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current).
                    data(item.restaurant.image2).
                    crossfade(true).
                    size(400, 400).
                    precision(Precision.EXACT).
                    build(),
                    contentDescription = item.restaurant.name,
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier.fillMaxWidth().
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

            Box(
                modifier = Modifier.fillMaxWidth().
                height(50.dp).
                background(Color.Black.copy(alpha = 0.5f)).
                align(Alignment.BottomCenter)
            )

            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                Spacer(modifier = Modifier.height(53.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ){
                    Spacer(modifier = Modifier.width(20.dp))

                    Box(
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { view() }
                            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(40.dp))
                    ){
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = ImageRequest.Builder(LocalContext.current).
                            data(item.restaurant.image).
                            crossfade(true).
                            size(400, 400).
                            precision(Precision.EXACT).
                            build(),
                            contentDescription = item.restaurant.name,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp).fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = item.restaurant.name,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(17.dp)
                )

                Text(
                    text = item.restaurant.review.toString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp,
                    modifier = Modifier
                )
            }
            Text(
                text = item.categories.joinToString(separator = " - ") { it.name },
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}