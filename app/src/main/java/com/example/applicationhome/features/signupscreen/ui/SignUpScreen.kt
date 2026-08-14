package com.example.applicationhome.features.signupscreen.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.data.model.SignUpErrors
import com.example.applicationhome.data.data.model.SignUpScreens
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    navigationController : NavHostController,
    viewModel : SignUpViewModel
){
    val interactionSource = remember { MutableInteractionSource() }

    val snackBarHostState = remember { SnackbarHostState() }
    val signUpStates = viewModel.signUpStates

    val clickState = rememberSaveable { mutableStateOf(true) }

    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val signupPages by viewModel.signupPages.collectAsStateWithLifecycle()

    val loading by viewModel.loading.collectAsState()

    val state by viewModel.isButtonEnabled.collectAsStateWithLifecycle()

    val signUpFullNameTextFields by viewModel.signUpFullNameTextFields.collectAsStateWithLifecycle()
    val signUpBasicTextFields by viewModel.signUpBasicTextFields.collectAsStateWithLifecycle()

    val textButtonsColor = if(isNetworkAvailable) Color.DarkOrange else Color.Gray

    BackHandler(enabled = true) {
        when(signupPages){
            SignUpScreens.BasicDataScreen -> {
                viewModel.lastPage{
                    if (navigationController.previousBackStackEntry != null) {
                        navigationController.popBackStack()
                    }
                }
            }

            SignUpScreens.OptionalDataScreen -> {
                navigationController.navigate(Screens.DashboardScreen.screen) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.navigationBarsPadding().fillMaxSize(),

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
                            when(signupPages){
                                SignUpScreens.BasicDataScreen -> {
                                    viewModel.lastPage{
                                        if (navigationController.previousBackStackEntry != null) {
                                            navigationController.popBackStack()
                                        }
                                    }
                                }

                                SignUpScreens.OptionalDataScreen -> {
                                    navigationController.navigate(Screens.DashboardScreen.screen) {
                                        popUpTo(0) { inclusive = true }
                                    }
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
            modifier = Modifier.fillMaxSize().background(Color.VeryLightGray).navigationBarsPadding(),
            contentAlignment = Alignment.BottomCenter
        ){
            Row(
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
                Spacer(modifier = Modifier.height(55.dp))
                Text(
                    text = "Sign up",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(710.dp)
                    .clip(shape = RoundedCornerShape(topStart = 100.dp))
                    .background(Color.VeryLightGray),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                NetworkErrorTopBar(isNetworkAvailable = isNetworkAvailable, padding = 80.dp)

                AnimatedContent(
                    targetState = signupPages,

                    transitionSpec = {
                        val slideDuration = 350
                        val fadeOutDuration = 180 // 👈 السر هنا: أسرع من السلايد عشان يختفي في نص الطريق!
                        val fadeInDuration = 250

                        if (targetState.index > initialState.index) {
                            // ➡️ الانتقال للأمام (من اللوجين للـ Register)
                            (slideInHorizontally(
                                animationSpec = tween(slideDuration, easing = FastOutSlowInEasing),
                                initialOffsetX = { fullWidth -> fullWidth / 3 } // 👈 ضفنا X
                            ) + fadeIn(animationSpec = tween(fadeInDuration)))
                                .togetherWith(
                                    slideOutHorizontally(
                                        animationSpec = tween(slideDuration, easing = FastOutSlowInEasing),
                                        targetOffsetX = { fullWidth -> -fullWidth / 3 } // 👈 ضفنا X
                                    ) + fadeOut(animationSpec = tween(fadeOutDuration))
                                )
                        } else {
                            // ⬅️ الرجوع للخلف (من الـ Register للـ Login)
                            (slideInHorizontally(
                                animationSpec = tween(slideDuration, easing = FastOutSlowInEasing),
                                initialOffsetX = { fullWidth -> -fullWidth / 3 } // 👈 ضفنا X
                            ) + fadeIn(animationSpec = tween(fadeInDuration)))
                                .togetherWith(
                                    slideOutHorizontally(
                                        animationSpec = tween(slideDuration, easing = FastOutSlowInEasing),
                                        targetOffsetX = { fullWidth -> fullWidth / 3 } // 👈 ضفنا X
                                    ) + fadeOut(animationSpec = tween(fadeOutDuration))
                                )
                        }
                    },

                    label = "SignUpNavAnimation"
                ){ screen ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        when(screen){

                            SignUpScreens.BasicDataScreen -> {
                                item {
                                    Spacer(modifier = Modifier.height(70.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ){
                                        signUpFullNameTextFields.forEach { item ->
                                            Box(
                                                modifier = Modifier.padding(start = item.startPadding, end = item.endPadding)
                                                    .height(55.dp)
                                                    .weight(1f)
                                                    .clip(shape = item.roundedCornerShape)
                                                    .background(Color.White)
                                                    .padding(start = 25.dp, end = 25.dp)
                                            ){
                                                NameTextField(item = item)
                                            }
                                        }

                                    }

                                    Spacer(modifier = Modifier.height(25.dp))

                                    signUpBasicTextFields.forEach { item ->

                                        SignupTextField(item)

                                        Spacer(modifier = Modifier.height(25.dp))
                                    }

                                    Spacer(modifier = Modifier.height(25.dp))

                                    Column(
                                        verticalArrangement = Arrangement.Bottom,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        MyButton(
                                            loading = loading,
                                            backgroundcolor = if(state) Color.DarkOrange else Color.LightGray,
                                            fontcolor = Color.White,
                                            horizontalPadding = 40.dp,
                                            title = "Create account"
                                        ){ if(state && isNetworkAvailable){ viewModel.onSignUpClicked() } }

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
                                                text = "Already have an account?",
                                                style = MaterialTheme.typography.titleLarge,
                                                color = Color.Black,
                                                fontSize = 15.sp
                                            )

                                            Text(
                                                text = "Login",
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
                                                            navigationController.navigate(Screens.LoginScreen.screen)
                                                        }else{
                                                            viewModel.snackbarError("Network error")
                                                        }
                                                    }
                                            )
                                        }
                                    }
                                }
                            }

                            SignUpScreens.OptionalDataScreen -> {
                                item {
                                    Spacer(modifier = Modifier.height(50.dp))
                                    Text(
                                        text = "Complete your profile",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 25.sp
                                    )
                                    Spacer(modifier = Modifier.height(50.dp))
                                    SignupTextFieldPage2(
                                        viewModel.phonenumberstate,
                                        viewModel.addressstate
                                    )
                                    Spacer(modifier = Modifier.height(25.dp))

                                    MyButton(
                                        loading = loading,
                                        backgroundcolor = Color.DarkOrange,
                                        fontcolor = Color.White,
                                        horizontalPadding = 40.dp,
                                        title = "Sign Up"
                                    ){
                                        if(clickState.value){

                                            clickState.value = false

                                            viewModel.onFinishAccountClicked(

                                                onSuccess = {
                                                    navigationController.navigate(Screens.DashboardScreen.screen) {
                                                        popUpTo(0) { inclusive = true }
                                                    }
                                                },

                                                onField = {
                                                    clickState.value = true
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(70.dp)) }
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