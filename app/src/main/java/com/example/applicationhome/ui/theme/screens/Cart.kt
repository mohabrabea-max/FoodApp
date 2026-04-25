package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.data.models.Snake
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.components.MyBottonBar2
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.view.model.BottomBarViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(navigationController : NavHostController, viewModelForBottomBar: BottomBarViewModel, viewModel: ItemScreenViewModel){
    var cart = Cart.cartList()
    Scaffold(
        modifier = Modifier.
        fillMaxSize().
        background(Color.LightBrownForBackground),
//        topBar = { MyTopBar({navigationController.popBackStack()}, {Icon(Icons.Default.ArrowBack, "Back", tint = Color.Black)}, {navigationController.navigate(
//            Screens.Search.screen)}, Icons.Default.Search, {navigationController.navigate(Screens.Favorite.screen)}, Icons.Default.Favorite) }
    ){
        Box(modifier = Modifier.background(Color.White)){
            Column{
                Box{
                    LazyVerticalGrid (
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ){
                        item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(100.dp))}
                        items(cart) { item ->
                            when (item) {
                                is FoodItem -> {
                                    CartBox(item, navigationController, viewModel)
                                }
                                is Snake -> {
                                    // هنا كوتلن فهم إن العنصر Snake
                                    // تقدر تستخدم: item.name, item.image (الـ Int)
                                    Text(text = "سناك: ${item.name}")
                                    // هنا الـ image نوعها Int (Resource ID)
                                    Image(painter = painterResource(id = item.image), contentDescription = null)
                                }
                            }
                        }
                    }
                    MyTopBar(
                        {navigationController.popBackStack()},
                        {Icon(Icons.Default.ArrowBack,
                            "Back", tint = Color.Black)},
                        {navigationController.navigate(Screens.Search.screen)},
                        Icons.Default.Search,
                        {navigationController.navigate(Screens.Favorite.screen)},
                        Icons.Default.Favorite
                    )
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                }
                Spacer(modifier = Modifier.height(100.dp))
                Text(text = cart.toString(), fontSize = 30.sp, color = Color.Black)
            }
            Column(modifier = Modifier.align(Alignment.BottomCenter)){
                Box(modifier = Modifier.fillMaxWidth().height(60.dp).pointerInput(Unit) { detectTapGestures { } }, contentAlignment = Alignment.Center){
                    MyBottonBar2(navigationController, viewModelForBottomBar)
                }
                Box(modifier = Modifier.fillMaxWidth().height(15.dp).pointerInput(Unit) { detectTapGestures { } }, contentAlignment = Alignment.Center){}
            }
        }
    }
}


@Composable
fun CartBox(item: FoodItem, navigationController : NavHostController, viewModel: ItemScreenViewModel){
    val context = LocalContext.current
    Box(
        modifier = Modifier.
        animateContentSize().
        aspectRatio(0.8f).
        clickable{
            viewModel.selectedItem = item
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
                    painter = painterResource(id = item.image[1]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier.fillMaxWidth().
                    background(Color.White.copy(alpha = 0.7f)).
                    padding(5.dp).
                    align(Alignment.BottomCenter),
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
            Box(
                modifier = Modifier.
                fillMaxWidth().
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
                        text = "${item.priceANDsize["Small"]} L.E",
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