package com.example.applicationhome.features.shimmers.boxes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.applicationhome.features.shimmers.tools.CircleShimmer
import com.example.applicationhome.features.shimmers.tools.SquareShimmer
import com.example.applicationhome.features.shimmers.tools.TextShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun MaxWidthItemShimmer(
    height : Int = 130,
    StringNumber : Int = 3
){
    val SquareShimmerSize = (height / 1.44).dp
    val Spacer = (height / 8.66).dp

    Row(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .height(height.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        SquareShimmer(size = SquareShimmerSize)

        Spacer(modifier = Modifier.width(Spacer))

        Column(
            modifier = Modifier.fillMaxHeight().padding(top = 20.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ){
            var width = 170.dp
            repeat(StringNumber){
                TextShimmer(width)
                width -= width/2
            }
        }
    }
}


@Composable
fun OffersShimmer(){
    Box(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth()
            .height(120.dp)
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(15.dp)
            )
    )
}


@Composable
fun CategoriesShimmer(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        CircleShimmer()

        Spacer(modifier = Modifier.height(7.dp))

        TextShimmer(50.dp)

        Spacer(modifier = Modifier.height(15.dp))
    }
}


@Composable
fun SearchResultBoxShimmer(){
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        MaxWidthItemShimmer(
            height = 110,
            StringNumber = 2,
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            items(5){
                FoodBoxShimmer()
            }
        }
    }
}


@Composable
fun FoodBoxShimmer(){
    Box(
        modifier = Modifier
            .width(155.dp)
            .height(220.dp)
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(30.dp)
            )
    )
}

@Composable
fun TextInSearchShimmer(){
    Column(
        modifier = Modifier
            .shimmer()
            .padding(start = 15.dp)
            .height(70.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ){
        var width = 170.dp
        repeat(2){
            TextShimmer(width)
            width -= width/2
        }
    }
}