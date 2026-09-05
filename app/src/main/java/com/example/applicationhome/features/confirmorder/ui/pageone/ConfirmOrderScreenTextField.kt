package com.example.applicationhome.features.confirmorder.ui.pageone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.designsystem.SquareRadioButton
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.ConfirmOrderScreenTextFieldEnum
import com.example.applicationhome.data.data.model.ProfileEditResult
import com.example.applicationhome.data.data.model.TextFieldClassFromConfirmOrderScreen


@Composable
fun ConfirmOrderScreenTextField(
    isEditEnabled : Boolean = true,
    item : TextFieldClassFromConfirmOrderScreen,
    isLastTextField : Boolean,
    isButtonClicked : Boolean,
    errorOutput : ProfileEditResult,
    isSavePhoneNumberSelected : Boolean,
    isSaveAddressSelected : Boolean,
    bottonStateChange : () -> Unit,
    onSavePhoneNumber : () -> Unit,
    onSaveAddressRadioButton : () -> Unit
){
    val errors = listOf(
        ConfirmOrderScreenTextFieldEnum.PHONE,
        ConfirmOrderScreenTextFieldEnum.PHONE_WITHOUT_BUTTON,
        ConfirmOrderScreenTextFieldEnum.HOUSE,
        ConfirmOrderScreenTextFieldEnum.STREET,
        ConfirmOrderScreenTextFieldEnum.TITLE
    )
    val focusRequester = remember { FocusRequester() }

    val error =
        isButtonClicked &&
        item.textField.text.isEmpty() &&
        errors.contains(item.type)

    val textColor =
        if (error) Color.Red
        else MaterialTheme.colorScheme.onSurfaceVariant


    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(
        modifier = Modifier.padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.Start
    ){
        OutlinedTextField(
            enabled = isEditEnabled,

            value = item.textField.text.toString(),

            onValueChange = { newText ->
                if(newText.length <= 11 || item.type != ConfirmOrderScreenTextFieldEnum.PHONE){
                    item.textField.edit { replace(0, length, newText) }
                    bottonStateChange()
                }
            },

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.DarkOrange,
                unfocusedBorderColor = Color.LightGray,
                errorBorderColor = Color.Red,
                disabledBorderColor = MaterialTheme.colorScheme.onTertiary
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

            leadingIcon = if(item.type == ConfirmOrderScreenTextFieldEnum.PHONE || item.type == ConfirmOrderScreenTextFieldEnum.PHONE_WITHOUT_BUTTON) {
                {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Spacer(modifier = Modifier.width(15.dp))

                        Text(
                            text = "+20",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
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
                    text = stringResource(item.title),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp
                )
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = if (
                        item.type == ConfirmOrderScreenTextFieldEnum.PHONE ||
                        item.type == ConfirmOrderScreenTextFieldEnum.PHONE_WITHOUT_BUTTON
                    ) KeyboardType.Phone else KeyboardType.Text,
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
                color = MaterialTheme.colorScheme.onSurface
            ),

            shape = RoundedCornerShape(20.dp)
        )

        if(item.type == ConfirmOrderScreenTextFieldEnum.PHONE){
            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                SquareRadioButton(selected = isSavePhoneNumberSelected){
                    onSavePhoneNumber()
                }

                Text(
                    text = stringResource(R.string.save_this_number),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 7.dp)
                )
            }
        }

        if(
            errorOutput == ProfileEditResult.PhoneNumberIncomplete &&
            item.type == ConfirmOrderScreenTextFieldEnum.PHONE
        ){
            Text(
                text = stringResource(R.string.phone_number_error_message),
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 10.dp)
            )
        }

        if(isLastTextField){
            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                SquareRadioButton(selected = isSaveAddressSelected){
                    onSaveAddressRadioButton()
                }

                Text(
                    text = stringResource(R.string.save_this_address),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 7.dp)
                )
            }
        }
    }
}