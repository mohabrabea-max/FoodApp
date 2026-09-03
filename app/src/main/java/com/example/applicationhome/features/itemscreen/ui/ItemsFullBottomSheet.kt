package com.example.applicationhome.features.itemscreen.ui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R
import com.example.applicationhome.data.data.model.BottomSheetActions
import com.example.applicationhome.data.data.model.BottomSheetItem
import com.example.applicationhome.data.data.model.MealSnacks
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.UserClass

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsFullBottomSheet(
    bottomSheetItem : BottomSheetItem,
    size : String,
    actions : BottomSheetActions,
    userData : UserClass,
    newCount : Int,
    animDuration : Int,
    animateIn : MutableTransitionState<Boolean>,
    openSnackBottomSheet : (MealSnacks) -> Unit
){
    val scrollState = rememberLazyListState()

    val price = bottomSheetItem.sizes[size]

    BackHandler(enabled = true){
        actions.closeBottomSheet()
    }

    AnimatedVisibility(
        visibleState = animateIn,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = animDuration),
            initialOffsetY = { fullHeight -> fullHeight }
        ) + fadeIn(animationSpec = tween(durationMillis = animDuration)),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = animDuration),
            targetOffsetY = { fullHeight -> fullHeight }
        ) + fadeOut(animationSpec = tween(durationMillis = animDuration))
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.BottomCenter
        ){
            Scaffold(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    ItemScreenTopBar(
                        scrollState = scrollState,
                        isMealInFavorite = bottomSheetItem.isFavorite,
                        backStack = { actions.closeBottomSheet() },
                        navigation = { actions.navigation(Screens.Search) },
                        addMealFavorite = { actions.addFavorite() },
                        removeMealFavorite = { actions.removeFavorite() }
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(15.dp)
                        ){
                            Text(
                                text = bottomSheetItem.name,
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = bottomSheetItem.details,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(25.dp))

                            Text(
                                text = "$price L.E",
                                fontSize = 30.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 5.dp, bottom = 15.dp)
                            )
                        }

                        when(bottomSheetItem){
                            is BottomSheetItem.MealItem -> {

                                Spacer(modifier = Modifier.height(10.dp))

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(MaterialTheme.colorScheme.surface)
                                ){
                                    Spacer(modifier = Modifier.height(15.dp))

                                    Text(
                                        text = stringResource(R.string.meal_snacks),
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 15.dp)
                                    )

                                    FlowRow(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ){
                                        val selectedDetail = bottomSheetItem.meal?.meal?.sizeOptions?.find { it.size == size }
                                        selectedDetail?.snack?.forEach { (snakeId, value) ->
                                            SnaksBoxForItemScreen(
                                                modifier = Modifier.size(160.dp),
                                                item = value
                                            ){
                                                openSnackBottomSheet(value)
                                            }
                                        }
                                    }
                                }
                            }

                            is BottomSheetItem.SnackItem -> {}
                        }

                        if(bottomSheetItem.sizes.size > 1){

                            Spacer(modifier = Modifier.height(10.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(10.dp)
                            ){
                                Spacer(modifier = Modifier.height(5.dp))

                                Text(
                                    text = stringResource(R.string.sizes),
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 5.dp)
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                ItemSize(
                                    bottomSheetItem.sizes.keys.toList(),
                                    size
                                ) { selectedSize ->
                                    actions.selectSize(selectedSize)
                                }
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
}