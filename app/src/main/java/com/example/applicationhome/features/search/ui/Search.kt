package com.example.applicationhome.features.search.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.applicationhome.data.data.model.Categories
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.features.homescreen.ui.CategoriesBar
import com.example.applicationhome.features.profile.ui.SearchResults

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Search(
    navigationController : NavHostController,
    searchViewModel : SearchViewModel
){
    val cartTotalNumber by searchViewModel.cartTotalNumber.collectAsStateWithLifecycle()

    val interactionSource = remember { MutableInteractionSource() }

    val searchHistory by searchViewModel.searchHistory.collectAsStateWithLifecycle()
    val searchHistoryAfterFiltering by searchViewModel.searchHistoryAfterFiltering.collectAsStateWithLifecycle()
    val searchList by searchViewModel.searchSuggestions.collectAsStateWithLifecycle()

    val search by searchViewModel.searchString.collectAsStateWithLifecycle()
    val searchSuggestions by searchViewModel.searchSuggestions.collectAsStateWithLifecycle()
    val searchResults = searchViewModel.searchResults.collectAsLazyPagingItems()

    val searchClickable by searchViewModel.searchClickable.collectAsStateWithLifecycle()


    Scaffold(
        modifier = Modifier.navigationBarsPadding().
        fillMaxSize(),
        containerColor = Color.White,
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
                onQueryChange = { searchViewModel.clickSearch(it) }
            )
        }
    ){ paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.Start
        ){
            if(search.isNotEmpty()){
                //       --------------------------\\ Last Search //--------------------------
                if(searchHistoryAfterFiltering.isNotEmpty()) items(searchHistoryAfterFiltering){ item ->
                    SearchSuggestions(
                        text = item,
                        searchText = search,
                        textColor = Color.Gray,
                        startIcon = Icons.Default.History,
                        iconsColor = Color.Gray,
                        textClickable = { searchViewModel.clickSearch(item) },
                        northWestClickable = { searchViewModel.searchFilter(item) }
                    )

                    Divider(
                        color = Color.LightGray.copy(alpha = 0.6f),
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                }

                //       --------------------------\\ New Search //--------------------------
                items(searchList){ item ->
                    SearchSuggestions(
                        text = item,
                        searchText = search,
                        textColor = Color.Black,
                        startIcon = Icons.Default.Search,
                        iconsColor = Color.Black,
                        textClickable = { searchViewModel.clickSearch(item) },
                        northWestClickable = { searchViewModel.searchFilter(item) }
                    )

                    if(item != searchList.last()) Divider(
                        color = Color.LightGray.copy(alpha = 0.6f),
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                }

            }else if(!searchClickable){
                //       --------------------------\\ Categories Bar //--------------------------
                item{
                    CategoriesBar(
                        listOf(
                            Categories(0, "Pizza", "Pizza"),
                            Categories(0, "Pizza", "Pizza"),
                            Categories(0, "Pizza", "Pizza"),
                            Categories(0, "Pizza", "Pizza"),
                            Categories(0, "Pizza", "Pizza"),
                            Categories(0, "Pizza", "Pizza")
                        ),
                        false,
                        0,
                        {},
                        {}
                    )

                    //Divider(color = Color.LightGray.copy(alpha = 0.6f))
                }

                //       --------------------------\\ Search History //--------------------------
                if(searchHistory.isNotEmpty()){
                    item{
                        Text(
                            text = "Search History",
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
                                    clickable = {  }
                                )
                            }
                        }
                    }
                }

                //       --------------------------\\ Search Results //--------------------------
            }else{
                items(
                    count = searchResults.itemCount,
                    key = searchResults.itemKey { it.restaurant.id }
                ){ index ->
                    val item = searchResults[index]

                    if(item != null){
                        SearchResults(
                            item,
                            mealClickable = { item ->
                                searchViewModel.selectMeal(item)
                            },
                            restaurantClickable = {
                                searchViewModel.selectedtype(0, item.restaurant.typ.toList().first())
                                searchViewModel.selectRestaurant(item.restaurant)
                            }
                        )
                    }
                }
            }
        }
    }
}