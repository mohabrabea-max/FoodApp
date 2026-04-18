package com.example.applicationhome.ui.theme.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.applicationhome.data.models.BottomBar
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.BackgroundForCards
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightBackgroundForCards
import com.example.applicationhome.ui.theme.MediumBrownForTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottonBar(navController : NavController){
    val context = LocalContext.current.applicationContext
    val selected = remember{mutableStateOf(Icons.Default.Home)}
        val bottomBar = listOf(
            BottomBar("Home", Icons.Default.Home, Screens.HomeScreen.screen),
            BottomBar("Profile", Icons.Default.Person, Screens.Profile.screen),
            BottomBar("Add", Icons.Default.Add, ""),
            BottomBar("Search", Icons.Default.Search, Screens.Search.screen),
            BottomBar("Settings", Icons.Default.Settings, Screens.Settings.screen)
    )
    BottomAppBar(
        contentColor = Color.BrownForFont,
        containerColor = Color.BackgroundForCards
    ){
        bottomBar.forEach{item ->
            if(item.title != "Add"){
                IconButton(
                    onClick = {
                        selected.value = item.icon
                        navController.navigate(item.screens){popUpTo(0)}
                    },
                    modifier = Modifier.weight(1f)
                ){
                    Icon(
                        item.icon,
                        contentDescription = "Home",
                        modifier = Modifier.size(26.dp),
                        tint = if(selected.value == item.icon) Color.MediumBrownForTitle else Color.BrownForFont
                    )
                }
            }else{
                Box(modifier = Modifier.weight(1f).padding(16.dp), contentAlignment = Alignment.Center){
                    FloatingActionButton(onClick = {Toast.makeText(context, "Open Bottom Sheet", Toast.LENGTH_SHORT).show()}, containerColor = Color.LightBackgroundForCards){
                        Icon(Icons.Default.Add, "Add", tint = Color.BrownForFont)
                    }
                }
            }
        }
    }
}