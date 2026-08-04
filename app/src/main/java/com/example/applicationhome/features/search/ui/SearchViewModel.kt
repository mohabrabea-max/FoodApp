package com.example.applicationhome.features.search.ui

import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.HomeScreenRepository
import com.example.applicationhome.core.domain.repository.ItemScreenRepository
import com.example.applicationhome.core.domain.repository.RestaurantScreenRepository
import com.example.applicationhome.core.domain.repository.SearchRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.local.entity.CategoriesEntity
import com.example.applicationhome.data.local.entity.RestaurantWithFeaturedMeals
import com.example.applicationhome.data.local.entity.SearchHistory
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
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
    cartRepository : CartRepository,
    private val searchRepository : SearchRepository,
    private val itemScreenRepository : ItemScreenRepository,
    homeScreenRepository : HomeScreenRepository,
    private val restaurantScreenRepository : RestaurantScreenRepository,
    userRepository: UserRepository,
    private val networkObserver : NetworkObserver
): ViewModel() {

    val userData = userRepository.userData


    //       *** ---------------------------- \\***  Categories  ***// ---------------------------- ***

    val categories : StateFlow<List<CategoriesEntity>> =
        homeScreenRepository.categoriesFromDatabase


    //       *** ---------------------------- \\***  Search  ***// ---------------------------- ***

    val cartTotalNumber = cartRepository.totalNumber

    private val _searchString = MutableStateFlow(TextFieldValue(""))
    val searchString : StateFlow<TextFieldValue> = _searchString.asStateFlow()

    private val _searchClickable = MutableStateFlow(false)
    val searchClickable : StateFlow<Boolean> = _searchClickable.asStateFlow()


    val searchSuggestions : StateFlow<List<String>> =
        combine(
            _searchString.map { it.text.trim() }.distinctUntilChanged(),
            snapshotFlow { searchHistoryAfterFiltering }
        ){ searchString, searchHistory ->
            searchString to searchHistory
        }
        .flatMapLatest { (searchString, searchHistory) ->
            if (searchString.isEmpty()) {
                flowOf(emptyList())
            } else {
                searchRepository.getSearchSuggestions(searchString)
                .map { list ->
                    if (searchString.isEmpty()) {
                        emptyList()
                    } else {
                        list.flatMap { it.split(",") }
                        .map { it.trim() }
                        .filter { suggestion ->
                            suggestion.contains(searchString, ignoreCase = true)
                            && searchHistory.value.none { suggestion.equals(it, ignoreCase = true)}
                        }
                        .distinct()
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val searchResults : Flow<PagingData<RestaurantWithFeaturedMeals>> =
        _searchString.flatMapLatest { searchText ->
            searchRepository.getRestaurantSearchResults(searchText.text)
        }.map { pagingData ->
            pagingData.map { restaurant ->
                val mealIds = restaurant.restaurant.topFiveMeals
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
            searchRepository.getSearchHistory(id).map { it -> it.map { it.title } }
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
            history.filter { it.startsWith(search.text, ignoreCase = true) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val isNetworkAvailable = MutableStateFlow(true)


    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable.value = available
            }
        }
    }



    fun searchFilter(search : String){
        _searchString.value = TextFieldValue(
            text = search,
            selection = TextRange(search.length)
        )
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

    fun unClickSearch(){
        _searchClickable.value = false
    }

    fun deleteFromSearchHistory(searchTitle : String){
        viewModelScope.launch {
            searchRepository.deleteFromSearchHistory(searchTitle)
        }
    }
}