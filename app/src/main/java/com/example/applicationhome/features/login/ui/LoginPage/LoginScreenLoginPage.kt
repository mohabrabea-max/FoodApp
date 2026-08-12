package com.example.applicationhome.features.login.ui.LoginPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.SignUpBasicTextFields
import com.example.applicationhome.features.signupscreen.ui.SignupTextField

@Composable
fun LoginScreenLoginPage(
    loginTextFields : List<SignUpBasicTextFields>,
    loading : Boolean,
    isButtonEnabled : Boolean,
    login : () -> Unit,
    signUpNavigation : () -> Unit,
    loginScreenNextPage : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

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
            text = "Login",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        loginTextFields.forEach { item ->

            if(item != loginTextFields.first()) Spacer(modifier = Modifier.height(25.dp))

            SignupTextField(item)
        }

        Text(
            text = "Forget your password",
            style = MaterialTheme.typography.titleLarge,
            color = Color.DarkOrange,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 50.dp)
                .align(Alignment.Start)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ){ loginScreenNextPage() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        MyButton(
            loading = loading,
            backgroundcolor = if(isButtonEnabled) Color.DarkOrange else Color.Gray,
            fontcolor = Color.White,
            horizontalPadding = 40.dp,
            title = "Login"
        ){
            login()
        }

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 46.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.5f)
            )

            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 110.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Icon(
                Icons.Default.Facebook,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Icon(
                Icons.Default.Facebook,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Icon(
                Icons.Default.Facebook,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "New user?",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontSize = 15.sp
            )

            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.titleLarge,
                color = Color.DarkOrange,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ){ signUpNavigation() }
            )
        }
    }
}