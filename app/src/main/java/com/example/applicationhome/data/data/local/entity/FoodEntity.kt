package com.example.applicationhome.data.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationhome.data.data.model.MealSizeDetail
import com.example.applicationhome.data.data.model.OrderItemsClass
import com.example.applicationhome.data.data.model.UserInformationInOrderClass

//enum class CategoryType { PIZZA, BURGER, CHICKEN, SWEET, ALL }
//
//
//enum class FoodSizes{ SMALL, MEDIUM, LARGE }
//
//sealed interface Food{
//    val id: Int
//    val name : String
//    val image : List<String>
//    var restaurantId : Int
//    val review : Double
//}
//
//data class FoodItem(
//    override val id : Int = 0,
//    val category : String = CategoryType.ALL.toString(),
//    override val name : String = "",
//    @SerializedName("images")
//    override val image : List<String> = listOf(""),
//    @SerializedName("sizes")
//    val sizeOptions : List<MealSizeDetail>,
//    override var restaurantId : Int = 0,
//    @SerializedName("rating")
//    override val review : Double = 0.0
//): Food
//
//data class Snack(
//    override val id : Int = 0,
//    override val name : String = "",
//    @SerializedName("images")
//    override val image : List<String> = listOf(""),
//    @SerializedName("prices")
//    val priceANDsize : Map<String, Double>,
//    override var restaurantId : Int = 0,
//    @SerializedName("rating")
//    override val review : Double = 0.0
//): Food
//
//data class Drink(
//    val id : Int,
//    val name : String,
//    val image : List<String>,
//    val priceANDsize : Map<String, Double>,
//    var restaurantId : Int
//)
//
//data class MealSizeDetail(
//    val size : String = "",
//    val price : Double = 0.0,
//    @SerializedName("details")
//    val snack : Map<Int, String> = mapOf(0 to "")
//)
//data class Categories(
//    val id : Int = 0,
//    val name : String = "",
//    val type : String = CategoryType.ALL.toString(),
//    val image : String = "",
//    val icon : String = ""
//)
//
//data class Offers(
//    val restaurantId : Int = 0,
//    val id : Int = 0,
//    @SerializedName("title")
//    val name : String = "",
//    val image : String = ""
//)
//
//data class Restaurants(
//    val id : Int = 0,
//    @SerializedName("types")
//    val typ : List<String> = listOf(),
//    val name : String = "",
//    @SerializedName("logo")
//    val image : String = "",
//    @SerializedName("main_image")
//    val image2 : String = "",
//    @SerializedName("rating")
//    val review : Double = 0.0,
//    val background : Color = Color.White
//)
//
//data class Options(
//    val title : String,
//    val icon : ImageVector,
//    val screen: String
//)

//data class Account(
//    val id : Int,
//    val title : String,
//    val empty : String,
//    var value: Any?,
//    val icon : ImageVector?
//)

//data class Settings(
//    val title : String,
//    val icon : ImageVector
//)
//
//data class ProfileOptions(
//    val title : String,
//    var description : String?,
//    val icon: ImageVector,
//    val screen : Screens
//)
//
//
//data class FavoriteClass(
//    val id : Int,
//    val typ : String,
//    val restaurants : Int
//)
//
//data class RestaurantsCount(
//    val meals : Int,
//    val snacks : Int,
//    val drinks : Int,
//    val offers : Int
//)

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



//data class FirebasePostResponse(val name : String)
//
//data class TextFieldClassFromConfirmOrderScreen(val textState : TextFieldState, val title: String)
//
//
//data class CartItemsClass(
//    val id : Int = 0,
//    val type : String = "",
//    val size : String = "",
//    val number : Int = 0
//)
//data class CartClass(
//    val cartItems : Map<String, CartItemsClass> = emptyMap<String, CartItemsClass>(),
//    val restaurantId : Int = 0,
//    val restaurantName : String = "",
//    val restaurantImage : String = ""
//)
//
//
//data class OrderItemsClass(
//    val mealId : Int = 0,
//    val mealName : String = "",
//    val size : String = "",
//    val price : Double = 0.0,
//    val quantity : Int = 0,
//    val image : String = "",
//    val type : String = ""
//)
//
//data class UserInformationInOrderClass(
//    val name : String = "",
//    val phonenumber : String = "",
//    val address : String = "",
//    val location : String = ""
//)
//
//data class OrdersClass(
//    val date : String = "",
//    val state : String = "",
//    val totalPrice : Double = 0.0,
//    val userInformation : UserInformationInOrderClass = UserInformationInOrderClass(),
//    val orderItems : List<OrderItemsClass> = listOf(OrderItemsClass()),
//    val restaurantName : String = "",
//    val restaurantImage : String = "",
//    val restaurantId : Int = 0
//)