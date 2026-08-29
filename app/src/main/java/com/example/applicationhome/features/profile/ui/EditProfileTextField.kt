package com.example.applicationhome.features.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.AccountTextFieldClass
import com.example.applicationhome.data.data.model.AccountTextFieldEnum
import com.example.applicationhome.data.data.model.ProfileEditResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileTextField(
    item : AccountTextFieldClass,
    isButtonClicked : Boolean,
    errorOutput : ProfileEditResult
){
    val noErrors = listOf(AccountTextFieldEnum.PHONE, AccountTextFieldEnum.ADDRESS)
    val focusRequester = remember { FocusRequester() }

    val textColor =
        if (
            isButtonClicked &&
            item.textField.text.isEmpty() &&
            !noErrors.contains(item.type)
        ) Color.Red

        else if(
            errorOutput == ProfileEditResult.PhoneNumberIncomplete &&
            item.type == AccountTextFieldEnum.PHONE
        ) Color.Red

        else Color.Gray


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
                    text = stringResource(item.title),
                    color = textColor,
                    fontSize = 15.sp
                )
            },

            leadingIcon = if(item.type == AccountTextFieldEnum.PHONE) {
                {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Spacer(modifier = Modifier.width(15.dp))

                        Text(
                            text = "+20",
                            fontSize = 16.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        VerticalDivider(
                            modifier = Modifier.height(22.dp),
                            thickness = 1.dp,
                            color = Color.LightGray
                        )

                        Spacer(modifier = Modifier.width(7.dp))
                    }
                }
            }else null,

            placeholder = {
                Text(
                    text = stringResource(item.emptyCount),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = if (item.type == AccountTextFieldEnum.PHONE) KeyboardType.Phone else KeyboardType.Text,
                imeAction = ImeAction.Done
            ),

            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),

            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            ),

            shape = RoundedCornerShape(20.dp),

            trailingIcon = if (item.icon != null) {
                {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.title),
                        modifier = Modifier.size(22.dp),
                        tint = if (item.icon == Icons.Default.Add) Color.Blue else Color.Gray
                    )
                }
            } else null
        )

        if(
            errorOutput == ProfileEditResult.PhoneNumberIncomplete &&
            item.type == AccountTextFieldEnum.PHONE
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