package com.example.applicationhome

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.applicationhome.ui.theme.BackgroundForCards
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.screens.HomeScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            HomeScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnTopAppBar(){
    val context = LocalContext.current.applicationContext
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = {Text(text = "Home")},
        navigationIcon = {
            IconButton(onClick = {Toast.makeText(context, "Home", Toast.LENGTH_SHORT).show()}){
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Add",
                    tint = Color.BrownForFont,

                    )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.BackgroundForCards, titleContentColor = Color.BrownForFont, navigationIconContentColor = Color.BrownForFont),
        actions = {
            IconButton(onClick = {Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()}){
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.BrownForFont)
            }
            IconButton(onClick = {Toast.makeText(context, "More", Toast.LENGTH_SHORT).show()}){
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More", tint = Color.BrownForFont)
            }
        }
    )
}