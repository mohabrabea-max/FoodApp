package com.example.applicationhome.features.confirmorder.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.theme.BrandBlue
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.Screens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmOrderScreen(
    navigationController : NavHostController,
    confirmOrderScreenViewModel : ConfirmOrderScreenViewModel
){
    val loading by confirmOrderScreenViewModel.loading.collectAsStateWithLifecycle()
    val isButtonClicked by confirmOrderScreenViewModel.isButtonClicked.collectAsStateWithLifecycle()
    val confirmOrderPages by confirmOrderScreenViewModel.confirmOrderPages.collectAsStateWithLifecycle()

    val textFieldConfirmOrderScreenList = confirmOrderScreenViewModel.textFieldConfirmOrderScreenList

    val confirmOrderError by confirmOrderScreenViewModel.confirmOrderError.collectAsStateWithLifecycle()

    val cart by confirmOrderScreenViewModel.cartItems.collectAsStateWithLifecycle()

    val totalprice by confirmOrderScreenViewModel.totalPrice.collectAsStateWithLifecycle()

    val bottonState by confirmOrderScreenViewModel.bottonState.collectAsStateWithLifecycle()

    val clickState = rememberSaveable { mutableStateOf(true) }

    val locationState by confirmOrderScreenViewModel.locationState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // جلب الموقع
            confirmOrderScreenViewModel.fetchCurrentLocation()
        }
    }

    // طلب الإذن فور فتح الشاشة
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    BackHandler(enabled = true) {
        when(confirmOrderPages){
            0 -> {
                if (navigationController.previousBackStackEntry != null) {
                    navigationController.popBackStack()
                }

                confirmOrderScreenViewModel.cleanTextField()
                confirmOrderScreenViewModel.lastPage()
            }

            1 -> { confirmOrderScreenViewModel.lastPage() }

            else -> { confirmOrderScreenViewModel.changePageNumber(1) }
        }
    }

    val color = if(bottonState) Color.DarkOrange else Color.Gray
    val fontcolor = if(isButtonClicked) Color.White else Color.White


    Scaffold(
        modifier = Modifier.navigationBarsPadding().fillMaxSize(),
        topBar = {
            MyTopBar(
                if(confirmOrderPages in 1..<3) Color.DarkOrange
                else Color.White,
                modifier = Modifier.fillMaxWidth().height(100.dp).shadow(elevation = 5.dp),
                if(confirmOrderPages in 1..<3) "Checkout"
                else "Location",
                if(confirmOrderPages in 1..<3) Color.White
                else Color.Black,
                {
                    IconButton(
                        onClick = {
                            if (confirmOrderPages == 1) {
                                confirmOrderScreenViewModel.lastPage()

                            } else if(confirmOrderPages == 0){
                                if (navigationController.previousBackStackEntry != null) {
                                    navigationController.popBackStack()
                                }

                                confirmOrderScreenViewModel.cleanTextField()
                                confirmOrderScreenViewModel.lastPage()

                            }else{
                                confirmOrderScreenViewModel.changePageNumber(1)
                            }
                        },
                        modifier = Modifier.padding(5.dp).border(
                            width = 1.dp,
                            color = Color.LightGray.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(30.dp)
                        ).shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = CircleShape)
                            .clip(CircleShape).size(40.dp).background(Color.White)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
                    }
                },
            )
        }
    ){
        Box(modifier = Modifier.background(Color.White)) {
            Box(modifier = Modifier.fillMaxSize()) {
                if(confirmOrderPages == 1){
                    PageOneConfirmOrder(
                        textFieldConfirmOrderScreenList = textFieldConfirmOrderScreenList,
                        isButtonClicked = isButtonClicked,
                        confirmOrderError = confirmOrderError,
                        location = locationState.locationName,
                        bottonStateChange = { confirmOrderScreenViewModel.bottonStateChange() },
                        openMaps = { confirmOrderScreenViewModel.changePageNumber(3) }
                    )

                } else if(confirmOrderPages == 2) {
                    PageTowConfirmOrder(cart, totalprice)

                } else{
                    if(locationState.isLoading){
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                color = Color.DarkOrange,
                                strokeWidth = 4.dp
                            )
                        }

                    }else{
                        StreetMapPage(
                            initialLatitude = locationState.latitude,
                            initialLongitude = locationState.longitude,
                            onLocationSelected = { latitude, longitude ->
                                confirmOrderScreenViewModel.updateSelectedLocation(latitude, longitude)
                            },
                            changePage = { confirmOrderScreenViewModel.changePageNumber(it) }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .pointerInput(Unit) { detectTapGestures { } }
                        .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if(confirmOrderPages == 1){
                    MyButton(
                        loading,
                        color,
                        fontcolor,
                        40.dp,
                        "Save address"
                    ) {
                        if(bottonState) confirmOrderScreenViewModel.bottonstate()
                    }
                }else if(confirmOrderPages == 2){
                    MyButton(
                        loading,
                        Color.BrandBlue,
                        Color.White,
                        40.dp,
                        "Confirm order"
                    ) {
                        if (clickState.value) {
                            clickState.value = false
                            confirmOrderScreenViewModel.uploadOrder(
                                onSuccess = {
                                    confirmOrderScreenViewModel.clearAllCart()
                                    navigationController.navigate(Screens.DashboardScreen.screen) {
                                        popUpTo(Screens.DashboardScreen.screen) {
                                            inclusive = true
                                        }
                                        confirmOrderScreenViewModel.lastPage()
                                    }
                                }
                            )
                        }
                    }
                }else{

                }
            }
        }
    }
}