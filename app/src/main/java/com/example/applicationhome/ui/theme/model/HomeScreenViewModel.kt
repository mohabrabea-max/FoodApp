package com.example.applicationhome.ui.theme.model

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
import com.example.applicationhome.data.data.repository.UserRepository
import com.example.applicationhome.domain.GetFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val itemScreenRepository : ItemScreenRepository,
    private val favoriteRepository : FavoriteRepository,
    private val homeScreenRepository : HomeScreenRepository,
    userRepository: UserRepository,
    private val getFavoriteUseCase : GetFavoriteUseCase,
    private val networkObserver: NetworkObserver
) : ViewModel(){

    val userData = userRepository.userData

    //       *** ---------------------------- \\***  Home Screen Items  ***// ---------------------------- ***

    val selected = MutableStateFlow(0)

    private val typ = MutableStateFlow("All")


    private val _restaurantsMenu = MutableStateFlow<Map<String, Restaurants>>(emptyMap())

    val restaurantsMenuIsLoading : StateFlow<Boolean> = homeScreenRepository.restaurantsMenuIsLoading

    val filterRestaurants = combine(
        _restaurantsMenu,
        typ
    ){ restaurants, type ->
        val restaurantsList = restaurants.values.toList()

        if(type == "All"){
            restaurantsList
        }else{
            restaurantsList.filter { it.typ.contains(type) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )




    private val _categories = MutableStateFlow<List<Categories>>(emptyList())
    val categories : StateFlow<List<Categories>> = _categories
    val categoriesIsLoading : StateFlow<Boolean> = homeScreenRepository.categoriesIsLoading


    private val _offers = MutableStateFlow<List<Offers>>(emptyList())
    val offers : StateFlow<List<Offers>> = _offers
    val offersIsLoading : StateFlow<Boolean> = homeScreenRepository.offersIsLoading

    val isNetworkAvailable = MutableStateFlow(true)


    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable.value = available
                if(available){
                    loadDataFromApi()
                }
            }
        }
    }

    fun loadDataFromApi() {
        viewModelScope.launch {
            val restaurants = homeScreenRepository.getRestaurantsFromApi()
            _restaurantsMenu.value += restaurants
        }
        viewModelScope.launch {
            _categories.value = emptyList()
            _categories.value += homeScreenRepository.getCategorieslistFromApi()
        }
        viewModelScope.launch {
            _offers.value = emptyList()
            _offers.value += homeScreenRepository.getOffersFromApi()
        }
        viewModelScope.launch {
            homeScreenRepository.restaurantCount()
        }
    }

    fun select(item : Categories){
        selected.value = item.id
        typ.value = item.type
    }

    fun unSelected(){
        selected.value = 0
        typ.value = "All"
    }


    //       *** ---------------------------- \\***  Item Screen  ***// ---------------------------- ***

    fun selectRestaurant(item : Restaurants){
        itemScreenRepository.selectRestaurant(item)
        viewModelScope.launch {
            try {
                val newData = favoriteRepository.getRestaurantToView(item.id)
                if(newData != null) itemScreenRepository.selectRestaurant(newData)
            }catch (e : Exception){
                null
            }
        }
    }

    fun selectedtype(index : Int, type : String){
        itemScreenRepository.selectedTypeInRestaurant(index, type)
    }


//       *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    val favoriteRestaurantsIds = favoriteRepository.favoriteRestaurantsIds

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
}