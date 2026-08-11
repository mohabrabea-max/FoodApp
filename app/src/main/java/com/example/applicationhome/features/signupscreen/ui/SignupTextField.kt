package com.example.applicationhome.features.signupscreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.data.model.SignUpBasicTextFields
import com.example.applicationhome.data.data.model.SignUpFullNameTextFields


@Composable
fun NameTextField(
    item : SignUpFullNameTextFields
){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        state = item.textField,
        modifier = Modifier.fillMaxSize(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        onKeyboardAction = {
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = Color.Black
        ),
        decorator = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                innerTextField()
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxHeight(),
        contentAlignment = Alignment.CenterStart
    ){
        if(item.textField.text.isEmpty()){
            Text(
                text = "First Name",
                color = if(!item.errorMessage) Color.Gray else Color.Red,
                fontSize = 18.sp
            )
        }
    }
}


@Composable
fun SignupTextField(
    item : SignUpBasicTextFields
){
    var isPasswordVisible by remember { mutableStateOf(!(item.title == "Password" || item.title == "Confirm password")) }

    val interactionSource = remember { MutableInteractionSource() }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp),
        horizontalAlignment = Alignment.Start
    ){
        Box(
            modifier = Modifier
                .height(55.dp)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(Color.White)
                .padding(start = 25.dp, end = 25.dp)
        ){
//        LaunchedEffect(emailstate) {
//            snapshotFlow { emailstate.text.toString() }
//                .collect {
//
//                }
//        }
            BasicSecureTextField(
                state = item.textField,

                textObfuscationMode =
                    if (isPasswordVisible) {
                        TextObfuscationMode.Visible
                    } else {
                        TextObfuscationMode.Hidden
                    },

                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 30.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                        }
                    },

                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),

                onKeyboardAction = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },

                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = Color.Black
                ),

                decorator = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    Icon(
                        item.icon,
                        contentDescription = null,
                        tint = if(item.errorMessage == null) Color.Gray else Color.Red,
                        modifier = Modifier.padding(end = 7.dp)
                    )

                    if(item.textField.text.isEmpty()){
                        Text(
                            text = item.title,
                            color = if(item.errorMessage == null) Color.Gray else Color.Red,
                            fontSize = 18.sp
                        )
                    }
                }

                if(item.title == "Password" || item.title == "Confirm password"){
                    Box(
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ){
                                isPasswordVisible = !isPasswordVisible
                            }
                    ){
                        Icon(
                            if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.DarkGray
                        )
                    }
                }
            }
        }

        if(item.errorMessage != null){
            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = item.errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
    }
}
