package com.example.applicationhome.features.login.ui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.features.signupscreen.ui.SignupTextField

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navigationController : NavHostController,
    loginViewModel: LoginViewModel
){
    val clickState = remember { mutableStateOf(true) }

    val loading by loginViewModel.loading.collectAsStateWithLifecycle()

    val isNetworkAvailable by loginViewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val loginTextFields by loginViewModel.loginTextFields.collectAsStateWithLifecycle()


    val isButtonEnabled by loginViewModel.isButtonLoginPageEnabled.collectAsStateWithLifecycle()


    BackHandler(enabled = true){
        if (navigationController.previousBackStackEntry != null) {
            navigationController.popBackStack()
        }
    }


    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),

        containerColor = Color.VeryLightGray,

        topBar = {
            MyTopBar(
                Color.DarkOrange.copy(alpha = 0f),
                modifier = Modifier.fillMaxWidth().height(100.dp),
                null,
                Color.White,
                {
                    IconButton(
                        onClick = {
                            if (navigationController.previousBackStackEntry != null) {
                                navigationController.popBackStack()
                            }
                        },
                        modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )
        }
    ){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.loginimage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(540.dp)
                )
            }

            Column(modifier = Modifier.align(Alignment.TopCenter)) {

                Spacer(modifier = Modifier.height(100.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Food",
                        color = Color.White,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .graphicsLayer(
                                scaleX = 1.1f,
                                scaleY = 1f
                            )
                    )

                    Text(
                        text = "App",
                        color = Color.White,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .graphicsLayer(
                                scaleX = 1.1f,
                                scaleY = 1f
                            )
                    )
                }
            }

            val interactionSource = remember { MutableInteractionSource() }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .clip(shape = RoundedCornerShape(topStart = 100.dp))
                    .background(Color.VeryLightGray),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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

                    if (item != loginTextFields.first()) Spacer(modifier = Modifier.height(25.dp))

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
                        ) { navigationController.navigate(Screens.ForgetPasswordScreen.screen) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                MyButton(
                    loading = loading,
                    backgroundcolor = if (isButtonEnabled) Color.DarkOrange else Color.Gray,
                    fontcolor = Color.White,
                    horizontalPadding = 40.dp,
                    title = "Login"
                ) {
                    if(isNetworkAvailable && clickState.value && isButtonEnabled){
                        clickState.value = false

                        loginViewModel.login(

                            onSuccess = { navigationController.popBackStack() },

                            onField = { clickState.value = true }
                        )
                    }
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
                ) {
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
                ) {
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
                            ) {  navigationController.navigate(Screens.SignUpScreen.screen) }
                    )
                }
            }
        }
    }
}