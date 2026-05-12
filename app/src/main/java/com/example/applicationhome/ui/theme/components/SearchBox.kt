package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.ui.theme.DarkOrange

@Composable
fun SearchBox(){
    Box(modifier = Modifier.fillMaxSize()){
        Box(
            modifier = Modifier.width(340.dp).
            height(50.dp).
            align(Alignment.Center).
            clip(CircleShape).
            background(Color.White).
            border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp)).
            padding(5.dp)
        ){
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    Text(
                        text = "Search food",
                        softWrap = false,
                        color = Color.Gray,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.DarkOrange),
                    contentAlignment = Alignment.Center
                ){
                    IconButton(onClick = {}){
                        Icon(
                            Icons.Default.Tune,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}