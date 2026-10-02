package com.example.applicationhome.features.ordersuccessscreen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.designsystem.SquerButton

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Preview
@Composable
fun OrderSuccess(navigation : () -> Unit = {}){
    BackHandler(enabled = true){
        navigation()
    }

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),

        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            ){
                SquerButton(
                    modifier = Modifier.fillMaxWidth(),
                    loading = false,
                    backgroundcolor = Color(0xFF00B050),
                    fontcolor = Color.White,
                    horizontalPadding = 40.dp,
                    title = stringResource(R.string.done)
                ){
                    navigation()
                }
            }
        }
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_order_success),
                contentDescription = "Success Icon",
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.order_placed_successfully),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}