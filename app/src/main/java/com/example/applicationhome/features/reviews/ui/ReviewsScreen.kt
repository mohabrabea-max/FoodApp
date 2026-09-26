package com.example.applicationhome.features.reviews.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.R
import com.example.applicationhome.core.data.local.entity.UserClass
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.bars.NetworkErrorTopBar
import com.example.applicationhome.core.ui.components.forCart.AlertDialogMessage
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.LoadingDialog
import com.example.applicationhome.core.ui.components.screens.EmptyScreen
import com.example.applicationhome.core.ui.model.ReviewsUIClass
import com.example.applicationhome.core.ui.model.UiStates
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.features.reviews.model.PutReviewStates
import com.example.applicationhome.features.reviews.model.ReviewsUIActions
import com.example.applicationhome.features.reviews.model.ReviewsUIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.microseconds

//@Preview
@Composable
fun ReviewsScreen(
    user : UserClass,
    uiState : ReviewsUIState,
    allReviews : LazyPagingItems<ReviewsUIClass>,
    reviewsUIActions : ReviewsUIActions
){

    val interactionSource = remember { MutableInteractionSource() }
    var selectedRating by rememberSaveable { mutableIntStateOf(0) }

    val scope = rememberCoroutineScope()
    val animDuration = 300
    val animateIn = remember(uiState.showReviewBottomSheet) {
        MutableTransitionState(false).apply {
            targetState = uiState.showReviewBottomSheet
        }
    }

    BackHandler(enabled = true){
        reviewsUIActions.popBack()
    }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),

        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            Column(modifier = Modifier.shadow(elevation = 3.dp)){
                MyTopBar(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    title = stringResource(R.string.reviews),
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    startaction = {
                        IconButton(
                            onClick = {
                                reviewsUIActions.popBack()
                            },
                            modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                        ){
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 15.dp, vertical = 15.dp),

                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .border(
                                width = 0.5.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ){
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(uiState.restaurant.image)
                                .crossfade(true)
                                .precision(Precision.EXACT)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(start = 13.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.Start
                    ){
                        Text(
                            text = uiState.restaurant.name,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )


                        Row(
                            modifier = Modifier
                                .height(25.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colorScheme.background),

                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700) ,
                                modifier = Modifier.size(21.dp)
                            )

                            Text(
                                text = uiState.restaurant.review,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier
                            )
                        }
                    }
                }

                NetworkErrorTopBar(isNetworkAvailable = uiState.loadingState != UiStates.Offline)
            }
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            item { Spacer(modifier = Modifier.height(10.dp)) }

            when(uiState.loadingState){
                UiStates.Success, UiStates.Offline -> {
                    when{
                        uiState.canUserReview -> {
                            item {
                                Text(
                                    text = stringResource(R.string.rate_this_restaurant),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = stringResource(R.string.tell_others_what_you_think),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.titleSmall
                                )

                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ){
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ){
                                        for (i in 1..5) {
                                            Icon(
                                                imageVector = if (i <= selectedRating) Icons.Default.Star else Icons.Outlined.StarOutline,
                                                contentDescription = "Star $i",
                                                tint = if (i <= selectedRating) Color.DarkOrange else Color.Gray,
                                                modifier = Modifier
                                                    .size(38.dp)
                                                    .clickable {
                                                        selectedRating = i
                                                        reviewsUIActions.onReviewClick(ReviewsUIClass(stars = i.toDouble()))
                                                    }
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = stringResource(R.string.write_a_review),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkOrange,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ){
                                            reviewsUIActions.onReviewClick(uiState.userNewReview)
                                        }
                                    )
                                }
                            }
                        }

                        uiState.userReview != null -> {
                            item {
                                Text(
                                    text = stringResource(R.string.your_review),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            item {
                                ReviewItemCard(
                                    review = uiState.userReview,
                                    onEditClick = { reviewsUIActions.onReviewClick(uiState.userReview) },
                                    onDeleteClick = { reviewsUIActions.onShowDeleteReviewDialog() }
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(10.dp)) }

                    when (allReviews.loadState.refresh) {
//                        is LoadState.Loading -> {
//                            item { LoadingDialog(true) }
//                        }


                        //                        allReviews.loadState.refresh is LoadState.Error -> {
                        //                            val e = allReviews.loadState.refresh as LoadState.Error
                        //                            ErrorScreen(message = e.error.localizedMessage ?: "حدث خطأ غير متوقع")
                        //                        }
                        is LoadState.NotLoading if allReviews.itemCount == 0 -> {
                            item{
                                EmptyScreen(
                                    title = stringResource(R.string.there_are_no_ratings),
                                    image = painterResource(R.drawable.emptyscreenicon),
                                    spacerheight = 100.dp
                                )
                            }
                        }

                        else -> {
                            item {
                                Text(
                                    text = stringResource(R.string.reviews),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            items(
                                count = allReviews.itemCount
                            ){ index ->
                                val review = allReviews[index]

                                review?.let {
                                    ReviewItemCard(review = review)
                                }
                            }
                        }
                    }
                }

                UiStates.Loading -> {
                    item{ LoadingDialog(true) }
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }

    ReviewBottomSheet(
        animateIn = animateIn,
        stars = uiState.userNewReview.stars.toInt(),
        comment = uiState.userReview?.comment?:"",
        userName = user.firstname,
        resName = uiState.restaurant.name,
        resImage = uiState.restaurant.image,
        canUserReview = uiState.isReviewsDeferred,
        putReviewStates = uiState.putReviewStates,
        animDuration = animDuration,
        onDismiss = {
            scope.launch {
                delay(animDuration.microseconds)
                reviewsUIActions.onCloseReviewBottomSheet()
            }
        },
        isReviewsDeferred = { review ->
            reviewsUIActions.isReviewsDeferred(review)
        },
        onPostClick = { stars, comment ->
            scope.launch {
                reviewsUIActions.onPostReview(stars, comment)
                delay(animDuration.microseconds)
                if(uiState.putReviewStates == PutReviewStates.Success) reviewsUIActions.onCloseReviewBottomSheet()
            }
        }
    )


    // --------------------------------------------\\ Alert Dialog Messages //--------------------------------------------

    if(uiState.showConfirmDeleteDialog){
        AlertDialogMessage(
            title = stringResource(R.string.disclaimer),
            content = stringResource(R.string.are_you_sure_you_want_to_delete_the_rating),
            confirmButtonText = stringResource(R.string.yes_i_m_sure),
            confirmButton = {
                reviewsUIActions.onDeleteReview()
                reviewsUIActions.onCloseDeleteReviewDialog()
            },
            dismissButtonText = stringResource(R.string.cancel),
            dismissButton = { reviewsUIActions.onCloseDeleteReviewDialog() }
        )
    }
}