package com.example.applicationhome.core.ui.components.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.SyncAllDataRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.repository.WelcomeScreenRepository
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.data.model.UserUiState
import com.example.applicationhome.data.local.entity.UserClass
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinalScreenViewModel @Inject constructor(
    private val syncAllDataRepository : SyncAllDataRepository,
    private val userRepository : UserRepository,
    private val cartRepository: CartRepository,
    welcomeScreenRepository : WelcomeScreenRepository,
    networkObserver: NetworkObserver
) : ViewModel() {
    val isFirsTimeToOpenApp = welcomeScreenRepository.isFirsTimeToOpenApp

    val isNetworkAvailable = networkObserver.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    private val _syncDataUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val syncDataUiState = _syncDataUiState.asStateFlow()

    private val _syncUserUiState = MutableStateFlow<UserUiState>(UserUiState.Starting)
    val syncUserUiState = _syncUserUiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private var userId = ""

    fun refreshData(){
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val network = isNetworkAvailable.first()

                executeSync(network, true)
            } catch (e: Exception) {
                Log.e("RefreshError", e.message.toString())
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private suspend fun executeSync(
        network : Boolean,
        isForceRefresh : Boolean = false
    ){
        if(!network){
            _syncDataUiState.value = HomeUiState.Offline
            return
        }

        if (isForceRefresh || _syncDataUiState.value != HomeUiState.Success){
            try {
                _syncDataUiState.value = HomeUiState.Loading
                syncAllDataRepository.syncDataParallel()
                _syncDataUiState.value = HomeUiState.Success
            } catch (e: Exception) {
                _syncDataUiState.value = HomeUiState.Offline
            }
        }
    }

    private suspend fun syncFavorite(user : UserClass, network: Boolean){
        if(user.id.isEmpty()){
            _syncUserUiState.value = UserUiState.GuestMode
            return
        }

        if(userId == user.id) return

        if(!network){
            _syncUserUiState.value = UserUiState.Offline
            return
        }

        val isValid = userRepository.validateUserOnAppLaunch()

        if(!isValid){
            _syncUserUiState.value = UserUiState.GuestMode
            return
        }

        try {
            syncAllDataRepository.syncFavoritesInDatabase(user.id)
            userId = user.id
            _syncUserUiState.value = UserUiState.Success
        } catch (e: Exception) {
        }
    }


    init {
        viewModelScope.launch {
            userRepository.observeSessionStatus()
        }

        viewModelScope.launch {
            combine(
                isNetworkAvailable,
                userRepository.userData
            ){ network, user ->
                Pair(network, user)
            }.distinctUntilChanged()
            .collectLatest { (network, user) ->
                syncFavorite(user, network)
            }
        }

        viewModelScope.launch {
            isNetworkAvailable.collect { network ->
                if(_syncDataUiState.value == HomeUiState.Success) return@collect

                executeSync(network)
            }
        }
    }
}