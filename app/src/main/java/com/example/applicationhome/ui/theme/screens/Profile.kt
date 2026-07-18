package com.example.applicationhome.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.data.data.model.ProfileEditResult
import com.example.applicationhome.ui.theme.BrandBlue
import com.example.applicationhome.ui.theme.DeepMatteBlack
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.components.bars.MyTopBar
import com.example.applicationhome.ui.theme.components.profileAndSetting.BirthdayDialog
import com.example.applicationhome.ui.theme.components.profileAndSetting.EditProfileTextField
import com.example.applicationhome.ui.theme.components.profileAndSetting.SelectBottomSheet
import com.example.applicationhome.ui.theme.components.profileAndSetting.SelectionOutlinedTextField
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
    val interactionSource = remember { MutableInteractionSource() }


    val errors = remember { mutableStateOf<ProfileEditResult>(ProfileEditResult.Success) }

    val selectedDate by profileViewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedGovernorate by profileViewModel.selectedGovernorate.collectAsStateWithLifecycle()
    val selectedCity by profileViewModel.selectedCity.collectAsStateWithLifecycle()

    val filteredGovernoratesList by profileViewModel.filteredGovernoratesList.collectAsStateWithLifecycle()
    val filteredCitiesList by profileViewModel.filteredCitiesList.collectAsStateWithLifecycle()

    val isButtonClicked by profileViewModel.isButtonClicked.collectAsStateWithLifecycle()

    val isDataEdited by profileViewModel.isDataEdited.collectAsStateWithLifecycle()

    val profileTextFields = profileViewModel.profileTextFields
    val profileSelection = profileViewModel.profileSelection

    val buttonColor = if(isDataEdited) Color.BrandBlue else Color.LightGray
    val buttonFontColor = if(isDataEdited) Color.White else Color.Black

    var showGovernorateBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showCityBottomSheet by rememberSaveable { mutableStateOf(false) }


    Scaffold(
        containerColor = Color.White,
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

                            Spacer(modifier = Modifier.height(10.dp))

                            EditProfileTextField(
                                item,
                                isButtonClicked,
                                errors.value,
                                { profileViewModel.isDataChanged() }
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    BirthdayDialog(
                        selectedDate,
                        { date ->
                            profileViewModel.selectDate(date)
                        },
                        { profileViewModel.isDataChanged() }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SelectionOutlinedTextField(
                        "Governorate",
                        selectedGovernorate,
                        { showGovernorateBottomSheet = true },
                        { profileViewModel.isDataChanged() }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SelectionOutlinedTextField(
                        "City",
                        selectedCity,
                        { showCityBottomSheet = true },
                        { profileViewModel.isDataChanged() },
                        selectedGovernorate.isNotEmpty()
                    )
                }
            }

            item{ Spacer(modifier = Modifier.height(90.dp)) }
        }
        if(showGovernorateBottomSheet){
            SelectBottomSheet(
                "Search Governorates",
                filteredGovernoratesList,
                { showGovernorateBottomSheet = false },
                {
                    profileViewModel.selectGovernorate(it)
                    showGovernorateBottomSheet = false
                },
                {
                    profileViewModel.unselectGovernorate()
                    showGovernorateBottomSheet = false
                },
                { profileViewModel.filterCities(it) }
            )
        }

        if(showCityBottomSheet){
            SelectBottomSheet(
                "Search Cities",
                filteredCitiesList,
                { showCityBottomSheet = false },
                {
                    profileViewModel.selectCity(it)
                    showCityBottomSheet = false
                },
                {
                    profileViewModel.unselectCity()
                    showCityBottomSheet = false
                },
                { profileViewModel.filterCities(it) }
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){
            Box(
                modifier = Modifier.fillMaxWidth().
                height(80.dp).
                shadow(elevation = 7.dp).
                background(Color.White).
                pointerInput(Unit) {
                    detectTapGestures { }
                }.
                padding(horizontal = 15.dp).
                navigationBarsPadding(),
                contentAlignment = Alignment.Center
            ){
                Box(
                    modifier = Modifier.fillMaxWidth(0.8f).
                    height(50.dp).
                    clip(RoundedCornerShape(50.dp)).
                    background(buttonColor).
                    clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        if(isDataEdited){
                            errors.value = profileViewModel.editeProfile()
                        }
                    }.
                    padding(15.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Save edites",
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = buttonFontColor,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}