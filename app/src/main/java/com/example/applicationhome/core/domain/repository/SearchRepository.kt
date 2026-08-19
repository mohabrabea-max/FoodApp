package com.example.applicationhome.core.domain.repository

import androidx.paging.PagingData
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SearchHistory
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getSearchSuggestions(searchText: String): Flow<List<String>>
    fun getRestaurantSearchResults(searchText: String): Flow<PagingData<RestaurantWithFavoriteStatus>>
    suspend fun getTopFiveMealsToView(mealIds: List<Int>): List<MealWithFavoriteStatus>
    fun getSearchHistory(userid : String): Flow<List<SearchHistory>>
    suspend fun addSearchTextToHistory(searchHistory : SearchHistory)
    suspend fun addGuestSearchHistoryToUser(userId: String)
    suspend fun deleteFromSearchHistory(searchTitle : String)
}