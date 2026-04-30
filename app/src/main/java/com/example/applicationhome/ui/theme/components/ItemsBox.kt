package com.example.applicationhome.ui.theme.components

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.view.model.ItemScreenViewModel

@Composable
fun ItemsBox(
    item: FoodItem,
    navigationController : NavHostController,
    viewModel: ItemScreenViewModel = viewModel()
){
    val context = LocalContext.current
    Box(
        modifier = Modifier.
        animateContentSize().
        aspectRatio(0.8f).
        clickable{
            viewModel.selectItem(item, item.priceANDsize.keys.last())
            navigationController.navigate(Screens.ItemScreen.screen)
        }.
        padding(5.dp).
        clip(RoundedCornerShape(10.dp))
    ){
        Column(
            modifier = Modifier.
            background(Color.White)
        ) {
            Box(modifier = Modifier.fillMaxSize().weight(4f)){
                Image(
                    painter = painterResource(id = item.image[0]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column{
                    Column(
                        modifier = Modifier.fillMaxSize().weight(5f),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceBetween
                    ){
                        Favorite(
                            modifier = Modifier.padding(10.dp).
                            clip(CircleShape).
                            size(35.dp).
                            background(Color.White.copy(alpha = 1f)),
                            food = item
                        )

                        AddBox(color = Color.White, food = item)
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth().
                        weight(1.3f).
                        background(Color.White.copy(alpha = 0.7f)).
                        padding(5.dp),
                        contentAlignment = Alignment.CenterStart
                    ){
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                            fontSize = 18.sp,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.
                fillMaxWidth().
                clip(RoundedCornerShape(10.dp)).
                background(Color.DarkOrange),
                contentAlignment = Alignment.Center
            ){
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Information",
                        tint = Color.White,
                        modifier = Modifier.
                        clip(CircleShape).
                        alpha(0.8f).
                        clickable {Toast.makeText(context, "Price : ${item.priceANDsize["Small"]}", Toast.LENGTH_SHORT).show()}
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${item.priceANDsize.values.last()} L.E",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}