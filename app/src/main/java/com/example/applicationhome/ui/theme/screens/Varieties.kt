package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.data.models.VarietiesMenu
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.view.model.FavoriteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Varieties(
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    navigationController : NavHostController,
    favoriteState : FavoriteViewModel
){
    val categories = VarietiesMenu.categoriesList()
    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.LightBrownForBackground),
        topBar = {
            MyTopBar(
                Color.DarkOrange,
                modifier = Modifier.
                fillMaxWidth().
                height(100.dp).
                shadow(elevation = 5.dp),
                "Home",
                {
                    IconButton(
                        onClick = {coroutineScope.launch{drawerState.open()}},
                        modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                    ) {
                        Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.White)
                    }
                },
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
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
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
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                    }
                }
            )
            Divider(color = Color.LightGray.copy(alpha = 0.5f))
        }
    ){
        Box(modifier = Modifier.fillMaxSize().background(Color.VeryLightGray)){
            LazyColumn(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.spacedBy(5.dp)){
                item{Spacer(modifier = Modifier.height(100.dp))}
                items(categories){item ->
                    Surface(modifier = Modifier.aspectRatio(3f).padding(5.dp).clip(RoundedCornerShape(10.dp)).clickable{}){
                        Row(modifier = Modifier.background(Color.White), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                            Image(
                                modifier = Modifier.weight(1f),
                                painter = painterResource(id = item.image),
                                contentDescription = item.name
                            )
                            VerticalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                            Text(modifier = Modifier.weight(1.5f).padding(10.dp), text = item.name, fontSize = 20.sp, color = Color.BrownForFont)
                            Favorite3(modifier = Modifier.weight(0.5f).fillMaxSize().padding(10.dp).clip(CircleShape).background(Color.VeryLightGray), favoriteState = favoriteState)
                        }
                    }
                }
                item{Spacer(modifier = Modifier.height(80.dp))}
            }
        }
    }
}