package com.example.applicationhome.features.locations.ui

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.bars.NetworkErrorTopBar
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.LoadingDialog
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.ActionsStates
import com.example.applicationhome.data.data.model.EditAddressModeState
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.data.model.LocationsScreens
import com.example.applicationhome.data.data.model.MapEntryPoint
import com.example.applicationhome.features.confirmorder.ui.mappage.StreetMapPage
import com.example.applicationhome.features.confirmorder.ui.pageone.PageOneConfirmOrder

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Locations(
    navigationController : NavHostController,
    viewModel : LocationsViewModel
){
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    var isEditEnabled by rememberSaveable { mutableStateOf(false) }
    val selectedAddress by viewModel.selectedAddress.collectAsStateWithLifecycle()

    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val topBarTitle by viewModel.topBarTitle.collectAsStateWithLifecycle()

    val addresses by viewModel.addresses.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    val textFieldConfirmOrderScreenList = viewModel.textFieldConfirmOrderScreenList
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if (isGranted) {
            viewModel.fetchCurrentLocation()
        }
    }

    BackHandler(enabled = true){
        when(currentScreen){
            is LocationsScreens.Map -> {
                viewModel.navigateBack{
                    if (navigationController.previousBackStackEntry != null) {
                        navigationController.popBackStack()
                    }
                }
            }

            else -> {
                isEditEnabled = false
                viewModel.navigateBack{
                    if (navigationController.previousBackStackEntry != null) {
                        navigationController.popBackStack()
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                MyTopBar(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    title = stringResource(topBarTitle),
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    startaction = {
                        IconButton(
                            onClick = {
                                when(currentScreen){
                                    is LocationsScreens.Map -> {
                                        viewModel.navigateBack{
                                            if (navigationController.previousBackStackEntry != null) {
                                                navigationController.popBackStack()
                                            }
                                        }
                                    }

                                    else -> {
                                        isEditEnabled = false
                                        viewModel.navigateBack{
                                            if (navigationController.previousBackStackEntry != null) {
                                                navigationController.popBackStack()
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(5.dp)
                                .clip(CircleShape)
                                .size(40.dp)
                        ){
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )

                NetworkErrorTopBar(isNetworkAvailable = isNetworkAvailable)
            }
        }
    ){ paddingValues ->
        when(screenState){
            HomeUiState.Loading -> {
                LoadingDialog(true)
            }

            else -> {
                AnimatedContent(
                    targetState = currentScreen,

                    transitionSpec = {
                        val animationSpec = tween<IntOffset>(durationMillis = 300)

                        val isForward = targetState.index > initialState.index

                        if (isForward) {
                            (slideInHorizontally(animationSpec) { fullWidth -> fullWidth } + fadeIn()) togetherWith
                                    (slideOutHorizontally(animationSpec) { fullWidth -> -fullWidth } + fadeOut())
                        } else {
                            (slideInHorizontally(animationSpec) { fullWidth -> -fullWidth } + fadeIn()) togetherWith
                                    (slideOutHorizontally(animationSpec) { fullWidth -> fullWidth } + fadeOut())
                        }
                    },

                    label = "CheckoutNavAnimation"
                ){ screen ->
                    when(screen){
                        LocationsScreens.Locations -> {
                            SelectAddress(
                                addresses = addresses,
                                paddingValues = paddingValues,
                                onNewAddressClickable = {
                                    isEditEnabled = true
                                    viewModel.clearTextFields()
                                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                    viewModel.navigateTo(LocationsScreens.Map(MapEntryPoint.Initial))
                                },
                                onAddressClickable = { address ->
                                    viewModel.selectAddress(address)
                                    viewModel.navigateTo(LocationsScreens.ViewAddressInformation(EditAddressModeState.ReadOnly))
                                },
                                onDeleteAddress = { userId, addressId ->
                                    viewModel.deleteAddress(
                                        userId = userId,
                                        orderId = addressId
                                    )
                                }
                            )
                        }

                        is LocationsScreens.ViewAddressInformation -> {
                            when(uiState.confirmOrderState){
                                ActionsStates.Loading -> {
                                    LoadingDialog(true)
                                }

                                else -> {
                                    PageOneConfirmOrder(
                                        isEditEnabled = isEditEnabled,
                                        textFieldConfirmOrderScreenList = textFieldConfirmOrderScreenList,
                                        isButtonClicked = uiState.isButtonClicked,
                                        confirmOrderError = uiState.confirmOrderError,
                                        confirmOrderState = uiState.confirmOrderState,
                                        bottonState = uiState.bottonState,
                                        location = uiState.locationState.locationName,
                                        locationImage = uiState.locationImage,
                                        isSavePhoneNumberSelected = false,
                                        isSaveAddressSelected = false,
                                        bottonStateChange = { viewModel.buttonStateChange() },
                                        openMaps = {
                                            viewModel.fetchCurrentLocation()
                                            viewModel.navigateTo(
                                                LocationsScreens.Map(
                                                    MapEntryPoint.UserData
                                                )
                                            )
                                        },
                                        onSaveAddress = {
                                            if(selectedAddress == null){
                                                viewModel.onSaveAddress()
                                            }else{
                                                viewModel.onEditAddress()
                                            }
                                            isEditEnabled = false
                                        },
                                        onSavePhoneNumber = {  },
                                        onSaveAddressRadioButton = {  },
                                        onEditeMode = {
                                            isEditEnabled = true
                                        }
                                    )
                                }
                            }
                        }

                        is LocationsScreens.Map -> {
                            if(uiState.locationState.isLoading){
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
                                    initialLatitude = uiState.locationState.latitude,
                                    initialLongitude = uiState.locationState.longitude,
                                    snackBarHostState = snackbarHostState,
                                    uiEvent = viewModel.uiEvent,
                                    isNetworkAvailable = isNetworkAvailable,
                                    onLocationSelected = { latitude, longitude ->
                                        viewModel.updateSelectedLocation(
                                            latitude,
                                            longitude
                                        )
                                    },
                                    fetchCurrentLocation = {
                                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                        viewModel.fetchCurrentLocation()
                                    },
                                    retryNetwork = { viewModel.retryNetwork() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}