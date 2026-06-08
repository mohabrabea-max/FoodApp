package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.R
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.data.models.repository.MenuRepository
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.ui.theme.model.FavoriteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Restaurants(
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    navigationController : NavHostController,
    favoriteState : FavoriteViewModel,
){
    val restaurantsMenu = MenuRepository.restaurantsMenu.values.toList()
    if (MenuRepository.restaurantsMenuisLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // دايرة التحميل الافتراضية في أندرويد
        }
    }else{
        Scaffold(
            modifier = Modifier.fillMaxSize().background(Color.VeryLightGray),
            topBar = {
                MyTopBar(
                    Color.DarkOrange,
                    modifier = Modifier.
                    fillMaxWidth().
                    height(100.dp).
                    shadow(elevation = 5.dp),
                    "Home",
                    {
                        IconButton(
                            onClick = {coroutineScope.launch{drawerState.open()}},
                            modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                        ) {
                            Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.White)
                        }
                    },
                    {
                        IconButton(onClick = {
                            navigationController.navigate(Screens.Notifications.screen){
                                popUpTo(navigationController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true

                                restoreState = true
                            }
                        }) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                        }
                        IconButton(onClick = {
                            navigationController.navigate(Screens.Search.screen){
                                popUpTo(navigationController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true

                                restoreState = true
                            }
                        }) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                        }
                    }
                )
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
            }
        ){
            Box(modifier = Modifier.fillMaxSize().background(Color.VeryLightGray)){
                LazyColumn(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.spacedBy(5.dp)){
                    item{Spacer(modifier = Modifier.height(100.dp))}
                    items(restaurantsMenu){item ->
                        Surface(modifier = Modifier.aspectRatio(3f).padding(5.dp).clip(RoundedCornerShape(10.dp)).clickable{}){
                            Row(modifier = Modifier.background(Color.White), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize().weight(1f).background(item.background),
                                    model = ImageRequest.Builder(LocalContext.current).
                                    data(item.image).
                                    crossfade(true).
                                    size(400, 400).
                                    precision(Precision.EXACT).
                                    build(),
                                    contentDescription = item.name,
                                    contentScale = ContentScale.Crop
                                )
                                VerticalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                                Column(modifier = Modifier.fillMaxSize().weight(1.5f).padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start){
                                    Row(modifier = Modifier.clickable{}, horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                                        Icon(modifier = Modifier.size(20.dp), imageVector = Icons.Default.Star, contentDescription = "Star", tint = Color(0xFFDAA520))
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(text = item.review.toString(), fontSize = 17.sp, color = Color.MediumBrownForTitle)
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Divider(modifier = Modifier.width(60.dp), color = Color.LightGray.copy(alpha = 0.5f))
                                    Spacer(modifier = Modifier.height(25.dp))
                                    Text(text = item.name, fontSize = 20.sp, color = Color.BrownForFont)
                                    Spacer(modifier = Modifier.height(30.dp))
                                }
                                Favorite3(modifier = Modifier.fillMaxSize().weight(0.5f).padding(10.dp).clip(CircleShape).background(Color.VeryLightGray), favoriteState = favoriteState)
                            }
                        }
                    }
                    item{Spacer(modifier = Modifier.height(80.dp))}
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Favorite3(
    modifier: Modifier = Modifier,
    modifier2 : Modifier = Modifier,
    favoriteState : FavoriteViewModel,
    //food : Restaurants
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    //val favorite = favoriteState.itemsCount[food.id] ?: false
//    val favorite2 = if(food in Favorite.favoritelist) true else false
    fun favorite1(){

//        if(favorite == true || favorite2 == true){
//            favoriteState.removeFavorite(food)
//            Toast.makeText(context, "Remove From Favorite", Toast.LENGTH_SHORT).show()
//        }else{
//            favoriteState.addFavorite(food)
//            Toast.makeText(context, "Add To Favorite", Toast.LENGTH_SHORT).show()
//        }

        scope.launch {
            // يكبر بسرعة لـ 1.3x في 100 مللي ثانية
            scale.animateTo(1.3f, animationSpec = tween(100))
            // يرجع لحجمه الطبيعي 1x بسرعة برضه
            scale.animateTo(1f, animationSpec = tween(100))
        }
    }

    IconButton(modifier = modifier, onClick = {favorite1()}){
        Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "More",
                tint = Color.DarkOrange,
                modifier = modifier2.size(20.dp).scale(scale.value)
            )
//        if(favorite == false && favorite2 == false) {
//            Icon(
//                imageVector = Icons.Default.FavoriteBorder,
//                contentDescription = "More",
//                tint = Color.DarkOrange,
//                modifier = modifier2.size(20.dp).scale(scale.value)
//            )
//        }else{
//            Icon(
//                imageVector = Icons.Default.Favorite,
//                contentDescription = "More",
//                tint = Color.DarkOrange,
//                modifier = modifier2.size(20.dp).scale(scale.value)
//            )
//        }
    }
}