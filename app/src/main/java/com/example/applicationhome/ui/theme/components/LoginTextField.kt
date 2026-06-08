package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationhome.ui.theme.model.LoginViewModel
import com.example.applicationhome.ui.theme.model.UserImageViewModel

@Composable
fun LoginTextField(loginViewModel: LoginViewModel, userImageViewModel : UserImageViewModel = viewModel()){
    val emailstate = loginViewModel.emailstate
    val passwordstate = loginViewModel.passwordstate
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier.padding(start = 40.dp, end = 40.dp)
            .height(55.dp)
            .fillMaxWidth()
            .clip(CircleShape)
            .background(Color.White)
            .padding(start = 25.dp, end = 25.dp)
    ){
        LaunchedEffect(emailstate) {
            snapshotFlow { emailstate.text.toString() }
                .collect {
                    loginViewModel.getData()
                }
        }
        BasicTextField(
            state = emailstate,
            modifier = Modifier.fillMaxSize().padding(start = 30.dp).
            onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    userImageViewModel.statetrue()
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            onKeyboardAction = {
                userImageViewModel.statefalse()
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
                    contentAlignment = Alignment.CenterStart // 👈 هنا بنقوله وسطن النص رأسياً وخليك على الشمال
                ) {
                    innerTextField() // 👈 ده النص الخام اللي بيتكتب
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Icon(
                Icons.Default.Email,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.padding(end = 7.dp)
            )
            if(emailstate.text.isEmpty()){
                Text(
                    text = "Email Address",
                    color = Color.Gray,
                    fontSize = 18.sp
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(25.dp))

    Box(
        modifier = Modifier.padding(start = 40.dp, end = 40.dp)
            .height(55.dp)
            .fillMaxWidth()
            .clip(CircleShape)
            .background(Color.White)
            .padding(start = 25.dp, end = 25.dp)
    ){
        LaunchedEffect(passwordstate) {
            snapshotFlow { passwordstate.text.toString() }
                .collect {
                    loginViewModel.getData()
                }
        }
        BasicTextField(
            state = passwordstate,
            modifier = Modifier.fillMaxSize().padding(start = 30.dp).
            onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    userImageViewModel.statetrue()
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            onKeyboardAction = {
                userImageViewModel.statefalse()
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
                    contentAlignment = Alignment.CenterStart // 👈 هنا بنقوله وسطن النص رأسياً وخليك على الشمال
                ) {
                    innerTextField() // 👈 ده النص الخام اللي بيتكتب
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.padding(end = 7.dp)
            )
            if(passwordstate.text.isEmpty()){
                Text(
                    text = "Password",
                    color = Color.Gray,
                    fontSize = 18.sp
                )
            }
        }
    }
}