package com.example.applicationhome.ui.theme.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.data.repository.ProfileData
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DeepMatteBlack
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.bars.MyTopBar
import com.example.applicationhome.ui.theme.components.profileAndSetting.SettingsBox
import com.example.applicationhome.ui.theme.components.profileAndSetting.SettingsOptionsBox
import com.example.applicationhome.ui.theme.components.profileAndSetting.UserImage
import com.example.applicationhome.ui.theme.model.SettingsViewModel
import com.example.applicationhome.ui.theme.model.UserImageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    navigationController : NavHostController,
    userImageViewModel: UserImageViewModel,
    settingsListState : LazyGridState,
    settingsViewModel : SettingsViewModel
){
    val userData by settingsViewModel.userData.collectAsStateWithLifecycle()

    val description = userData.phonenumber.ifEmpty { userData.email }

    val interactionSource = remember { MutableInteractionSource() }

    val profileoptions = ProfileData.profileOptions()
    val context = LocalContext.current as? Activity

    BackHandler(enabled = true) {
        // ده بيمسح الأبلكيشن من الـ Background ويقفله تماماً
        context?.finishAffinity()
    }

    Scaffold(
        modifier = Modifier.navigationBarsPadding().fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.clip(RoundedCornerShape(30.dp)).shadow(elevation = 3.dp)){
                MyTopBar(
                    Color.White,
                    modifier = Modifier.
                    fillMaxWidth().
                    height(100.dp),
                    "Settings",
                    Color.DeepMatteBlack,
                    {
                        IconButton(
                            onClick = {coroutineScope.launch{drawerState.open()}},
                            modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                        ) {
                            Icon(painterResource(id = R.drawable.custom_menu), contentDescription = null, tint = Color.DeepMatteBlack)
                        }
                    },
                    {
                        IconButton(onClick = {}){
                            Icon(
                                Icons.Default.DarkMode,
                                contentDescription = null,
                                tint = Color.DeepMatteBlack
                            )
                        }
                    }
                )
                Column(
                    modifier = Modifier.fillMaxWidth().
                    height(90.dp).
                    background(Color.LightGray).
                    clickable (
                        interactionSource = interactionSource,
                        indication = null
                    ){ navigationController.navigate(Screens.Profile.screen) }
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
                                text = "${userData.firstname} ${userData.lastname}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.BrownForFont
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = description,
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
                state = settingsListState,
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