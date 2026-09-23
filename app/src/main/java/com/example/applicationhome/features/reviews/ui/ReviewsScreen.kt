package com.example.applicationhome.features.reviews.ui

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.applicationhome.core.ui.model.ReviewsUIClass
import com.example.applicationhome.features.reviews.model.ReviewsUIActions
import com.example.applicationhome.features.reviews.model.ReviewsUIState

@Composable
fun ReviewsScreen(
    uiState : ReviewsUIState,
    allReviews : LazyPagingItems<ReviewsUIClass>,
    reviewsUIActions : ReviewsUIActions
){

}