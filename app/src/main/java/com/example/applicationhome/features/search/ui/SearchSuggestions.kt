package com.example.applicationhome.features.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthWest
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.applicationhome.core.ui.theme.formatSingleWordInSearch

@Composable
fun SearchSuggestions(
    text : String,
    searchText : String = "",
    textColor : Color = Color.Black,
    startIcon : ImageVector,
    iconsColor : Color = Color.Black,
    textClickable : () -> Unit,
    northWestClickable : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { textClickable() },
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector = startIcon,
                contentDescription = "Search Icon",
                tint = iconsColor,
                modifier = Modifier.size(25.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = text.formatSingleWordInSearch(searchText),
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ){ northWestClickable() }
        ){
            Icon(
                imageVector = Icons.Default.NorthWest,
                contentDescription = "Search Icon",
                tint = iconsColor,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}