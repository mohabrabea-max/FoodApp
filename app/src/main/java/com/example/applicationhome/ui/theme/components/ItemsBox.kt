package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.data.models.repository.MenuRepository
import com.example.applicationhome.view.model.ItemScreenViewModel

@Composable
fun ItemsBox(
    item: FoodItem,
    navigationController : NavHostController,
    itemScreenViewModel: ItemScreenViewModel,
    actions : @Composable ColumnScope.() -> Unit = {}
){
    if (MenuRepository.foodMenuListisLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // دايرة التحميل الافتراضية في أندرويد
        }
    }else{
        val images = item.image.size
        val pagerState = rememberPagerState(pageCount = {images})
        Box(
            modifier = Modifier.padding(7.dp).shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(30.dp)).
            background(Color.White).
            aspectRatio(0.65f).
            clickable{
                itemScreenViewModel.selectItem(item, item.sizeOptions.last().size)
                navigationController.navigate(Screens.ItemScreen.screen)
            }.
            padding(start = 20.dp, end = 15.dp, top = 15.dp, bottom = 20.dp)
        ){
            Column(modifier = Modifier.fillMaxSize().background(Color.White)){
                Box(modifier = Modifier.fillMaxWidth().weight(2f), contentAlignment = Alignment.Center){
                    HorizontalPager(
                        modifier = Modifier.fillMaxSize(0.95f),
                        state = pagerState

                    ){ page ->
                        AsyncImage(
                            modifier = Modifier.padding(top = 15.dp, end = 5.dp).fillMaxSize().clip(RoundedCornerShape(10.dp)),
                            model = ImageRequest.Builder(LocalContext.current).
                            data(item.image[page]).
                            crossfade(true).
                            size(400, 400).
                            precision(Precision.EXACT).
                            build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceBetween,
                        content = actions
                    )
                }

                Column(modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween){
                    Text(
                        text = item.name,
                        fontSize = 14.sp,
                        color = Color.Black,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.name,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = item.sizeOptions.last().price.toString() + " E.G",
                        fontSize = 16.sp,
                        color = Color.Black,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}