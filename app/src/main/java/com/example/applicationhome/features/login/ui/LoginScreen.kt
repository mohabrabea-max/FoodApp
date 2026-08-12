package com.example.applicationhome.features.login.ui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.LoginPages
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.features.login.ui.EmailPage.LoginScreenChickEmailPage
import com.example.applicationhome.features.login.ui.LoginPage.LoginScreenLoginPage
import com.example.applicationhome.features.login.ui.NewPasswordPage.LoginScreenChangePasswordPage
import com.example.applicationhome.features.login.ui.VerificationCodePage.LoginScreenVerificationCodePage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navigationController : NavHostController,
    loginViewModel: LoginViewModel
){
    val clickState = remember { mutableStateOf(true) }

    val currentScreen by loginViewModel.currentScreen.collectAsStateWithLifecycle()

    val loading by loginViewModel.loading.collectAsStateWithLifecycle()
    val verificationCodeLoading by loginViewModel.verificationCodeLoading.collectAsStateWithLifecycle()

    val isNetworkAvailable by loginViewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val loginTextFields by loginViewModel.loginTextFields.collectAsStateWithLifecycle()
    val checkEmailTextFieldObject by loginViewModel.checkEmailTextFieldObject.collectAsStateWithLifecycle()
    val verificationCodeTextFieldObject by loginViewModel.verificationCodeTextFieldObject.collectAsStateWithLifecycle()


    val isButtonLoginPageEnabled by loginViewModel.isButtonLoginPageEnabled.collectAsStateWithLifecycle()
    val isButtonCheckEmailPageEnabled by loginViewModel.isButtonCheckEmailPageEnabled.collectAsStateWithLifecycle()


    BackHandler(enabled = true){
        loginViewModel.navigateBack(
            onExitLoginScreen = {
                if (navigationController.previousBackStackEntry != null) {
                    navigationController.popBackStack()
                }
            },
            onChangeClickState = { clickState.value = it }
        )
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
                            loginViewModel.navigateBack(
                                onExitLoginScreen = {
                                    if (navigationController.previousBackStackEntry != null) {
                                        navigationController.popBackStack()
                                    }
                                },
                                onChangeClickState = { clickState.value = it }
                            )
                        },
                        modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )
        }
    ){
        AnimatedContent(
            targetState = currentScreen,

            transitionSpec = {
                val animationSpec = tween<IntOffset>(durationMillis = 300)

                val isForward = targetState.index > initialState.index

                if (isForward) {
                    (slideInHorizontally(animationSpec) { fullWidth -> fullWidth } + fadeIn()) togetherWith
                            (slideOutHorizontally(animationSpec) { fullWidth -> -fullWidth } + fadeOut())
                } else {
                    (slideInHorizontally(animationSpec) { fullWidth -> -fullWidth } + fadeIn()) togetherWith
                            (slideOutHorizontally(animationSpec) { fullWidth -> fullWidth } + fadeOut())
                }
            },

            label = "LoginNavAnimation"
        ){ screen ->

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ){
                Box(
                    modifier = Modifier.align(Alignment.TopCenter)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.loginimage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(540.dp)
                    )
                }

                Column(modifier = Modifier.align(Alignment.TopCenter)){

                    Spacer(modifier = Modifier.height(100.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
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

                when(screen){
                    LoginPages.LoginPage -> {
                        LoginScreenLoginPage(
                            loginTextFields = loginTextFields,
                            loading = loading,
                            isButtonEnabled = isButtonLoginPageEnabled,
                            login = {
                                if(isNetworkAvailable && clickState.value && isButtonLoginPageEnabled){
                                    clickState.value = false

                                    loginViewModel.login(

                                        onSuccess = { navigationController.popBackStack() },

                                        onField = { clickState.value = true }
                                    )
                                }
                            },
                            signUpNavigation = {
                                navigationController.navigate(Screens.SignUpScreen.screen)
                            },
                            loginScreenNextPage = {
                                loginViewModel.navigateTo(LoginPages.EmailPage){
                                    clickState.value = it
                                }
                            }
                        )
                    }

                    LoginPages.EmailPage -> {
                        LoginScreenChickEmailPage(
                            item = checkEmailTextFieldObject,
                            loading = loading,
                            isButtonEnabled = isButtonCheckEmailPageEnabled,
                            chickEmail = {
                                if(isNetworkAvailable && clickState.value && isButtonCheckEmailPageEnabled){
                                    clickState.value = false

                                    loginViewModel.chickEmail{
                                        clickState.value = it
                                    }
                                }
                            },
                        )
                    }

                    LoginPages.VerificationCodePage -> {
                        LoginScreenVerificationCodePage(
                            item = verificationCodeTextFieldObject,
                            loading = loading,
                            verificationCodeLoading = verificationCodeLoading,
                            resendVerificationCode = { loginViewModel.sendVerificationCode() }
                        )
                    }

                    LoginPages.ChangePasswordPage -> {
                        LoginScreenChangePasswordPage()
                    }
                }
            }
        }
    }
}