package com.example.applicationhome.core.ui.theme.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.SyncAllDataRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinalScreenViewModel @Inject constructor(
    private val syncAllDataRepository : SyncAllDataRepository,
    private val favoriteRepository : FavoriteRepository,
    private val userRepository : UserRepository,
    networkObserver: NetworkObserver
) : ViewModel() {
    val isNetworkAvailable = networkObserver.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    init {
        viewModelScope.launch {
            combine(
                isNetworkAvailable,
                userRepository.userData
            ){ network, user -> Pair(network, user) }
            .distinctUntilChanged()
            .collect { (network, user) ->
                if (network) {
                    syncAllDataRepository.syncDataParallel()
                    if(user.id.isNotEmpty()) favoriteRepository.syncFavoritesInDatabase(user.id)
                }
            }
        }
    }
}