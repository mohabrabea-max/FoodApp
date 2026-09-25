package com.example.applicationhome.features.reviews.model

import com.example.applicationhome.core.ui.model.RestaurantsUIClass
import com.example.applicationhome.core.ui.model.ReviewsUIClass
import com.example.applicationhome.core.ui.model.UiStates

sealed interface PutReviewStates {
    data object Idl : PutReviewStates
    data object Loading : PutReviewStates
    data object Success : PutReviewStates
    data object Failure : PutReviewStates
}

data class ReviewsUIState(
    val resId : Int = 0,
    val restaurant : RestaurantsUIClass = RestaurantsUIClass(),
    val userReview : ReviewsUIClass? = null,
    val userNewReview : ReviewsUIClass = ReviewsUIClass(),
    val isReviewsDeferred : Boolean = false,
    val showReviewBottomSheet : Boolean = false,
    val isUserDidOrderFromRestaurant : Boolean = false,
    val showConfirmDeleteDialog : Boolean = false,
    val loadingState : UiStates = UiStates.Loading,
    val putReviewStates : PutReviewStates = PutReviewStates.Idl
){
    val canUserReview = isUserDidOrderFromRestaurant && userReview == null
}

data class ReviewsUIActions(
    val popBack : () -> Unit,
    val onReviewClick : (review : ReviewsUIClass) -> Unit,
    val onCloseReviewBottomSheet : () -> Unit,
    val isReviewsDeferred : (review : ReviewsUIClass) -> Unit,
    val onPostReview : (stars : Int, comment : String) -> Unit,
    val onDeleteReview : () -> Unit,
    val onShowDeleteReviewDialog : () -> Unit,
    val onCloseDeleteReviewDialog : () -> Unit
)