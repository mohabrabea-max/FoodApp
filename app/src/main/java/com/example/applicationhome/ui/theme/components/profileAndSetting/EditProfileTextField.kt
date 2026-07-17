package com.example.applicationhome.ui.theme.components.profileAndSetting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.MediumBrownForTitle

@Composable
fun EditProfileTextField(
    item : AccountTextFieldClass,
    isButtonClicked : Boolean,
    isDataChanged : () -> Unit
){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
        Column (modifier = Modifier.weight(7f), horizontalAlignment = Alignment.Start){
            Text(text = item.title, fontSize = 17.sp, color = Color.BrownForFont)

            Spacer(modifier = Modifier.height(10.dp))

            Box(contentAlignment = Alignment.CenterStart){
                if(item.textField.text.isEmpty()){
                    Text(
                        text = item.emptyCount,
                        color = if(isButtonClicked) Color.Red else Color.Gray,
                        fontSize = 14.sp
                    )
                }
                LaunchedEffect(item.textField) {
                    snapshotFlow { item.textField.text.toString() }
                        .collect {
                            isDataChanged()
                        }
                }
                BasicTextField(
                    state = item.textField,
                    modifier = Modifier.fillMaxSize().
                    onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType =
                            if(item.title == "Phone number")
                                KeyboardType.Phone
                            else
                                KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    onKeyboardAction = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color.MediumBrownForTitle,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
        if(item.icon != null){
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {  }
            ){
                Icon(
                    modifier = Modifier.size(22.dp),
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = if(item.icon == Icons.Default.Add) Color.Blue else Color.Gray
                )
            }
        }
    }
}