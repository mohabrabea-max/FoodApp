package com.example.applicationhome.ui.theme.components

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.example.applicationhome.data.models.Favorite
import com.example.applicationhome.data.models.Restaurants
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.view.model.APIData
import com.example.applicationhome.view.model.FavoriteViewModel
import kotlinx.coroutines.launch

@Composable
fun RestaurantsBox(item : Restaurants, favoriteState : FavoriteViewModel, apiData : APIData){
    if (apiData.restaurantsMenuisLoading) {
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
            shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(30.dp)).
            clickable{}
        ){
            Box(modifier = Modifier.clickable {  }){
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
                    Row(modifier = Modifier.fillMaxWidth().background(Color.Black.copy(alpha = 0f)).padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                        Row(
                            modifier = Modifier.shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(5.dp)).
                            background(Color.VeryLightGray).
                            border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(5.dp)).
                            padding(top = 2.dp, bottom = 2.dp, start = 4.dp, end = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ){
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700) , modifier = Modifier.size(15.dp))
                            Text(text = item.review.toString(), color = Color.Black, fontSize = 15.sp, modifier = Modifier)
                        }
                        Favorite2(
                            modifier = Modifier.
                            clip(CircleShape).
                            border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(30.dp)).
                            size(35.dp).
                            background(Color.VeryLightGray),
                            restaurants = item,
                            favoriteState = favoriteState
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
                        clickable {  }.
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

@SuppressLint("UnrememberedMutableState")
@Composable
fun Favorite2(
    modifier: Modifier = Modifier,
    modifier2 : Modifier = Modifier,
    restaurants: Restaurants,
    favoriteState : FavoriteViewModel
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    val favorite = favoriteState.itemsCount[restaurants.id] ?: false
    val favorite2 = if(restaurants in Favorite.favoriteRestaurantslist) true else false
    fun favorite1(){

        if(favorite == true || favorite2 == true){
            favoriteState.removeRestaurantsFavorite(restaurants)
            Toast.makeText(context, "Remove From Favorite", Toast.LENGTH_SHORT).show()
        }else{
            favoriteState.addRestaurantsFavorite(restaurants)
            Toast.makeText(context, "Add To Favorite", Toast.LENGTH_SHORT).show()
        }

        scope.launch {
            // يكبر بسرعة لـ 1.3x في 100 مللي ثانية
            scale.animateTo(1.3f, animationSpec = tween(100))
            // يرجع لحجمه الطبيعي 1x بسرعة برضه
            scale.animateTo(1f, animationSpec = tween(100))
        }
    }

    IconButton(modifier = modifier, onClick = {favorite1()}){
        if(favorite == false && favorite2 == false) {
            Icon(
                imageVector = Icons.Default.BookmarkBorder,
                contentDescription = "More",
                tint = Color.Black,
                modifier = modifier2.size(20.dp).scale(scale.value)
            )
        }else{
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = "More",
                tint = Color.DarkOrange,
                modifier = modifier2.size(20.dp).scale(scale.value)
            )
        }
    }
}