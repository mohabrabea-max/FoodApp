package com.example.applicationhome.ui.theme.screens

import android.R.attr.name
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.view.model.ItemScreenViewModel

@Composable
fun ItemScreen(viewModel: ItemScreenViewModel){
    val item = viewModel.selectedItem
    if(item != null){
        Box(
            modifier = Modifier.fillMaxSize().
            background(Color.LightBrownForBackground)
        ){
            Column(modifier = Modifier.fillMaxSize()){
                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = "$name Logo"
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = item.name,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.BrownForFont
                )
                Text(
                    text = item.description
                )
                Text(
                    text = item.price.toString(),
                    fontSize = 30.sp,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.BrownForFont
                )
            }
        }
    }

}