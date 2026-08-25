package com.example.applicationhome.features.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.ui.theme.BrownForFont
import com.example.applicationhome.data.data.model.ProfileOptions
import com.example.applicationhome.data.data.model.Settings
import com.example.applicationhome.data.data.model.SettingsScreens

@Composable
fun SettingsOptionsBox(item : ProfileOptions, navigation : () -> Unit){
    val interactionSource = remember { MutableInteractionSource() }

    Box{
        Box(
            modifier = Modifier.aspectRatio(2.3f).
            padding(start = 5.dp, end = 5.dp).
            clip(RoundedCornerShape(10.dp)).
            background(Color.White).
            clickable(
                interactionSource = interactionSource,
                indication = null
            ){ navigation() }
        ){
            Row(
                modifier = Modifier.fillMaxSize().padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Icon(
                    item.icon,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center){
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontSize = 18.sp,
                    )
                    if(item.description != null){
                        Text(
                            text = item.description.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Gray,
                            fontSize = 15.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsBox(settings : List<Settings>, onClickable : (SettingsScreens) -> Unit){
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = Modifier.padding(start = 5.dp, end = 5.dp).fillMaxSize().clip(RoundedCornerShape(10.dp)).background(Color.White)){
        settings.forEach{ item ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ){
                        onClickable(item.option)
                    }
                    .padding(start = 17.dp, end = 17.dp)
            ){
                Spacer(modifier = Modifier.height(25.dp))
                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically){
                    Icon(modifier = Modifier.size(20.dp), imageVector = item.icon, contentDescription = item.title, tint = Color.Black)
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(text = item.title, fontSize = 17.sp, color = Color.BrownForFont)
                }
                Spacer(modifier = Modifier.height(25.dp))
                if(item != settings.last()) Divider(color = Color.LightGray.copy(alpha = 0.2f))
            }
        }
    }
}