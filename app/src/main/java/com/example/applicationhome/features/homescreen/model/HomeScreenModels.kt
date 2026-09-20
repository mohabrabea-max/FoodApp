package com.example.applicationhome.features.homescreen.model

import androidx.paging.compose.LazyPagingItems
import com.example.applicationhome.core.data.local.entity.CategoriesEntity
import com.example.applicationhome.core.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.core.data.local.entity.OffersEntity
import com.example.applicationhome.core.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.core.data.local.entity.UserClass

data class HomeScreenActions(
    val select : (CategoriesEntity) -> Unit = {},
    val unSelected : () -> Unit = {},
    val addRestaurantsFavorite : (FavoriteRestaurantEntity) -> Unit = {},
    val removeRestaurantsFavorite : (Int) -> Unit = {},
    val closeBottomSheet : () -> Unit = {}
)

data class HomeScreenParameters(
    val isNetworkAvailable : Boolean = false,
    val categories : List<CategoriesEntity> = emptyList(),
    val categorySelected : Int = 0,
    val userData : UserClass = UserClass(),
    val restaurants : LazyPagingItems<RestaurantWithFavoriteStatus>? = null,
    val offers : List<OffersEntity> = emptyList()
)