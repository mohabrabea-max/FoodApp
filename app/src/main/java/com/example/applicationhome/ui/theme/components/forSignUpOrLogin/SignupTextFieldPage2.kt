package com.example.applicationhome.ui.theme.components.forSignUpOrLogin

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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
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
import com.example.applicationhome.ui.theme.model.SignUpViewModel
import com.example.applicationhome.ui.theme.model.UserImageViewModel

@Composable
fun SignupTextFieldPage2(signUpViewModel : SignUpViewModel, userImageViewModel : UserImageViewModel = viewModel()){
    val phonenumberstate = signUpViewModel.phonenumberstate
    val addressstate = signUpViewModel.addressstate

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
        LaunchedEffect(phonenumberstate) {
            snapshotFlow { phonenumberstate.text.toString() }
                .collect { newValue ->
                }
        }
        BasicTextField(
            state = phonenumberstate,
            modifier = Modifier.fillMaxSize().padding(start = 30.dp).
            onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    userImageViewModel.statetrue()
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
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
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Icon(
                Icons.Default.Phone,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.padding(end = 7.dp)
            )
            if(phonenumberstate.text.isEmpty()){
                Text(
                    text = "Phone number (Optional)",
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
        LaunchedEffect(addressstate) {
            snapshotFlow { addressstate.text.toString() }
                .collect { newValue ->
                }
        }
        BasicTextField(
            state = addressstate,
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
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.padding(end = 7.dp)
            )
            if(addressstate.text.isEmpty()){
                Text(
                    text = "Address (Optional)",
                    color = Color.Gray,
                    fontSize = 18.sp
                )
            }
        }
    }
}