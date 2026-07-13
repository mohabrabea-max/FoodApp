package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.model.Categories
import com.example.applicationhome.data.data.model.Offers
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.data.remote.NetworkObserver
import com.example.applicationhome.data.data.repository.HomeScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val homeScreenRepository : HomeScreenRepository,
    private val networkObserver: NetworkObserver
) : ViewModel(){
    var selected by mutableStateOf(0)

    var typ by mutableStateOf("All")


    private val _restaurantsMenu = mutableStateMapOf<String, Restaurants>()

    val restaurantsMenuIsLoading : StateFlow<Boolean> = homeScreenRepository.restaurantsMenuIsLoading


    val filterRestaurants = derivedStateOf {
        if(typ == "All"){
            _restaurantsMenu.values.toList()
        }else{
            _restaurantsMenu.filter { it.value.typ.contains(typ) }.values.toList()
        }
    }


    private val _categories = mutableStateListOf<Categories>()
    val categories : List<Categories> get() = _categories
    val categoriesIsLoading : StateFlow<Boolean> = homeScreenRepository.categoriesIsLoading


    private val _offers = mutableStateListOf<Offers>()
    val offers : List<Offers> get() = _offers
    val offersIsLoading : StateFlow<Boolean> = homeScreenRepository.offersIsLoading

    var isNetworkAvailable by mutableStateOf(true)


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