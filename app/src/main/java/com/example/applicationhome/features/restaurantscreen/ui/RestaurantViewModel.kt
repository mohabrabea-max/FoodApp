package com.example.applicationhome.features.restaurantscreen.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.RestaurantRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CartUseCase
import com.example.applicationhome.core.domain.usecase.GetFavoriteUseCase
import com.example.applicationhome.data.data.model.AddToCartStates
import com.example.applicationhome.data.data.model.BottomSheetItem
import com.example.applicationhome.data.data.model.CategoriesInWithTitle
import com.example.applicationhome.data.data.model.CategoryEnum
import com.example.applicationhome.data.data.model.CategoryInterface
import com.example.applicationhome.data.data.model.Drink
import com.example.applicationhome.data.data.model.RestaurantUiState
import com.example.applicationhome.data.data.model.ShowSnackBarEvent
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RestaurantViewModel @Inject constructor(
    savedStateHandle : SavedStateHandle,
    networkObserver : NetworkObserver,
    cartRepository : CartRepository,
    private val restaurantRepository : RestaurantRepository,
    private val userRepository : UserRepository,
    private val cartUseCase : CartUseCase,
    private val getFavoriteUseCase : GetFavoriteUseCase
): ViewModel(){

    val userData = userRepository.userData


//    *** ---------------------------- \\***  Restaurant Items  ***// ---------------------------- ***

    private val _resId = MutableStateFlow(0)

    private val _selectedTypeIndex = MutableStateFlow(0)
    val selectedTypeIndex : StateFlow<Int> = _selectedTypeIndex.asStateFlow()
    private val _typeInRestaurantScreen = MutableStateFlow(CategoriesInWithTitle())
    val typeInRestaurantScreen = _typeInRestaurantScreen.asStateFlow()

    val screenCategoryInterface = _typeInRestaurantScreen.map {
        when(it.category){
            CategoryEnum.BURGER.name -> CategoryInterface.Burgers
            CategoryEnum.PIZZA.name -> CategoryInterface.Pizza
            CategoryEnum.CHICKEN.name -> CategoryInterface.Chicken
            CategoryEnum.KOSHARY.name -> CategoryInterface.Koshary
            CategoryEnum.GRILL.name -> CategoryInterface.Grill
            CategoryEnum.SNACKS.name -> CategoryInterface.Snacks
            CategoryEnum.DRINK.name -> CategoryInterface.Drinks
            else -> CategoryInterface.Custom
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoryInterface.Custom
    )

    private val _mealSize = MutableStateFlow("")
    val mealSize : StateFlow<String> = _mealSize.asStateFlow()

    val foodMenuList : Flow<PagingData<MealWithFavoriteStatus>> = combine(
        _resId,
        _typeInRestaurantScreen
    ) { resId, type ->
        Pair(resId, type)
    }.flatMapLatest { (resId, type) ->
        restaurantRepository.getMealsFromDatabase(resId, type.category)
    }.cachedIn(viewModelScope)

    val snackMenuList : Flow<PagingData<SnackWithFavoriteStatus>> =
        _resId.flatMapLatest { resId ->
            restaurantRepository.getSnacksFromDatabase(resId)
        }.cachedIn(viewModelScope)

    private val _drinkMenuMap = MutableStateFlow<Map<String, Drink>>(emptyMap())

    val drinkMenuList = combine(
        _drinkMenuMap,
        _resId
    ) { menuMap, resId ->
        menuMap.filter { it.value.restaurantId == resId }.values
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    val restaurantOffersMenuList : StateFlow<List<OffersEntity>> =
        _resId.flatMapLatest{ id ->
            restaurantRepository.getRestaurantOffersFromDatabase(id)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val isNetworkAvailable =  networkObserver.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    private val _uiState = MutableStateFlow(RestaurantUiState())
    val uiState = _uiState.asStateFlow()

    private val _mealId = MutableStateFlow<Int?>(null)
    private val _snackId = MutableStateFlow<Int?>(null)


    init {
        _resId.value = checkNotNull(savedStateHandle["restaurantId"])
        val mealIdString : String? = savedStateHandle["mealId"]
        val mealId : Int? = mealIdString?.toIntOrNull()

        val snackIdString : String? = savedStateHandle["snackId"]
        val snackId : Int? = snackIdString?.toIntOrNull()

        loadRestaurantDetails(_resId.value, mealId, snackId)

        viewModelScope.launch {
            combine(_mealId, _snackId) { meal, snack ->
                Pair(meal, snack)
            }.flatMapLatest { (meal, snack) ->
                when {
                    meal != null -> {
                        restaurantRepository.getMealByIdFromDatabase(meal)
                            .map {  BottomSheetItem.MealItem(it) }
                    }
                    snack != null -> {
                        restaurantRepository.getSnackByIdFromDatabase(snack)
                            .map {  BottomSheetItem.SnackItem(it) }
                    }
                    else -> flowOf(null)
                }
            }.collect { item ->
                _uiState.update {
                    it.copy(
                        bottomSheetItem = item
                    )
                }
            }
        }
    }


    private fun loadRestaurantDetails(restaurantId : Int, mealId : Int?, snackId : Int?){
        viewModelScope.launch {
            val foodFlow : Flow<BottomSheetItem?> =
                when{
                    mealId != null -> {
                        restaurantRepository.getMealByIdFromDatabase(mealId)
                            .map { BottomSheetItem.MealItem(it) }
                    }
                    snackId != null -> {
                        restaurantRepository.getSnackByIdFromDatabase(snackId)
                            .map { BottomSheetItem.SnackItem(it) }
                    }
                    else -> flowOf(null)
                }

            val restaurantFlow = restaurantRepository.getRestaurantByIdFromDatabase(restaurantId)
                .filterNotNull()

            launch {
                selectedtype(0, restaurantFlow.first().restaurant.typ.minByOrNull { it.index }!!)
            }

            combine(foodFlow, restaurantFlow) { food, res ->
                Pair(food, res)
            }.collect { (meal, restaurant) ->
                _uiState.update {
                    it.copy(
                        restaurantData = restaurant,
                        bottomSheetItem = meal
                    )
                }

                selectSize(meal?.sizes?.keys?.last() ?: "")
            }
        }
    }

    fun selectedtype(index : Int, category : CategoriesInWithTitle){
        _selectedTypeIndex.value = index
        _typeInRestaurantScreen.value = category
    }

    fun selectMeal(id : Int, size : String){
        _mealId.value = id
        _mealSize.value = size
    }

    fun selectSnack(id : Int, size : String){
        _snackId.value = id
        _mealSize.value = size
    }

    fun selectSize(size : String){
        _mealSize.value = size
    }

    fun closeItemScreen(){
        _mealId.value = null
        _snackId.value = null

        _uiState.update {
            it.copy(
                bottomSheetItem = null
            )
        }

        _mealSize.value = ""

        deletenewCount()
        println(_uiState.value.bottomSheetItem)
    }


//       *** ---------------------------- \\***  Cart  ***// ---------------------------- ***

    private val _errorInCart = MutableStateFlow<AddToCartStates>(AddToCartStates.Idle)
    val errorInCart = _errorInCart.asStateFlow()

    private val _snackBarChannel = Channel<ShowSnackBarEvent>(Channel.BUFFERED)
    val snackBarChannel = _snackBarChannel.receiveAsFlow()

    val cartInformation = cartRepository.cartInformation

    val cartItems = cartRepository.cartItems

    val totalNumber = cartRepository.totalNumber

    val totalPrice = cartRepository.totalPrice

    private val newFoodInCart = MutableStateFlow<CartItemsClass?>(null)
    private val newFoodInCartSize = MutableStateFlow<String?>(null)

    private val _newCount = MutableStateFlow(0)
    val newCount = _newCount.asStateFlow()


    fun plus(food: CartItemsClass, size : String, cartNavigation : () -> Unit){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            val state = cartUseCase.plus(userId, food, size)

            when(state){
                AddToCartStates.Success -> {
                    sendAddedToCartChannel{ cartNavigation() }
                }

                is AddToCartStates.ErrorInCartRestaurant -> {
                    alertDialogTrue(
                        AddToCartStates.ErrorInCartRestaurant(
                            title = "Start a new cart?",
                            message = "A new order will clear your cart with '${cartInformation.value?.restaurantName}'",
                            state.food,
                            state.size
                        )
                    )

                    newFoodInCartSize.value = state.size
                    newFoodInCart.value = state.food
                }

                is AddToCartStates.ErrorInLoginState -> {
                    alertDialogTrue(
                        AddToCartStates.ErrorInLoginState(
                            title = "Sign in required!",
                            message = "Please sign in or create an account to add items to your cart and proceed with your order."
                        )
                    )
                }

                else -> {}
            }
        }
    }

    fun updateCount(food : CartItemsClass, size : String, newCount : Int, cartNavigation : () -> Unit, onCloseItemScreen : () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            val state = cartUseCase.plus(userId, food, size, newCount)

            when(state){
                AddToCartStates.Success -> {
                    sendAddedToCartChannel{ cartNavigation() }
                    onCloseItemScreen()
                    deletenewCount()
                }

                is AddToCartStates.ErrorInCartRestaurant -> {
                    alertDialogTrue(
                        AddToCartStates.ErrorInCartRestaurant(
                            title = "Start a new cart?",
                            message = "A new order will clear your cart with '${cartInformation.value?.restaurantName}'",
                            state.food,
                            state.size
                        )
                    )

                    newFoodInCartSize.value = state.size
                    newFoodInCart.value = state.food
                }

                is AddToCartStates.ErrorInLoginState -> {
                    alertDialogTrue(
                        AddToCartStates.ErrorInLoginState(
                            title = "Sign in required!",
                            message = "Please sign in or create an account to add items to your cart and proceed with your order."
                        )
                    )
                }

                else -> {}
            }
        }
    }

    fun minus(food: CartItemsClass, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.minus(userId, food, size)
        }
    }

    fun clearAndStartNewCart(count : Int, cartNavigation : () -> Unit, onCloseItemScreen : () -> Unit) {
        viewModelScope.launch {
            val newFood = newFoodInCart.value
            val newSize = newFoodInCartSize.value
            val userId = userRepository.userData.value.id
            cartUseCase.clearAllCart(userId)

            if(newFood != null && newSize != null){
                cartUseCase.plus(userId, newFood, newSize, count)

                sendAddedToCartChannel{ cartNavigation() }

                onCloseItemScreen()

                deletenewCount()

                newFoodInCart.value = null
                newFoodInCartSize.value = null
            }
        }
    }

    private fun alertDialogTrue(error : AddToCartStates){
        _errorInCart.value = error
    }

    fun alertDialogFalse(){
        _errorInCart.value = AddToCartStates.Idle
    }

    fun plusnewCount(){
        _newCount.value += 1
    }

    fun minusnewCount(){
        _newCount.value -= 1
    }

    fun deletenewCount(){
        _newCount.value = 0
    }

    private fun sendAddedToCartChannel(cartNavigation : () -> Unit){
        viewModelScope.launch {
            _snackBarChannel.send(
                ShowSnackBarEvent.AddedToCart(
                    message = "Added to cart",
                    actionLabel = "View",
                    action = { cartNavigation() }
                )
            )
        }
    }
//    private fun sendRemovedFromCartChannel(undo : () -> Unit){
//        viewModelScope.launch {
//            _snackBarChannel.send(
//                ShowSnackBarEvent.RemoveFromCart(
//                    message = "Removed from cart",
//                    actionLabel = "Undo",
//                    undo = { undo() }
//                )
//            )
//        }
//    }

//    private fun sendAddedToFavoriteChannel(favoriteNavigation : () -> Unit){
//        viewModelScope.launch {
//            _snackBarChannel.send(
//                ShowSnackBarEvent.AddedToFavorite(
//                    message = "Added to favorite",
//                    actionLabel = "View",
//                    action = { favoriteNavigation() }
//                )
//            )
//        }
//    }
//    private fun sendRemovedFromFavoriteChannel(undo : () -> Unit){
//        viewModelScope.launch {
//            _snackBarChannel.send(
//                ShowSnackBarEvent.RemoveFromFavorite(
//                    message = "Removed from favorite",
//                    actionLabel = "Undo",
//                    undo = { undo() }
//                )
//            )
//        }
//    }


    //       *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    fun addItemInBottomSheetToFavorite(){
        viewModelScope.launch {
            when(_uiState.value.bottomSheetItem){
                is BottomSheetItem.MealItem -> {
                    val favoriteMealEntity = FavoriteMealEntity(
                        _uiState.value.bottomSheetItem?.id ?: 0,
                        userData.value.id,
                        _uiState.value.bottomSheetItem?.restaurantId ?: 0,
                        false,
                        false
                    )
                    addMealFavorite(favoriteMealEntity)
                }

                is BottomSheetItem.SnackItem -> {
                    val favoriteSnackEntity = FavoriteSnackEntity(
                        _uiState.value.bottomSheetItem?.id ?: 0,
                        userData.value.id,
                        _uiState.value.bottomSheetItem?.restaurantId ?: 0,
                        false,
                        false
                    )
                    addSnackFavorite(favoriteSnackEntity)
                }

                else -> {}
            }
        }
    }

    fun removeItemInBottomSheetToFavorite(){
        viewModelScope.launch {
            when(_uiState.value.bottomSheetItem){
                is BottomSheetItem.MealItem -> {
                    removeMealFavorite(_uiState.value.bottomSheetItem?.id ?: 0)
                }

                is BottomSheetItem.SnackItem -> {
                    removeSnackFavorite(_uiState.value.bottomSheetItem?.id ?: 0)
                }

                else -> {}
            }
        }
    }


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
}