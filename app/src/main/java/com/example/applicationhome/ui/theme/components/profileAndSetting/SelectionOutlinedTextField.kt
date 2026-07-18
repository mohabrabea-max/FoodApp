package com.example.applicationhome.ui.theme.components.profileAndSetting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectionOutlinedTextField(
    title : String,
    selected : String,
    showBottomSheet : () -> Unit,
    isDataChanged : () -> Unit,
    isEnabled : Boolean = true,
){
    LaunchedEffect(selected) {
        snapshotFlow { selected }
            .collect { isDataChanged() }
    }

    val colorWhenEmpty = if(isEnabled) Color.Gray else Color.LightGray

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
                disabledTextColor = Color.Black,
                disabledLabelColor = Color.Black,
                disabledTrailingIconColor = colorWhenEmpty,
                errorBorderColor = Color.Red,
                disabledBorderColor = Color.LightGray
            ),

            label = {
                Text(
                    text = title,
                    color = colorWhenEmpty,
                    fontSize = 15.sp
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