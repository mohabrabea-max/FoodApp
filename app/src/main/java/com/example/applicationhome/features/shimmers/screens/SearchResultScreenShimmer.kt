package com.example.applicationhome.features.shimmers.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.applicationhome.features.shimmers.boxes.SearchResultBoxShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun SearchResultScreenShimmer(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .shimmer(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        repeat(2){
            SearchResultBoxShimmer()

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}