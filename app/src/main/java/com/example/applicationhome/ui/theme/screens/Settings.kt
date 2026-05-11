package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.MyTopBar
import com.example.applicationhome.ui.theme.components.SettingsBox
import com.example.applicationhome.ui.theme.components.SettingsOptionsBox
import com.example.applicationhome.ui.theme.components.UserImage
import com.example.applicationhome.view.model.UserImageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(drawerState : DrawerState, coroutineScope : CoroutineScope, navigationController : NavHostController, userImageViewModel: UserImageViewModel){
    val scrollState = rememberLazyGridState()
    val profileoptions = ProfileData.profileOptions()
    val context = LocalContext.current as? Activity
    BackHandler(enabled = true) {
        // ده بيمسح الأبلكيشن من الـ Background ويقفله تماماً
        context?.finishAffinity()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.clip(RoundedCornerShape(30.dp)).shadow(elevation = 3.dp)){
                MyTopBar(
                    Color.White,
                    modifier = Modifier.
                    fillMaxWidth().
                    height(100.dp),
                    title = "Settings",
                    {
                        IconButton(
                            onClick = {coroutineScope.launch{drawerState.open()}},
                            modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                        ) {
                            Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.Black)
                        }
                    },
                    {
                        IconButton(onClick = {}){
                            Icon(
                                Icons.Default.DarkMode,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    }
                )
                Column(
                    modifier = Modifier.fillMaxWidth().
                    height(90.dp).
                    background(Color.LightGray).
                    clickable { navigationController.navigate(Screens.Profile.screen) }
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
                            UserImage(userImageViewModel)
                        }
                        Spacer(modifier = Modifier.width(15.dp))
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
                }
            }
        }
    ){
        Column(modifier = Modifier.statusBarsPadding().background(Color.VeryLightGray).padding(10.dp)){
            LazyVerticalGrid(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.Center
            ){
                item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(150.dp))}
                items(profileoptions){item ->
                    SettingsOptionsBox(item, navigationController)
                }
                item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(5.dp))}
                item(span = { GridItemSpan(2) }){
                    Row(modifier = Modifier.padding(start = 5.dp), horizontalArrangement = Arrangement.Start){
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                            fontSize = 18.sp,
                        )
                    }
                }
                item(span = { GridItemSpan(2) }){
                    SettingsBox()
                }
                item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(80.dp))}
            }
        }
    }
}