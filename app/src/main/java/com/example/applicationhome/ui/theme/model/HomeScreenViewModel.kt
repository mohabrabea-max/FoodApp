package com.example.applicationhome.ui.theme.model

import android.app.Application
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.NetworkObserver
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.Offers
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.repository.HomeScreenRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    application : Application,
    private val homeScreenRepository : HomeScreenRepository
) : AndroidViewModel(application){
    var selected by mutableStateOf(0)

    var typ by mutableStateOf("All")


    private val _restaurantsMenu = mutableStateMapOf<String, Restaurants>()

    val restaurantsMenuIsLoading : StateFlow<Boolean> = homeScreenRepository.restaurantsMenuIsLoading


    val filterRestaurants = derivedStateOf {
        if(typ == "All"){
            _restaurantsMenu.values
        }else{
            _restaurantsMenu.filter { it.value.typ.contains(typ) }.values
        }
    }


    private val _categories = mutableStateListOf<Categories>()
    val categories : List<Categories> get() = _categories
    val categoriesIsLoading : StateFlow<Boolean> = homeScreenRepository.categoriesIsLoading


    private val _offers = mutableStateListOf<Offers>()
    val offers : List<Offers> get() = _offers
    val offersIsLoading : StateFlow<Boolean> = homeScreenRepository.offersIsLoading


    private val networkObserver = NetworkObserver(application.applicationContext)
    var isNetworkAvailable by mutableStateOf(false)


    init {
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
            val restaurants = homeScreenRepository.getRestaurantsFromApi()
            _restaurantsMenu += restaurants
        }
        viewModelScope.launch {
            _categories.clear()
            _categories += homeScreenRepository.getCategorieslistFromApi()
        }
        viewModelScope.launch {
            _offers.clear()
            _offers += homeScreenRepository.getOffersFromApi()
        }
        viewModelScope.launch {
            homeScreenRepository.restaurantCount()
        }
    }

    fun selected(item : Categories){
        selected = item.id
        typ = item.type
    }

    fun unSelected(){
        selected = 0
        typ = "All"
    }
}