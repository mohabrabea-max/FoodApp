package com.example.applicationhome.features.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.applicationhome.core.domain.model.mealsEntityToFoodItem
import com.example.applicationhome.core.domain.model.restaurantsEntityToRestaurants
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.ItemScreenRepository
import com.example.applicationhome.core.domain.repository.SearchRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.RestaurantWithFeaturedMeals
import com.example.applicationhome.data.local.entity.RestaurantsEntity
import com.example.applicationhome.data.local.entity.SearchHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val cartRepository : CartRepository,
    private val searchRepository : SearchRepository,
    private val itemScreenRepository : ItemScreenRepository,
    private val favoriteRepository : FavoriteRepository,
    private val userRepository: UserRepository
): ViewModel() {

    val userData = userRepository.userData

    //       *** ---------------------------- \\***  Search  ***// ---------------------------- ***

    val cartTotalNumber = cartRepository.totalNumber

    private val _searchString = MutableStateFlow("")
    val searchString : StateFlow<String> = _searchString.asStateFlow()

    private val _searchClickable = MutableStateFlow(false)
    val searchClickable : StateFlow<Boolean> = _searchClickable.asStateFlow()


    val searchSuggestions : StateFlow<List<String>> =
        _searchString.flatMapLatest { searchText ->
            searchRepository.getSearchSuggestions(searchText)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val searchResults : Flow<PagingData<RestaurantWithFeaturedMeals>> =
        _searchString.flatMapLatest { searchText ->
            searchRepository.getRestaurantSearchResults(searchText)
        }.map { pagingData ->
            pagingData.map { restaurant ->
                val mealIds = restaurant.topFiveMeals
                    .split(",")
                    .mapNotNull { it.trim().toIntOrNull() }
                val topFiveMeals = if (mealIds.isNotEmpty()){
                    searchRepository.getTopFiveMealsToView(mealIds)
                }else{
                    emptyList()
                }
                RestaurantWithFeaturedMeals(
                    restaurant,
                    topFiveMeals
                )
            }
        }.cachedIn(viewModelScope)



    val searchHistory : StateFlow<List<String>> =
        userData.flatMapLatest { user ->
            val id = user.id
            if(id.isNotEmpty()){
                searchRepository.getSearchHistory(id)
            }else{
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val searchHistoryAfterFiltering : StateFlow<List<String>> =
        combine(
            searchHistory,
            _searchString
        ){ history, search ->
            history.filter { it.startsWith(search) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )



    fun searchFilter(search : String){
        _searchString.value = search
    }

    fun clickSearch(search : String){
        searchFilter(search)
        _searchClickable.value = true

        viewModelScope.launch {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val date = current.format(formatter)

            searchRepository.addSearchTextToHistory(
                SearchHistory(
                    userData.value.id,
                    search,
                    date
                )
            )
        }
    }


    //       *** ---------------------------- \\***  Item Screen  ***// ---------------------------- ***

    fun selectMeal(item: MealsEntity) {
        val foodItem = item.mealsEntityToFoodItem()

        val size = item.sizeOptions.find { it.size == "Small" || it.size.contains("Pieces") }?.size ?: ""

        itemScreenRepository.selectMeal(foodItem, size)
    }

    fun selectRestaurant(item : RestaurantsEntity){
        val restaurant = item.restaurantsEntityToRestaurants()

        itemScreenRepository.selectRestaurant(restaurant)

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
}