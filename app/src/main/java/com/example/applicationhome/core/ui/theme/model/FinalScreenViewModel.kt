package com.example.applicationhome.core.ui.theme.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.SyncAllDataRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.local.entity.UserClass
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
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
    networkObserver: NetworkObserver
) : ViewModel() {
    val isNetworkAvailable = networkObserver.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    private val _syncDataUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val syncDataUiState = _syncDataUiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()


    fun refreshData(){
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val network = isNetworkAvailable.first()
                val user = userRepository.userData.first()

                executeSync(network, user, true)
            } catch (e: Exception) {
                Log.e("RefreshError", e.message.toString())
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private suspend fun executeSync(
        network: Boolean,
        user: UserClass,
        isForceRefresh: Boolean = false
    ){
        if (!network) return

        if (isForceRefresh || _syncDataUiState.value != HomeUiState.Success) {
            _syncDataUiState.value = HomeUiState.Loading
            try {
                syncAllDataRepository.syncDataParallel()
                _syncDataUiState.value = HomeUiState.Success
            } catch (e: Exception) {
                _syncDataUiState.value = HomeUiState.Offline
            }
        }

        if (user.id.isNotEmpty()) {
            val isValid = userRepository.validateUserOnAppLaunch()
            if (isValid) {
                try {
                    syncAllDataRepository.syncFavoritesInDatabase(user.id)
                } catch (e: Exception) {
                    Log.e("FavoriteSyncError", "الخلل هنا: ${e.javaClass.simpleName} - ${e.message}", e)
                }
            }
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
            ) { network, user -> Pair(network, user) }
                .distinctUntilChanged()
                .collect { (network, user) ->
                    executeSync(network, user)
                }
        }
    }
}