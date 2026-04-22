package com.example.applicationhome.ui.theme.screens

import android.R.attr.name
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.BackgroundForCards
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.components.Favorite
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(scrollBehavior: TopAppBarScrollBehavior, navigationController : NavHostController, viewModel: ItemScreenViewModel){
    val item = viewModel.selectedItem
    val selected = remember{mutableStateOf("Mediam")}
    var size = 3
    if(item != null){
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).fillMaxSize().
            background(Color.LightBrownForBackground),
            topBar = {MyTopBar2(scrollBehavior, item.id, navigationController)}
        ){innerPadding ->
            Box(modifier = Modifier.background(Color.LightBrownForBackground).padding(innerPadding)){
                LazyColumn(modifier = Modifier.fillMaxSize()){
                    item {
                        Column(modifier = Modifier.fillMaxSize().padding(10.dp)){
                            Box(modifier = Modifier.fillMaxWidth().height(300.dp)){
                                Image(
                                    painter = painterResource(id = item.image),
                                    contentDescription = "$name Logo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.
                                    fillMaxSize()
                                )
                            }
                            Divider()
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
                                    text = item.description,
                                    color = Color.MediumBrownForTitle,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "${item.price} L.E",
                                fontSize = 30.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.BrownForFont,
                                modifier = Modifier.padding(10.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Divider()
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
                                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable{selected.value = "Beg"}, contentAlignment = Alignment.Center){
                                            if(selected.value == "Beg"){
                                                Box(modifier = Modifier.fillMaxSize().background(Color.BrownForFont), contentAlignment = Alignment.Center){
                                                    Text(text = "Beg", color = Color.BackgroundForCards)
                                                }
                                            }else{
                                                Text(text = "Beg", color = Color.BrownForFont)
                                            }
                                        }
                                        VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.height(30.dp))
                                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable{selected.value = "Mediam"}, contentAlignment = Alignment.Center){
                                            if(selected.value == "Mediam"){
                                                Box(modifier = Modifier.fillMaxSize().background(Color.BrownForFont), contentAlignment = Alignment.Center){
                                                    Text(text = "Mediam", color = Color.BackgroundForCards)
                                                }
                                            }else{
                                                Text(text = "Mediam", color = Color.BrownForFont)
                                            }
                                        }
                                        VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.height(30.dp))
                                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable{selected.value = "Small"}, contentAlignment = Alignment.Center){
                                            if(selected.value == "Small"){
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
                                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable{selected.value = "Beg"}, contentAlignment = Alignment.Center){
                                            if(selected.value == "Beg"){
                                                Box(modifier = Modifier.fillMaxSize().background(Color.BrownForFont), contentAlignment = Alignment.Center){
                                                    Text(text = "Beg", color = Color.BackgroundForCards)
                                                }
                                            }else{
                                                Text(text = "Beg", color = Color.BrownForFont)
                                            }
                                        }
                                        VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.padding(5.dp))
                                        Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable{selected.value = "Mediam"}, contentAlignment = Alignment.Center){
                                            if(selected.value == "Mediam"){
                                                Box(modifier = Modifier.fillMaxSize().background(Color.BrownForFont), contentAlignment = Alignment.Center){
                                                    Text(text = "Mediam", color = Color.BackgroundForCards)
                                                }
                                            }else{
                                                Text(text = "Mediam", color = Color.BrownForFont)
                                            }
                                        }
                                    }
                                }else{
                                    Box(modifier = Modifier.fillMaxSize().clickable{}, contentAlignment = Alignment.Center){
                                        Text(text = item.size, color = Color.BrownForFont)
                                    }
                                }

                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth().height(60.dp).align(Alignment.BottomCenter), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                    AddBox2(color = Color.LightGray, id = item.id)
                    Spacer(modifier = Modifier.width(10.dp))
                    CartButton2(modifier = Modifier)
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
fun AddBox2(color : Color, id: Int, ordernumber : AddBoxViewModel = viewModel()){
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
        clickable {ordernumber.addBoxNumberPlus(id)}.
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
                    clickable {ordernumber.addBoxNumberPlus(id)},
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
                                    ordernumber.updateCount(id, newCount)
                                }
                            }else{
                                val newCount = newValue.toIntOrNull() ?: 0
                                ordernumber.updateCount(id, newCount)
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
                    clickable {ordernumber.addBoxNumberMinus(id)},
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
fun MyTopBar2(scrollBehavior: TopAppBarScrollBehavior, item: Int, navController: NavHostController){
    val maxHeight = 100.dp
    val density = LocalDensity.current
    val currentHeight by remember {
        derivedStateOf {
            (maxHeight + with(density) { scrollBehavior.state.heightOffset.toDp() })
                .coerceAtLeast(0.dp)
        }
    }
    Surface(
        modifier = Modifier.
        fillMaxWidth().
        height(currentHeight).
        statusBarsPadding(),
        color = Color.BackgroundForCards
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically, // العناصر كلها هتتوسطن أوتوماتيكياً!
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {if (navController.previousBackStackEntry != null) { navController.popBackStack() } } ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.BrownForFont)
            }
            Text(
                text = "Home",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.BrownForFont
            )
            Row{
                IconButton(onClick = {navController.navigate(Screens.ItemScreen.screen)}) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.BrownForFont)
                }
                Favorite(modifier = Modifier.padding(10.dp).clip(CircleShape).size(35.dp).background(Color.LightBrownForBackground.copy(alpha = 0.8f)),id = item)
            }
        }
    }
}