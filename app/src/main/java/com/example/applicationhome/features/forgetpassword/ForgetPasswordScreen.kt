package com.example.applicationhome.features.forgetpassword

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.applicationhome.features.forgetpassword.EmailPage.LoginScreenChickEmailPage
import com.example.applicationhome.features.forgetpassword.NewPasswordPage.LoginScreenChangePasswordPage
import com.example.applicationhome.features.forgetpassword.VerificationCodePage.LoginScreenVerificationCodePage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgetPasswordScreen(
    navigationController : NavHostController,
    viewModel: ForgetPasswordScreenViewModel
){
    val clickState = remember { mutableStateOf(true) }

    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val verificationCodeLoading by viewModel.verificationCodeLoading.collectAsStateWithLifecycle()

    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val checkEmailTextFieldObject by viewModel.checkEmailTextFieldObject.collectAsStateWithLifecycle()
    val verificationCodeTextFieldObject by viewModel.verificationCodeTextFieldObject.collectAsStateWithLifecycle()

    val isButtonCheckEmailPageEnabled by viewModel.isButtonCheckEmailPageEnabled.collectAsStateWithLifecycle()


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
            ){
                Spacer(modifier = Modifier.height(30.dp))

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
                            LoginScreenChangePasswordPage()
                        }
                    }
                }
            }
        }
    }
}