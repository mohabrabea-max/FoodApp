package com.example.applicationhome.features.restaurantscreen.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.paging.compose.LazyPagingItems
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.designsystem.TopBarButtons
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.MealBoxIcon
import com.example.applicationhome.core.ui.model.FoodItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiscountMealsDialog(
    title : String,
    meals : LazyPagingItems<FoodItem.MealItem>,
    snacks : LazyPagingItems<FoodItem.SnackItem>,
    onMealClickable : (FoodItem) -> Unit,
    dismissButton : () -> Unit
){
    Dialog(
        onDismissRequest = { dismissButton() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ){
        Card(
            modifier = Modifier
                .fillMaxSize(0.9f)
                .shadow(elevation = 10.dp, spotColor = Color.LightGray.copy(0.5f), shape = RoundedCornerShape(25.dp)),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ){
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),

                containerColor = MaterialTheme.colorScheme.background,

                topBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        MyTopBar(
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth().height(70.dp),
                            title = title,
                            titleColor = MaterialTheme.colorScheme.onSurface,
                            startaction = {
                                TopBarButtons(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = stringResource(R.string.close),
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    onClick = { dismissButton() },
                                    elevation = 0.dp,
                                    border = 1.dp
                                )
                            }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.onTertiary)
                    }
                }
            ){
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                ){
                    item{Spacer(modifier = Modifier.height(90.dp))}

                    if(meals.itemCount > 0) item {
                        Text(
                            text = stringResource(R.string.meals),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 7.dp)
                        )
                    }

                    items(
                        count = meals.itemCount
                    ){ index ->
                        val item = meals[index]

                        item?.let {
                            val price = item.sizes.values.last()
                            val discount = item.discount?.discount

                            MealsBoxForRestaurantScreen(
                                price = price,
                                discount = discount,
                                details = null,
                                name = item.name,
                                image = item.image,
                                aspectRatio = 2f,
                                cardNavigationClickable = {
                                    onMealClickable(item)
                                },
                                actions = {
                                    Box(modifier = Modifier)

                                    MealBoxIcon(
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                            )
                        }
                    }

                    if(snacks.itemCount > 0) item {
                        Text(
                            text = stringResource(R.string.snacks),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 7.dp)
                        )
                    }

                    items(
                        count = snacks.itemCount
                    ){ index ->
                        val item = snacks[index]

                        item?.let {
                            val price = item.sizes.values.last()
                            val discount = item.discount?.discount

                            MealsBoxForRestaurantScreen(
                                price = price,
                                discount = discount,
                                details = null,
                                name = item.name,
                                image = item.image,
                                aspectRatio = 2f,
                                cardNavigationClickable = {
                                    onMealClickable(item)
                                },
                                actions = {
                                    Box(modifier = Modifier)

                                    MealBoxIcon(
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                            )
                        }
                    }

                    item{Spacer(modifier = Modifier.height(50.dp))}
                }
            }
        }
    }
}