package com.example.applicationhome.features.profile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectionOutlinedTextField(
    title : String,
    selected : String,
    showBottomSheet : () -> Unit,
    isEnabled : Boolean = true,
){
    val colorWhenEmpty = if(isEnabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSecondary

    val interactionSource = remember { MutableInteractionSource() }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ){ if(isEnabled) showBottomSheet() }
    ) {
        OutlinedTextField(
            value = selected,

            onValueChange = {  },

            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface,
                disabledTrailingIconColor = colorWhenEmpty,
                errorBorderColor = Color.Red,
                disabledBorderColor = colorWhenEmpty
            ),

            label = {
                Text(
                    text = title,
                    color = colorWhenEmpty,
                    fontSize = 16.sp
                )
            },

            readOnly = true,

            trailingIcon = {
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),

            enabled = false,

            shape = RoundedCornerShape(20.dp),
        )
    }
}