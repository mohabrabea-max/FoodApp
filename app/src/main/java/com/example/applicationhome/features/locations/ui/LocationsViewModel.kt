package com.example.applicationhome.features.locations.ui

import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.R
import com.example.applicationhome.core.domain.repository.AddressesRepository
import com.example.applicationhome.core.domain.repository.LocationRepository
import com.example.applicationhome.core.domain.repository.SyncAllDataRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.AddAddressUseCase
import com.example.applicationhome.core.domain.usecase.ValidateFormUseCase
import com.example.applicationhome.data.data.model.AddressesUiState
import com.example.applicationhome.data.data.model.CheckoutFormState
import com.example.applicationhome.data.data.model.EditAddressModeState
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.data.model.LocationsScreenDialogs
import com.example.applicationhome.data.data.model.LocationsScreens
import com.example.applicationhome.data.data.model.MapEntryPoint
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
class LocationsViewModel @Inject constructor(
    private val locationRepository : LocationRepository,
    private val addressesRepository : AddressesRepository,
    private val syncAllDataRepository: SyncAllDataRepository,
    private val validateFormUseCase : ValidateFormUseCase,
    private val addAddressUseCase : AddAddressUseCase,
    private val userRepository : UserRepository,
    private val networkObserver: NetworkObserver
): ViewModel() {
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

    val addresses =
        userRepository.userData.flatMapLatest {
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


    private val _uiState = MutableStateFlow(AddressesUiState())
    val uiState = _uiState.asStateFlow()

    private val _checkoutFormState = CheckoutFormState()
    val checkoutFormState = _checkoutFormState


    // --------------------------------------------\\ Screens //--------------------------------------------
    private val _backStack = MutableStateFlow<List<LocationsScreens>>(
        listOf(LocationsScreens.Locations)
    )

    val currentScreen = _backStack
        .map { it.last() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LocationsScreens.Locations
        )

    val topBarTitle : StateFlow<Int> =
        _backStack.map { screen ->
            when(val item = screen.last()){
                LocationsScreens.Locations -> {
                    R.string.addresses
                }

                is LocationsScreens.ViewAddressInformation -> {
                    when(item.state){
                        EditAddressModeState.Edit -> R.string.edite_address
                        EditAddressModeState.ReadOnly -> R.string.address_details
                    }
                }

                is LocationsScreens.Map -> {
                    R.string.location
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = R.string.location
        )


    fun navigateTo(screen : LocationsScreens) {
        _backStack.update { it + screen }
    }

    fun navigateBack(onExit : () -> Unit){
        _backStack.update { currentStack ->
            if(currentStack.size > 1){
                currentStack.dropLast(1)
            }else{
                onExit()
                currentStack
            }
        }
    }


    // --------------------------------------------\\ Addresses //--------------------------------------------

    private val _selectedAddress = MutableStateFlow<AddressesEntity?>(null)
    val selectedAddress = _selectedAddress.asStateFlow()

    private val _screenState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val screenState = _screenState.asStateFlow()

    private suspend fun syncAddresses(){
        _screenState.value = HomeUiState.Loading

        val id = userRepository.userData.value.id
        if(id.isNotEmpty()){
            val result = syncAllDataRepository.syncAddresses(id)
            _screenState.value = result
        }else{
            _screenState.value = HomeUiState.Success
        }
    }

    fun clearTextFields(){
        _checkoutFormState.addressTitle.clearText()
        _checkoutFormState.houseState.clearText()
        _checkoutFormState.streetState.clearText()
        _checkoutFormState.phoneNumberState.clearText()
        _checkoutFormState.addressLabelState.clearText()
        _checkoutFormState.additionalDirectionsState.clearText()
    }

    fun selectAddress(address : AddressesEntity){
        _selectedAddress.value = address
    }

    private fun unSelectAddress(){
        _selectedAddress.value = null
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

    fun onSaveAddress(){
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
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isButtonClicked = true,
                            streetAndHome = Pair(
                                _checkoutFormState.streetState.text.toString(),
                                _checkoutFormState.houseState.text.toString()
                            )
                        )
                    }

                    addAddressUseCase(
                        userId = userRepository.userData.value.id,
                        addressId = _selectedAddress.value?.addressId?: 0L,
                        title = _checkoutFormState.addressTitle.text.toString(),
                        house = _checkoutFormState.houseState.text.toString(),
                        street = _checkoutFormState.streetState.text.toString(),
                        phoneNumber = _checkoutFormState.phoneNumberState.text.toString(),
                        additionalDirectionsState = _checkoutFormState.additionalDirectionsState.text.toString(),
                        addressLabelState = _checkoutFormState.addressLabelState.text.toString(),
                        latLocation = _uiState.value.locationState.latitude.toString(),
                        lngLocation = _uiState.value.locationState.longitude.toString(),
                        locationName = _uiState.value.locationState.locationName,
                        locationFullName = _uiState.value.locationState.locationFullName
                    )

                    clearTextFields()

                    _backStack.update{ listOf(LocationsScreens.Locations) }
                }
            }

            else -> {
                _uiState.update { it.copy(isButtonClicked = false) }
            }
        }
    }


    private val _addressToDelete = MutableStateFlow<Pair<String, Long>?>(null)
    fun deleteAddress(){
        viewModelScope.launch {
            val address = _addressToDelete.value
            if(address != null) addressesRepository.deleteAddress(
                userId = address.first,
                addressId = address.second
            )

            closeAlertDialogMessage()
        }
    }

    init {
        viewModelScope.launch {
            isNetworkAvailable.collect { available ->
                if(available){
                    if(_screenState.value != HomeUiState.Success) syncAddresses()
                }else{
                    if(_screenState.value != HomeUiState.Success) _screenState.value = HomeUiState.Offline
                }
            }
        }

        viewModelScope.launch {
            _selectedAddress.collect { address ->
                if(address != null){
                    clearTextFields()

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

        viewModelScope.launch {
            currentScreen.collect { screen ->
                if(screen == LocationsScreens.Locations){
                    unSelectAddress()
                }
            }
        }
    }


    // --------------------------------------------\\ Map //--------------------------------------------

    private fun onLocationSelected(){
        val currentMapScreen = _backStack.value.lastOrNull() as? LocationsScreens.Map ?: return

        when(currentMapScreen.entryPoint){
            MapEntryPoint.Initial -> {
                navigateTo(LocationsScreens.ViewAddressInformation(EditAddressModeState.Edit))
            }

            else -> {
                navigateBack(onExit = {})
            }
        }
    }

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


    // --------------------------------------------\\ Dialogs //--------------------------------------------

    private val _dialogs = MutableStateFlow<LocationsScreenDialogs>(LocationsScreenDialogs.None)
    val dialogs = _dialogs.asStateFlow()

    fun deleteDialog(userId : String, orderId : Long){
        _addressToDelete.value = Pair(userId, orderId)
        _dialogs.value = LocationsScreenDialogs.Delete()
    }

    fun showDetailsDialog(address : AddressesEntity){
        selectAddress(address)
        _dialogs.value = LocationsScreenDialogs.ShowDetailsDialog(address)
    }

    fun closeAlertDialogMessage(){
        _addressToDelete.value = null
        _dialogs.value = LocationsScreenDialogs.None
    }
}