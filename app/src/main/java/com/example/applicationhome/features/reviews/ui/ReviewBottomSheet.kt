package com.example.applicationhome.features.reviews.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.designsystem.TopBarButtons
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.LoadingDialog
import com.example.applicationhome.core.ui.model.ReviewsUIClass
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.features.reviews.model.PutReviewStates

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewBottomSheet(
    animateIn : MutableTransitionState<Boolean>,
    stars : Int,
    comment : String,
    userName : String,
    resName : String,
    resImage : String,
    canUserReview : Boolean,
    putReviewStates : PutReviewStates,
    animDuration : Int = 300,
    onDismiss : () -> Unit,
    isReviewsDeferred : (review : ReviewsUIClass) -> Unit,
    onPostClick : (rating: Int, comment: String) -> Unit
){
    val maxChars = 500
    var selectedRating by rememberSaveable(stars) { mutableIntStateOf(stars) }
    var commentText by rememberSaveable(comment) { mutableStateOf(comment) }

    val focusManager = LocalFocusManager.current

    BackHandler(enabled = animateIn.targetState){
        onDismiss()
        selectedRating = stars
        commentText = comment
    }

    AnimatedVisibility(
        visibleState = animateIn,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = animDuration),
            initialOffsetY = { fullHeight -> fullHeight }
        ) + fadeIn(animationSpec = tween(durationMillis = animDuration)),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = animDuration),
            targetOffsetY = { fullHeight -> fullHeight }
        ) + fadeOut(animationSpec = tween(durationMillis = animDuration))
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                ),
            contentAlignment = Alignment.BottomCenter
        ){
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(animationSpec = tween(durationMillis = animDuration))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { focusManager.clearFocus() }
                    ),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ){
                LoadingDialog(putReviewStates == PutReviewStates.Loading)

                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 7.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ){
                            TopBarButtons(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(R.string.close),
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                onClick = {
                                    onDismiss()
                                    selectedRating = stars
                                    commentText = comment
                                },
                                elevation = 0.dp,
                                border = 1.dp
                            )

                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .size(40.dp)
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
                                        .data(resImage)
                                        .crossfade(true)
                                        .precision(Precision.EXACT)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Text(
                                text = resName,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        TextButton(
                            onClick = {
                                if (selectedRating > 0) {
                                    onPostClick(selectedRating, commentText)
                                }
                            },
                            enabled = selectedRating > 0 && canUserReview,
                            shape = CircleShape,
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (selectedRating > 0 && canUserReview) Color.DarkOrange else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            ),
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = Color.DarkOrange,
                                disabledContainerColor = MaterialTheme.colorScheme.surface,
                                disabledContentColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ){
                            Text(
                                text = stringResource(R.string.post),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }


                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ){
                        val firstChar = userName.firstOrNull()?.uppercase() ?: "?"
                        val avatarColor = remember(userName) { getUserAvatarColor(userName) }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(avatarColor),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = firstChar,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 15.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        for (i in 1..5) {
                            val isSelected = i <= selectedRating

                            val scale by animateFloatAsState(
                                targetValue = if (isSelected) 1.2f else 1.0f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioHighBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                label = "StarScale"
                            )

                            val animatedColor by animateColorAsState(
                                targetValue = if (isSelected) Color.DarkOrange else Color.Gray,
                                animationSpec = tween(durationMillis = 200),
                                label = "StarColor"
                            )

                            Icon(
                                imageVector = if (isSelected) Icons.Default.Star else Icons.Outlined.StarOutline,
                                contentDescription = "Star $i",
                                tint = animatedColor,
                                modifier = Modifier
                                    .size(35.dp)
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ){
                                        selectedRating = i
                                        isReviewsDeferred(ReviewsUIClass(stars = selectedRating.toDouble(), comment = commentText))
                                        focusManager.clearFocus()
                                    }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { newText ->
                            if (newText.length <= maxChars) {
                                commentText = newText
                            }
                            isReviewsDeferred(ReviewsUIClass(stars = selectedRating.toDouble(), comment = commentText))
                        },
                        placeholder = {
                            Text(
                                text = "Describe your experience (optional)",
                                color = Color.Gray
                            )
                        },
                        supportingText = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "${commentText.length}/$maxChars",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (commentText.length == maxChars) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                        minLines = 1,
                        maxLines = 10,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.DarkOrange,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}