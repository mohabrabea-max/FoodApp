package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.Color
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
import com.example.applicationhome.data.models.Food
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.BackgroundForCards
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.Orange
import com.example.applicationhome.ui.theme.components.Favorite
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(scrollBehavior: TopAppBarScrollBehavior, navigationController : NavHostController, viewModel: ItemScreenViewModel){
    val food : Food
    val item = viewModel.selectedItem
    val price = viewModel.selectedSize.value
    val images = item?.image?.size ?: 0
    val pagerState = rememberPagerState(pageCount = {images})
    val size = item?.priceANDsize?.size
    if(item != null){
        Scaffold(
            modifier = Modifier.fillMaxSize().
            background(Color.LightBrownForBackground),
            topBar = {MyTopBar2(item.id, navigationController)},
            bottomBar = {
                Box(modifier = Modifier.fillMaxWidth().height(90.dp), contentAlignment = Alignment.TopCenter){
                    Row(modifier = Modifier.fillMaxWidth().height(60.dp).align(Alignment.Center), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                        AddBox2(color = Color.LightGray, id = item.id, food = item)
                        Spacer(modifier = Modifier.width(10.dp))
                        CartButton2(modifier = Modifier)
                    }
                }
            }
        ){innerPadding ->
            Box(modifier = Modifier.background(Color.LightBrownForBackground).padding(innerPadding)){
                LazyColumn(modifier = Modifier.fillMaxSize()){
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
                                text = "${item.priceANDsize[price]} L.E",
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
                                background(Color.BackgroundForCards.copy(alpha = 0.8f)),
                                contentAlignment = Alignment.Center
                            ){
                                if(size == 3){
                                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable{viewModel.bigSize()}, contentAlignment = Alignment.Center){
                                            if(price == "Big"){
                                                Box(modifier = Modifier.fillMaxSize().background(Color.BrownForFont), contentAlignment = Alignment.Center){
                                                    Text(text = "Big", color = Color.BackgroundForCards)
                                                }
                                            }else{
                                                Text(text = "Big", color = Color.BrownForFont)
                                            }
                                        }
                                        VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.height(30.dp))
                                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable{viewModel.mediumSize()}, contentAlignment = Alignment.Center){
                                            if(price == "Medium"){
                                                Box(modifier = Modifier.fillMaxSize().background(Color.BrownForFont), contentAlignment = Alignment.Center){
                                                    Text(text = "Medium", color = Color.BackgroundForCards)
                                                }
                                            }else{
                                                Text(text = "Medium", color = Color.BrownForFont)
                                            }
                                        }
                                        VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.height(30.dp))
                                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable{viewModel.smallSize()}, contentAlignment = Alignment.Center){
                                            if(price == "Small"){
                                                Box(modifier = Modifier.fillMaxSize().background(Color.BrownForFont), contentAlignment = Alignment.Center){
                                                    Text(text = "Small", color = Color.BackgroundForCards)
                                                }
                                            }else{
                                                Text(text = "Small", color = Color.BrownForFont)
                                            }
                                        }
                                    }
                                }else if(size == 2){
                                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable{viewModel.bigSize()}, contentAlignment = Alignment.Center){
                                            if(price == "Beg"){
                                                viewModel.bigSize()
                                                Box(modifier = Modifier.fillMaxSize().background(Color.BrownForFont), contentAlignment = Alignment.Center){
                                                    Text(text = "Beg", color = Color.BackgroundForCards)
                                                }
                                            }else{
                                                Text(text = "Beg", color = Color.BrownForFont)
                                            }
                                        }
                                        VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.padding(5.dp))
                                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable{viewModel.mediumSize()}, contentAlignment = Alignment.Center){
                                            if(price == "Medium"){
                                                viewModel.mediumSize()
                                                Box(modifier = Modifier.fillMaxSize().background(Color.BrownForFont), contentAlignment = Alignment.Center){
                                                    Text(text = "Medium", color = Color.BackgroundForCards)
                                                }
                                            }else{
                                                Text(text = "Medium", color = Color.BrownForFont)
                                            }
                                        }
                                    }
                                }else{
                                    Box(modifier = Modifier.fillMaxSize().clickable{}, contentAlignment = Alignment.Center){
                                        Text(text = price, color = Color.BrownForFont)
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun CartButton2(modifier: Modifier = Modifier, cartNumber : AddBoxViewModel = viewModel()){
    val context = LocalContext.current
    var cart2 = cartNumber.totalCart.value ?: 0
    if(cart2 > 0){
        Box(
            modifier = modifier.size(150.dp).
            aspectRatio(3f).
            clip(shape = RoundedCornerShape(30.dp)).
            background(Color.Blue).
            clickable{
                cartNumber.addToCart()
                Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show()
            }.
            border(width = 0.5.dp, color = Color.BrownForFont.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp)).
            padding(5.dp),
            contentAlignment = Alignment.Center
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.White, modifier = Modifier.weight(1f))
                VerticalDivider(modifier = Modifier.align(Alignment.CenterVertically))
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                    Text(text = cart2.toString(), color = Color.White)
                }
            }
        }
    }
    else{
        Box(
            modifier = Modifier.size(150.dp).
            aspectRatio(3f).
            clip(shape = RoundedCornerShape(30.dp)).
            background(Color.LightGray).
            clickable{}.
            border(width = 0.5.dp, color = Color.BrownForFont.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp)).
            padding(5.dp),
            contentAlignment = Alignment.Center
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.BrownForFont, modifier = Modifier.weight(1f))
                VerticalDivider(color = Color.BrownForFont, modifier = Modifier.align(Alignment.CenterVertically))
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                    Text(text = "Cart", color = Color.BrownForFont)
                }
            }
        }
    }
}

