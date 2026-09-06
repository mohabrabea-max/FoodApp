package com.example.applicationhome.features.confirmorder.ui

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.R
import com.example.applicationhome.core.domain.repository.AddressesRepository
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.LocationRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.PaymentUseCase
import com.example.applicationhome.core.domain.usecase.UploadOrderUseCase
import com.example.applicationhome.core.domain.usecase.ValidateFormUseCase
import com.example.applicationhome.data.data.model.ActionsStates
import com.example.applicationhome.data.data.model.CheckoutFormState
import com.example.applicationhome.data.data.model.ConfirmOrderScreens
import com.example.applicationhome.data.data.model.ConfirmOrderUiState
import com.example.applicationhome.data.data.model.LocationsScreenDialogs
import com.example.applicationhome.data.data.model.MapEntryPoint
import com.example.applicationhome.data.data.model.PaymentApiState
import com.example.applicationhome.data.data.model.PaymentMethod
import com.example.applicationhome.data.data.model.PaymentState
import com.example.applicationhome.data.data.model.PaymobBillingData
import com.example.applicationhome.data.data.model.ProfileEditResult
import com.example.applicationhome.data.data.model.UiEvent
import com.example.applicationhome.data.local.entity.AddressesEntity
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ConfirmOrderScreenViewModel @Inject constructor(
    userRepository : UserRepository,
    cartRepository : CartRepository,
    private val locationRepository : LocationRepository,
    private val addressesRepository : AddressesRepository,
    private val uploadOrderUseCase : UploadOrderUseCase,
    private val validateFormUseCase : ValidateFormUseCase,
    private val paymentUseCase : PaymentUseCase,
    private val networkObserver: NetworkObserver
) : ViewModel() {
    val addresses = userRepository.userData.flatMapLatest {
        val id = it.id
        if(id.isNotEmpty()){
            addressesRepository.getAddresses(id)
        }else{
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _uiState = MutableStateFlow(ConfirmOrderUiState())
    val uiState = _uiState.asStateFlow()

    private val _checkoutFormState = CheckoutFormState()
    val checkoutFormState = _checkoutFormState

    val phoneNumber : StateFlow<String> = snapshotFlow { _checkoutFormState.phoneNumberState.text.toString() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    val cartItems = cartRepository.cartItems

    val userData = userRepository.userData

    val totalPrice = cartRepository.totalPrice

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


    // --------------------------------------------\\ Screens //--------------------------------------------
    private val _backStack = MutableStateFlow<List<ConfirmOrderScreens>>(
        listOf(ConfirmOrderScreens.SelectAddress)
    )

    val currentScreen = _backStack
        .map { it.last() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConfirmOrderScreens.SelectAddress
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
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    locationState = it.locationState.copy(
                        isLoading = true
                    )
                )
            }

            locationRepository.fetchCurrentLocation()
                .onSuccess { location ->
                    _uiState.update {
                        it.copy(
                            locationState = it.locationState.copy(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                isLoading = false
                            )
                        )
                    }
                }.onFailure {
                    _uiState.update {
                        it.copy(
                            locationState =  it.locationState.copy(isLoading = false)
                        )
                    }
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

            locationRepository.getAddressFromLocation(lat, lng)
                .onSuccess { location ->
                    _uiState.update {
                        it.copy(
                            locationState =  it.locationState.copy(
                                latitude = lat,
                                longitude = lng,
                                locationName = location.locationName,
                                locationFullName = location.locationFullName
                            )
                        )
                    }
                }.onFailure {
                    _uiState.update {
                        it.copy(
                            locationState = it.locationState.copy(
                                latitude = lat,
                                longitude = lng,
                                locationName = "Unknown address",
                                locationFullName = ""
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
                phoneNumber = _checkoutFormState.phoneNumberState.text.toString()
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


    // --------------------------------------------\\ Save Buttons //--------------------------------------------

    fun savePhoneNumber(){
        _uiState.update {
            it.copy(
                isSavePhoneNumberSelected = !it.isSavePhoneNumberSelected
            )
        }
    }

    fun saveAddress(){
        _uiState.update {
            it.copy(
                isSaveAddressSelected = !it.isSaveAddressSelected
            )
        }
    }


    // --------------------------------------------\\ Finish Confirm Order //--------------------------------------------

    private val _addressId = MutableStateFlow<Long?>(null)

    fun newAddress(){
        _checkoutFormState.addressTitle.clearText()
        _checkoutFormState.houseState.clearText()
        _checkoutFormState.streetState.clearText()
        _checkoutFormState.phoneNumberState.clearText()
        _checkoutFormState.addressLabelState.clearText()
        _checkoutFormState.additionalDirectionsState.clearText()

        _checkoutFormState.phoneNumberState.edit {
            replace(0, length, userData.value.phonenumber)
        }

        _checkoutFormState.addressLabelState.edit {
            replace(
                0, length,
                if(userData.value.city.isNotEmpty()) userData.value.city + " - " + userData.value.governorate
                else userData.value.governorate
            )
        }

        navigateTo(ConfirmOrderScreens.Map(MapEntryPoint.Initial))
    }

    fun onSelectAddress(address : AddressesEntity){
        _addressId.value = address.addressId

        updateSelectedLocation(
            lat = address.latLocation.toDouble(),
            lng = address.lngLocation.toDouble()
        )

        _checkoutFormState.houseState.edit {  replace(0, length, address.house) }
        _checkoutFormState.streetState.edit {  replace(0, length, address.street) }
        _checkoutFormState.phoneNumberState.edit {  replace(0, length, address.phoneNumber) }

        onBottonStateChange()
    }

    fun buttonStateChange(){
        _uiState.update {
            it.copy(
                bottonState =
                    _checkoutFormState.houseState.text.isNotEmpty() &&
                            _checkoutFormState.streetState.text.isNotEmpty() &&
                            _checkoutFormState.phoneNumberState.text.isNotEmpty()
            )
        }
    }

    fun onBottonStateChange(){
        val result = validateFormUseCase(
            phoneNumber = _checkoutFormState.phoneNumberState.text.toString(),
            house = _checkoutFormState.houseState.text.toString(),
            street = _checkoutFormState.streetState.text.toString()
        )

        _uiState.update {
            it.copy(
                confirmOrderError = result
            )
        }

        when(result){
            ProfileEditResult.Success -> {
                _uiState.update {
                    it.copy(
                        isButtonClicked = true,
                        streetAndHome = Pair(
                            _checkoutFormState.streetState.text.toString(),
                            _checkoutFormState.houseState.text.toString()
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
                addressId = _addressId.value,
                title = _checkoutFormState.addressTitle.text.toString(),
                house = _checkoutFormState.houseState.text.toString(),
                street = _checkoutFormState.streetState.text.toString(),
                phoneNumber = _checkoutFormState.phoneNumberState.text.toString(),
                additionalDirectionsState = _checkoutFormState.additionalDirectionsState.text.toString(),
                addressLabelState = _checkoutFormState.addressLabelState.text.toString(),
                latLocation = _uiState.value.locationState.latitude.toString(),
                lngLocation = _uiState.value.locationState.longitude.toString(),
                locationName = _uiState.value.locationState.locationName,
                locationFullName = _uiState.value.locationState.locationFullName,
                isSavePhoneNumberSelected = _uiState.value.isSavePhoneNumberSelected,
                isSaveAddressSelected = _uiState.value.isSaveAddressSelected
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


    // --------------------------------------------\\ Dialogs //--------------------------------------------

    private val _dialogs = MutableStateFlow<LocationsScreenDialogs>(LocationsScreenDialogs.None)
    val dialogs = _dialogs.asStateFlow()

    private val _selectedAddress = MutableStateFlow<AddressesEntity?>(null)
    val selectedAddress = _selectedAddress.asStateFlow()


    fun showDetailsDialog(address : AddressesEntity){
        _selectedAddress.value = address
        _dialogs.value = LocationsScreenDialogs.ShowDetailsDialog(address)
    }

    fun closeAlertDialogMessage(){
        _dialogs.value = LocationsScreenDialogs.None
        _selectedAddress.value = null
    }


    fun clearTextFields(){
        _checkoutFormState.addressTitle.clearText()
        _checkoutFormState.houseState.clearText()
        _checkoutFormState.streetState.clearText()
        _checkoutFormState.phoneNumberState.clearText()
        _checkoutFormState.addressLabelState.clearText()
        _checkoutFormState.additionalDirectionsState.clearText()
    }

    init {
        viewModelScope.launch {

            _checkoutFormState.phoneNumberState.edit {
                replace(0, length, userData.value.phonenumber)
            }

            _checkoutFormState.addressLabelState.edit {
                replace(
                    0, length,
                    if(userData.value.city.isNotEmpty()) userData.value.city + " - " + userData.value.governorate
                    else userData.value.governorate
                )
            }
        }

        viewModelScope.launch {
            _selectedAddress.collect { address ->
                if(address != null){
                    _checkoutFormState.addressTitle.edit {
                        replace(0, length, address.title)
                    }
                    _checkoutFormState.houseState.edit {
                        replace(0, length, address.house)
                    }
                    _checkoutFormState.streetState.edit {
                        replace(0, length, address.street)
                    }
                    _checkoutFormState.phoneNumberState.edit {
                        replace(0, length, address.phoneNumber)
                    }
                    _checkoutFormState.addressLabelState.edit {
                        replace(0, length, address.addressLabelState)
                    }
                    _checkoutFormState.additionalDirectionsState.edit {
                        replace(0, length, address.additionalDirectionsState)
                    }

                    updateSelectedLocation(
                        lat = address.latLocation.toDouble(),
                        lng = address.lngLocation.toDouble()
                    )
                }else{
                    clearTextFields()
                }
            }
        }
    }
}