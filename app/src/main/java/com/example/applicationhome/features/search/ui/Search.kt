package com.example.applicationhome.features.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.features.homescreen.ui.CategoriesBar
import com.example.applicationhome.features.shimmers.boxes.CategoriesShimmer
import com.example.applicationhome.features.shimmers.boxes.TextInSearchShimmer
import com.example.applicationhome.features.shimmers.screens.SearchResultScreenShimmer
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Search(
    navigationController : NavHostController,
    searchViewModel : SearchViewModel,
    syncDataUiState : HomeUiState,
    isRefreshing : Boolean,
    onRefresh : () -> Unit
){
    val refreshState = rememberPullToRefreshState()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val cartTotalNumber by searchViewModel.cartTotalNumber.collectAsStateWithLifecycle()
    val categories by searchViewModel.categories.collectAsStateWithLifecycle()

    val searchHistory by searchViewModel.searchHistory.collectAsStateWithLifecycle()
    val searchHistoryAfterFiltering by searchViewModel.searchHistoryAfterFiltering.collectAsStateWithLifecycle()

    val search by searchViewModel.searchString.collectAsStateWithLifecycle()
    val searchSuggestions by searchViewModel.searchSuggestions.collectAsStateWithLifecycle()
    val searchResults = searchViewModel.searchResults.collectAsLazyPagingItems()

    val searchClickable by searchViewModel.searchClickable.collectAsStateWithLifecycle()


    Scaffold(
        modifier = Modifier
            .navigationBarsPadding().
            fillMaxSize(),

        topBar = {
            SearchScreenTopBar(
                totalInCart = cartTotalNumber,
                searchText = search,
                backClick = {
                    if (navigationController.previousBackStackEntry != null) {
                        navigationController.popBackStack()
                    }
                    searchViewModel.searchFilter("")
                },
                cartClick = { navigationController.navigate(Screens.Cart.screen) },
                onQueryChange = { searchViewModel.searchFilter(it.text) },
                clickSearch = { if(it.isNotEmpty()) searchViewModel.clickSearch(it) },
                unClickSearch = {searchViewModel.unClickSearch()},
                focusManager = focusManager,
                keyboardController = keyboardController
            )
        }
    ){ paddingValues ->
        PullToRefreshBox(
            isRefreshing = if(search.text.isEmpty()) isRefreshing else false,

            onRefresh = {
                if(search.text.isEmpty()){
                    onRefresh()
                }
            },

            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),

            state = refreshState,

            indicator = {
                if(search.text.isEmpty()){
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = if(search.text.isEmpty()) isRefreshing else false,
                        containerColor = MaterialTheme.colorScheme.surface,
                        color = Color.DarkOrange,
                        state = refreshState
                    )
                }
            },

            contentAlignment = Alignment.Center
        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ){
                if(search.text.isNotEmpty() && !searchClickable){

                    //       --------------------------\\ Last Search //--------------------------
                    when(syncDataUiState){
                        HomeUiState.Success, HomeUiState.Offline -> {
                            if(searchHistoryAfterFiltering.isNotEmpty()) items(searchHistoryAfterFiltering){ item ->
                                var isMenuExpanded by remember { mutableStateOf(false) }

                                SearchSuggestions(
                                    text = item,
                                    searchText = search.text,
                                    textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    startIcon = Icons.Default.History,
                                    iconsColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textClickable = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()

                                        searchViewModel.clickSearch(item)
                                    },
                                    northWestClickable = { searchViewModel.searchFilter(item) },
                                    longClick = { isMenuExpanded = true }
                                )

                                DropdownMenu(
                                    expanded = isMenuExpanded,
                                    onDismissRequest = { isMenuExpanded = false },
                                    offset = DpOffset(x = 25.dp, y = 120.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    shadowElevation = 10.dp,
                                    containerColor = MaterialTheme.colorScheme.surface
                                ){
                                    DropdownMenuItem(
                                        text = { Text("Delete", color = Color.Red) },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Outlined.Clear,
                                                contentDescription = null,
                                                tint = Color.Red
                                            )
                                        },
                                        onClick = {
                                            isMenuExpanded = false
                                            searchViewModel.deleteFromSearchHistory(item)
                                        }
                                    )
                                }

                                HorizontalDivider(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp),
                                    thickness = DividerDefaults.Thickness,
                                    color = Color.LightGray.copy(alpha = 0.6f)
                                )
                            }

                            //       --------------------------\\ New Search //--------------------------
                            items(searchSuggestions){ item ->
                                SearchSuggestions(
                                    text = item,
                                    searchText = search.text,
                                    textColor = MaterialTheme.colorScheme.onSurface,
                                    startIcon = Icons.Default.Search,
                                    iconsColor = MaterialTheme.colorScheme.onSurface,
                                    textClickable = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()

                                        searchViewModel.clickSearch(item)
                                    },
                                    northWestClickable = { searchViewModel.searchFilter(item) }
                                )

                                if(item != searchSuggestions.last()) {
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .padding(horizontal = 20.dp),
                                        thickness = DividerDefaults.Thickness,
                                        color = Color.LightGray.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }

                        HomeUiState.Loading -> {
                            items(5) {
                                Spacer(modifier = Modifier.height(15.dp))

                                TextInSearchShimmer()
                            }
                        }
                    }

                }else if(search.text.isEmpty()){

                    //       --------------------------\\ Categories Bar //--------------------------
                    when(syncDataUiState){
                        HomeUiState.Success, HomeUiState.Offline -> {
                            item{
                                CategoriesBar(
                                    categories = categories,
                                    selected = 0,
                                    select = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()

                                        searchViewModel.clickSearch(it.name)
                                    },
                                    unSelect = {}
                                )

                                //Divider(color = Color.LightGray.copy(alpha = 0.6f))
                            }

                            //       --------------------------\\ Search History //--------------------------
                            if(searchHistory.isNotEmpty()){
                                item{
                                    Text(
                                        text = stringResource(R.string.search_history),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
                                    )
                                }
                                item{
                                    FlowRow(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ){
                                        searchHistory.forEach { item ->
                                            SearchHistoryBox(
                                                text = item,
                                                clickable = {
                                                    keyboardController?.hide()
                                                    focusManager.clearFocus()

                                                    searchViewModel.clickSearch(item)
                                                },
                                                delete = { searchViewModel.deleteFromSearchHistory(item) }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        HomeUiState.Loading -> {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .shimmer()
                                        .background(MaterialTheme.colorScheme.surface),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    Spacer(modifier = Modifier.height(15.dp))

                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(horizontal = 15.dp),
                                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                                    ){
                                        items(6){
                                            CategoriesShimmer()
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //       --------------------------\\ Search Results //--------------------------
                }else if(search.text.isNotEmpty() && searchClickable){
                    when(syncDataUiState){
                        HomeUiState.Success, HomeUiState.Offline -> {
                            items(
                                count = searchResults.itemCount,
                                key = searchResults.itemKey { it.restaurant.restaurant.id }
                            ){ index ->
                                val item = searchResults[index]

                                item?.let{
                                    SearchResults(
                                        item,
                                        mealClickable = { item ->
                                            navigationController.navigate(Screens.RestaurantScreen.createRouteWithMeal(restaurantId = item.meal.restaurantId, mealId = item.meal.id))
                                        },
                                        restaurantClickable = {
                                            navigationController.navigate(Screens.RestaurantScreen.createRoute(restaurantId = item.restaurant.restaurant.id))
                                        }
                                    )
                                }
                            }
                        }

                        HomeUiState.Loading -> {
                            item { SearchResultScreenShimmer() }
                        }
                    }
                }
            }
        }
    }
}