package com.example.applicationhome.features.reviews.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.applicationhome.core.data.remote.NetworkObserver
import com.example.applicationhome.core.data.remote.dto.ReviewsForPut
import com.example.applicationhome.core.domain.repository.RestaurantRepository
import com.example.applicationhome.core.domain.repository.ReviewsRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.ui.mapper.restaurantDomainClassToRestaurantsUiClass
import com.example.applicationhome.core.ui.mapper.reviewsDomainClassToReviewsUIClass
import com.example.applicationhome.core.ui.model.RestaurantsUIClass
import com.example.applicationhome.core.ui.model.ReviewsUIClass
import com.example.applicationhome.core.ui.model.UiStates
import com.example.applicationhome.features.reviews.model.PutReviewStates
import com.example.applicationhome.features.reviews.model.ReviewsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val reviewsRepository : ReviewsRepository,
    private val restaurantRepository : RestaurantRepository,
    userRepository : UserRepository,
    networkObserver : NetworkObserver,
    savedStateHandle : SavedStateHandle
): ViewModel(){
    val isNetworkAvailable =  networkObserver.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
    val userData = userRepository.userData
    private val _resId = MutableStateFlow(0)
    val restaurant =
        _resId.flatMapLatest {  id ->
            restaurantRepository.getRestaurantByIdFromDatabase(id)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )


    private val _uiState = MutableStateFlow(ReviewsUIState())
    val uiState = _uiState.asStateFlow()

    val allReviews : Flow<PagingData<ReviewsUIClass>> =
        combine(
            _resId,
            userData
        ){ resId, user ->
            Pair(resId, user)
        }.flatMapLatest { (resId, user) ->
            reviewsRepository.getAllReviewsFromDatabase(
                resId = resId,
                userId = user.id
            ).map { item ->
                item.map {
                    it.reviewsDomainClassToReviewsUIClass()
                }
            }
        }.cachedIn(viewModelScope)



    private fun observeUserReview() {
        viewModelScope.launch {
            combine(_resId, userData) { resId, user -> Pair(resId, user) }
                .flatMapLatest { (resId, user) ->
                    reviewsRepository.getUserReviewFromDatabase(resId, user.id)
                }.collect { domainReview ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            userReview = domainReview?.reviewsDomainClassToReviewsUIClass()
                        )
                    }
                }
        }
    }

    private fun checkIfUserCanReview() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(loadingState = UiStates.Loading)
            }

            combine(_resId, userData) { resId, user ->
                resId to user
            }.onEach { (resId, user) ->
                reviewsRepository.checkIfUserDidOrderedFromRestaurant(resId, user.id)
                    .onSuccess { result ->
                        syncRestaurantReviews(resId)
                        _uiState.update { currentState ->
                            currentState.copy(isUserDidOrderFromRestaurant = result)
                        }
                    }.onFailure {
                        _uiState.update { currentState ->
                            currentState.copy(loadingState = UiStates.Offline)
                        }
                    }
            }.launchIn(viewModelScope)
        }
    }


    fun isReviewsDeferred(review : ReviewsUIClass){
        _uiState.update { currentState ->
            val finalReview = currentState.userReview?.copy(
                stars = review.stars,
                comment = review.comment
            )?: ReviewsUIClass(
                stars = review.stars,
                comment = review.comment
            )

            currentState.copy(
                isReviewsDeferred = finalReview != _uiState.value.userReview,
            )
        }
    }

    fun onReviewClick(review : ReviewsUIClass){
        _uiState.update { currentState ->
            currentState.copy(
                userNewReview = review,
                showReviewBottomSheet = true
            )
        }
        isReviewsDeferred(review)
    }
    fun onCloseReviewBottomSheet(){
        _uiState.update { currentState ->
            currentState.copy(showReviewBottomSheet = false)
        }
    }


    fun onPostReview(stars : Int, comment : String){
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(putReviewStates = PutReviewStates.Loading)
            }
            val review = ReviewsForPut(
                userName = userData.value.firstname + " " + userData.value.lastname,
                resId = _resId.value,
                stars = stars.toDouble(),
                comment = comment
            )

            reviewsRepository.putReview(
                review = review,
                userId = userData.value.id
            ).onSuccess {
                syncRestaurantReviews(_resId.value)

                _uiState.update { currentState ->
                    currentState.copy(
                        showReviewBottomSheet = false,
                        putReviewStates = PutReviewStates.Success
                    )
                }
            }.onFailure {
                _uiState.update { currentState ->
                    currentState.copy(putReviewStates = PutReviewStates.Failure)
                }
            }
        }
    }

    fun onShowDeleteReviewDialog(){
        _uiState.update { currentState ->
            currentState.copy(showConfirmDeleteDialog = true)
        }
    }
    fun onCloseDeleteReviewDialog(){
        _uiState.update { currentState ->
            currentState.copy(showConfirmDeleteDialog = false)
        }
    }
    fun deleteReview(){
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(putReviewStates = PutReviewStates.Loading)
            }

            reviewsRepository.deleteReview(
                resId = _resId.value,
                userId = userData.value.id
            ).onSuccess {
                _uiState.update { currentState ->
                    currentState.copy(putReviewStates = PutReviewStates.Success)
                }
            }.onFailure {
                _uiState.update { currentState ->
                    currentState.copy(putReviewStates = PutReviewStates.Failure)
                }
            }
        }
    }

    private fun loadRestaurantDetails(restaurantId : Int){
        viewModelScope.launch {
            val restaurantFlow = restaurantRepository.getRestaurantByIdFromDatabase(restaurantId)

            restaurantFlow.collect { restaurantDomain ->

                val restaurantUi = restaurantDomain?.restaurantDomainClassToRestaurantsUiClass()
                    ?: RestaurantsUIClass()

                _uiState.update { currentState ->
                    currentState.copy(
                        restaurant = restaurantUi
                    )
                }
            }
        }
    }

    private fun syncRestaurantReviews(resId : Int){
        viewModelScope.launch {
            reviewsRepository.syncRestaurantReviewsFromAPI(resId)
                .onSuccess {
                    observeUserReview()
                    _uiState.update { currentState ->
                        currentState.copy(loadingState = UiStates.Success)
                    }
                }.onFailure {
                    _uiState.update { currentState ->
                        currentState.copy(loadingState = UiStates.Offline)
                    }
                }
        }
    }


    init {
        _resId.value = checkNotNull(savedStateHandle["restaurantId"])

        viewModelScope.launch {
            combine(
                isNetworkAvailable,
                _resId
            ){ network, resId ->
                network to resId
            }.onEach { (network, resId) ->
                loadRestaurantDetails(resId)

                if(network){
                    if(_uiState.value != UiStates.Success) checkIfUserCanReview()
                }else{
                    if(_uiState.value != UiStates.Success){
                        _uiState.update { currentState ->
                            currentState.copy(loadingState = UiStates.Offline)
                        }
                    }
                }
                observeUserReview()
            }.launchIn(viewModelScope)
        }
    }
}