package com.example.applicationhome.ui.theme.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.NetworkObserver
import com.example.applicationhome.data.models.repository.MenuRepository.restaurantCount
import com.example.applicationhome.data.models.repository.MenuRepository.restaurantcount
import com.example.applicationhome.data.models.repository.MenuRepository.uploadCategorieslistFromApi
import com.example.applicationhome.data.models.repository.MenuRepository.uploadOffersFromApi
import com.example.applicationhome.data.models.repository.MenuRepository.uploadRestaurantsFromApi
import kotlinx.coroutines.launch

class APIData(application : Application) : AndroidViewModel(application){
    private val networkObserver = NetworkObserver(application.applicationContext)
    var isNetworkAvailable by mutableStateOf(false)


    init { //  أول ما الـ ViewModel يبدأ، بنشغل مراقب النت فوراً
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable = available
                if(available){
                    loadDataFromApi()
                }
            }
        }
    }

    fun loadDataFromApi() {
        viewModelScope.launch {
            uploadRestaurantsFromApi()
        }
        viewModelScope.launch {
            uploadCategorieslistFromApi()
        }
        viewModelScope.launch {
            uploadOffersFromApi()
        }
        viewModelScope.launch {
            restaurantcount = restaurantCount()?.toMutableMap()
        }
    }
}