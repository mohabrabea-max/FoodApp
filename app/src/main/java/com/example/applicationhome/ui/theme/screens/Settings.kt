package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.data.models.ProfileData
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightBackgroundForCards
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.components.MyTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(drawerState : DrawerState, coroutineScope : CoroutineScope, navigationController : NavHostController){
    val context = LocalContext.current.applicationContext
    val settings = ProfileData.settingsata()
    Scaffold(
        modifier = Modifier.background(Color.LightBrownForBackground).
        fillMaxSize(),
        topBar = { MyTopBar({coroutineScope.launch{drawerState.open()}}, {Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.Black)}, {navigationController.navigate(Screens.Notifications.screen)}, Icons.Default.Notifications, {navigationController.navigate(Screens.Search.screen)}, Icons.Default.Search) },
    ){ innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(Color.LightBrownForBackground).padding(innerPadding),verticalArrangement = Arrangement.spacedBy(16.dp)){
            item{
                Box(
                    modifier = Modifier.
                    height(120.dp).
                    background(Color.LightBackgroundForCards).
                    clickable{
                        coroutineScope.launch{drawerState.close()}
                        navigationController.navigate(Screens.Profile.screen)
                    }
                ){
                    Row(
                        modifier = Modifier.fillMaxSize().padding(10.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier.size(70.dp).
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
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.BrownForFont
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "01011223344",
                                fontSize = 18.sp,
                                color = Color.MediumBrownForTitle
                            )
                        }
                        VerticalDivider()
                        Box(modifier = Modifier.weight(1f).fillMaxSize().clickable{Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()}){
                            Icon(modifier = Modifier.size(50.dp).align(Alignment.Center), imageVector = Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.Red)
                        }
                    }

                }
                Divider()
            }
            items(settings){item ->
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
        }
    }
}