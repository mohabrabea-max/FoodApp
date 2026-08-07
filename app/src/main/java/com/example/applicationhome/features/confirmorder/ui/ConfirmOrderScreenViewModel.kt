package com.example.applicationhome.features.confirmorder.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.LocationRepository
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CartUseCase
import com.example.applicationhome.data.data.model.MapUiState
import com.example.applicationhome.data.data.model.OrderItemsClass
import com.example.applicationhome.data.data.model.OrdersClass
import com.example.applicationhome.data.data.model.ProfileEditResult
import com.example.applicationhome.data.data.model.TextFieldClassFromConfirmOrderScreen
import com.example.applicationhome.data.data.model.UserInformationInOrderClass
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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
) : ViewModel() {

    val phoneNumberState = TextFieldState()
    val houseState = TextFieldState()
    val housetextFieldState = MutableStateFlow(false)
    val streetState = TextFieldState()
    val streettextFieldState = MutableStateFlow(false)

    val additionalDirectionsState = TextFieldState()
    val addressLabelState = TextFieldState()

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

    val confirmOrderPages = MutableStateFlow(0)

    val cartItems = cartRepository.cartItems

    val userData = userRepository.userData

    val totalPrice = cartRepository.totalPrice

    val loading : StateFlow<Boolean> = orderRepository.loading

    val bottonState = MutableStateFlow(false)

    val isButtonClicked = MutableStateFlow(false)

    var address1 = ""
    var address2 = ""

    private val _confirmOrderError = MutableStateFlow<ProfileEditResult?>(null)
    val confirmOrderError = _confirmOrderError.asStateFlow()

    private val _locationState = MutableStateFlow(MapUiState())
    val locationState = _locationState.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO){
            userData.collect { user ->
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
        }

    }

    fun bottonStateChange(){
        if(
            houseState.text.isNotEmpty() &&
            streetState.text.isNotEmpty() &&
            phoneNumberState.text.isNotEmpty()
        ){
            bottonState.value = true
        }else{
            bottonState.value = false
        }
    }

    fun bottonstate(){
        val validPrefixes = listOf("010", "011", "012", "015")

        if(
            (
                phoneNumberState.text.length != 11
                        || !validPrefixes.any { phoneNumberState.text.contains(it) }
            )
            && phoneNumberState.text.isNotEmpty()
        ) {
            isButtonClicked.value = false
            _confirmOrderError.value = ProfileEditResult.PhoneNumberIncomplete
            return
        }

        if(
            houseState.text.isEmpty()
            && streetState.text.isEmpty()
        ){
            isButtonClicked.value = false
            _confirmOrderError.value = ProfileEditResult.DataIncomplete
            return
        }


        isButtonClicked.value = true
        _confirmOrderError.value = ProfileEditResult.Success

        address1 = "${houseState.text} - ${streetState.text}"
        address2 = "${
            if(additionalDirectionsState.text.isNotEmpty()) " - " + additionalDirectionsState.text
            else ""
        } ${
            if(addressLabelState.text.isNotEmpty()) " - " + addressLabelState.text
            else ""
        }"

        nextPage()
    }

    fun cleanTextField(){
        houseState.clearText()
        streetState.clearText()
        phoneNumberState.clearText()
        additionalDirectionsState.clearText()
        addressLabelState.clearText()
        housetextFieldState.value = false
        streettextFieldState.value = false
    }

    fun uploadOrder(onSuccess: () -> Unit){
        viewModelScope.launch {
            val currentUser = userRepository.userData.first()
            val userId = currentUser.id

            val orderInformation = cartRepository.getCartData(userId).first()
            val currentCartItems = cartRepository.getCartItems(userId).first()

            val firstname = currentUser.firstname
            val lastname = currentUser.lastname


            var subtotal = 0.0
            currentCartItems.forEach { item ->
                subtotal += item?.totalPrice ?: 0.0
            }
            val delivery = 55.0
            val service = 8.0
            val totalPrice = subtotal + delivery + service

            val orderItems = mutableListOf<OrderItemsClass>()
            currentCartItems.forEach { item ->
                orderItems += OrderItemsClass(
                    item?.mealId ?: 0,
                    item?.name ?: "",
                    item?.size ?: "",
                    item?.priceOfOne ?: 0.0,
                    item?.quantity ?: 0,
                    item?.image ?: "",
                    item?.type ?: ""
                )
            }
            if(orderInformation != null){
                val order = OrdersClass(
                    "",
                    "Preparing",
                    subtotal,
                    delivery,
                    service,
                    totalPrice,
                    UserInformationInOrderClass(
                        "$firstname $lastname",
                        phoneNumberState.text.toString(),
                        if (additionalDirectionsState.text.isNotEmpty() || addressLabelState.text.isNotEmpty())
                            address1 + address2
                        else address1,
                        "(${_locationState.value.latitude} , ${_locationState.value.longitude})",
                        _locationState.value.locationFullName
                    ),
                    orderItems,
                    orderInformation.restaurantName,
                    orderInformation.restaurantImage,
                    orderInformation.restaurantId
                )
                orderRepository.uploadOrderRequest(order, userId)
            }
            onSuccess()
            cleanTextField()
        }
    }

    fun clearAllCart(){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            cartUseCase.clearAllCart(userId)
        }
    }

    fun nextPage(){
        confirmOrderPages.value += 1
    }

    fun lastPage(){
        confirmOrderPages.value -= 1
    }

    fun changePageNumber(number : Int){
        confirmOrderPages.value = number
    }
}