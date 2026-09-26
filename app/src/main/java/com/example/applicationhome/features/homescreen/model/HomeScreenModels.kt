package com.example.applicationhome.features.homescreen.model

import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.paging.compose.LazyPagingItems
import com.example.applicationhome.R
import com.example.applicationhome.core.data.local.entity.CategoriesEntity
import com.example.applicationhome.core.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.core.data.local.entity.OffersEntity
import com.example.applicationhome.core.data.local.entity.UserClass
import com.example.applicationhome.core.ui.model.RestaurantsUIClass

@Keep
data class HomeScreenActions(
    val select : (CategoriesEntity) -> Unit = {},
    val unSelected : () -> Unit = {},
    val addRestaurantsFavorite : (FavoriteRestaurantEntity) -> Unit = {},
    val removeRestaurantsFavorite : (Int) -> Unit = {},
    val closeBottomSheet : () -> Unit = {},
    val showDialogForNotifications : () -> Unit = {},
    val showDialogForRequestRejected : () -> Unit = {},
    val closeDialogForNotifications : () -> Unit = {}
)

@Keep
data class HomeScreenParameters(
    val isNetworkAvailable : Boolean = false,
    val categories : List<CategoriesEntity> = emptyList(),
    val categorySelected : Int = 0,
    val userData : UserClass = UserClass(),
    val restaurants : LazyPagingItems<RestaurantsUIClass>? = null,
    val offers : List<OffersEntity> = emptyList(),
    val showDialogForNotifications : NotificationsDialog = NotificationsDialog.Non
)

sealed interface NotificationsDialog{
    data object Non : NotificationsDialog
    data class DialogForTurnOnNotifications(@StringRes val message : Int = R.string.please_turn_on_notifications_to_receive_the_latest_updates) : NotificationsDialog
    data class RequestRejected(@StringRes val message : Int = R.string.the_request_was_rejected_you_will_not_be_able_to_receive_notifications) : NotificationsDialog
}