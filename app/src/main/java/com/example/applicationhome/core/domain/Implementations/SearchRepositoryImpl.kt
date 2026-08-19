package com.example.applicationhome.core.domain.Implementations

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.applicationhome.core.domain.repository.SearchRepository
import com.example.applicationhome.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SearchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao
): SearchRepository {
    override fun getSearchSuggestions(searchText: String): Flow<List<String>>{
        val trimmedSearchText = searchText.trim()

        if(trimmedSearchText.isEmpty()){
            return flowOf(emptyList())
        }

        val formattedSearchText = "$trimmedSearchText*"
        return foodAndRestaurantsDao.getSearchSuggestions(formattedSearchText)
    }


    override fun getRestaurantSearchResults(searchText: String): Flow<PagingData<RestaurantWithFavoriteStatus>>{
        return Pager(
            config = PagingConfig(
                pageSize = 10,         // حجم الدفعة (كل مرة يجيب 20 مطعم)
                prefetchDistance = 5,  // يبدأ يحمل الصفحة الجاية لما يتبقي 5 كروت بس في السكرول
                enablePlaceholders = false
            ),
            pagingSourceFactory = { foodAndRestaurantsDao.getRestaurantSearchResults(searchText) }
        ).flow
    }

    override suspend fun getTopFiveMealsToView(mealIds: List<Int>): List<MealWithFavoriteStatus> =
        foodAndRestaurantsDao.getTopFiveMealsToView(mealIds)

    override fun getSearchHistory(userid : String): Flow<List<SearchHistory>> =
        foodAndRestaurantsDao.getSearchHistory(userid)

    override suspend fun addSearchTextToHistory(searchHistory : SearchHistory){
        foodAndRestaurantsDao.addSearchTextToHistory(searchHistory)
    }

    override suspend fun addGuestSearchHistoryToUser(userId: String){
        foodAndRestaurantsDao.addGuestSearchHistoryToUser(userId)
    }

    override suspend fun deleteFromSearchHistory(searchTitle : String){
        foodAndRestaurantsDao.deleteFromSearchHistory(searchTitle)
    }
}