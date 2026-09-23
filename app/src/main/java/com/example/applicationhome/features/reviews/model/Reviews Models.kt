package com.example.applicationhome.features.reviews.model

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
    val userReview : ReviewsUIClass? = null,
    val showReviewBottomSheet : Boolean = false,
    val canUserReview : Boolean = false,
    val loadingState : UiStates = UiStates.Loading,
    val putReviewStates : PutReviewStates = PutReviewStates.Idl
)

data class ReviewsUIActions(
    val popBack : () -> Unit,
    val onReviewClick : () -> Unit,
    val onCloseReviewBottomSheet : () -> Unit,
    val onPostReview : (stars : Int, comment : String) -> Unit,
    val onDeleteReview : () -> Unit
)