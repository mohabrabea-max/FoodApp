package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.applicationhome.ui.theme.model.ConfirmOrderScreenViewModel
import com.example.applicationhome.ui.theme.model.UserImageViewModel

@Composable
fun ConfirmOrderScreenTextField(confirmOrderScreenViewModel : ConfirmOrderScreenViewModel, userImageViewModel : UserImageViewModel = viewModel()){

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val houseState = confirmOrderScreenViewModel.houseState
    val streetState = confirmOrderScreenViewModel.streetState

    Box(
        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            .height(60.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(15.dp))
            .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(start = 15.dp, end = 15.dp)
    ){
        LaunchedEffect(houseState) {
            snapshotFlow { houseState.text.toString() }
                .collect {
                    confirmOrderScreenViewModel.bottonstate()
                }
        }
        BasicTextField(
            state = houseState,
            modifier = Modifier.fillMaxSize().
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
            if(houseState.text.isEmpty()){
                Text(
                    text = "House",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(15.dp))

    Box(
        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            .height(60.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(15.dp))
            .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(start = 15.dp, end = 15.dp)
    ){
        LaunchedEffect(streetState) {
            snapshotFlow { streetState.text.toString() }
                .collect {
                    confirmOrderScreenViewModel.bottonstate()
                }
        }
        BasicTextField(
            state = streetState,
            modifier = Modifier.fillMaxSize().
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
            if(streetState.text.isEmpty()){
                Text(
                    text = "Street",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        }
    }
}











@Composable
fun ConfirmOrderScreenTextField2(confirmOrderScreenViewModel : ConfirmOrderScreenViewModel, userImageViewModel : UserImageViewModel = viewModel()){

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val phoneNumberState = confirmOrderScreenViewModel.phoneNumberState
    val additionalDirectionsState = confirmOrderScreenViewModel.additionalDirectionsState
    val addressLabelState = confirmOrderScreenViewModel.addressLabelState

    Box(
        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            .height(60.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(15.dp))
            .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(start = 15.dp, end = 15.dp)
    ){
        LaunchedEffect(phoneNumberState) {
            snapshotFlow { phoneNumberState.text.toString() }
                .collect {
                    confirmOrderScreenViewModel.bottonstate()
                }
        }
        BasicTextField(
            state = phoneNumberState,
            modifier = Modifier.fillMaxSize().
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
            if(phoneNumberState.text.isEmpty()){
                Text(
                    text = "Phone number",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(15.dp))

    Box(
        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            .height(60.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(15.dp))
            .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(start = 15.dp, end = 15.dp)
    ){
        LaunchedEffect(additionalDirectionsState) {
            snapshotFlow { additionalDirectionsState.text.toString() }
                .collect {
                    confirmOrderScreenViewModel.bottonstate()
                }
        }
        BasicTextField(
            state = additionalDirectionsState,
            modifier = Modifier.fillMaxSize().
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
            if(additionalDirectionsState.text.isEmpty()){
                Text(
                    text = "Additional directions (optional)",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(15.dp))

    Box(
        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            .height(60.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(15.dp))
            .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(start = 15.dp, end = 15.dp)
    ){
        LaunchedEffect(addressLabelState) {
            snapshotFlow { addressLabelState.text.toString() }
                .collect {
                    confirmOrderScreenViewModel.bottonstate()
                }
        }
        BasicTextField(
            state = addressLabelState,
            modifier = Modifier.fillMaxSize().
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
            if(addressLabelState.text.isEmpty()){
                Text(
                    text = "Address label (optional)",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        }
    }
}