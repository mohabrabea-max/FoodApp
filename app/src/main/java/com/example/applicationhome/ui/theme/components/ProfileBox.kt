package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.example.applicationhome.data.models.ProfileData
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.view.model.UserImageViewModel

@Composable
fun ProfileBox(userImageViewModel: UserImageViewModel){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val profile = ProfileData.profileData()
    val focusRequesters = remember {
        profile.associate { it.id to FocusRequester() }
    }
    Column(modifier = Modifier.fillMaxSize().background(Color.VeryLightGray).padding(15.dp)){
        Spacer(modifier = Modifier.height(70.dp))
        Column(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)).background(Color.White).padding(17.dp)){
            Text(text = "Personal Information", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(15.dp))
            profile.forEach{ item ->
                val focusRequester = focusRequesters[item.id]
                Column(modifier = Modifier.fillMaxSize()){
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                        Column (modifier = Modifier.weight(7f), horizontalAlignment = Alignment.Start){
                            //Spacer(modifier = Modifier.width(15.dp))
                            Text(text = item.title, fontSize = 17.sp, color = Color.BrownForFont)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row {
                                Spacer(modifier = Modifier.width(3.dp))
                                if(item.title == "First Name" || item.title == "Last Name" || item.title == "Phone number"){
                                    val state = rememberTextFieldState()
                                    Box(contentAlignment = Alignment.CenterStart){
                                        if (state.text.isEmpty()) {
                                            Text(
                                                text = item.title,
                                                color = Color.Gray,
                                                fontSize = 14.sp
                                            )
                                        }
                                        BasicTextField(
                                            state = state,
                                            modifier = Modifier.fillMaxSize().
                                            then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier).
                                            onFocusChanged { focusState ->
                                                if (focusState.isFocused) {
                                                    userImageViewModel.statetrue()
                                                }
                                            },
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType =
                                                    if(item.title == "First Name" || item.title == "Last Name")
                                                        KeyboardType.Text
                                                    else
                                                        KeyboardType.Phone, imeAction = ImeAction.Done
                                            ),
                                            onKeyboardAction = {
                                                userImageViewModel.statefalse()
                                                keyboardController?.hide()
                                                focusManager.clearFocus()
                                            },
                                            textStyle = TextStyle(
                                                fontSize = 14.sp,
                                                color = Color.MediumBrownForTitle
                                            )
                                        )
                                    }

                                }else if(item.title == "Email"){
                                    Text(
                                        text = item.value.toString(),
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                        if(item.icon != null){
                            IconButton(modifier = Modifier.weight(1f), onClick = {focusRequester?.requestFocus()}){
                                Icon(
                                    modifier = Modifier.size(22.dp),
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = if(item.icon == Icons.Default.Add) Color.Blue else Color.Gray
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    if(item != profile.last()) Divider(color = Color.LightGray.copy(alpha = 0.2f))
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}