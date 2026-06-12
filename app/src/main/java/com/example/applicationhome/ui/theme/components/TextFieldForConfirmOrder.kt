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
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.houseState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.housetextFieldState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.streetState
import com.example.applicationhome.data.models.repository.ConfirmOrderScreenTextField.streettextFieldState
import com.example.applicationhome.ui.theme.model.ConfirmOrderScreenViewModel

@Composable
fun TextFieldForConfirmOrder(confirmOrderScreenViewModel : ConfirmOrderScreenViewModel, textState : TextFieldState, title : String){
    var color by remember { mutableStateOf(Color.Gray) }
    var alpha by remember { mutableStateOf(0.2f) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            .height(60.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(15.dp))
            .border(width = 1.dp, color = color.copy(alpha = alpha), shape = RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(start = 15.dp, end = 15.dp)
    ){
        LaunchedEffect(textState) {
            snapshotFlow { textState.text.toString() }
                .collect {
                    confirmOrderScreenViewModel.bottonstate()
                }
        }
        BasicTextField(
            state = textState,
            modifier = Modifier.fillMaxSize().
            onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    if(textState == houseState && houseState.text.isEmpty() && housetextFieldState){
                        color = Color.Red
                        alpha = 1f
                    }else if(textState == streetState && streetState.text.isEmpty() && streettextFieldState){
                        color = Color.Red
                        alpha = 1f
                    }else if(textState == houseState && houseState.text.isNotEmpty() && housetextFieldState){
                        color = Color.Gray
                        alpha = 0.2f
                    }else if(textState == streetState && streetState.text.isNotEmpty() && streettextFieldState){
                        color = Color.Gray
                        alpha = 0.2f
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            onKeyboardAction = {
                if(textState == houseState) { confirmOrderScreenViewModel.housetextFieldStatetrue() }
                else if(textState == streetState) { confirmOrderScreenViewModel.streettextFieldtrue() }
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
            if(textState.text.isEmpty()){
                Text(
                    text = title,
                    color = color,
                    fontSize = 15.sp
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}