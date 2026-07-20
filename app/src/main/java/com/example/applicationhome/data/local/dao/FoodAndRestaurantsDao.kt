package com.example.applicationhome.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.RestaurantsEntity
import com.example.applicationhome.data.local.entity.SearchHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodAndRestaurantsDao {

    //----------------------------------------------------------------\\ Search //----------------------------------------------------------------

    @Query("SELECT name FROM search_fts WHERE search_fts MATCH :searchText || '*' LIMIT 10")
    fun getSearchSuggestions(searchText: String): Flow<List<String>>

    @Query("""
        SELECT restaurants_entity.* FROM restaurants_entity 
        JOIN search_fts ON restaurants_entity.id = search_fts.rowid 
        WHERE search_fts MATCH :searchText || '*'
    """)
    fun getRestaurantSearchResults(searchText: String): PagingSource<Int, RestaurantsEntity>

    @Query("SELECT * FROM meals_entity WHERE id IN (:mealIds)")
    suspend fun getTopFiveMealsToView(mealIds: List<Int>): List<MealsEntity>


    //----------------------------------------------------------------\\ Search History //----------------------------------------------------------------

    @Query("SELECT * FROM search_history WHERE userId = :userid")
    fun getSearchHistory(userid : String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearchTextToHistory(searchHistory : SearchHistory)
}