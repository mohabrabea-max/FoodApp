package com.example.applicationhome.data.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NetworkObserver @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable : StateFlow<Boolean> = _isNetworkAvailable.asStateFlow()

    init{
        observeNetworkChanges()
    }

    private fun observeNetworkChanges(){
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(
            networkRequest,
            object : ConnectivityManager.NetworkCallback(){
                override fun onAvailable(network: Network){
                    super.onAvailable(network)
                    _isNetworkAvailable.value = true
                }

                override fun onLost(network: Network){
                    super.onLost(network)
                    _isNetworkAvailable.value = false
                }
            }
        )
    }
}