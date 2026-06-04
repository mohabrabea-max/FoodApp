package com.example.applicationhome.view.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuList
import com.example.applicationhome.data.models.repository.MenuRepository.isNetworkAvailable
import com.example.applicationhome.data.models.repository.MenuRepository.uploadCategorieslistFromApi
import com.example.applicationhome.data.models.repository.MenuRepository.uploadFoodMenuFromApi
import com.example.applicationhome.data.models.repository.MenuRepository.uploadOffersFromApi
import com.example.applicationhome.data.models.repository.MenuRepository.uploadRestaurantOffersFromApi
import com.example.applicationhome.data.models.repository.MenuRepository.uploadRestaurantsFromApi
import com.example.applicationhome.data.models.repository.MenuRepository.uploadSnacksMenuFromApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class APIData(application : Application) : AndroidViewModel(application){
       //   دي عشان نظهر رسالة تحذير لما النت يفصل


    init {
        observeNetworkChanges() //  أول ما الـ ViewModel يبدأ، بنشغل مراقب النت فوراً
    }

    private fun observeNetworkChanges(){
        val connectivityManager = getApplication<Application>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isNetworkAvailable = true
                if(foodMenuList.isEmpty()){
                    loadDataFromApi()
                }
            }
            override fun onLost(network: Network){
                super.onLost(network)
                isNetworkAvailable = false
            }
        })
    }

    fun loadDataFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            uploadRestaurantsFromApi()
        }
        viewModelScope.launch(Dispatchers.IO) {
            uploadCategorieslistFromApi()
        }
        viewModelScope.launch(Dispatchers.IO) {
            uploadOffersFromApi()
        }
    }

    fun loadRestaurantData(resId : Int){
        viewModelScope.launch(Dispatchers.IO) {
            uploadFoodMenuFromApi(resId)
        }
        viewModelScope.launch(Dispatchers.IO) {
            uploadSnacksMenuFromApi(resId)
        }
        viewModelScope.launch(Dispatchers.IO) {
            uploadRestaurantOffersFromApi(resId)
        }
    }
}