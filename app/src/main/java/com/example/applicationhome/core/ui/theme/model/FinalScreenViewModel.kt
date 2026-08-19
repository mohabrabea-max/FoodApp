package com.example.applicationhome.core.ui.theme.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.SyncAllDataRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.data.model.UserUiState
import com.example.applicationhome.data.local.entity.UserClass
import com.example.applicationhome.data.remote.NetworkObserver
import com.example.applicationhome.features.WelcomeScreen.repository.WelcomeScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinalScreenViewModel @Inject constructor(
    private val syncAllDataRepository : SyncAllDataRepository,
    private val userRepository : UserRepository,
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

    private suspend fun syncFavorite(user : UserClass){
        if (user.id.isNotEmpty()) {
            val isValid = userRepository.validateUserOnAppLaunch()

            if(isValid){
                try {
                    syncAllDataRepository.syncFavoritesInDatabase(user.id)
                } catch (e: Exception) {
                    _syncUserUiState.value = UserUiState.GuestMode
                    return                }
            }else{
                _syncUserUiState.value = UserUiState.GuestMode
                return
            }
        }else{
            _syncUserUiState.value = UserUiState.GuestMode
            return
        }
    }


    init {
        viewModelScope.launch {
            userRepository.observeSessionStatus()
        }

        viewModelScope.launch {
            userRepository.userData.collectLatest { user ->
                syncFavorite(user)
            }
        }

        viewModelScope.launch {
            isNetworkAvailable.collectLatest { network ->
                if(_syncDataUiState.value == HomeUiState.Success){
                    return@collectLatest
                }

                executeSync(network)
            }
        }
    }
}