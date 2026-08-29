package com.example.applicationhome.features.forgetpassword.NewPasswordPage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.SignUpBasicTextFields
import com.example.applicationhome.features.signupscreen.ui.SignupTextField

@Composable
fun LoginScreenChangePasswordPage(
    textFields : List<SignUpBasicTextFields>,
    loading : Boolean,
    isButtonEnabled : Boolean,
    changePassword : () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(
            modifier = Modifier.fillMaxWidth(0.65f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.reset_password),
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.set_s_strong_password_to_secure_access_always_stay_safe),
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        textFields.forEach { item ->
            if (item != textFields.first()) Spacer(modifier = Modifier.height(25.dp))
            SignupTextField(item = item)
        }

        Spacer(modifier = Modifier.height(25.dp))

        MyButton(
            loading = loading,
            backgroundcolor = if(isButtonEnabled) Color.DarkOrange else Color.LightGray,
            fontcolor = Color.White,
            horizontalPadding = 40.dp,
            title = stringResource(R.string.change_password)
        ){
            changePassword()
        }
    }
}