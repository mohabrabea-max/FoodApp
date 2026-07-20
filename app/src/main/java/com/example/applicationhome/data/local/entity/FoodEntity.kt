package com.example.applicationhome.data.local.entity

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.example.applicationhome.data.data.model.MealSizeDetail
import com.example.applicationhome.data.data.model.OrderItemsClass
import com.example.applicationhome.data.data.model.UserInformationInOrderClass

@Entity(tableName = "meals_entity")
data class MealsEntity(
    @PrimaryKey val id : Int = 0,
    val category : String = "ALL",
    val name : String = "",
    val details : String = "",
    val image : String = "",
    val sizeOptions : List<MealSizeDetail> = listOf(),
    var restaurantId : Int = 0,
    val review : Double = 0.0
)

@Entity(tableName = "snacks_entity")
data class SnacksEntity(
    @PrimaryKey val id : Int = 0,
    val name : String = "",
    val details : String = "",
    val image : String = "",
    val priceANDsize : Map<String, Double> = emptyMap(),
    var restaurantId : Int = 0,
    val review : Double = 0.0
)

@Entity(tableName = "restaurants_entity")
data class RestaurantsEntity(
    @PrimaryKey val id : Int = 0,
    val name : String = "",
    val typ : List<String> = listOf(),
    val image : String = "",
    val image2 : String = "",
    val review : Double = 0.0,
    val background : Color = Color.White,
    val searchKeywords: String = "",
    val topFiveMeals : String = ""
)

data class RestaurantWithFeaturedMeals(
    val restaurant: RestaurantsEntity = RestaurantsEntity(),
    val topMeals: List<MealsEntity> = emptyList()
)

@Entity(tableName = "search_fts")
@Fts4
data class SearchFtsEntity(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val rowid : Int = 0,
    val name : String = "",
    val description: String = ""
)

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey val userId : String,
    val title : String = "",
    val time : String = ""
)



@Entity(tableName = "users")
data class UserClass(
    val id : String = "",
    val firstname : String = "Guest",
    val lastname : String = "",
    @PrimaryKey                 //   عشان الايميل ميتكررش في جدول اليوزرز
    val email : String = "",
    val password : String = "",
    val phonenumber : String = "",
    val birthday : String = "",
    val governorate : String = "",
    val city : String = "",
    val address : String = "",
    val isActive : Boolean = false
)

data class UpdateAccountState(
    val email : String = "",
    val isActive : Boolean = false
)


@Entity(
    tableName = "cart_items",
    primaryKeys = ["userId", "mealKey"]
)
data class CartItemsClass(
    val userId : String = "",
    val mealKey : String = "",
    val mealId : Int = 0,
    val name : String = "",
    val type : String = "",
    val size : String = "",
    val quantity: Int = 0,
    val priceOfOne : Double = 0.0,
    val totalPrice : Double = 0.0,
    val image : String = "",
    val restaurantId : Int = 0
)

@Entity(tableName = "cart")
data class CartClass(
    @PrimaryKey val userId : String = "",
    val restaurantId : Int = 0,
    val restaurantName : String = "",
    val restaurantImage : String = ""
)


@Entity(
    tableName = "favorite_food",
    primaryKeys = ["userId", "mealId"]
)
data class FavoriteFoodDatabase(
    val userId : String = "",
    val mealId : Int = 0,
    val type : String = "",
    val name : String = "",
    val details : String = "",
    val image : String = "",
    val sizeOptions : List<MealSizeDetail> = emptyList(),
    val restaurantId : Int = 0,
    val review : Double = 0.0,
    val isSynced : Boolean = false,
    val isDeletedOffline : Boolean = false
)


@Entity(
    tableName = "favorite_snacks",
    primaryKeys = ["userId", "snackId"]
)
data class FavoriteSnacksDatabase(
    val userId : String = "",
    val snackId : Int = 0,
    val name : String = "",
    val details : String = "",
    val image : String = "",
    val priceANDsize : Map<String, Double> = emptyMap(),
    val restaurantId : Int = 0,
    val review : Double = 0.0,
    val isSynced : Boolean = false,
    val isDeletedOffline : Boolean = false
)


@Entity(
    tableName = "favorite_restaurant",
    primaryKeys = ["userId", "restaurantId"]
)
data class FavoriteRestaurantDatabase(
    val userId : String = "",
    val restaurantId : Int = 0,
    val name : String = "",
    val image : String = "",
    val image2 : String = "",
    val type : List<String> = emptyList(),
    val isSynced : Boolean = false,
    val isDeletedOffline : Boolean = false
)


@Entity(
    tableName = "orders_history",
    primaryKeys = ["orderId", "userId"]
)
data class OrdersDatabaseClass(
    val orderId : Long = 0,
    val userId : String = "",
    val date : String = "",
    val state : String = "",
    val subtotal : Double = 0.0,
    val delivery : Double = 0.0,
    val service : Double = 0.0,
    val totalPrice : Double = 0.0,
    val restaurantName : String = "",
    val restaurantImage : String = "",
    val restaurantId : Int = 0,
    val userInformation : UserInformationInOrderClass = UserInformationInOrderClass(),
    val orderItems : List<OrderItemsClass> = emptyList()
)