package com.example.applicationhome.ui.theme.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightBackgroundForCards
import com.example.applicationhome.ui.theme.LightBrownForBackground

@Composable
fun AddBox(){
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var number by remember {mutableStateOf(0)}
    var targetWidth = if(number == 0) 35.dp else 120.dp
    Box(
        modifier = Modifier.
        animateContentSize().
        padding(10.dp).
        height(35.dp).
        width(targetWidth).
        clip(CircleShape).
        background(Color.LightBackgroundForCards.copy(alpha = 0.8f)).
        clickable {number++},
        contentAlignment = Alignment.Center
    ){
        if(number == 0) {
            Text(
                text = "+",
                fontSize = 20.sp,
                style = MaterialTheme.typography.labelLarge,
                color = Color.BrownForFont,
                textAlign = TextAlign.Center
            )
        }else{
            Row(
                modifier = Modifier.
                fillMaxSize().
                background(Color.LightBrownForBackground)
            ){
                Box(
                    modifier = Modifier.
                    weight(1f).
                    fillMaxHeight().
                    clickable {number++},
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "+",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.BrownForFont,
                        textAlign = TextAlign.Center
                    )
                }
                Box(
                    modifier = Modifier.
                    weight(1f).
                    fillMaxHeight().
                    clickable {number},
                    contentAlignment = Alignment.Center
                ){
                    BasicTextField(
                        value = number.toString(),
                        onValueChange = { newValue ->
                            if (newValue.isNotEmpty()) {
                                number = newValue.toIntOrNull() ?: number
                            }else{
                                number = newValue.toIntOrNull() ?: 0
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        ),
                        singleLine = true,
                        textStyle = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                    )
                }
                Box(
                    modifier = Modifier.
                    weight(1f).
                    fillMaxHeight().
                    animateContentSize().
                    clickable {number--},
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "-",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.BrownForFont,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}