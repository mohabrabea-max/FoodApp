package com.example.applicationhome.ui.theme.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.NetworkObserver
import com.example.applicationhome.data.models.repository.MenuRepository.uploadFoodMenuFromApi
import com.example.applicationhome.data.models.repository.MenuRepository.uploadRestaurantOffersFromApi
import com.example.applicationhome.data.models.repository.MenuRepository.uploadSnacksMenuFromApi
import kotlinx.coroutines.launch

class RestaurantViewModel (application : Application) : AndroidViewModel(application){
    private val networkObserver = NetworkObserver(application.applicationContext)
    var isNetworkAvailable by mutableStateOf(false)
    var resid by mutableStateOf(0)

    init { //  أول ما الـ ViewModel يبدأ، بنشغل مراقب النت فوراً
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable = available
            }
        }
    }

    fun loadRestaurantId(resId : Int){
        resid = resId
    }

    fun restaurantData(){
        viewModelScope.launch {
            uploadFoodMenuFromApi(resid)
        }
        viewModelScope.launch {
            uploadSnacksMenuFromApi(resid)
        }
        viewModelScope.launch {
            uploadRestaurantOffersFromApi(resid)
        }
    }
}