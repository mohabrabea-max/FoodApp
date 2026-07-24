package com.example.applicationhome.features.profile.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.RestaurantWithFeaturedMeals
import com.example.applicationhome.features.favorite.ui.MealsBoxForFavoriteScreen

//@Preview(showBackground = true)
@Composable
fun SearchResults(
    item : RestaurantWithFeaturedMeals,
    mealClickable : (MealsEntity) -> Unit = {},
    restaurantClickable : () -> Unit = {}
){
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ){ restaurantClickable() },
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        //------------------------------\\ Restaurant Information //------------------------------
        Row(
            modifier = Modifier.padding(15.dp).fillMaxWidth().height(90.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).
                data(item.restaurant.image).
                crossfade(true).
                precision(Precision.EXACT).
                build(),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(start = 13.dp)
            ){
                Text(
                    text = item.restaurant.name,
                    fontSize = 18.sp,
                    color = Color.Black,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Row(
                    modifier = Modifier
                        .width(70.dp)
                        .height(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700) ,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "${item.restaurant.review}",
                        color = Color.Black,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                    )
                    Text(
                        text = "(1k+)",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        style = TextStyle(letterSpacing = (-0.7).sp),
                        modifier = Modifier
                    )
                }
            }
        }

        //------------------------------\\ Restaurant Meals //------------------------------
        LazyRow(
            modifier = Modifier.height(270.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            item{ Spacer(modifier = Modifier.width(15.dp)) }

            items(item.topMeals){ item ->
                MealsBoxForFavoriteScreen(
                    false,
                    item.name,
                    item.image,
                    item.sizeOptions.find { it.size == "Small" || it.size.contains("Pieces") },
                    { mealClickable(item) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Divider(
            color = Color.LightGray.copy(alpha = 0.6f),
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )
    }
}