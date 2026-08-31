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
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.data.model.ProfileOptions
import com.example.applicationhome.data.data.model.Settings
import com.example.applicationhome.data.data.model.SettingsScreens

@Composable
fun SettingsOptionsBox(item : ProfileOptions, navigation : () -> Unit){
    val interactionSource = remember { MutableInteractionSource() }

    val description = item.description

    Box{
        Box(
            modifier = Modifier.aspectRatio(2.2f).
            padding(start = 5.dp, end = 5.dp).
            clip(RoundedCornerShape(10.dp)).
            background(MaterialTheme.colorScheme.surface).
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
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center){
                    Text(
                        text = stringResource(item.title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 17.sp,
                    )
                    if(description != null){
                        Text(
                            text = stringResource(description),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsBox(
    settings : List<Settings>,
    contentColor : Color = MaterialTheme.colorScheme.onSurface,
    onClickable : (SettingsScreens) -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
    ){
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
                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        modifier = Modifier
                            .size(19.dp),
                        imageVector = item.icon,
                        contentDescription = stringResource(item.title),
                        tint = contentColor
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    Text(
                        text = stringResource(item.title),
                        fontSize = 16.sp,
                        color = contentColor
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if(item != settings.last()) HorizontalDivider(
                    Modifier,
                    DividerDefaults.Thickness,
                    color = Color.LightGray.copy(alpha = 0.3f)
                )
            }
        }
    }
}