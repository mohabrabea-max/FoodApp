package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.CategoriesImage
import com.example.applicationhome.data.models.Restaurants
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.MediumBrownForTitle

@Composable
fun CategoriesBox(category : CategoriesImage, navigationController : NavHostController){
    val context = LocalContext.current.applicationContext
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Box(
            modifier = Modifier.
            size(70.dp).
            clip(CircleShape).
            background(Color.LightGray.copy(alpha = 0.4f)).
            clickable{ navigationController.navigate(Screens.Menu.screen) },
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = category.icon),
                contentDescription = "${category.name} Logo",
                modifier = Modifier.
                fillMaxSize().
                padding(20.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = category.name,
            fontSize = 18.sp,
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun RestaurantsBox(item : Restaurants){
    Box(modifier = Modifier.width(100.dp).height(150.dp).padding(5.dp).clip(RoundedCornerShape(10.dp)).clickable{}){
        Column(modifier = Modifier.fillMaxSize().background(Color.White), horizontalAlignment = Alignment.CenterHorizontally){
            Image(modifier = Modifier.weight(1f).background(item.background),painter = painterResource(id = item.image), contentDescription = item.name, contentScale = ContentScale.Crop)
            VerticalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Row(modifier = Modifier.weight(1.5f).padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
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
        }
    }
}