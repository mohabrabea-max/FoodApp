package com.example.applicationhome.ui.theme.components.profileAndSetting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.data.model.AccountTextFieldClass
import com.example.applicationhome.data.data.model.ProfileEditResult
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.VeryLightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileTextField(
    item : AccountTextFieldClass,
    isButtonClicked : Boolean,
    errorOutput : ProfileEditResult,
    isDataChanged : () -> Unit
){
    val noErrors = listOf("Phone number", "Address")
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(item.textField) {
        snapshotFlow { item.textField.text.toString() }
            .collect { isDataChanged() }
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(horizontalAlignment = Alignment.Start){
        OutlinedTextField(
            value = item.textField.text.toString(),

            onValueChange = { newText -> item.textField.edit { replace(0, length, newText) } },

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.DarkOrange,
                unfocusedBorderColor = Color.LightGray,
                errorBorderColor = Color.Red,
                disabledBorderColor = Color.VeryLightGray
            ),

            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(vertical = 8.dp),

            label = {
                Text(
                    text = item.title,
                    color =
                        if (
                            isButtonClicked &&
                            item.textField.text.isEmpty() &&
                            !noErrors.contains(item.title)
                        ) Color.Red
                        else if(
                            errorOutput == ProfileEditResult.PhoneNumberIncomplete &&
                            item.title == "Phone number"
                        ) Color.Red
                        else Color.BrownForFont
                )
            },

            placeholder = {
                Text(
                    text = item.emptyCount,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = if (item.title == "Phone number") KeyboardType.Phone else KeyboardType.Text,
                imeAction = ImeAction.Done
            ),

            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),

            textStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            ),

            shape = RoundedCornerShape(20.dp),

            trailingIcon = if (item.icon != null) {
                {
                    IconButton(onClick = { focusRequester.requestFocus() }) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(22.dp),
                            tint = if (item.icon == Icons.Default.Add) Color.Blue else Color.Gray
                        )
                    }
                }
            } else null
        )

        if(
            errorOutput == ProfileEditResult.PhoneNumberIncomplete &&
            item.title == "Phone number"
        ){
            Text(
                text = "Phone number must be 11 digits and start with 010, 011, 012, or 015.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
    }
}