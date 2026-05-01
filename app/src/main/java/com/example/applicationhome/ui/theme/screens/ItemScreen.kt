package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.data.models.Snake
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.Orange
import com.example.applicationhome.ui.theme.components.BottonBarForItemScreen
import com.example.applicationhome.ui.theme.components.Favorite
import com.example.applicationhome.ui.theme.components.ItemSize
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    navigationController : NavHostController,
    viewModel: ItemScreenViewModel,
    addBoxViewModel : AddBoxViewModel = viewModel()
){
    var cart = Cart.cartmap
    val item = viewModel.selectedItem
    val size = viewModel.selectedSize.value
    val images = item?.image?.size ?: 0
    val pagerState = rememberPagerState(pageCount = {images})
    var key: CartKey? = null
    when (item) {
        is FoodItem -> {
            key = CartKey(item, size)
        }
        is Snake -> { }
    }
    if(item != null){
        Scaffold(
            modifier = Modifier.fillMaxSize().
            background(Color.White),
            topBar = {
                MyTopBar(
                    modifier = Modifier.
                    fillMaxWidth().
                    height(100.dp).
                    shadow(elevation = 5.dp),
                item.name,
                {if (navigationController.previousBackStackEntry != null) { navigationController.popBackStack() } },
                {Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)},
                {
                    IconButton(onClick = {
                        navigationController.navigate(Screens.Search.screen){
                            popUpTo(navigationController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                    }
                    Favorite(
                        modifier = Modifier.padding(10.dp).clip(CircleShape).size(35.dp),
                        food = item
                    )
                }
            )
                Divider(color = Color.LightGray.copy(alpha = 0.5f))},
            bottomBar = {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).pointerInput(Unit) { detectTapGestures { } }, contentAlignment = Alignment.Center){
                    if(cart.containsKey(key)){
                        Box(
                            modifier = Modifier.width(200.dp).
                            height(70.dp).
                            clip(RoundedCornerShape(50.dp)).
                            background(Color.Yellow).align(Alignment.TopCenter).
                            border(width = 0.3.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(50.dp)).
                            pointerInput(Unit) {
                                detectTapGestures { }
                            }
                        ){
                            Text(
                                text = "${cart[key]} added in your cart",
                                modifier = Modifier.padding(5.dp).align(Alignment.TopCenter)
                            )
                        }
                    }
                    BottonBarForItemScreen(addBoxViewModel, item, size)
                }
            }
        ){
            Box(modifier = Modifier.background(Color.White)){
                Box{
                    LazyColumn(modifier = Modifier.fillMaxSize()){
                        item{Spacer(modifier = Modifier.height(100.dp))}
                        item {
                            Column(modifier = Modifier.fillMaxSize().padding(10.dp)){
                                Box(modifier = Modifier.fillMaxWidth().height(400.dp).clip(RoundedCornerShape(10.dp))){
                                    HorizontalPager(
                                        state = pagerState,
                                        modifier = Modifier.fillMaxSize()
                                    ) { page ->
                                        Image(
                                            painter = painterResource(id = item.image[page]),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    Row(
                                        Modifier.height(50.dp).fillMaxWidth().background(Color.Black.copy(alpha = 0.5f)).align(Alignment.BottomCenter),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        repeat(item.image.size) { iteration ->
                                            val color = if (pagerState.currentPage == iteration) Color.Orange else Color.LightGray
                                            Box(
                                                modifier = Modifier.padding(4.dp).clip(CircleShape).background(color).size(5.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(modifier = Modifier.width(300.dp).align(Alignment.CenterHorizontally), color = Color.BrownForFont)
                                Spacer(modifier = Modifier.height(20.dp))
                                Column{
                                    Text(
                                        text = item.name,
                                        fontSize = 20.sp,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.BrownForFont,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                    Text(
                                        text = item.description.toString(),
                                        color = Color.MediumBrownForTitle,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Divider(modifier = Modifier.width(300.dp).padding(10.dp))
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "${item.priceANDsize[size]} L.E",
                                    fontSize = 30.sp,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.BrownForFont,
                                    modifier = Modifier.padding(10.dp)
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Divider(modifier = Modifier.width(300.dp).padding(10.dp))
                                Spacer(modifier = Modifier.height(20.dp))
                                ItemSize(viewModel)
                            }
                        }
                        item{Spacer(modifier = Modifier.height(90.dp))}
                    }
                }
                Column(modifier = Modifier.align(Alignment.BottomCenter)){
                    Box(modifier = Modifier.fillMaxWidth().height(15.dp).pointerInput(Unit) { detectTapGestures { } }, contentAlignment = Alignment.Center){}
                }
            }
        }
    }
}