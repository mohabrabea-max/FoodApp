package com.example.applicationhome.features.confirmorder.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.BuildConfig
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.LocationRepository
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CartUseCase
import com.example.applicationhome.core.domain.usecase.PaymentUseCase
import com.example.applicationhome.data.data.model.ActionsStates
import com.example.applicationhome.data.data.model.CheckoutUiState
import com.example.applicationhome.data.data.model.ConfirmOrderScreens
import com.example.applicationhome.data.data.model.MapEntryPoint
import com.example.applicationhome.data.data.model.MapUiState
import com.example.applicationhome.data.data.model.OrderItemsClass
import com.example.applicationhome.data.data.model.OrderStatesEnum
import com.example.applicationhome.data.data.model.OrdersClass
import com.example.applicationhome.data.data.model.PaymentApiState
import com.example.applicationhome.data.data.model.PaymentMethod
import com.example.applicationhome.data.data.model.PaymentState
import com.example.applicationhome.data.data.model.PaymobBillingData
import com.example.applicationhome.data.data.model.ProfileEditResult
import com.example.applicationhome.data.data.model.TextFieldClassFromConfirmOrderScreen
import com.example.applicationhome.data.data.model.UiEvent
import com.example.applicationhome.data.data.model.UserInformationInOrderClass
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
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val orderRepository : OrderRepository,
    private val locationRepository : LocationRepository,
    private val cartUseCase: CartUseCase,
    private val paymentUseCase : PaymentUseCase,
    private val networkObserver: NetworkObserver
) : ViewModel() {
    private val phoneNumberState = TextFieldState()
    val phoneNumber : StateFlow<String> = snapshotFlow { phoneNumberState.text.toString() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )
    private val houseState = TextFieldState()
    private val streetState = TextFieldState()

    private val additionalDirectionsState = TextFieldState()
    private val addressLabelState = TextFieldState()

    val textFieldConfirmOrderScreenList = listOf(
        TextFieldClassFromConfirmOrderScreen(houseState, "House"),
        TextFieldClassFromConfirmOrderScreen(streetState, "Street"),
        TextFieldClassFromConfirmOrderScreen(phoneNumberState, "Phone number"),
        TextFieldClassFromConfirmOrderScreen(
            additionalDirectionsState,
            "Additional directions (optional)"
        ),
        TextFieldClassFromConfirmOrderScreen(addressLabelState, "Address label (optional)"),
    )

    val cartItems = cartRepository.cartItems

    val userData = userRepository.userData

    val totalPrice = cartRepository.totalPrice

    private var address1 = ""
    private var address2 = ""

    private val _streetAndHome = MutableStateFlow(Pair("", ""))
    val streetAndHome = _streetAndHome.asStateFlow()

    private val _confirmOrderError = MutableStateFlow<ProfileEditResult?>(null)
    val confirmOrderError = _confirmOrderError.asStateFlow()

    private val _locationState = MutableStateFlow(MapUiState())
    val locationState = _locationState.asStateFlow()

    private val _locationImage = MutableStateFlow("")
    val locationImage = _locationImage.asStateFlow()

    private val _confirmOrderState = MutableStateFlow<ActionsStates>(ActionsStates.Idle)
    val confirmOrderState = _confirmOrderState.asStateFlow()


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

            phoneNumberState.edit {
                replace(0, length, user.phonenumber)
            }

            addressLabelState.edit {
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

    val topBatTitle : StateFlow<String> =
        _backStack.map { screen ->
            when(screen.last()){
                ConfirmOrderScreens.UserData, ConfirmOrderScreens.Checkout -> {
                   "Checkout"
                }

                ConfirmOrderScreens.PaymentGateway -> {
                    "Payment"
                }

                else -> {
                    "Location"
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Location"
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
        _locationState.update { item ->
            item.copy(isLoading = true)
        }

        try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->
                if(location != null){
                    _locationState.update { item ->
                        item.copy(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            isLoading = false
                        )
                    }
                }else{
                    _locationState.update { item ->
                        item.copy(isLoading = false)
                    }
                }
            }.addOnFailureListener {
                _locationState.update { item ->
                    item.copy(isLoading = false)
                }
            }
        } catch (e : SecurityException){
            _locationState.update { item ->
                item.copy(isLoading = false)
            }
        }
    }

    fun updateSelectedLocation(lat: Double, lng: Double){
        viewModelScope.launch {
            updateStaticMapUrl(lat, lng)
            locationRepository.getAddressFromLocation(lat, lng){ areaName, fullAddress ->
                _locationState.update { item ->
                    item.copy(
                        latitude = lat,
                        longitude = lng,
                        locationName = areaName,
                        locationFullName = fullAddress
                    )
                }
            }

            onLocationSelected()
        }
    }

    private fun updateStaticMapUrl(lat: Double, lon: Double) {
        val zoom = 16
        val width = 600
        val height = 300
        val apiKey = BuildConfig.GEOAPIFY_MAP_API_KEY

        _locationImage.value = "https://maps.geoapify.com/v1/staticmap?" +
                "style=osm-carto&" +
                "width=$width&height=$height&" +
                "center=lonlat:$lon,$lat&" +
                "zoom=$zoom&" +
                "marker=lonlat:$lon,$lat;color:%23ff0000;size:medium&" +
                "apiKey=$apiKey"
    }


    // --------------------------------------------\\ Payment Methods //--------------------------------------------
    private val _payMethodState = MutableStateFlow(CheckoutUiState())
    val payMethodState = _payMethodState.asStateFlow()

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState = _paymentState.asStateFlow()


    fun onPaymentMethodSelected(method: PaymentMethod){
        _payMethodState.update {
            it.copy(selectedPaymentMethod = method)
        }
    }

    fun openPaymentWebView(){
        _paymentState.value = PaymentState.Loading
    }

    fun onPaymentStateChanged(paymentState : PaymentState){
        _paymentState.value = paymentState
    }


    // --------------------------------------------\\ Payment Checkout //--------------------------------------------
    private val _paymentApiState = MutableStateFlow<PaymentApiState>(PaymentApiState.Idle)
    val paymentApiState = _paymentApiState.asStateFlow()


    fun startPayment(){
        viewModelScope.launch {
            _paymentApiState.value = PaymentApiState.Loading

            val billingData = PaymobBillingData(
                firstName = userData.value.firstname,
                lastName = userData.value.lastname,
                email = userData.value.email,
                phoneNumber = phoneNumberState.text.toString()
            )

            val result = paymentUseCase(
                orderPrice = totalPrice.value,
                billingData = billingData,
                integrationId = _payMethodState.value.selectedPaymentMethod.integrationId
            )

            result.onSuccess { token ->
                _paymentApiState.value = PaymentApiState.Success(token)
                navigateTo(ConfirmOrderScreens.PaymentGateway)
            }.onFailure { error ->
                _paymentApiState.value = PaymentApiState.Error(error.message ?: "An error occurred during the payment process")
            }
        }
    }

    fun onPaymentApiStateChanged(paymentState : PaymentApiState){
        _paymentApiState.value = paymentState
    }


    // --------------------------------------------\\ Finish Confirm Order //--------------------------------------------
    private val _bottonState = MutableStateFlow(false)
    val bottonState = _bottonState.asStateFlow()

    private val _isButtonClicked = MutableStateFlow(false)
    val isButtonClicked = _isButtonClicked.asStateFlow()


    fun bottonStateChange(){
        if(
            houseState.text.isNotEmpty() &&
            streetState.text.isNotEmpty() &&
            phoneNumberState.text.isNotEmpty()
        ){
            _bottonState.value = true
        }else{
            _bottonState.value = false
        }
    }

    fun onBottonStateChange(){
        val validPrefixes = listOf("010", "011", "012", "015")

        if(
            (
                phoneNumberState.text.length != 11
                        || !validPrefixes.any { phoneNumberState.text.contains(it) }
            )
            && phoneNumberState.text.isNotEmpty()
        ) {
            _isButtonClicked.value = false
            _confirmOrderError.value = ProfileEditResult.PhoneNumberIncomplete
            return
        }

        if(
            houseState.text.isEmpty()
            && streetState.text.isEmpty()
        ){
            _isButtonClicked.value = false
            _confirmOrderError.value = ProfileEditResult.DataIncomplete
            return
        }


        _isButtonClicked.value = true
        _confirmOrderError.value = ProfileEditResult.Success

        address1 = "${houseState.text} - ${streetState.text}"
        address2 = "${
            if(additionalDirectionsState.text.isNotEmpty()) " - " + additionalDirectionsState.text
            else ""
        } ${
            if(addressLabelState.text.isNotEmpty()) " - " + addressLabelState.text
            else ""
        }"

        _streetAndHome.value = Pair(streetState.text.toString(), houseState.text.toString())

        navigateTo(ConfirmOrderScreens.Checkout)
    }

    fun uploadOrder(onSuccess : () -> Unit, onField : () -> Unit){
        viewModelScope.launch {
            val currentUser = userRepository.userData.first()
            val userId = currentUser.id

            val orderInformation = cartRepository.getCartData(userId).first()
            if(orderInformation == null){
                onField()
                return@launch
            }

            val firstname = currentUser.firstname
            val lastname = currentUser.lastname

            val currentCartItems = cartRepository.getCartItems(userId).first()

            val subtotal = currentCartItems.sumOf { it?.totalPrice ?: 0.0 }
            val delivery = 55.0
            val service = 8.0
            val totalPrice = subtotal + delivery + service

            val orderItems = currentCartItems.mapNotNull { item ->
                item?.let {
                    OrderItemsClass(
                        mealId = it.mealId,
                        mealName = it.name,
                        size = it.size,
                        price = it.priceOfOne,
                        quantity = it.quantity,
                        image = it.image,
                        type = it.type
                    )
                }

            }

            val order = OrdersClass(
                date = "",
                state = OrderStatesEnum.PREPARING.rawValue,
                subtotal = subtotal,
                delivery = delivery,
                service = service,
                totalPrice = totalPrice,
                userInformation = UserInformationInOrderClass(
                    "$firstname $lastname",
                    phoneNumberState.text.toString(),
                    if (additionalDirectionsState.text.isNotEmpty() || addressLabelState.text.isNotEmpty())
                        address1 + address2
                    else address1,
                    "(${_locationState.value.latitude} , ${_locationState.value.longitude})",
                    _locationState.value.locationFullName
                ),
                orderItems = orderItems,
                orderHistory = emptyList(),
                restaurantName = orderInformation.restaurantName,
                restaurantImage = orderInformation.restaurantImage,
                restaurantId = orderInformation.restaurantId,
            )

            _confirmOrderState.value = ActionsStates.Loading

            val result = orderRepository.uploadOrderRequest(order, userId)

            if(result.isSuccess){
                clearAllCart()
                onSuccess()
                _confirmOrderState.value = ActionsStates.Success
            }else{
                onField()
                _confirmOrderState.value = ActionsStates.Failed("Network error")
            }
        }
    }

    private fun clearAllCart(){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.clearAllCart(userId)
        }
    }
}