package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DeepMatteBlack
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.bars.MyTopBar
import com.example.applicationhome.ui.theme.components.profileAndSetting.DateModalBottomSheet
import com.example.applicationhome.ui.theme.components.profileAndSetting.EditProfileTextField
import com.example.applicationhome.ui.theme.model.ProfileViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity",
    "UnrememberedMutableState"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navigationController : NavHostController,
    profileViewModel: ProfileViewModel
){
    val selectedDay by profileViewModel.selectedDay.collectAsStateWithLifecycle()
    val selectedMonth by profileViewModel.selectedMonth.collectAsStateWithLifecycle()
    val selectedYear by profileViewModel.selectedYear.collectAsStateWithLifecycle()

    val isButtonClicked by profileViewModel.isButtonClicked.collectAsStateWithLifecycle()

    val isDataEdited by profileViewModel.isDataEdited.collectAsStateWithLifecycle()

    val sheetStateViewModel by profileViewModel.sheetState.collectAsStateWithLifecycle()

    val profileTextFields = profileViewModel.profileTextFields
    val profileSelection = profileViewModel.profileSelection


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
                        IconButton(
                            onClick = {
                                if(isDataEdited) profileViewModel.editeProfile()
                            },
                            enabled = isDataEdited
                        ){
                            Icon(
                                Icons.Default.Done,
                                contentDescription = null,
                                tint = if(!isDataEdited) Color.Gray else Color.Blue
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
                Column(modifier = Modifier.padding(15.dp).fillMaxSize().clip(RoundedCornerShape(10.dp)).background(Color.White).padding(17.dp)){

                    Text(text = "Personal Information", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                    Spacer(modifier = Modifier.height(15.dp))

                    profileTextFields.forEach{ item ->
                        Column(modifier = Modifier.fillMaxSize()){

                            Spacer(modifier = Modifier.height(15.dp))

                            EditProfileTextField(
                                item,
                                isButtonClicked,
                                { profileViewModel.isDataChanged() }
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Divider(color = Color.LightGray.copy(alpha = 0.2f))
                        }
                    }

                    profileSelection.forEach{ item ->
                        Column(modifier = Modifier.fillMaxSize()){

                            Spacer(modifier = Modifier.height(15.dp))

                            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(
                                    modifier = Modifier.weight(7f),
                                    horizontalAlignment = Alignment.Start
                                ){
                                    Text(
                                        text = item.title,
                                        fontSize = 17.sp,
                                        color = Color.BrownForFont
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))
                                }

                                IconButton(
                                    modifier = Modifier.weight(1f),
                                    onClick = { profileViewModel.stateTrue() }
                                ){
                                    Icon(
                                        modifier = Modifier.size(22.dp),
                                        imageVector = item.icon,
                                        contentDescription = item.title,
                                        tint = Color.Blue
                                    )
                                }
                            }

                            if(item != profileSelection.last()) Spacer(modifier = Modifier.height(15.dp))
                            else Spacer(modifier = Modifier.height(5.dp))

                            if(item != profileSelection.last()) Divider(color = Color.LightGray.copy(alpha = 0.2f))
                        }
                    }
                }
                if(sheetStateViewModel){
                    DateModalBottomSheet(
                        selectedDay,
                        selectedMonth,
                        selectedYear,
                        { profileViewModel.stateFalse() },
                        { profileViewModel.setDay(it) },
                        { profileViewModel.setMonth(it) },
                        { profileViewModel.setYear(it) },
                        { profileViewModel.birthday(selectedDay, selectedMonth, selectedYear) }
                    )
                }
            }
        }
    }
}