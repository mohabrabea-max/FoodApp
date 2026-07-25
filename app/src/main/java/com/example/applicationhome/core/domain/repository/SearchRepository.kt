package com.example.applicationhome.core.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.applicationhome.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.RestaurantsEntity
import com.example.applicationhome.data.local.entity.SearchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao
) {
    fun getSearchSuggestions(searchText: String): Flow<List<String>>{
        val trimmedSearchText = searchText.trim()

        if(trimmedSearchText.isEmpty()){
            return flowOf(emptyList())
        }

        val formattedSearchText = "$trimmedSearchText*"
        return foodAndRestaurantsDao.getSearchSuggestions(formattedSearchText)
    }


    fun getRestaurantSearchResults(searchText: String): Flow<PagingData<RestaurantsEntity>>{
        return Pager(
            config = PagingConfig(
                pageSize = 20,         // حجم الدفعة (كل مرة يجيب 20 مطعم)
                prefetchDistance = 5,  // يبدأ يحمل الصفحة الجاية لما يتبقي 5 كروت بس في السكرول
                enablePlaceholders = false
            ),
            pagingSourceFactory = { foodAndRestaurantsDao.getRestaurantSearchResults(searchText) }
        ).flow
    }

    suspend fun getTopFiveMealsToView(mealIds: List<Int>): List<MealsEntity> =
        foodAndRestaurantsDao.getTopFiveMealsToView(mealIds)

    fun getSearchHistory(userid : String): Flow<List<SearchHistory>> =
        foodAndRestaurantsDao.getSearchHistory(userid)

    suspend fun addSearchTextToHistory(searchHistory : SearchHistory){
        foodAndRestaurantsDao.addSearchTextToHistory(searchHistory)
    }

    suspend fun addGuestSearchHistoryToUser(userId: String){
        foodAndRestaurantsDao.addGuestSearchHistoryToUser(userId)
    }

    suspend fun deleteFromSearchHistory(searchTitle : String){
        foodAndRestaurantsDao.deleteFromSearchHistory(searchTitle)
    }
}