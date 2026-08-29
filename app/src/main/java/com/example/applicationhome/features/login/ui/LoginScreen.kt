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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.bars.NetworkErrorTopBar
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.showNetworkSnackBar
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.MatteBlack
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.LoginStates
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.data.model.SignUpBasicTextFields
import com.example.applicationhome.data.data.model.SignUpErrors
import com.example.applicationhome.features.signupscreen.ui.SignupTextField
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navigationController : NavHostController,
    viewModel: LoginViewModel
){
    val interactionSource = remember { MutableInteractionSource() }

    val snackBarHostState = remember { SnackbarHostState() }
    val signUpStates = viewModel.signUpStates

    val clickState = remember { mutableStateOf(true) }

    val loading by viewModel.loading.collectAsStateWithLifecycle()

    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val loginTextFields by viewModel.loginTextFields.collectAsStateWithLifecycle()

    val errorType by viewModel.errorType.collectAsStateWithLifecycle()

    val isButtonEnabled by viewModel.isButtonLoginPageEnabled.collectAsStateWithLifecycle()

    val textButtonsColor = if(isNetworkAvailable) Color.DarkOrange else Color.Gray


    BackHandler(enabled = true){
        if (navigationController.previousBackStackEntry != null) {
            navigationController.popBackStack()
        }else{
            navigationController.navigate(Screens.DashboardScreen.screen) {
                popUpTo(0) { inclusive = true }
            }
        }
    }


    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),

        containerColor = Color.VeryLightGray,

        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .padding(horizontal = 32.dp)
            ){ data ->
                Surface(
                    color = Color.MatteBlack,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(15.dp),
                    shadowElevation = 4.dp
                ){
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                }
            }
        },

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
                            }else{
                                navigationController.navigate(Screens.DashboardScreen.screen) {
                                    popUpTo(0) { inclusive = true }
                                }
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .clip(shape = RoundedCornerShape(topStart = 100.dp))
                    .background(Color.VeryLightGray),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NetworkErrorTopBar(isNetworkAvailable = isNetworkAvailable, padding = 80.dp)

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 35.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                loginTextFields.forEach { item ->

                    if (item != loginTextFields.first()) Spacer(modifier = Modifier.height(25.dp))

                    SignupTextField(
                        SignUpBasicTextFields(
                            title = item.title,
                            textField = item.textField,
                            errorMessage = null,
                            icon = item.icon,
                            type = item.type
                        )
                    )
                }

                when(errorType){
                    is LoginStates.Error -> {
                        Spacer(modifier = Modifier.height(10.dp))

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 40.dp)
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .background(Color(0xFFF9E2DE)),

                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ){
                            Text(
                                text = (errorType as LoginStates.Error).errorMessage,
                                color = Color(0xFF4A1211),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    else -> {}
                }

                Text(
                    text = stringResource(R.string.forget_your_password),
                    style = MaterialTheme.typography.titleLarge,
                    color = textButtonsColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(start = 50.dp)
                        .align(Alignment.Start)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ){
                            if(isNetworkAvailable){
                                navigationController.navigate(Screens.ForgetPasswordScreen.screen)
                            }else{
                                viewModel.snackbarError("Network error")
                            }
                        }
                )

                Spacer(modifier = Modifier.height(10.dp))

                MyButton(
                    loading = loading,
                    backgroundcolor = if (isButtonEnabled && isNetworkAvailable) Color.DarkOrange else Color.LightGray,
                    fontcolor = Color.White,
                    horizontalPadding = 40.dp,
                    title = stringResource(R.string.login)
                ) {
                    if(isNetworkAvailable && clickState.value && isButtonEnabled){
                        clickState.value = false

                        viewModel.login(

                            onSuccess = {
                                navigationController.navigate(Screens.DashboardScreen.screen) {
                                    popUpTo(0) { inclusive = true }
                                }
                            },

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
                        text = stringResource(R.string.or),
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
                        text = stringResource(R.string.new_user),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontSize = 15.sp
                    )

                    Text(
                        text = stringResource(R.string.sign_up),
                        style = MaterialTheme.typography.titleLarge,
                        color = textButtonsColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                if(isNetworkAvailable){
                                    navigationController.navigate(Screens.SignUpScreen.screen)
                                }else{
                                    viewModel.snackbarError("Network error")
                                }
                            }
                    )
                }
            }
        }

        LaunchedEffect(Unit){
            signUpStates.collect { message ->
                when(message){
                    is SignUpErrors.Error -> {
                        launch {
                            snackBarHostState.showNetworkSnackBar(
                                message = message.message
                            )
                        }
                    }
                }
            }
        }
    }
}