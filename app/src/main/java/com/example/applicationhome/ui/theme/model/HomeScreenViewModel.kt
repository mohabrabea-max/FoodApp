package com.example.applicationhome.ui.theme.model

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.data.model.Categories
import com.example.applicationhome.data.data.model.Offers
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.data.remote.NetworkObserver
import com.example.applicationhome.data.data.repository.FavoriteRepository
import com.example.applicationhome.data.data.repository.HomeScreenRepository
import com.example.applicationhome.data.data.repository.ItemScreenRepository
import com.example.applicationhome.domain.GetFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val itemScreenRepository : ItemScreenRepository,
    private val favoriteRepository : FavoriteRepository,
    private val homeScreenRepository : HomeScreenRepository,
    private val getFavoriteUseCase : GetFavoriteUseCase,
    private val networkObserver: NetworkObserver
) : ViewModel(){


    //       *** ---------------------------- \\***  Home Screen Items  ***// ---------------------------- ***

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


    //       *** ---------------------------- \\***  Item Screen  ***// ---------------------------- ***

    fun selectRestaurant(item : Restaurants){
        itemScreenRepository.selectRestaurant(item)
        viewModelScope.launch {
            try {
                val newData = favoriteRepository.getRestaurantToView(item.id)
                if(newData != null) itemScreenRepository.selectRestaurant(newData)
            }catch (e : Exception){
                Log.e("SelectRestaurant", "Error fetching fresh data", e)
            }
        }
    }

    fun selectedtype(index : Int, type : String){
        itemScreenRepository.selectedTypeInRestaurant(index, type)
    }


//       *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    fun addRestaurantsFavorite(restaurants: FavoriteRestaurantDatabase){
        viewModelScope.launch {
            getFavoriteUseCase.addRestaurantsFavorite(restaurants)
        }
    }

    fun removeRestaurantsFavorite(resId : Int){
        viewModelScope.launch {
            getFavoriteUseCase.removeRestaurantsFavorite(resId)
        }
    }

    fun isRestaurantInFavorite(resId : Int): Flow<Boolean> {
        return getFavoriteUseCase.isRestaurantInFavorite(resId)
    }
}