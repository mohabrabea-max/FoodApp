package com.example.applicationhome

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.applicationhome.ui.theme.BackgroundForCards
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.screens.HomeScreen

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            FinalScreen(scrollBehavior)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalScreen(scrollBehavior: TopAppBarScrollBehavior){
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LearnTopAppBar(scrollBehavior)
        }
    ){innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){HomeScreen()}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnTopAppBar(scrollBehavior: TopAppBarScrollBehavior){
    val context = LocalContext.current.applicationContext
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = {Text(text = "Home")},
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = {Toast.makeText(context, "Home", Toast.LENGTH_SHORT).show()}){
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Add",
                    tint = Color.BrownForFont,

                    )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.BackgroundForCards,scrolledContainerColor = Color.BackgroundForCards, titleContentColor = Color.BrownForFont, navigationIconContentColor = Color.BrownForFont),
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