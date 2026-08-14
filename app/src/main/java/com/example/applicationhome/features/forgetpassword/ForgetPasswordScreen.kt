package com.example.applicationhome.features.forgetpassword

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.bars.NetworkErrorTopBar
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.showNetworkSnackBar
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.MatteBlack
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.LoginPages
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.data.model.SignUpErrors
import com.example.applicationhome.features.forgetpassword.EmailPage.LoginScreenChickEmailPage
import com.example.applicationhome.features.forgetpassword.NewPasswordPage.LoginScreenChangePasswordPage
import com.example.applicationhome.features.forgetpassword.VerificationCodePage.LoginScreenVerificationCodePage
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgetPasswordScreen(
    navigationController : NavHostController,
    viewModel: ForgetPasswordScreenViewModel
){
    val clickState = remember { mutableStateOf(true) }

    val snackBarHostState = remember { SnackbarHostState() }
    val signUpStates = viewModel.signUpStates

    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val verificationCodeLoading by viewModel.verificationCodeLoading.collectAsStateWithLifecycle()

    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val checkEmailTextFieldObject by viewModel.checkEmailTextFieldObject.collectAsStateWithLifecycle()
    val verificationCodeTextFieldObject by viewModel.verificationCodeTextFieldObject.collectAsStateWithLifecycle()
    val newPasswordTextFields by viewModel.newPasswordTextFields.collectAsStateWithLifecycle()

    val isButtonCheckEmailPageEnabled by viewModel.isButtonCheckEmailPageEnabled.collectAsStateWithLifecycle()
    val isButtonChangePasswordPageEnabled by viewModel.isButtonChangePasswordPageEnabled.collectAsStateWithLifecycle()


    BackHandler(enabled = true){
        viewModel.navigateBack(
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
                            viewModel.navigateBack(
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
                        text = "Change password",
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(650.dp)
                    .clip(shape = RoundedCornerShape(topStart = 100.dp))
                    .background(Color.VeryLightGray),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                NetworkErrorTopBar(isNetworkAvailable = isNetworkAvailable, padding = 80.dp)

                Spacer(modifier = Modifier.height(30.dp))

                AnimatedContent(
                    targetState = currentScreen,

                    transitionSpec = {
                        val slideDuration = 350
                        val fadeOutDuration = 180
                        val fadeInDuration = 250

                        if (targetState.index > initialState.index) {
                            (slideInHorizontally(
                                animationSpec = tween(slideDuration, easing = FastOutSlowInEasing),
                                initialOffsetX = { fullWidth -> fullWidth / 3 }
                            ) + fadeIn(animationSpec = tween(fadeInDuration)))
                                .togetherWith(
                                    slideOutHorizontally(
                                        animationSpec = tween(slideDuration, easing = FastOutSlowInEasing),
                                        targetOffsetX = { fullWidth -> -fullWidth / 3 }
                                    ) + fadeOut(animationSpec = tween(fadeOutDuration))
                                )
                        } else {
                            (slideInHorizontally(
                                animationSpec = tween(slideDuration, easing = FastOutSlowInEasing),
                                initialOffsetX = { fullWidth -> -fullWidth / 3 }
                            ) + fadeIn(animationSpec = tween(fadeInDuration)))
                                .togetherWith(
                                    slideOutHorizontally(
                                        animationSpec = tween(slideDuration, easing = FastOutSlowInEasing),
                                        targetOffsetX = { fullWidth -> fullWidth / 3 }
                                    ) + fadeOut(animationSpec = tween(fadeOutDuration))
                                )
                        }
                    },

                    label = "ForgetPasswordNavAnimation"
                ){ screen ->

                    when(screen){
                        LoginPages.EmailPage -> {
                            LoginScreenChickEmailPage(
                                item = checkEmailTextFieldObject,
                                loading = loading,
                                isButtonEnabled = isButtonCheckEmailPageEnabled,
                                chickEmail = {
                                    if(isNetworkAvailable && clickState.value && isButtonCheckEmailPageEnabled){
                                        clickState.value = false

                                        viewModel.checkEmail{
                                            clickState.value = true
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
                                resendVerificationCode = { viewModel.sendVerificationCode() }
                            )
                        }

                        LoginPages.ChangePasswordPage -> {
                            LoginScreenChangePasswordPage(
                                textFields = newPasswordTextFields,
                                loading = loading,
                                isButtonEnabled = isButtonChangePasswordPageEnabled,
                                changePassword = {
                                    viewModel.onChangePasswordClicked {
                                        navigationController.navigate(Screens.LoginScreen.screen) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }
                    }
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