package com.example.applicationhome.ui.theme.components.forHomeScreenOrMenu

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.models.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.model.CartViewModel
import com.example.applicationhome.ui.theme.model.ItemScreenViewModel

@Composable
fun ItemsBox(
    foodMenuIsLoading : Boolean,
    item: FavoriteFoodDatabase,
    navigationController : NavHostController,
    itemScreenViewModel: ItemScreenViewModel,
    cartViewModel: CartViewModel,
    actions : @Composable ColumnScope.() -> Unit = {}
){
    val sizeOptions = item .sizeOptions.find { it.size == "Small" || it.size.contains("Pieces") }
    val size = sizeOptions?.size ?: ""
    val price = sizeOptions?.price ?: 0.0

    if (foodMenuIsLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // دايرة التحميل الافتراضية في أندرويد
        }
    }else{
        Box(
            modifier = Modifier.padding(7.dp).shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(30.dp)).
            background(Color.White).
            aspectRatio(0.65f).
            clickable{
                itemScreenViewModel.selectItem(item, size)
                navigationController.navigate(Screens.ItemScreen.screen)
                cartViewModel.deletenewCount()
            }.
            padding(start = 20.dp, end = 15.dp, top = 15.dp, bottom = 20.dp)
        ){
            Column(modifier = Modifier.fillMaxSize().background(Color.White)){
                Box(modifier = Modifier.fillMaxWidth().weight(2f), contentAlignment = Alignment.Center){
                    AsyncImage(
                        modifier = Modifier.padding(top = 15.dp, end = 5.dp).fillMaxSize().clip(RoundedCornerShape(10.dp)),
                        model = ImageRequest.Builder(LocalContext.current).
                        data(item.image).
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
                        text = item.name,
                        fontSize = 14.sp,
                        color = Color.Black,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.name,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "$price E.G",
                        fontSize = 16.sp,
                        color = Color.Black,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MealBoxIcon(){
    Box(
        modifier = Modifier.
        animateContentSize().
        size(35.dp).
        clip(CircleShape).
        background(Color.White),
        contentAlignment = Alignment.Center
    ){
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.DarkOrange,
            modifier = Modifier.fillMaxSize().padding(5.dp)
        )
    }
}