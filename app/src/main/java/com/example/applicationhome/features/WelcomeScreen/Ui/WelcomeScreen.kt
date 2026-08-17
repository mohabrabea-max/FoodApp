package com.example.applicationhome.features.WelcomeScreen.Ui

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.theme.BrownForFont
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.Screens

@SuppressLint("ContextCastToActivity")
@Composable
fun WelcomeScreen(
    viewModel : WelcomeScreenViewModel,
    navigationController : NavHostController
){
    val interactionSource = remember { MutableInteractionSource() }

    val context = LocalContext.current as? Activity
    BackHandler(enabled = true) { context?.finishAffinity() }


    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f)
            ) {
                EmbeddedVideoPlayer(
                    videoResId = R.raw.appintrovideo,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CurvedBottomShape()),
                )

                // شريط العلم وزر Skip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // زر Skip
                    Text(
                        text = "Skip",
                        color = Color.White,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ){
                                viewModel.updateFirstTimeToOpenApp()

                                navigationController.navigate(Screens.DashboardScreen.screen) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                    )
                }

                // نص Welcome to و بادج talabat
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome to",
                        color = Color.White,
                        fontSize = 47.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(35.dp))
                }
            }

            // 2️⃣ الجزء السفلي: الوصف والأزرار
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    text = "Log in or sign up to save more,\nshop faster, and get personalized perks",
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    color = Color.BrownForFont,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(15.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MyOutlinedButton(
                        title = "Login to an existing account",
                        icon = null
                    ){
                        viewModel.updateFirstTimeToOpenApp()
                        navigationController.navigate(Screens.LoginScreen.screen) {
                            popUpTo(0) { inclusive = true }
                        }
                    }

                    MyOutlinedButton(
                        title = "Create account",
                        icon = null
                    ){
                        viewModel.updateFirstTimeToOpenApp()
                        navigationController.navigate(Screens.SignUpScreen.screen) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(70.dp))

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.DarkOrange),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Mohab\nFood App",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }

    }
}

fun CurvedBottomShape() = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height - 70f)
    // رسم منحنى بـ QuadTo
    quadraticBezierTo(
        size.width / 2f, size.height + 70f, // نقطة التحكم للأسفل
        0f, size.height - 70f
    )
    close()
}

@Composable
fun MyOutlinedButton(
    title : String,
    icon : ImageVector?,
    contentColor : Color = Color.Black,
    onClick : () -> Unit
){
    OutlinedButton(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = contentColor)
    ) {
        if(icon != null){
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))
        }

        Text(
            text = title,
            color = contentColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}