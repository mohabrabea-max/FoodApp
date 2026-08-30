package com.example.applicationhome.features.confirmorder.ui

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.R
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.LocationRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.PaymentUseCase
import com.example.applicationhome.core.domain.usecase.UploadOrderUseCase
import com.example.applicationhome.core.domain.usecase.ValidateFormUseCase
import com.example.applicationhome.data.data.model.ActionsStates
import com.example.applicationhome.data.data.model.CheckoutFormState
import com.example.applicationhome.data.data.model.ConfirmOrderScreenTextFieldEnum
import com.example.applicationhome.data.data.model.ConfirmOrderScreens
import com.example.applicationhome.data.data.model.ConfirmOrderUiState
import com.example.applicationhome.data.data.model.MapEntryPoint
import com.example.applicationhome.data.data.model.PaymentApiState
import com.example.applicationhome.data.data.model.PaymentMethod
import com.example.applicationhome.data.data.model.PaymentState
import com.example.applicationhome.data.data.model.PaymobBillingData
import com.example.applicationhome.data.data.model.ProfileEditResult
import com.example.applicationhome.data.data.model.TextFieldClassFromConfirmOrderScreen
import com.example.applicationhome.data.data.model.UiEvent
import com.example.applicationhome.data.remote.NetworkObserver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmOrderScreenViewModel @Inject constructor(
    private val fusedLocationClient : FusedLocationProviderClient,
    userRepository : UserRepository,
    cartRepository : CartRepository,
    private val locationRepository : LocationRepository,
    private val uploadOrderUseCase : UploadOrderUseCase,
    private val validateFormUseCase : ValidateFormUseCase,
    private val paymentUseCase : PaymentUseCase,
    private val networkObserver: NetworkObserver
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConfirmOrderUiState())
    val uiState = _uiState.asStateFlow()

    private val checkoutFormState = CheckoutFormState()

    val phoneNumber : StateFlow<String> = snapshotFlow { checkoutFormState.phoneNumberState.text.toString() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    val textFieldConfirmOrderScreenList = listOf(
        TextFieldClassFromConfirmOrderScreen(
            checkoutFormState.houseState,
            R.string.house,
            ConfirmOrderScreenTextFieldEnum.HOUSE
        ),
        TextFieldClassFromConfirmOrderScreen(
            checkoutFormState.streetState,
            R.string.street,
            ConfirmOrderScreenTextFieldEnum.STREET
        ),
        TextFieldClassFromConfirmOrderScreen(
            checkoutFormState.phoneNumberState,
            R.string.phone_number,
            ConfirmOrderScreenTextFieldEnum.PHONE
        ),
        TextFieldClassFromConfirmOrderScreen(
            checkoutFormState.additionalDirectionsState,
            R.string.additional_directions_optional,
            ConfirmOrderScreenTextFieldEnum.ADDITIONAL
        ),
        TextFieldClassFromConfirmOrderScreen(
            checkoutFormState.addressLabelState,
            R.string.address_label_optional,
            ConfirmOrderScreenTextFieldEnum.ADDRESS
        )
    )

    val cartItems = cartRepository.cartItems

    val userData = userRepository.userData

    val totalPrice = cartRepository.totalPrice

    private var address1 = ""
    private var address2 = ""


    val isNetworkAvailable = networkObserver.isNetworkAvailable
        .onEach { available ->
            if(!available){
                _uiEvent.send(UiEvent.ShowNetworkError)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    private val _uiEvent = Channel<UiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()


    init {
        viewModelScope.launch {
            val user = userData.first()

            checkoutFormState.phoneNumberState.edit {
                replace(0, length, user.phonenumber)
            }

            checkoutFormState.addressLabelState.edit {
                replace(
                    0, length,
                    if(user.city.isNotEmpty()) user.city + " - " + user.governorate
                    else user.governorate
                )
            }
        }
    }



    // --------------------------------------------\\ Screens //--------------------------------------------
    private val _backStack = MutableStateFlow<List<ConfirmOrderScreens>>(
        listOf(ConfirmOrderScreens.Map(MapEntryPoint.Initial))
    )

    val currentScreen = _backStack
        .map { it.last() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConfirmOrderScreens.Map(MapEntryPoint.Initial)
        )

    val topBatTitle : StateFlow<Int> =
        _backStack.map { screen ->
            when(screen.last()){
                ConfirmOrderScreens.UserData, ConfirmOrderScreens.Checkout -> {
                   R.string.checkout
                }

                ConfirmOrderScreens.PaymentGateway -> {
                    R.string.payment
                }

                else -> {
                    R.string.location
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = R.string.location
        )


    fun navigateTo(screen: ConfirmOrderScreens) {
        _backStack.update { it + screen }
    }

    fun navigateBack(onExitConfirmOrderScreens : () -> Unit){
        _backStack.update { currentStack ->
            if(currentStack.size > 1){
                currentStack.dropLast(1)
            }else{
                onExitConfirmOrderScreens()
                currentStack
            }
        }
    }

    private fun onLocationSelected(){
        val currentMapScreen = _backStack.value.lastOrNull() as? ConfirmOrderScreens.Map ?: return

        when(currentMapScreen.entryPoint){
            MapEntryPoint.Initial -> {
                navigateTo(ConfirmOrderScreens.UserData)
            }

            MapEntryPoint.UserData, MapEntryPoint.Checkout -> {
                navigateBack(onExitConfirmOrderScreens = {})
            }
        }
    }


    // --------------------------------------------\\ Location //--------------------------------------------
    fun retryNetwork(){
        viewModelScope.launch {
            val isConnected = networkObserver.isCurrentlyConnected()

            if(isConnected) {
                fetchCurrentLocation()
            }else{
                _uiEvent.send(UiEvent.ShowNetworkError)
            }
        }
    }

    fun fetchCurrentLocation(){
        _uiState.update {
            it.copy(
                locationState = it.locationState.copy(
                    isLoading = true
                )
            )
        }

        try{
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->
                if(location != null){
                    _uiState.update {
                        it.copy(
                            locationState = it.locationState.copy(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                isLoading = false
                            )
                        )
                    }

                }else{
                    _uiState.update {
                        it.copy(
                            locationState =  it.locationState.copy(isLoading = false)
                        )
                    }
                }
            }.addOnFailureListener {
                _uiState.update {
                    it.copy(
                        locationState =  it.locationState.copy(isLoading = false)
                    )
                }
            }
        }catch (e : SecurityException){
            _uiState.update {
                it.copy(
                    locationState =  it.locationState.copy(isLoading = false)
                )
            }
        }
    }

    fun updateSelectedLocation(lat: Double, lng: Double){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    locationImage = locationRepository.buildStaticMapUrl(lat, lng)
                )
            }

            locationRepository.getAddressFromLocation(lat, lng){ areaName, fullAddress ->
                _uiState.update {
                    it.copy(
                        locationState =  it.locationState.copy(
                            latitude = lat,
                            longitude = lng,
                            locationName = areaName,
                            locationFullName = fullAddress
                        )
                    )
                }
            }

            onLocationSelected()
        }
    }


    // --------------------------------------------\\ Payment Methods //--------------------------------------------

    fun onPaymentMethodSelected(method: PaymentMethod){
        _uiState.update {
            it.copy(
                payMethodState = it.payMethodState.copy(selectedPaymentMethod = method)
            )
        }
    }

    fun openPaymentWebView(){
        _uiState.update {
            it.copy(
                paymentState = PaymentState.Loading
            )
        }
    }

    fun onPaymentStateChanged(paymentState : PaymentState){
        _uiState.update {
            it.copy(
                paymentState = paymentState
            )
        }
    }


    // --------------------------------------------\\ Payment Checkout //--------------------------------------------

    fun startPayment(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    paymentApiState = PaymentApiState.Loading
                )
            }

            val billingData = PaymobBillingData(
                firstName = userData.value.firstname,
                lastName = userData.value.lastname,
                email = userData.value.email,
                phoneNumber = checkoutFormState.phoneNumberState.text.toString()
            )

            val result = paymentUseCase(
                orderPrice = totalPrice.value,
                billingData = billingData,
                integrationId = _uiState.value.payMethodState.selectedPaymentMethod.integrationId
            )

            result.onSuccess { token ->
                _uiState.update {
                    it.copy(
                        paymentApiState = PaymentApiState.Success(token)
                    )
                }
                navigateTo(ConfirmOrderScreens.PaymentGateway)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        paymentApiState = PaymentApiState.Error(error.message ?: "An error occurred during the payment process")
                    )
                }
            }
        }
    }

    fun onPaymentApiStateChanged(paymentState : PaymentApiState){
        _uiState.update {
            it.copy(
                paymentApiState = paymentState
            )
        }
    }


    // --------------------------------------------\\ Finish Confirm Order //--------------------------------------------

    fun savePhoneNumber(){
        _uiState.update {
            it.copy(
                isSavePhoneNumberSelected = !it.isSavePhoneNumberSelected
            )
        }
    }


    // --------------------------------------------\\ Finish Confirm Order //--------------------------------------------

    fun buttonStateChange(){
        _uiState.update {
            it.copy(
                bottonState =
                    checkoutFormState.houseState.text.isNotEmpty() &&
                    checkoutFormState.streetState.text.isNotEmpty() &&
                    checkoutFormState.phoneNumberState.text.isNotEmpty()
            )
        }
    }

    fun onBottonStateChange(){
        val result = validateFormUseCase(
            phoneNumber = checkoutFormState.phoneNumberState.text.toString(),
            house = checkoutFormState.houseState.text.toString(),
            street = checkoutFormState.streetState.text.toString()
        )

        _uiState.update {
            it.copy(
                confirmOrderError = result
            )
        }

        when(result){
            ProfileEditResult.Success -> {
                address1 = "${checkoutFormState.houseState.text} - ${checkoutFormState.streetState.text}"
                address2 = "${
                    if(checkoutFormState.additionalDirectionsState.text.isNotEmpty()) " - " + checkoutFormState.additionalDirectionsState.text
                    else ""
                } ${
                    if(checkoutFormState.addressLabelState.text.isNotEmpty()) " - " + checkoutFormState.addressLabelState.text
                    else ""
                }"

                _uiState.update {
                    it.copy(
                        isButtonClicked = true,
                        streetAndHome = Pair(
                            checkoutFormState.streetState.text.toString(),
                            checkoutFormState.houseState.text.toString()
                        )
                    )
                }

                navigateTo(ConfirmOrderScreens.Checkout)
            }

            else -> {
                _uiState.update { it.copy(isButtonClicked = false) }
            }
        }
    }

    fun uploadOrder(onSuccess : () -> Unit, onFailed : () -> Unit){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    confirmOrderState = ActionsStates.Loading
                )
            }

            uploadOrderUseCase(
                phoneNumber = checkoutFormState.phoneNumberState.text.toString(),
                address = if (
                        checkoutFormState.additionalDirectionsState.text.isNotEmpty() ||
                        checkoutFormState.addressLabelState.text.isNotEmpty()
                    )
                    address1 + address2
                else address1,
                location = "(${_uiState.value.locationState.latitude} , ${_uiState.value.locationState.longitude})",
                locationFullName = _uiState.value.locationState.locationFullName,
                isSavePhoneNumberSelected = _uiState.value.isSavePhoneNumberSelected
            ).onSuccess {
                onSuccess()
                _uiState.update {
                    it.copy(
                        confirmOrderState = ActionsStates.Success
                    )
                }
            }.onFailure {
                onFailed()
                _uiState.update {
                    it.copy(
                        confirmOrderState = ActionsStates.Failed("Network error")
                    )
                }
            }
        }
    }
}