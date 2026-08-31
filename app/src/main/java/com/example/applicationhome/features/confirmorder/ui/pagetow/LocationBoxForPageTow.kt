package com.example.applicationhome.features.confirmorder.ui.pagetow

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.core.ui.theme.DarkOrange

@Composable
fun LocationBoxForPageTow(
    locationImage : String,
    city : String,
    streetAndHome : Pair<String, String>,
    phoneNumber : String,
    changeLocation : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier.padding(20.dp)
            .height(250.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(15.dp))
            .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(15.dp))

    ){
        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
        ){
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current).
                data(locationImage).
                crossfade(true).
                precision(Precision.EXACT).
                build(),
                contentDescription = locationImage,
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f).padding(end = 5.dp)
            )

            Column(
                modifier = Modifier
                    .weight(5f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = "House (${city})",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "${streetAndHome.first}, ${streetAndHome.second}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )

                Text(
                    text = "Phone number: +20 $phoneNumber",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ){ changeLocation() },
                contentAlignment = Alignment.CenterEnd
            ){
                Text(
                    text = "Change",
                    color = Color.DarkOrange,
                    fontSize = 15.sp
                )
            }
        }
    }
}