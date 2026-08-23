package com.example.applicationhome.features.shimmers.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.applicationhome.features.shimmers.boxes.CategoriesShimmer
import com.example.applicationhome.features.shimmers.boxes.MaxWidthItemShimmer
import com.example.applicationhome.features.shimmers.boxes.OffersShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun HomeScreenShimmer(){
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(
        modifier = Modifier
            .testTag("shimmer")
            .fillMaxSize()
            .shimmer(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.height(15.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ){
            items(6){
                CategoriesShimmer()
            }
        }

        HorizontalDivider(
            color = Color.LightGray.copy(alpha = 0.7f),
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 10.dp
        ){
            OffersShimmer()
        }

        HorizontalDivider(
            color = Color.LightGray.copy(alpha = 0.7f),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.width(10.dp))

        repeat(6){
            MaxWidthItemShimmer()
        }
    }
}