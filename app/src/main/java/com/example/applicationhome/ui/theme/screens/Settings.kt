package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.data.models.ProfileData
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightOrange
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.components.MyBottonBar
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.view.model.BottomBarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(drawerState : DrawerState, coroutineScope : CoroutineScope, navigationController : NavHostController, viewModelForBottomBar : BottomBarViewModel){
    val context = LocalContext.current.applicationContext
    val settings = ProfileData.settingsata()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Box(
                modifier = Modifier.navigationBarsPadding().fillMaxWidth().
                height(100.dp).
                pointerInput(Unit) { detectTapGestures { } },
                contentAlignment = Alignment.BottomCenter
            ){
                MyBottonBar(navigationController, viewModelForBottomBar)
            }
        },
        topBar = {
            Column {
                MyTopBar(
                    modifier = Modifier.
                    fillMaxWidth().
                    height(100.dp),
                    "Settings",
                    {navigationController.popBackStack()},
                    {Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)},
                    {
                        IconButton(onClick = {
                            navigationController.navigate(Screens.Notifications.screen){
                                popUpTo(navigationController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true

                                restoreState = true
                            }
                        }) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.Black)
                        }
                        IconButton(onClick = {
                            navigationController.navigate(Screens.Search.screen){
                                popUpTo(navigationController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true

                                restoreState = true
                            }
                        }) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                        }
                    }
                )
                Column(
                    modifier = Modifier.fillMaxWidth().
                    height(90.dp).
                    background(Color.LightOrange).
                    clickable{
                        coroutineScope.launch{drawerState.close()}
                        navigationController.navigate(Screens.Profile.screen)
                    }
                ){
                    Row(
                        modifier = Modifier.fillMaxSize().padding(start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier.size(50.dp).
                            clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.myphoto),
                                contentDescription = "My Photo",
                                modifier = Modifier.
                                fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(2.5f),horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center){
                            Text(
                                text = "Mohab Rabea",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.BrownForFont
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "01011223344",
                                fontSize = 15.sp,
                                color = Color.MediumBrownForTitle
                            )
                        }
                    }
                    Divider()
                }
            }
        },
    ){
        LazyColumn {
            item {
                Column(modifier = Modifier.fillMaxSize().background(Color.LightGray.copy(alpha = 0.3f)).padding(15.dp)){
                    Spacer(modifier = Modifier.height(190.dp))
                    Column(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp)).background(Color.White).padding(17.dp)){
                        settings.forEach{ item ->
                            Column(modifier = Modifier.fillMaxSize().clickable{ }){
                                Spacer(modifier = Modifier.height(25.dp))
                                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically){
                                    Icon(modifier = Modifier.size(20.dp), imageVector = item.icon, contentDescription = item.title, tint = Color.Black)
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(text = item.title, fontSize = 17.sp, color = Color.BrownForFont)
                                    //Text(text = item.value, fontSize = 13.sp, color = Color.MediumBrownForTitle)
                                }
                                Spacer(modifier = Modifier.height(25.dp))
                                Divider(color = Color.LightGray.copy(alpha = 0.2f))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}