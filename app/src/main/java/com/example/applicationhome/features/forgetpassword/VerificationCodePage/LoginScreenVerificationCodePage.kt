package com.example.applicationhome.features.forgetpassword.VerificationCodePage

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.VerificationTextFields
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("DefaultLocale")
@Composable
fun LoginScreenVerificationCodePage(
    item : VerificationTextFields,
    totalSeconds: Int = 30,
    loading : Boolean,
    verificationCodeLoading : Boolean,  // هنعمل دايرة تحميل كبيرة تعمل ظل على الشاشة كلها
    resendVerificationCode : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    var timeRemaining by rememberSaveable { mutableStateOf(totalSeconds) }
    val resendVerificationCodeState = timeRemaining == 0


    LaunchedEffect(key1 = timeRemaining, key2 = verificationCodeLoading){

        if(timeRemaining > 0 && !verificationCodeLoading){

            delay(1000L.milliseconds)

            timeRemaining--

        }

    }

//    var endTimeMillis by rememberSaveable { mutableLongStateOf(System.currentTimeMillis() + 30_000L) }
//    LaunchedEffect(key1 = endTimeMillis){
//        val currentTime = System.currentTimeMillis()
//        val secondsLeft = ((endTimeMillis - currentTime) / 1000).toInt()
//
//        if (secondsLeft > 0) {
//            timeRemaining = secondsLeft
//        } else {
//            timeRemaining = 0
//        }
//
//        delay(1000L.milliseconds)
//    }

    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)


    LaunchedEffect(item.textField.text){
        if(item.textField.text.length == 6){
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(
            modifier = Modifier.fillMaxWidth(0.65f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = stringResource(R.string.verification_code),
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.enter_the_verification_code_to_confirm_your_identity),
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Box(contentAlignment = Alignment.Center) {
            val emptyTextToolbar = remember {
                object : TextToolbar {
                    override val status: TextToolbarStatus = TextToolbarStatus.Hidden
                    override fun hide() {}
                    override fun showMenu(
                        rect: Rect,
                        onCopyRequested: (() -> Unit)?,
                        onPasteRequested: (() -> Unit)?,
                        onCutRequested: (() -> Unit)?,
                        onSelectAllRequested: (() -> Unit)?
                    ) {}
                }
            }

            val transparentSelectionColors = TextSelectionColors(
                handleColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )

            CompositionLocalProvider(
                LocalTextToolbar provides emptyTextToolbar,
                LocalTextSelectionColors provides transparentSelectionColors
            ) {
                BasicTextField(
                    state = item.textField,

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),

                    onKeyboardAction = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },

                    // تحديد إن الإدخال أرقام فقط وما يزيدش عن طول الكود
                    inputTransformation = InputTransformation.maxLength(6),

                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        color = Color.Transparent
                    ),

                    modifier = Modifier.fillMaxSize().matchParentSize().alpha(0f)
                )

                // 2. المربعات الظاهرة للمستخدم
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(6) { index ->
                        val char = item.textField.text.getOrNull(index)?.toString() ?: ""
                        val isFocused = item.textField.text.length == index
                        val isEmpty = char.isEmpty()

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .border(
                                    width = if (isFocused || !isEmpty || item.error) 2.dp else 1.dp,

                                    color = if (isFocused) Color.DarkOrange
                                    else if(isEmpty) Color.LightGray
                                    else item.stateColor,

                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = stringResource(R.string.didn_t_get_the_code),
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = stringResource(R.string.resend_it),
                style = MaterialTheme.typography.titleLarge,
                color = if(resendVerificationCodeState) Color.DarkOrange else Color.Gray,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ){
                        if(resendVerificationCodeState) {

                            resendVerificationCode()

                            if(!verificationCodeLoading){
                                timeRemaining = totalSeconds
                            }
                        }
                    }
            )


            if(!resendVerificationCodeState){
                Spacer(modifier = Modifier.width(3.dp))

                Text(
                    text = stringResource(R.string.in__time) + " $formattedTime",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Gray,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            if(verificationCodeLoading) {
                Spacer(modifier = Modifier.width(5.dp))

                CircularProgressIndicator(
                    color = Color.DarkOrange,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .size(20.dp)  //  الجم عامل مشكلة في الUI
                        .align(Alignment.CenterVertically),
                )
            }
        }

        if(loading) {
            CircularProgressIndicator(color = Color.DarkOrange)
        }
    }
}