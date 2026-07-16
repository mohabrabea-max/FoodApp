package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.applicationhome.ui.theme.DeepMatteBlack
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.bars.MyTopBar
import com.example.applicationhome.ui.theme.components.profileAndSetting.ProfileBox
import com.example.applicationhome.ui.theme.model.ProfileViewModel
import com.example.applicationhome.ui.theme.model.UserImageViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity",
    "UnrememberedMutableState"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navigationController : NavHostController,
    userImageViewModel: UserImageViewModel,
    profileViewModel: ProfileViewModel
){
    var edite by mutableStateOf(false)

    var stat = userImageViewModel.stat

    var pading = if(stat) 210.dp else 0.dp

    Scaffold(
        modifier = Modifier.navigationBarsPadding().
        fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.shadow(elevation = 3.dp)){
                MyTopBar(
                    Color.White,
                    modifier = Modifier.
                    fillMaxWidth().
                    height(100.dp).
                    shadow(elevation = 5.dp),
                    "Profile",
                    Color.DeepMatteBlack,
                    {
                        IconButton(
                            onClick = {if (navigationController.previousBackStackEntry != null) { navigationController.popBackStack() } },
                            modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.DeepMatteBlack)
                        }
                    },
                    actions = {
                        IconButton(onClick = {}){
                            Icon(
                                Icons.Default.Done,
                                contentDescription = null,
                                tint = if(edite == false) Color.DeepMatteBlack else Color.Green
                            )
                        }
                    }
                )
            }
        }
    ){
        LazyColumn(modifier = Modifier.fillMaxSize().background(Color.VeryLightGray)){
            item{ Spacer(modifier = Modifier.height(120.dp)) }
            item {
                ProfileBox(userImageViewModel, profileViewModel)
            }
            item { Spacer(modifier = Modifier.height(pading)) }
        }
    }
}