package com.example.applicationhome.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    //             --------------------------------------   Meals    -------------------------------------

    @Transaction
    @Query("""
        SELECT m.* FROM meals_entity m
        INNER JOIN favorite_meals f ON m.id = f.mealId
        WHERE f.userId = :userId AND f.isDeletedOffline = 0
            """)
    fun getFoodFromDatabase(userId : String) : Flow<List<MealWithFavoriteStatus>>

    @Upsert
    suspend fun addFoodToFavorite(foodItem : List<FavoriteMealEntity>)

    @Query(" UPDATE OR REPLACE favorite_meals SET userId =:userId WHERE userId = '' AND isDeletedOffline = 0 ")
    suspend fun updateGuestMealsFavoriteToUser(userId : String)

    @Query("DELETE FROM favorite_meals WHERE userId = ''")
    suspend fun deleteGuestMealsFavorite()

    @Transaction
    suspend fun addGuestMealsFavoriteToUser(userId: String){
        updateGuestMealsFavoriteToUser(userId)
        deleteGuestMealsFavorite()
    }

    @Query("DELETE FROM favorite_meals WHERE userId = :userId AND mealId IN (:mealIds)")
    suspend fun deleteFoodFromDatabase(userId: String, mealIds: List<Int>)

    @Query("SELECT * FROM favorite_meals WHERE isDeletedOffline = 1 AND isSynced = 1")
    suspend fun getFoodDeletedOffline() : List<FavoriteMealEntity>

    @Query("UPDATE favorite_meals SET isDeletedOffline = 1 WHERE userId = :userId AND mealId = :mealId")
    suspend fun markFoodAsDeletedOffline(userId: String, mealId: Int)

    @Query("SELECT * FROM favorite_meals WHERE isSynced = 0 AND isDeletedOffline = 0")
    suspend fun getUnSyncedFood() : List<FavoriteMealEntity>

    @Update
    suspend fun markMealsAsSynced(meals: List<FavoriteMealEntity>)

    @Query("DELETE FROM favorite_meals WHERE isDeletedOffline = 1 AND isSynced = 0")
    suspend fun cleanUpLocalOnlyDeletedMeals()

    @Query("DELETE FROM favorite_meals")
    suspend fun deleteAllFavoriteMeals()


    //             --------------------------------------   Snacks    -------------------------------------

    @Transaction
    @Query("""
            SELECT m.* FROM snacks_entity m
            INNER JOIN favorite_snacks f ON m.id = f.snackId
            WHERE f.userId = :userId AND f.isDeletedOffline = 0
            """)
    fun getSnacksFromDatabase(userId : String) : Flow<List<SnackWithFavoriteStatus>>

    @Upsert
    suspend fun addSnacksToFavorite(snacksItems : List<FavoriteSnackEntity>)

    @Query("UPDATE OR REPLACE favorite_snacks SET userId = :userId WHERE userId = '' AND isDeletedOffline = 0 ")
    suspend fun updateGuestSnacksFavoriteToUser(userId : String)

    @Query("DELETE FROM favorite_snacks WHERE userId = ''")
    suspend fun deleteGuestSnacksFavorite()

    @Transaction
    suspend fun addGuestSnacksFavoriteToUser(userId: String){
        updateGuestSnacksFavoriteToUser(userId)
        deleteGuestSnacksFavorite()
    }

    @Query("DELETE FROM favorite_snacks WHERE userId = :userId AND snackId IN (:snackIds)")
    suspend fun deleteSnacksFromDatabase(userId: String, snackIds: List<Int>)

    @Query("SELECT * FROM favorite_snacks WHERE isDeletedOffline = 1 AND isSynced = 1")
    suspend fun getSnacksDeletedOffline() : List<FavoriteSnackEntity>

    @Query("UPDATE favorite_snacks SET isDeletedOffline = 1 WHERE userId = :userId AND snackId = :snackId")
    suspend fun markSnacksAsDeletedOffline(userId: String, snackId: Int)

    @Query("SELECT * FROM favorite_snacks WHERE isSynced = 0 AND isDeletedOffline = 0")
    suspend fun getUnSyncedSnacks() : List<FavoriteSnackEntity>

    @Update
    suspend fun markSnacksAsSynced(snacks: List<FavoriteSnackEntity>)

    @Query("DELETE FROM favorite_snacks WHERE isDeletedOffline = 1 AND isSynced = 0")
    suspend fun cleanUpLocalOnlyDeletedSnacks()

    @Query("DELETE FROM favorite_snacks")
    suspend fun deleteAllFavoriteSnacks()


    //              --------------------------------------   Restaurants    -------------------------------------

    @Transaction
    @Query("""
            SELECT m.* FROM restaurants_entity m
            INNER JOIN favorite_restaurants f ON m.id = f.resId
            WHERE f.userId = :userId AND f.isDeletedOffline = 0
            """)
    fun getRestaurantsFromDatabase(userId : String) : Flow<List<RestaurantWithFavoriteStatus>>

    @Upsert
    suspend fun addRestaurantToFavorite(restaurant : List<FavoriteRestaurantEntity>)

    @Query("UPDATE OR REPLACE favorite_restaurants SET userId = :userId WHERE userId = '' AND isDeletedOffline = 0 ")
    suspend fun updateGuestRestaurantsFavoriteToUser(userId : String)

    @Query("DELETE FROM favorite_restaurants WHERE userId = ''")
    suspend fun deleteGuestRestaurantsFavorite()

    @Transaction
    suspend fun addGuestRestaurantsFavoriteToUser(userId: String){
        updateGuestRestaurantsFavoriteToUser(userId)
        deleteGuestRestaurantsFavorite()
    }

    @Query("DELETE FROM favorite_restaurants WHERE userId = :userId AND resId IN (:resIds)")
    suspend fun deleteRestaurantFromDatabase(userId: String, resIds: List<Int>)

    @Query("SELECT * FROM favorite_restaurants WHERE isDeletedOffline = 1 AND isSynced = 1")
    suspend fun getRestaurantsDeletedOffline() : List<FavoriteRestaurantEntity>

    @Query("UPDATE favorite_restaurants SET isDeletedOffline = 1 WHERE userId = :userId AND resId = :resId")
    suspend fun markRestaurantsAsDeletedOffline(userId: String, resId: Int)

    @Query("SELECT * FROM favorite_restaurants WHERE isSynced = 0 AND isDeletedOffline = 0")
    suspend fun getUnSyncedRestaurants() : List<FavoriteRestaurantEntity>

    @Update
    suspend fun markRestaurantsAsSynced(restaurants: List<FavoriteRestaurantEntity>)

    @Query("DELETE FROM favorite_restaurants WHERE isDeletedOffline = 1 AND isSynced = 0")
    suspend fun cleanUpLocalOnlyDeletedRestaurants()

    @Query("DELETE FROM favorite_restaurants")
    suspend fun deleteAllFavoriteRestaurants()


    //              --------------------------------------   Transaction    -------------------------------------

    @Transaction
    suspend fun addAllToFavorite(
        foodItems : List<FavoriteMealEntity>,
        snacksItems : List<FavoriteSnackEntity>,
        restaurant : List<FavoriteRestaurantEntity>
    ){
        deleteAllFromFavorite()

        addFoodToFavorite(foodItems)
        addSnacksToFavorite(snacksItems)
        addRestaurantToFavorite(restaurant)
    }

    @Transaction
    suspend fun deleteAllFromFavorite(){
        deleteAllFavoriteMeals()
        deleteAllFavoriteSnacks()
        deleteAllFavoriteRestaurants()
    }
}