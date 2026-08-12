package com.example.applicationhome.features.login.ui.EmailPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.SignUpBasicTextFields
import com.example.applicationhome.features.signupscreen.ui.SignupTextField

@Composable
fun LoginScreenChickEmailPage(
    item : SignUpBasicTextFields,
    loading : Boolean,
    isButtonEnabled : Boolean,
    chickEmail : () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(550.dp)
            .clip(shape = RoundedCornerShape(topStart = 100.dp))
            .background(Color.VeryLightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Enter your email address.",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Gray,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        SignupTextField(item = item)

        Spacer(modifier = Modifier.height(25.dp))

        MyButton(
            loading = loading,
            backgroundcolor = if(isButtonEnabled) Color.DarkOrange else Color.Gray,
            fontcolor = Color.White,
            horizontalPadding = 40.dp,
            title = "Next"
        ){
            chickEmail()
        }
    }
}