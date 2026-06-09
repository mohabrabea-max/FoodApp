package com.example.applicationhome.ui.theme.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.NetworkObserver
import com.example.applicationhome.data.models.repository.MenuRepository.foodMenuList
import com.example.applicationhome.data.models.repository.MenuRepository.restaurantOffers
import com.example.applicationhome.data.models.repository.MenuRepository.restaurantcount
import com.example.applicationhome.data.models.repository.MenuRepository.snacks
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
        val restaurantscount = restaurantcount?.get(resid)
        if(restaurantscount != null){
            viewModelScope.launch {
                if(foodMenuList.distinctBy { it.id }.count { it.restaurantId == resid } < restaurantscount.meals){
                    uploadFoodMenuFromApi(resid)
                }
                if(snacks.distinctBy { it.id }.count { it.restaurantId == resid } < restaurantscount.snacks){
                    uploadSnacksMenuFromApi(resid)
                }
                if(restaurantOffers.distinctBy { it.id }.count { it.restaurantId == resid } < restaurantscount.offers){
                    uploadRestaurantOffersFromApi(resid)
                }
            }
        }
    }
}