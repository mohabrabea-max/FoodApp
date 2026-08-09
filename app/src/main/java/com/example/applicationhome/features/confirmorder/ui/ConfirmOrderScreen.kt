package com.example.applicationhome.features.confirmorder.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.applicationhome.core.ui.components.bars.ErrorTopBar
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.ActionsStates
import com.example.applicationhome.data.data.model.PaymentApiState
import com.example.applicationhome.data.data.model.PaymentState
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.features.confirmorder.ui.mappage.StreetMapPage
import com.example.applicationhome.features.confirmorder.ui.pageone.PageOneConfirmOrder
import com.example.applicationhome.features.confirmorder.ui.pagetow.PageTowConfirmOrder
import com.example.applicationhome.features.confirmorder.ui.pagetow.PaymobWebViewScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmOrderScreen(
    navigationController : NavHostController,
    confirmOrderScreenViewModel : ConfirmOrderScreenViewModel
){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isNetworkAvailable by confirmOrderScreenViewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val payMethodState by confirmOrderScreenViewModel.payMethodState.collectAsStateWithLifecycle()
    val paymentState by confirmOrderScreenViewModel.paymentState.collectAsStateWithLifecycle()
    val paymentApiState by confirmOrderScreenViewModel.paymentApiState.collectAsStateWithLifecycle()

    val confirmOrderState by confirmOrderScreenViewModel.confirmOrderState.collectAsStateWithLifecycle()
    val isButtonClicked by confirmOrderScreenViewModel.isButtonClicked.collectAsStateWithLifecycle()
    val confirmOrderPages by confirmOrderScreenViewModel.confirmOrderPages.collectAsStateWithLifecycle()

    val textFieldConfirmOrderScreenList = confirmOrderScreenViewModel.textFieldConfirmOrderScreenList

    val confirmOrderError by confirmOrderScreenViewModel.confirmOrderError.collectAsStateWithLifecycle()

    val cart by confirmOrderScreenViewModel.cartItems.collectAsStateWithLifecycle()

    val totalprice by confirmOrderScreenViewModel.totalPrice.collectAsStateWithLifecycle()

    val bottonState by confirmOrderScreenViewModel.bottonState.collectAsStateWithLifecycle()

    val clickState = rememberSaveable { mutableStateOf(true) }

    val locationState by confirmOrderScreenViewModel.locationState.collectAsStateWithLifecycle()
    val locationImage by confirmOrderScreenViewModel.locationImage.collectAsStateWithLifecycle()

    val streetAndHome by confirmOrderScreenViewModel.streetAndHome.collectAsStateWithLifecycle()
    val phoneNumber by confirmOrderScreenViewModel.phoneNumber.collectAsStateWithLifecycle()

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

                confirmOrderScreenViewModel.lastPage()
            }

            1 -> { confirmOrderScreenViewModel.lastPage() }

            5 -> {
                confirmOrderScreenViewModel.onPaymentApiStateChanged(PaymentApiState.Idle)
                confirmOrderScreenViewModel.changePageNumber(2)
            }

            else -> {
                confirmOrderScreenViewModel.changePageNumber(1)

            }
        }
    }

    val color = if(bottonState) Color.DarkOrange else Color.VeryLightGray
    val fontcolor = if(bottonState) Color.White else Color.LightGray


    Scaffold(
        modifier = Modifier.navigationBarsPadding().fillMaxSize(),

        containerColor = Color.White,

        topBar = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                MyTopBar(
                    Color.White,
                    modifier = Modifier.fillMaxWidth().height(100.dp).shadow(elevation = 5.dp),
                    if(confirmOrderPages in 1..<3) "Checkout"
                    else if(confirmOrderPages == 5) "PayMent"
                    else "Location",
                    Color.Black,
                    {
                        IconButton(
                            onClick = {
                                if (confirmOrderPages == 1) {
                                    confirmOrderScreenViewModel.lastPage()

                                } else if(confirmOrderPages == 0){
                                    if (navigationController.previousBackStackEntry != null) {
                                        navigationController.popBackStack()
                                    }

                                    confirmOrderScreenViewModel.lastPage()

                                }else if(confirmOrderPages == 5){
                                    confirmOrderScreenViewModel.onPaymentApiStateChanged(PaymentApiState.Idle)
                                    confirmOrderScreenViewModel.changePageNumber(2)
                                }else{
                                    confirmOrderScreenViewModel.changePageNumber(1)
                                }
                            },
                            modifier = Modifier
                                .padding(5.dp)
                                .clip(CircleShape).size(40.dp).background(Color.White)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
                        }
                    },
                )

                if(confirmOrderPages in 1..<3 && !isNetworkAvailable){
                    ErrorTopBar()
                }
            }
        },

        bottomBar = {
            // --------------------------------------------\\ Buttons //--------------------------------------------
            Column(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .pointerInput(Unit) { detectTapGestures { } },
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                // --------------------------------------------\\ Page 1 Button //--------------------------------------------
                if(confirmOrderPages == 1){
                    MyButton(
                        confirmOrderState == ActionsStates.Loading,
                        color,
                        fontcolor,
                        40.dp,
                        "Save address"
                    ) {
                        if(bottonState) confirmOrderScreenViewModel.bottonstate()
                    }

                    // --------------------------------------------\\ Page 2 Button //--------------------------------------------
                }else if(confirmOrderPages == 2){

                }
            }
        }
    ){
        Box(modifier = Modifier.fillMaxSize()) {

            // --------------------------------------------\\ Page 1 //--------------------------------------------
            if(confirmOrderPages == 1){
                PageOneConfirmOrder(
                    textFieldConfirmOrderScreenList = textFieldConfirmOrderScreenList,
                    isButtonClicked = isButtonClicked,
                    confirmOrderError = confirmOrderError,
                    location = locationState.locationName,
                    locationImage = locationImage,
                    bottonStateChange = { confirmOrderScreenViewModel.bottonStateChange() },
                    openMaps = { confirmOrderScreenViewModel.changePageNumber(3) }
                )

            // --------------------------------------------\\ Page 2 //--------------------------------------------
            } else if(confirmOrderPages == 2) {
                PageTowConfirmOrder(
                    confirmOrderState = confirmOrderState,
                    snackBarHostState = snackbarHostState,
                    scope = scope,
                    cart = cart,
                    totalPrice = totalprice,
                    locationImage = locationImage,
                    city = locationState.locationName,
                    streetAndHome = streetAndHome,
                    phoneNumber = phoneNumber,
                    payMethodState = payMethodState.selectedPaymentMethod,
                    paymentState = paymentState,
                    onMethodSelected = { confirmOrderScreenViewModel.onPaymentMethodSelected(it) },
                    changePageNumber = { confirmOrderScreenViewModel.changePageNumber(5) },
                    startPayment = { confirmOrderScreenViewModel.startPayment() },
                    uploadOrder = { paymentButtonState ->
                        if (clickState.value && paymentButtonState) {
                            clickState.value = false
                            confirmOrderScreenViewModel.uploadOrder(
                                onSuccess = {
                                    navigationController.navigate(Screens.DashboardScreen.screen) {
                                        popUpTo(Screens.DashboardScreen.screen) {
                                            inclusive = true
                                        }
                                    }
                                },

                                onField = {
                                    clickState.value = true
                                }
                            )
                        }

                    }
                )

            // --------------------------------------------\\ Paymob Web View //--------------------------------------------
            }else if(confirmOrderPages == 5){
                when(val state = paymentApiState) {
                    is PaymentApiState.Success -> {
                        PaymobWebViewScreen(
                            paymentToken = state.paymentToken,
                            onPaymentStateChanged = { paymentState ->
                                confirmOrderScreenViewModel.onPaymentStateChanged(paymentState)

                                when(paymentState){
                                    PaymentState.Success -> {
                                        confirmOrderScreenViewModel.onPaymentApiStateChanged(PaymentApiState.Idle)
                                        confirmOrderScreenViewModel.changePageNumber(1)
                                    }

                                    PaymentState.Loading -> {

                                    }

                                    PaymentState.Failed -> {
                                        confirmOrderScreenViewModel.onPaymentApiStateChanged(PaymentApiState.Idle)
                                    }

                                    PaymentState.Idle -> {
                                        confirmOrderScreenViewModel.onPaymentApiStateChanged(PaymentApiState.Idle)
                                    }
                                }
                            }
                        )
                    }

                    is PaymentApiState.Loading -> {
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
                    }

                    else -> { confirmOrderScreenViewModel.changePageNumber(2) }
                }


                // --------------------------------------------\\ Map Page //--------------------------------------------
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
                        snackBarHostState = snackbarHostState,
                        scope = scope,
                        uiEvent = confirmOrderScreenViewModel.uiEvent,
                        isNetworkAvailable = isNetworkAvailable,
                        onLocationSelected = { latitude, longitude ->
                            confirmOrderScreenViewModel.updateSelectedLocation(
                                latitude,
                                longitude
                            )
                        },
                        changePage = {
                            if (confirmOrderPages == 4) {
                                confirmOrderScreenViewModel.changePageNumber(2)
                            } else {
                                confirmOrderScreenViewModel.changePageNumber(1)
                            }
                        },
                        retryNetwork = { confirmOrderScreenViewModel.retryNetwork() }
                    )
                }
            }
        }
    }
}