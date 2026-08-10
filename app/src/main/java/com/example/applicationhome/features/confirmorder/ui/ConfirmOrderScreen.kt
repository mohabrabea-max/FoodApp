package com.example.applicationhome.features.confirmorder.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.bars.NetworkErrorTopBar
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.ConfirmOrderScreens
import com.example.applicationhome.data.data.model.MapEntryPoint
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
    val currentScreen by confirmOrderScreenViewModel.currentScreen.collectAsStateWithLifecycle()

    val topBatTitle by confirmOrderScreenViewModel.topBatTitle.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val isNetworkAvailable by confirmOrderScreenViewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val payMethodState by confirmOrderScreenViewModel.payMethodState.collectAsStateWithLifecycle()
    val paymentState by confirmOrderScreenViewModel.paymentState.collectAsStateWithLifecycle()
    val paymentApiState by confirmOrderScreenViewModel.paymentApiState.collectAsStateWithLifecycle()

    val confirmOrderState by confirmOrderScreenViewModel.confirmOrderState.collectAsStateWithLifecycle()
    val isButtonClicked by confirmOrderScreenViewModel.isButtonClicked.collectAsStateWithLifecycle()

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
        confirmOrderScreenViewModel.navigateBack{
            if (navigationController.previousBackStackEntry != null) {
                navigationController.popBackStack()
            }
        }
    }


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
                    topBatTitle,
                    Color.Black,
                    {
                        IconButton(
                            onClick = {
                                confirmOrderScreenViewModel.navigateBack{
                                    if (navigationController.previousBackStackEntry != null) {
                                        navigationController.popBackStack()
                                    }
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

                if(!isNetworkAvailable){
                    NetworkErrorTopBar()
                }
            }
        }
    ){
        AnimatedContent(
            targetState = currentScreen,

            transitionSpec = {
                val animationSpec = tween<IntOffset>(durationMillis = 300)

                // إذا كان رقم الشاشة الجديدة أكبر من القديمة = تقدم للأمام، غير ذلك = رجوع للخلف
                val isForward = targetState.index > initialState.index

                if (isForward) {
                    // ➡️ حركة التقدم: يدخل من اليمين ويخرج لليسار
                    (slideInHorizontally(animationSpec) { fullWidth -> fullWidth } + fadeIn()) togetherWith
                            (slideOutHorizontally(animationSpec) { fullWidth -> -fullWidth } + fadeOut())
                } else {
                    // ⬅️ حركة الرجوع: يدخل من اليسار ويخرج لليمين
                    (slideInHorizontally(animationSpec) { fullWidth -> -fullWidth } + fadeIn()) togetherWith
                            (slideOutHorizontally(animationSpec) { fullWidth -> fullWidth } + fadeOut())
                }
            },

            label = "CheckoutNavAnimation"
        ){ screen ->
            when(screen){
                // --------------------------------------------\\ Map Page //--------------------------------------------
                is ConfirmOrderScreens.Map -> {
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
                            uiEvent = confirmOrderScreenViewModel.uiEvent,
                            isNetworkAvailable = isNetworkAvailable,
                            onLocationSelected = { latitude, longitude ->
                                confirmOrderScreenViewModel.updateSelectedLocation(
                                    latitude,
                                    longitude
                                )
                            },
                            fetchCurrentLocation = { confirmOrderScreenViewModel.fetchCurrentLocation() },
                            retryNetwork = { confirmOrderScreenViewModel.retryNetwork() }
                        )
                    }
                }

                // --------------------------------------------\\ Page 1 //--------------------------------------------
                is ConfirmOrderScreens.UserData -> {
                    PageOneConfirmOrder(
                        textFieldConfirmOrderScreenList = textFieldConfirmOrderScreenList,
                        isButtonClicked = isButtonClicked,
                        confirmOrderError = confirmOrderError,
                        confirmOrderState = confirmOrderState,
                        bottonState = bottonState,
                        location = locationState.locationName,
                        locationImage = locationImage,
                        bottonStateChange = { confirmOrderScreenViewModel.bottonStateChange() },
                        openMaps = {
                            confirmOrderScreenViewModel.fetchCurrentLocation()
                            confirmOrderScreenViewModel.navigateTo(ConfirmOrderScreens.Map(MapEntryPoint.UserData))
                        },
                        onBottonStateChange = { confirmOrderScreenViewModel.onBottonStateChange() }
                    )
                }

                // --------------------------------------------\\ Page 2 //--------------------------------------------
                is ConfirmOrderScreens.Checkout -> {
                    PageTowConfirmOrder(
                        confirmOrderState = confirmOrderState,
                        snackBarHostState = snackbarHostState,
                        cart = cart,
                        totalPrice = totalprice,
                        locationImage = locationImage,
                        city = locationState.locationName,
                        streetAndHome = streetAndHome,
                        phoneNumber = phoneNumber,
                        payMethodState = payMethodState.selectedPaymentMethod,
                        paymentState = paymentState,
                        paymentApiState = paymentApiState,
                        onMethodSelected = { confirmOrderScreenViewModel.onPaymentMethodSelected(it) },
                        openMaps = {
                            confirmOrderScreenViewModel.fetchCurrentLocation()
                            confirmOrderScreenViewModel.navigateTo(ConfirmOrderScreens.Map(MapEntryPoint.Checkout))
                        },
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
                }

                // --------------------------------------------\\ Paymob Web View //--------------------------------------------
                is ConfirmOrderScreens.PaymentGateway -> {
                    when(val state = paymentApiState) {
                        is PaymentApiState.Success -> {
                            PaymobWebViewScreen(
                                paymentToken = state.paymentToken,
                                onPaymentStateChanged = { paymentState ->
                                    confirmOrderScreenViewModel.onPaymentStateChanged(paymentState)

                                    when(paymentState){
                                        PaymentState.Success -> {
                                            confirmOrderScreenViewModel.onPaymentApiStateChanged(PaymentApiState.Idle)
                                            confirmOrderScreenViewModel.navigateTo(ConfirmOrderScreens.Map(MapEntryPoint.Checkout))
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

                        else -> { confirmOrderScreenViewModel.navigateTo(ConfirmOrderScreens.Checkout) }
                    }
                }
            }
        }
    }
}