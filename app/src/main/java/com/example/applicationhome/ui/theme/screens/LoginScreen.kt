package com.example.applicationhome.ui.theme.screens

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.data.models.model.Screens
import com.example.applicationhome.data.models.repository.UserRepository
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.LoginTextField
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.view.model.LoginViewModel
import com.example.applicationhome.view.model.UserImageViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navigationController : NavHostController,
    loginViewModel: LoginViewModel,
    userImageViewModel: UserImageViewModel = viewModel()
){
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            MyTopBar(
                Color.DarkOrange.copy(alpha = 0f),
                modifier = Modifier.
                fillMaxWidth().
                height(100.dp),
                null,
                {
                    IconButton(
                        onClick = {if (navigationController.previousBackStackEntry != null) { navigationController.popBackStack() } },
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
                Spacer(modifier = Modifier.height(100.dp))
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(Color.Black)
                ){
                    Text(
                        text = "Food",
                        color = Color.White,
                        fontSize = 40.sp,
                        modifier = Modifier.align(Alignment.Center)
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
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 35.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                LoginTextField(loginViewModel)

                TextButton(
                    onClick = {},
                    contentPadding = PaddingValues(horizontal = 5.dp),
                    modifier = Modifier.padding(start = 40.dp).align(Alignment.Start)
                ){
                    Text(
                        text = "Forget your password",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.DarkOrange,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                LoginButton(loginViewModel, navigationController)

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
                    TextButton(
                        onClick = {navigationController.navigate(Screens.SignUpScreen.screen)},
                        contentPadding = PaddingValues(start = 0.dp),
                        modifier = Modifier.padding(start = 3.dp)
                    ){
                        Text(
                            text = "Sign Up",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.DarkOrange,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginButton(loginViewModel: LoginViewModel, navigationController: NavHostController){
    var isEmailTrue = loginViewModel.isEmailTrue
    var isPasswordTrue = loginViewModel.isPasswordTrue
    Box(
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp)
            .height(50.dp)
            .fillMaxWidth()
            .clip(CircleShape)
            .background(if(loginViewModel.emailstate.text.isNotEmpty() && loginViewModel.passwordstate.text.isNotEmpty()) Color.DarkOrange else Color.Gray)
            .clickable {
                if(isEmailTrue && isPasswordTrue){
                    println("true")
                    navigationController.navigate(Screens.HomeScreen.screen) {navigationController.popBackStack()}
                    loginViewModel.login(UserRepository.userData, UserRepository.userId)
                    loginViewModel.bottonstate()
                }else if(isEmailTrue && isPasswordTrue == false){
                    println("true false")
                }else{
                    println("false false")
                }
            },
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "Login",
            style = MaterialTheme.typography.bodyLarge,
            color = if(loginViewModel.emailstate.text.isNotEmpty() && loginViewModel.passwordstate.text.isNotEmpty()) Color.White else Color.Black,
            fontSize = 18.sp
        )
    }
}