package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.Cart
import com.example.applicationhome.data.models.CartKey
import com.example.applicationhome.data.models.Food
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.data.models.Snake
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.Orange
import com.example.applicationhome.ui.theme.components.Favorite
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(scrollBehavior: TopAppBarScrollBehavior, navigationController : NavHostController, viewModel: ItemScreenViewModel, addBoxViewModel : AddBoxViewModel = viewModel()){
    val itemsCount = addBoxViewModel.itemsCount
    var cart = Cart.cartmap
    val item = viewModel.selectedItem
    val size = viewModel.selectedSize.value
    val images = item?.image?.size ?: 0
    val pagerState = rememberPagerState(pageCount = {images})
    val price = item?.priceANDsize?.size
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
                                Box(
                                    modifier = Modifier.
                                    animateContentSize().
                                    padding(10.dp).
                                    height(50.dp).
                                    width(180.dp).
                                    clip(CircleShape).
                                    background(Color.White).
                                    border(width = 0.5.dp, color = Color.DarkGray.copy(alpha = 1f), shape = RoundedCornerShape(50.dp)),
                                    contentAlignment = Alignment.Center
                                ){
                                    if(price == 3){
                                        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                                            Box(
                                                modifier = Modifier.weight(1f).
                                                fillMaxHeight().
                                                clickable{
                                                    viewModel.bigSize()
                                                },
                                                contentAlignment = Alignment.Center
                                            ){
                                                if(size == "Big"){
                                                    Box(modifier = Modifier.fillMaxSize().background(Color.Orange), contentAlignment = Alignment.Center){
                                                        Text(text = "Big", color = Color.Black)
                                                    }
                                                }else{
                                                    Text(text = "Big", color = Color.DarkOrange)
                                                }
                                            }
                                            VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.height(30.dp))
                                            Box(
                                                modifier = Modifier.weight(1f).
                                                fillMaxHeight().
                                                clickable{
                                                    viewModel.mediumSize()
                                                },
                                                contentAlignment = Alignment.Center
                                            ){
                                                if(size == "Medium"){
                                                    Box(modifier = Modifier.fillMaxSize().background(Color.Orange), contentAlignment = Alignment.Center){
                                                        Text(text = "Medium", color = Color.Black)
                                                    }
                                                }else{
                                                    Text(text = "Medium", color = Color.DarkOrange)
                                                }
                                            }
                                            VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.height(30.dp))
                                            Box(
                                                modifier = Modifier.weight(1f).
                                                fillMaxHeight().
                                                clickable{
                                                    viewModel.smallSize(item)
                                                },
                                                contentAlignment = Alignment.Center
                                            ){
                                                if(size == "Small"){
                                                    Box(modifier = Modifier.fillMaxSize().background(Color.Orange), contentAlignment = Alignment.Center){
                                                        Text(text = "Small", color = Color.Black)
                                                    }
                                                }else{
                                                    Text(text = "Small", color = Color.DarkOrange)
                                                }
                                            }
                                        }
                                    }else if(price == 2){
                                        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                                            Box(
                                                modifier = Modifier.weight(1f).
                                                fillMaxHeight().
                                                clickable{
                                                    viewModel.bigSize()
                                                },
                                                contentAlignment = Alignment.Center
                                            ){
                                                if(size == "Beg"){
                                                    viewModel.bigSize()
                                                    Box(modifier = Modifier.fillMaxSize().background(Color.Orange), contentAlignment = Alignment.Center){
                                                        Text(text = "Beg", color = Color.Black)
                                                    }
                                                }else{
                                                    Text(text = "Beg", color = Color.DarkOrange)
                                                }
                                            }
                                            VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.padding(5.dp))
                                            Box(
                                                modifier = Modifier.weight(1f).
                                                fillMaxHeight().
                                                clickable{
                                                    viewModel.mediumSize()
                                                },
                                                contentAlignment = Alignment.Center
                                            ){
                                                if(size == "Medium"){
                                                    viewModel.mediumSize()
                                                    Box(modifier = Modifier.fillMaxSize().background(Color.Orange), contentAlignment = Alignment.Center){
                                                        Text(text = "Medium", color = Color.Black)
                                                    }
                                                }else{
                                                    Text(text = "Medium", color = Color.DarkOrange)
                                                }
                                            }
                                        }
                                    }else{
                                        Box(modifier = Modifier.fillMaxSize().clickable{}, contentAlignment = Alignment.Center){
                                            Text(text = size, color = Color.BrownForFont)
                                        }
                                    }

                                }
                            }
                        }
                        item{Spacer(modifier = Modifier.height(90.dp))}
                    }
                    MyTopBar2(food = item, navigationController)
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                }
                Column(modifier = Modifier.align(Alignment.BottomCenter)){
//                    Box(modifier = Modifier.fillMaxWidth().height(60.dp), contentAlignment = Alignment.Center){
//                        CartButton()
//                    }
//                    Box(
//                        modifier = Modifier.fillMaxWidth().
//                        height(60.dp).
//                        pointerInput(Unit) { detectTapGestures { } },
//                        contentAlignment = Alignment.Center
//                    ){
//
//                    }
                    Box(modifier = Modifier.fillMaxWidth().height(15.dp).pointerInput(Unit) { detectTapGestures { } }, contentAlignment = Alignment.Center){}
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottonBarForItemScreen(
    ordernumber : AddBoxViewModel = viewModel(),
    food : Food,
    size : String
){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var cartkey = CartKey(food, size)
    var count = ordernumber.itemsCount[cartkey] ?: 0
    var color : Color
    var fontColor : Color
    if(count == 0){
        color = Color.LightGray
        fontColor = Color.Black
    }else{
        color = Color.Blue
        fontColor = Color.White
    }
    Box(
        modifier = Modifier.width(220.dp).
        height(50.dp).
        clip(RoundedCornerShape(50.dp)).
        background(color).
        border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(50.dp)).
        pointerInput(Unit) {
            detectTapGestures { }
        }
    ){
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier.weight(2.5f).
                height(50.dp).
                clip(RoundedCornerShape(50.dp)).
                background(Color.White).
                border(width = 0.5.dp, color = Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(50.dp))
            ){
                Row(
                    modifier = Modifier.
                    fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                ){
                    Box(
                        modifier = Modifier.
                        weight(1f).
                        fillMaxHeight().
                        clickable {ordernumber.addBoxNumberPlus(food, size)},
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "+",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                    VerticalDivider(color = Color.Black, modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier.
                        weight(1f).
                        fillMaxHeight().
                        clickable {count},
                        contentAlignment = Alignment.Center
                    ){
                        BasicTextField(
                            value = count.toString(),
                            onValueChange = { newValue ->
                                if (newValue.isNotEmpty()) {
                                    if(newValue.all {it.isDigit()} && newValue.length <= 2){
                                        val newCount = newValue.toIntOrNull() ?: count
                                        ordernumber.updateCount(food, size, newCount)
                                    }
                                }else{
                                    val newCount = newValue.toIntOrNull() ?: 0
                                    ordernumber.updateCount(food, size, newCount)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            ),
                            singleLine = true,
                            textStyle = TextStyle(
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        )
                    }
                    VerticalDivider(color = Color.Black, modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier.
                        weight(1f).
                        fillMaxHeight().
                        animateContentSize().
                        clickable {ordernumber.addBoxNumberMinus(food, size)},
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "-",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Box(modifier = Modifier.weight(1f)){
                IconButton(onClick = {

                    ordernumber.addToCart(food, size)
                    Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show()}){
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        tint = fontColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar2(food: Food, navController: NavHostController){
    Surface(
        modifier = Modifier.
        fillMaxWidth().
        height(100.dp).
        shadow(elevation = 5.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {if (navController.previousBackStackEntry != null) { navController.popBackStack() } } ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
            }
            Text(
                text = "Home",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.DarkOrange
            )
            Row(verticalAlignment = Alignment.CenterVertically){
                IconButton(onClick = {navController.navigate(Screens.Search.screen)}) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                }
                Favorite(modifier = Modifier.padding(10.dp).clip(CircleShape).size(35.dp),food = food)
            }
        }
    }
}