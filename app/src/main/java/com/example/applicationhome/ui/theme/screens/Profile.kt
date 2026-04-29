package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.text.style.TextAlign
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
import com.example.applicationhome.ui.theme.components.MyBottonBar2
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.view.model.BottomBarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(scrollBehavior: TopAppBarScrollBehavior, drawerState : DrawerState, coroutineScope : CoroutineScope, navigationController : NavHostController, viewModelForBottomBar: BottomBarViewModel){
    val context = LocalContext.current.applicationContext
    val profile = ProfileData.profileData()
    Scaffold(
        modifier = Modifier.
        fillMaxSize().
        background(Color.White),
        topBar = {
            MyTopBar(
                "Profile",
                {coroutineScope.launch{drawerState.open()}},
                {Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.Black)},
                {
                    navigationController.navigate(Screens.Notifications.screen){
                        popUpTo(navigationController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true

                        restoreState = true
                    }
                },
                Icons.Default.Notifications,
                {
                    navigationController.navigate(Screens.Settings.screen){
                        popUpTo(navigationController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true

                        restoreState = true
                    }
                },
                Icons.Default.Settings
            )
        }
    ){
        Box(modifier = Modifier.background(Color.White)){
            LazyColumn(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.spacedBy(16.dp)){
                item{Spacer(modifier = Modifier.height(80.dp))}
                item{
                    Column(
                        modifier = Modifier.fillMaxSize().background(Color.LightOrange),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ){
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(modifier = Modifier.size(150.dp).clip(CircleShape),contentAlignment = Alignment.Center){
                            Image(
                                painter = painterResource(id = R.drawable.myphoto),
                                contentDescription = "Food Logo",
                                modifier = Modifier.
                                fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "Mohab Rabea",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider()
                    }
                }
                items(profile){item ->
                    NavigationDrawerItem(
                        label = {
                            Column {
                                Text(text = item.title, fontSize = 20.sp, color = Color.BrownForFont)
                                Text(text = item.value, fontSize = 13.sp, color = Color.MediumBrownForTitle)
                            }
                        },
                        selected = false,
                        icon = {Icon(modifier = Modifier.size(30.dp), imageVector = item.icon, contentDescription = item.title, tint = Color.BrownForFont)},
                        onClick = {}
                    )
                    Spacer(modifier = Modifier.height(17.dp))
                    Divider()
                }
                item{
                    Box(modifier = Modifier.fillMaxSize().padding(50.dp), contentAlignment = Alignment.Center){
                        IconButton(modifier = Modifier.size(50.dp),onClick = {Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()}){
                            Icon(modifier = Modifier.size(50.dp), imageVector = Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.Red)
                        }
                    }
                }
                item{Spacer(modifier = Modifier.height(90.dp))}
            }
            Column(modifier = Modifier.align(Alignment.BottomCenter)){
                Box(modifier = Modifier.fillMaxWidth().height(60.dp).pointerInput(Unit) { detectTapGestures { } }, contentAlignment = Alignment.Center){
                    MyBottonBar2(navigationController, viewModelForBottomBar)
                }
                Box(modifier = Modifier.fillMaxWidth().height(20.dp).pointerInput(Unit) { detectTapGestures { } }, contentAlignment = Alignment.Center){}
            }
        }
    }
}