package com.example.applicationhome.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Fts4
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.applicationhome.data.data.model.CategoriesInWithTitle
import com.example.applicationhome.data.data.model.MealSizeDetail
import com.example.applicationhome.data.data.model.OrderHistoryClass
import com.example.applicationhome.data.data.model.OrderItemsClass
import com.example.applicationhome.data.data.model.UserInformationInOrderClass

@Entity(
    tableName = "categories_entity",
    indices = [
        Index(value = ["id"])
    ]
)
data class CategoriesEntity(
    @PrimaryKey val id : Int = 0,
    val name : String = "",
    val type : String = "ALL",
    val image : String = "",
    val icon : String = "",
    val updatedAt : Long = 0L
)


@Entity(tableName = "offers_entity")
data class OffersEntity(
    val restaurantId : Int = 0,
    @PrimaryKey val id : Int = 0,
    val name : String = "",
    val image : String = "",
    val updatedAt : Long = 0L
)


@Entity(
    tableName = "meals_entity",
    indices = [
        Index(value = ["restaurantId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = RestaurantsEntity::class,
            parentColumns = ["id"],
            childColumns = ["restaurantId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MealsEntity(
    @PrimaryKey val id : Int = 0,
    val category : String = "ALL",
    val name : String = "",
    val details : String = "",
    val image : String = "",
    val sizeOptions : List<MealSizeDetail> = listOf(),
    val restaurantId : Int = 0,
    val review : Double = 0.0
)

@Entity(
    tableName = "snacks_entity",
    indices = [
        Index(value = ["restaurantId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = RestaurantsEntity::class,
            parentColumns = ["id"],
            childColumns = ["restaurantId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SnacksEntity(
    @PrimaryKey val id : Int = 0,
    val name : String = "",
    val details : String = "",
    val image : String = "",
    val priceANDsize : Map<String, Double> = emptyMap(),
    val restaurantId : Int = 0,
    val review : Double = 0.0
)

@Entity(tableName = "restaurants_entity")
data class RestaurantsEntity(
    @PrimaryKey val id : Int = 0,
    val name : String = "",
    val typ : List<CategoriesInWithTitle> = listOf(),
    val image : String = "",
    val image2 : String = "",
    val review : Double = 0.0,
    val background : String = "",
    val searchKeywords: String = "",
    val topFiveMeals : String = ""
)

@Entity(
    tableName = "restaurant_category_cross_ref",
    primaryKeys = ["restaurantId", "categoryId"]
)
data class RestaurantCategoryCrossRef(
    val restaurantId : Int = 0,
    val categoryId : Int = 0
)

data class RestaurantWithFeaturedMeals(
    val restaurant: RestaurantWithFavoriteStatus,
    val topMeals: List<MealWithFavoriteStatus>
)

@Entity(tableName = "search_fts")
@Fts4(
    contentEntity = RestaurantsEntity::class,
    tokenizerArgs = ["tokenchars=,"]
)
data class SearchFtsEntity(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val rowid : Int = 0,
    val name : String = "",
    val searchKeywords: String = ""
)

@Entity(tableName = "search_history")
data class SearchHistory(
    val userId : String = "",
    @PrimaryKey val title : String = "",
    val time : String = ""
)



@Entity(tableName = "users")
data class UserClass(
    val id : String = "",
    val firstname : String = "Guest",
    val lastname : String = "",
    @PrimaryKey                 //   عشان الايميل ميتكررش في جدول اليوزرز
    val email : String = "",
    val phonenumber : String = "",
    val birthday : String = "",
    val governorate : String = "",
    val city : String = "",
    val address : String = "",
    val isActive: Boolean = false
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


//        *** ---------------------------- \\***  Favorite Meals  ***// ---------------------------- ***

@Entity(
    tableName = "favorite_meals",
    primaryKeys = ["mealId", "userId"],
    foreignKeys = [
        ForeignKey(  //                       الجزء دا لحماية الداتا بيز من اضافة حاجة مش موجودة في MealsEntity
            entity = MealsEntity::class,
            parentColumns = ["id"],
            childColumns = ["mealId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FavoriteMealEntity(
    val mealId : Int = 0,
    val userId : String = "",
    val restaurantId : Int = 0,
    val isSynced : Boolean = false,
    val isDeletedOffline : Boolean = false
)

data class MealWithFavoriteStatus(
    @Embedded val meal : MealsEntity = MealsEntity(),
    @Relation(  //                      الجزء دا لربط جدول MealsEntity بجدول FavoriteMealEntity و عرضهم كداتا كلاس MealWithFavoriteStatus
        parentColumn = "id",
        entityColumn = "mealId"
    )
    val favoriteInfo : FavoriteMealEntity?
){
    val isFavorite : Boolean get() = favoriteInfo != null && !favoriteInfo.isDeletedOffline
}


//        *** ---------------------------- \\***  Favorite Snacks  ***// ---------------------------- ***

@Entity(
    tableName = "favorite_snacks",
    primaryKeys = ["snackId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = SnacksEntity::class,
            parentColumns = ["id"],
            childColumns = ["snackId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FavoriteSnackEntity(
    val snackId : Int = 0,
    val userId : String = "",
    val restaurantId : Int = 0,
    val isSynced : Boolean = false,
    val isDeletedOffline : Boolean = false
)

data class SnackWithFavoriteStatus(
    @Embedded val snack : SnacksEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "snackId"
    )
    val favoriteInfo : FavoriteSnackEntity?
){
    val isFavorite : Boolean get() = favoriteInfo != null && !favoriteInfo.isDeletedOffline
}


//        *** ---------------------------- \\***  Favorite Restaurants  ***// ---------------------------- ***

@Entity(
    tableName = "favorite_restaurants",
    primaryKeys = ["resId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = RestaurantsEntity::class,
            parentColumns = ["id"],
            childColumns = ["resId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FavoriteRestaurantEntity(
    val resId : Int = 0,
    val userId : String = "",
    val isSynced : Boolean = false,
    val isDeletedOffline : Boolean = false
)

data class RestaurantWithFavoriteStatus(
    @Embedded val restaurant : RestaurantsEntity = RestaurantsEntity(),
    @Relation(
        parentColumn = "id",
        entityColumn = "resId"
    )
    val favoriteInfo : FavoriteRestaurantEntity? = null,

    @Relation(
        parentColumn = "id",               // id المطعم في جدول المطاعم
        entityColumn = "id",               // id التصنيف في جدول التصنيفات
        associateBy = Junction(
            value = RestaurantCategoryCrossRef::class,
            parentColumn = "restaurantId",    // عمود المطعم جوه جدول الكوبري
            entityColumn = "categoryId"       // عمود التصنيف جوه جدول الكوبري
        )
    )
    val categories : List<CategoriesEntity> = emptyList() // هنا مش محتاجين نكتب emptyList() لأن Room تلقائيا لو ملقتش حاجة في categories هترجع emptyList() لوحدها
){
    val isFavorite : Boolean get() = favoriteInfo != null && !favoriteInfo.isDeletedOffline
}



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
    val orderItems : List<OrderItemsClass> = emptyList(),
    val orderHistory : List<OrderHistoryClass> = emptyList(),
    val updatedAt : Long = 0L
)