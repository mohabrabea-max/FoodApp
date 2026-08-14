package com.example.applicationhome.features.profile.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.theme.BrandBlue
import com.example.applicationhome.core.ui.theme.DeepMatteBlack
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.ProfileEditResult

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity",
    "UnrememberedMutableState"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navigationController : NavHostController,
    profileViewModel: ProfileViewModel
){
    val loading by profileViewModel.loading.collectAsStateWithLifecycle()

    val editeProfileError by profileViewModel.editeProfileError.collectAsStateWithLifecycle()

    val selectedDate by profileViewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedGovernorate by profileViewModel.selectedGovernorate.collectAsStateWithLifecycle()
    val selectedCity by profileViewModel.selectedCity.collectAsStateWithLifecycle()

    val filteredGovernoratesList by profileViewModel.filteredGovernoratesList.collectAsStateWithLifecycle()
    val filteredCitiesList by profileViewModel.filteredCitiesList.collectAsStateWithLifecycle()

    val isButtonClicked by profileViewModel.isButtonClicked.collectAsStateWithLifecycle()

    val isDataEdited by profileViewModel.isDataEdited.collectAsStateWithLifecycle()

    val profileTextFields = profileViewModel.profileTextFields

    val searchString by profileViewModel.searchString.collectAsStateWithLifecycle()

    val buttonColor = if(isDataEdited) Color.BrandBlue else Color.LightGray

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
                    modifier = Modifier.fillMaxWidth().height(100.dp).shadow(elevation = 5.dp),
                    "Profile",
                    Color.DeepMatteBlack,
                    {
                        IconButton(
                            onClick = {
                                if (navigationController.previousBackStackEntry != null) {
                                    navigationController.popBackStack()
                                }
                            },
                            modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.DeepMatteBlack
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            BottomBarForProfileScreen(
                loading,
                buttonColor,
                isDataEdited
            ){
                profileViewModel.editeProfile()
            }
        }
    ){ paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(Color.VeryLightGray).padding(paddingValues)){
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
                                editeProfileError ?: ProfileEditResult.NetworkError
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    BirthdayDialog(
                        selectedDate,
                        { date ->
                            profileViewModel.selectDate(date)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SelectionOutlinedTextField(
                        "Governorate",
                        selectedGovernorate,
                        { showGovernorateBottomSheet = true }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SelectionOutlinedTextField(
                        "City",
                        selectedCity,
                        { showCityBottomSheet = true },
                        selectedGovernorate.isNotEmpty()
                    )
                }
            }
        }

        //          -----------------------------------------------\\ Bottom Sheets //-----------------------------------------------


        if(showGovernorateBottomSheet){
            SelectBottomSheet(
                "Search Governorates",
                searchString,
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
                { profileViewModel.searchFilter(it) }
            )
        }

        if(showCityBottomSheet){
            SelectBottomSheet(
                "Search Cities",
                searchString,
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
                { profileViewModel.searchFilter(it) }
            )
        }
    }
}





@Composable
fun BottomBarForProfileScreen(
    loading : Boolean,
    buttonColor : Color,
    isDataEdited : Boolean,
    action : () -> Unit
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
        MyButton(
            loading,
            buttonColor,
            Color.White,
            40.dp,
            "Save edites"
        ) {
            if (isDataEdited) {
                action()
            }
        }
    }
}