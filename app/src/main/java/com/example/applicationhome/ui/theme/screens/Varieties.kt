package com.example.applicationhome.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.models.VarietiesMenu
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightBackgroundForCards
import com.example.applicationhome.ui.theme.LightBrownForBackground

@Composable
fun Varieties(){
    val categories = VarietiesMenu.categoriesList()
    Surface(modifier = Modifier.fillMaxSize().background(Color.LightBrownForBackground),color = Color.LightBrownForBackground){
        LazyColumn(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.spacedBy(5.dp)){
            items(categories){item ->
                Surface(modifier = Modifier.aspectRatio(3f).padding(5.dp).shadow(elevation = 3.dp, shape = RoundedCornerShape(10.dp)).clickable{}){
                    Row(modifier = Modifier.background(Color.LightBackgroundForCards), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                        Image(
                            modifier = Modifier.weight(1f),
                            painter = painterResource(id = item.image),
                            contentDescription = item.name
                        )
                        VerticalDivider()
                        Text(modifier = Modifier.weight(1.5f).padding(10.dp), text = item.name, fontSize = 20.sp, color = Color.BrownForFont)
                        VerticalDivider()
                        //Favorite(modifier = Modifier.weight(0.5f).fillMaxSize().padding(10.dp).clip(CircleShape).background(Color.LightBrownForBackground), food = item)
                    }
                }
            }
        }
    }
}