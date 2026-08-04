package com.example.applicationhome.core.ui.components.forHomeScreenOrMenu

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.ui.theme.BrownForFont
import com.example.applicationhome.core.ui.theme.MediumBrownForTitle
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.BottomSheetActions
import com.example.applicationhome.data.data.model.BottomSheetItem
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.UserClass
import com.example.applicationhome.features.itemscreen.ui.BottomBarForItemScreen
import com.example.applicationhome.features.itemscreen.ui.ItemScreenImage
import com.example.applicationhome.features.itemscreen.ui.ItemScreenTopBar
import com.example.applicationhome.features.itemscreen.ui.ItemSize
import com.example.applicationhome.features.itemscreen.ui.RatingsAndReviews
import com.example.applicationhome.features.itemscreen.ui.SnaksBoxForItemScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsFullBottomSheet(
    bottomSheetItem : BottomSheetItem,
    size: String,
    actions : BottomSheetActions,
    userData : UserClass,
    newCount : Int
){
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { targetValue ->
            targetValue != SheetValue.Hidden
        }
    )

    val scrollState = rememberLazyListState()

    val price = bottomSheetItem.sizes[size]


    ModalBottomSheet(
        onDismissRequest = { actions.closeBottomSheet() },
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        dragHandle = null,
        sheetState = sheetState,
        containerColor = Color.White
    ){
        Scaffold(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize(),
            containerColor = Color.VeryLightGray,
            topBar = {
                ItemScreenTopBar(
                    scrollState,
                    bottomSheetItem.isFavorite,
                    { actions.closeBottomSheet() },
                    { actions.navigation(Screens.Search) },
                    { actions.addFavorite() },
                    { actions.removeFavorite() }
                )
            }
        ){
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                state = scrollState,
                modifier = Modifier.padding(horizontal = 10.dp).fillMaxSize()
            ){
                item{
                    Column{
                        Spacer(modifier = Modifier.height(50.dp))
                        ItemScreenImage(
                            scrollState,
                            bottomSheetItem.image
                        )
                    }
                }
                item {
                    //Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier.
                        shadow(elevation = 10.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(20.dp)).
                        fillMaxWidth().
                        background(Color.White).
                        padding(15.dp)
                    ){
                        Text(
                            text = bottomSheetItem.name,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = bottomSheetItem.details,
                            color = Color.MediumBrownForTitle
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "$price L.E",
                            fontSize = 30.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 15.dp, bottom = 15.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    when(bottomSheetItem){
                        is BottomSheetItem.MealItem -> {
                            Column(
                                modifier = Modifier.
                                shadow(elevation = 10.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(20.dp)).
                                fillMaxWidth().
                                background(Color.White)
                            ){
                                Spacer(modifier = Modifier.height(15.dp))
                                Text(
                                    text = "Meal snacks",
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 15.dp)
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                LazyRow {
                                    item{Spacer(modifier = Modifier.width(7.dp))}
                                    item{
                                        val selectedDetail = bottomSheetItem.meal?.meal?.sizeOptions?.find { it.size == size }
                                        selectedDetail?.snack?.forEach { (snakeId, value) ->
                                            SnaksBoxForItemScreen(
                                                modifier = Modifier.size(170.dp),
                                                value
                                            )
                                        }
                                    }
                                    item{Spacer(modifier = Modifier.width(7.dp))}
                                }
                            }
                        }
                        is BottomSheetItem.SnackItem -> {}
                    }


                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier.
                        shadow(elevation = 10.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(20.dp)).
                        fillMaxWidth().
                        background(Color.White).
                        padding(10.dp)
                    ){
                        if(size != null) ItemSize(
                            bottomSheetItem.sizes.keys.toList(),
                            size
                        ) { selectedSize ->
                            actions.selectSize(selectedSize)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    RatingsAndReviews(
                        bottomSheetItem.review
                    )
                }
                item{Spacer(modifier = Modifier.height(150.dp))}
            }


            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom){
                val price = bottomSheetItem.sizes[size] ?: 0.0
                val totalPrice = newCount * price
                BottomBarForItemScreen(
                    price,
                    newCount,
                    { actions.minusnewCount() },
                    { actions.plusnewCount() },
                    {
                        val category = when(bottomSheetItem){
                            is BottomSheetItem.MealItem -> {
                                bottomSheetItem.meal?.meal?.category ?: ""
                            }
                            is BottomSheetItem.SnackItem -> {
                                "Snack"
                            }
                        }

                        val meal = CartItemsClass(
                            userData.id,
                            "${bottomSheetItem.id}_${size}",
                            bottomSheetItem.id,
                            bottomSheetItem.name,
                            category,
                            size,
                            newCount,
                            price,
                            totalPrice,
                            bottomSheetItem.image,
                            bottomSheetItem.restaurantId
                        )

                        actions.updateCount(
                            meal,
                            size,
                            newCount
                        )
                    }
                )
            }
        }
    }
}