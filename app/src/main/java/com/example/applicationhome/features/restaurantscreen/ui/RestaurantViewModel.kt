package com.example.applicationhome.features.restaurantscreen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.HomeScreenRepository
import com.example.applicationhome.core.domain.repository.ItemScreenRepository
import com.example.applicationhome.core.domain.repository.RestaurantScreenRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CartUseCase
import com.example.applicationhome.core.domain.usecase.GetFavoriteUseCase
import com.example.applicationhome.data.data.model.Drink
import com.example.applicationhome.data.data.model.Offers
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.SnacksEntity
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val networkObserver: NetworkObserver,
    homeScreenRepository: HomeScreenRepository,
    cartRepository: CartRepository,
    private val restaurantScreenRepository : RestaurantScreenRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userRepository: UserRepository,
    private val itemScreenRepository : ItemScreenRepository,
    private val cartUseCase : CartUseCase,
    private val getFavoriteUseCase : GetFavoriteUseCase
): ViewModel(){

    val userData = userRepository.userData


//    *** ---------------------------- \\***  Restaurant Items  ***// ---------------------------- ***

    val resid = itemScreenRepository.resId

    val selectedTypeIndex = itemScreenRepository.selectedTypeIndex
    val typeInRestaurantScreen = itemScreenRepository.typeInRestaurantScreen

    val restaurantCount = homeScreenRepository.restaurantCount


    private val _foodMenuList : StateFlow<List<MealsEntity>> =
        resid.flatMapLatest { item ->
            restaurantScreenRepository.getMealsFromDatabase(item)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val foodMenuList : StateFlow<List<MealsEntity>> = combine(
        _foodMenuList,
        resid,
        typeInRestaurantScreen
    ) { menuList, resId, type ->
        menuList.filter {
            it.restaurantId == resId
                    && it.category == type
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val foodMenuListIsLoading : StateFlow<Boolean> = restaurantScreenRepository.foodMenuListIsLoading

    private val _snackMenuList : StateFlow<List<SnacksEntity>> =
        resid.flatMapLatest { item ->
            restaurantScreenRepository.getSnacksFromDatabase(item)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val snackMenuList : StateFlow<List<SnacksEntity>> = combine(
        _snackMenuList,
        resid
    ) { menuMap, resId ->
        menuMap.filter { it.restaurantId == resId }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val snacksIsLoading : StateFlow<Boolean> = restaurantScreenRepository.snacksIsLoading



    private val _drinkMenuMap = MutableStateFlow<Map<String, Drink>>(emptyMap())

    val drinkMenuList = combine(
        _drinkMenuMap,
        resid
    ) { menuMap, resId ->
        menuMap.filter { it.value.restaurantId == resId }.values
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val drinkMenuIsLoading : StateFlow<Boolean> = restaurantScreenRepository.drinkMenuIsLoading


    val _restaurantOffersMenuMap = MutableStateFlow<Map<String, Offers>>(emptyMap())

    val restaurantOffersMenuList = combine(
        _restaurantOffersMenuMap,
        resid
    ) { menuMap, resId ->
        menuMap.filter { it.value.restaurantId == resId }.values.toList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val restaurantOffersLoading : StateFlow<Boolean> = restaurantScreenRepository.restaurantOffersLoading

    val isNetworkAvailable = MutableStateFlow(false)


    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable.value = available
            }
        }
    }



    fun restaurantData(){
        val restaurantsCount = restaurantCount[resid.value]
        if(restaurantsCount != null){
            viewModelScope.launch {
                val offersMenu = async {
                    if(restaurantOffersMenuList.value.size < restaurantsCount.offers){
                        restaurantScreenRepository.uploadRestaurantOffersFromApi(resid.value)
                    } else { emptyMap() }
                }

                _restaurantOffersMenuMap.value += offersMenu.await()
            }
        }
    }

    fun selectedtype(index : Int, type : String){
        itemScreenRepository.selectedTypeInRestaurant(index, type)
    }


//       *** ---------------------------- \\***  Cart  ***// ---------------------------- ***

    val errorInCart = MutableStateFlow(false)

    val cartInformation = cartRepository.cartInformation

    val cartItems = cartRepository.cartItems

    val totalNumber = cartRepository.totalNumber

    val totalPrice = cartRepository.totalPrice

    val newCount = MutableStateFlow(0)

    val newFoodInCart = MutableStateFlow<CartItemsClass?>(null)
    val newFoodInCartSize = MutableStateFlow<String?>(null)


    fun plus(food: CartItemsClass, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            val state = cartUseCase.plus(userId, food, size)
            if(state != null){
                alertDialogTrue()
                newFoodInCartSize.value = state.first
                newFoodInCart.value = state.second
            }
        }
    }

    fun minus(food: CartItemsClass, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.minus(userId, food, size)
        }
    }

    fun clearAndStartNewCart(count : Int) {
        viewModelScope.launch {
            val newFood = newFoodInCart.value
            val newSize = newFoodInCartSize.value
            val userId = userRepository.userData.value.id
            val finally = cartUseCase.clearAndStartNewCart(userId, newFoodInCart.value, newFoodInCartSize.value)

            if(finally && newFood != null && newSize != null){
                cartUseCase.updateCount(userId, newFood, newSize, count)
                deletenewCount()
                newFoodInCart.value = null
                newFoodInCartSize.value = null
            }
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.delete(userId, foodId, size)
        }
    }

    fun deletenewCount(){
        newCount.value = 0
    }

    fun alertDialogTrue(){
        errorInCart.value = true
    }

    fun alertDialogFalse(){
        errorInCart.value = false
    }


    //       *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***


    val favoriteMealsIds = favoriteRepository.favoriteMealsIds

    val favoriteSnacksIds = favoriteRepository.favoriteSnacksIds

    val favoriteRestaurantsIds = favoriteRepository.favoriteRestaurantsIds


    fun addMealFavorite(food : FavoriteMealEntity){
        viewModelScope.launch {
            getFavoriteUseCase.addMealFavorite(food)
        }
    }
    fun addSnackFavorite(snack : FavoriteSnackEntity){
        viewModelScope.launch {
            getFavoriteUseCase.addSnackFavorite(snack)
        }
    }
    fun addRestaurantsFavorite(restaurants: FavoriteRestaurantEntity){
        viewModelScope.launch {
            getFavoriteUseCase.addRestaurantsFavorite(restaurants)
        }
    }


    fun removeMealFavorite(mealId : Int){
        viewModelScope.launch {
            getFavoriteUseCase.removeMealFavorite(mealId)
        }
    }
    fun removeSnackFavorite(snackId : Int){
        viewModelScope.launch {
            getFavoriteUseCase.removeSnackFavorite(snackId)
        }
    }
    fun removeRestaurantsFavorite(resId : Int){
        viewModelScope.launch {
            getFavoriteUseCase.removeRestaurantsFavorite(resId)
        }
    }


    //       *** ---------------------------- \\***  Item Screen  ***// ---------------------------- ***

    val selectedRestaurant = itemScreenRepository.selectedRestaurant


    fun selectMeal(item: MealsEntity, size : String) {
        itemScreenRepository.selectMeal(item, size)
    }
    fun selectSnack(item: SnacksEntity, size : String){
        itemScreenRepository.selectSnack(item, size)
    }
}