@Composable
fun AddBox2(color : Color, id: Int, ordernumber : AddBoxViewModel = viewModel(), food : Food){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var count = ordernumber.itemsCount[id] ?: 0
    var activid = ordernumber.activId == id
    var targetWidth = if(count == 0 || activid == false) 50.dp else 120.dp
    Box(
        modifier = Modifier.
        animateContentSize().
        height(50.dp).
        width(targetWidth).
        clip(CircleShape).
        background(color).
        clickable {ordernumber.addBoxNumberPlus(food)}.
        border(width = 0.5.dp, color = Color.BrownForFont.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.Center
    ){
        if(count == 0) {
            Text(
                text = "+",
                fontSize = 20.sp,
                style = MaterialTheme.typography.labelLarge,
                color = Color.BrownForFont,
                textAlign = TextAlign.Center
            )
        }else if(count > 0 && activid == false){
            Text(
                text = count.toString(),
                fontSize = 20.sp,
                style = MaterialTheme.typography.labelLarge,
                color = Color.BrownForFont,
                textAlign = TextAlign.Center
            )
        }else{
            Row(
                modifier = Modifier.
                fillMaxSize().
                background(color),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
            ){
                Box(
                    modifier = Modifier.
                    weight(1f).
                    fillMaxHeight().
                    clickable {ordernumber.addBoxNumberPlus(food)},
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "+",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.BrownForFont,
                        textAlign = TextAlign.Center
                    )
                }
                VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.height(20.dp))
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
                                    ordernumber.updateCount(food, newCount)
                                }
                            }else{
                                val newCount = newValue.toIntOrNull() ?: 0
                                ordernumber.updateCount(food, newCount)
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
                            fontSize = 20.sp
                        )
                    )
                }
                VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier.
                    weight(1f).
                    fillMaxHeight().
                    animateContentSize().
                    clickable {ordernumber.addBoxNumberMinus(food)},
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "-",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.BrownForFont,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar2(item: Int, navController: NavHostController){
    Surface(
        modifier = Modifier.
        fillMaxWidth().
        height(100.dp).
        statusBarsPadding(),
        color = Color.Black
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {if (navController.previousBackStackEntry != null) { navController.popBackStack() } } ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Orange)
            }
            Text(
                text = "Home",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Orange
            )
            Row{
                IconButton(onClick = {navController.navigate(Screens.ItemScreen.screen)}) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Orange)
                }
                Favorite(modifier = Modifier.padding(10.dp).clip(CircleShape).size(35.dp).background(Color.Black.copy(alpha = 0.8f)),id = item)
            }
        }
    }
}