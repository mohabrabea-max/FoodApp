package com.example.applicationhome.features.signupscreen.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.Screens
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    navigationController : NavHostController,
    signUpViewModel : SignUpViewModel
){
    val signupPages by signUpViewModel.signupPages.collectAsStateWithLifecycle()

    val loading by signUpViewModel.loading.collectAsState()
    val isLogin by signUpViewModel.isEmailChecked.collectAsState()

    val page by signUpViewModel.signupPages.collectAsStateWithLifecycle()
    val state by signUpViewModel.bottonState.collectAsStateWithLifecycle()

    DisposableEffect(Unit){
        onDispose {
            signUpViewModel.clearFields()
        }
    }
    Scaffold(
        modifier = Modifier.navigationBarsPadding().fillMaxSize(),
        topBar = {
            MyTopBar(
                Color.DarkOrange.copy(alpha = 0f),
                modifier = Modifier.fillMaxWidth().height(100.dp),
                null,
                Color.White,
                {
                    IconButton(
                        onClick = {
                            if (signupPages == 1) {
                                if (navigationController.previousBackStackEntry != null) {
                                    navigationController.popBackStack()
                                }
                            } else {
                                signUpViewModel.lastPage()
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
                    text = "Sign Up",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(710.dp)
                    .clip(shape = RoundedCornerShape(topStart = 100.dp))
                    .background(Color.VeryLightGray),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                item {
                    if(signupPages == 1){
                        Spacer(modifier = Modifier.height(70.dp))
                        NameTextField(
                            signUpViewModel.firstnamestate,
                            signUpViewModel.lastnamestate,
                            { signUpViewModel.bottonstate() }
                        )
                        Spacer(modifier = Modifier.height(25.dp))
                        SignupTextField(
                            signUpViewModel.emailstate,
                            signUpViewModel.passwordstate,
                            signUpViewModel.confirmpasswordstate,
                            { signUpViewModel.bottonstate() }
                        )
                        Spacer(modifier = Modifier.height(25.dp))
                    }else{
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
                            signUpViewModel.phonenumberstate,
                            signUpViewModel.addressstate
                        )
                        Spacer(modifier = Modifier.height(25.dp))
                    }

                    Spacer(modifier = Modifier.height(25.dp))
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        SignUpButton(
                            page,
                            state,
                            loading,
                            isLogin,
                            { signUpViewModel.nextPage() },
                            { signUpViewModel.createAccount() },
                            { signUpViewModel.signUpButton() },
                            { navigationController.popBackStack() }
                        )

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
                            TextButton(
                                onClick = {navigationController.navigate(Screens.LoginScreen.screen)},
                                contentPadding = PaddingValues(start = 0.dp),
                            ){
                                Text(
                                    text = "Login",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.DarkOrange,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(70.dp)) }
            }
        }
    }
}

@Composable
fun SignUpButton(
    page : Int,
    state : Boolean,
    loading : Boolean,
    isLogin : Boolean,
    nextPage : () -> Unit,
    createAccount : () -> Unit,
    signUpButton : () -> Unit,
    backStack : () -> Unit
){
    val clickState = remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(isLogin){
        if(isLogin){
            nextPage()
        }
    }
    Box(
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp)
            .height(50.dp)
            .fillMaxWidth()
            .clip(CircleShape)
            .background(if(state || page == 2) Color.DarkOrange else Color.Gray)
            .clickable {
                if(state && page == 1){
                    createAccount()
                }else if(page == 2 && clickState.value){
                    scope.launch {
                        clickState.value = false
                        signUpButton()
                        backStack()
                    }

                }
            },
        contentAlignment = Alignment.Center
    ){
        if(loading){
            CircularProgressIndicator(
                color = Color.White
            )
        }else{
            Text(
                text = if(page == 1) "Next" else "Sign Up",
                style = MaterialTheme.typography.bodyLarge,
                color = if(state || page == 2) Color.White else Color.Black,
                fontSize = 18.sp
            )
        }
    }
}