package com.example.applicationhome.features.orders.ui.lastorders

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.bars.NetworkErrorTopBar
import com.example.applicationhome.core.ui.components.forCart.AlertDialogMessage
import com.example.applicationhome.core.ui.components.screens.EmptyScreen
import com.example.applicationhome.core.ui.theme.DeepMatteBlack
import com.example.applicationhome.data.data.model.ActiveOrderDialog
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.data.model.OrdersHistoryScreens
import com.example.applicationhome.data.data.model.RepurchaseOrderStates
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.data.model.UiEventOrderCancelled
import com.example.applicationhome.features.orders.ui.OrderScreenViewModel
import com.example.applicationhome.features.orders.ui.orderscreen.OrderScreen
import com.example.applicationhome.features.shimmers.screens.OrdersHistoryShimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LastOrdersScreen(
    navigationController : NavHostController,
    orderScreenViewModel : OrderScreenViewModel
){
    val scope = rememberCoroutineScope()

    val isNetworkAvailable by orderScreenViewModel.isNetworkAvailable.collectAsStateWithLifecycle()

    val screenState by orderScreenViewModel.screenState.collectAsStateWithLifecycle()

    val preparingOrdersHistory by orderScreenViewModel.preparingOrdersHistory.collectAsStateWithLifecycle()
    val deliveredOrdersHistory by orderScreenViewModel.deliveredOrdersHistory.collectAsStateWithLifecycle()
    val cancelledOrdersHistory by orderScreenViewModel.cancelledOrdersHistory.collectAsStateWithLifecycle()

    val screens = orderScreenViewModel.statesBar
    val initialPage by orderScreenViewModel.initialPage.collectAsStateWithLifecycle()
    var hasHandled by rememberSaveable { mutableStateOf(false) }
    val pagerState = rememberPagerState(initialPage = initialPage,pageCount = { screens.size })

    LaunchedEffect(screenState){
        if(screenState != HomeUiState.Loading && !hasHandled){
            pagerState.animateScrollToPage(
                page = initialPage,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
            hasHandled = true
        }
    }

    val uiEventOrderCancelled = orderScreenViewModel.uiEventOrderCancelled
    LaunchedEffect(Unit){
        uiEventOrderCancelled.collect { eventOrderCancelled ->
            if(eventOrderCancelled == UiEventOrderCancelled.OrderCancelledSuccessfully){
                pagerState.animateScrollToPage(
                    page = initialPage,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            }
        }
    }

    val actionState by orderScreenViewModel.actionState.collectAsStateWithLifecycle()
    val activeDialog by orderScreenViewModel.activeDialog.collectAsStateWithLifecycle()

    val selectedOrder by orderScreenViewModel.selectedOrder.collectAsStateWithLifecycle()
    val correctTimelineStep by orderScreenViewModel.correctTimelineStep.collectAsStateWithLifecycle()

    val animDuration = 300
    val animateIn = remember(selectedOrder){
        MutableTransitionState(false).apply {
            targetState = selectedOrder != null
        }
    }

    BackHandler(enabled = true){
        if (navigationController.previousBackStackEntry != null) {
            navigationController.popBackStack()
        }
    }


    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),

        containerColor = Color.White,

        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                MyTopBar(
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth().height(100.dp).shadow(elevation = 5.dp),
                    title = stringResource(R.string.orders_history),
                    titleColor = Color.DeepMatteBlack,
                    startaction = {
                        IconButton(
                            onClick = {
                                if (navigationController.previousBackStackEntry != null) {
                                    navigationController.popBackStack()
                                }
                            },
                            modifier = Modifier.padding(5.dp).clip(CircleShape).size(40.dp)
                                .background(Color.White)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.DeepMatteBlack
                            )
                        }
                    }
                )

                NetworkErrorTopBar(isNetworkAvailable = isNetworkAvailable)

                Spacer(modifier = Modifier.height(5.dp))

                OrdersHistoryStatesBar(
                    items = orderScreenViewModel.statesBar,
                    selectedIndex = pagerState.currentPage,
                    onItemSelection = { index ->
                        scope.launch {
                            pagerState.animateScrollToPage(
                                page = index,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            )
                        }
                    },
                    itemTitle = { screen -> stringResource(screen.title) }
                )
            }
        }
    ){
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 2,
            modifier = Modifier.fillMaxSize()
        ){ page ->
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxSize()
            ){
                item{Spacer(modifier = Modifier.height(160.dp))}

                when(screenState){
                    HomeUiState.Success, HomeUiState.Offline -> {
                        when(screens[page]){
                            OrdersHistoryScreens.Preparing -> {
                                if(preparingOrdersHistory.isNotEmpty()){
                                    items(preparingOrdersHistory){ item ->
                                        Spacer(modifier = Modifier.height(10.dp))
                                        LastOrdersBox(item){
                                            orderScreenViewModel.openOrderScreen(item)
                                        }
                                    }
                                }else{
                                    item { EmptyScreen(R.string.no_previous_requests) }
                                }
                            }

                            OrdersHistoryScreens.Delivered -> {
                                if(deliveredOrdersHistory.isNotEmpty()){
                                    items(deliveredOrdersHistory){ item ->
                                        Spacer(modifier = Modifier.height(10.dp))
                                        LastOrdersBox(item){
                                            orderScreenViewModel.openOrderScreen(item)
                                        }
                                    }
                                }else{
                                    item { EmptyScreen(R.string.no_previous_requests) }
                                }
                            }

                            OrdersHistoryScreens.Cancelled -> {
                                if(cancelledOrdersHistory.isNotEmpty()){
                                    items(cancelledOrdersHistory){ item ->
                                        Spacer(modifier = Modifier.height(10.dp))
                                        LastOrdersBox(item){
                                            orderScreenViewModel.openOrderScreen(item)
                                        }
                                    }
                                }else{
                                    item { EmptyScreen(R.string.no_previous_requests) }
                                }
                            }
                        }
                    }

                    HomeUiState.Loading -> {
                        item { OrdersHistoryShimmer() }
                    }
                }

                item{Spacer(modifier = Modifier.height(50.dp))}
            }
        }
    }


    // --------------------------------------------\\ Order Details Screen //--------------------------------------------

    selectedOrder?.let { item ->
        OrderScreen(
            order = item,
            isNetworkAvailable = isNetworkAvailable,
            timelineStep = correctTimelineStep,
            animateIn = animateIn,
            animDuration = animDuration,
            actionState = actionState,
            onRepurchase = { orderScreenViewModel.openCheckRepurchaseAlertDialogMessage() },
            onCancel = { orderScreenViewModel.openCheckCancelOrderAlertDialogMessage() },
            onCloseOrderScreen = {
                scope.launch {
                    animateIn.targetState = false
                    delay(animDuration.toLong().milliseconds)
                    orderScreenViewModel.closeOrderScreen()
                }
            }
        )
    }


    // --------------------------------------------\\ Alert Dialog Messages //--------------------------------------------

    when(val dialog = activeDialog){
        ActiveOrderDialog.None -> {}

        // --------------------------------------------------- Cancel Order -------------

        ActiveOrderDialog.ConfirmCancel -> {
            val order = selectedOrder
            if(order != null) AlertDialogMessage(
                title = stringResource(R.string.disclaimer),
                content = stringResource(R.string.are_you_sure_you_want_to_cancel_the_order),
                confirmButtonText = stringResource(R.string.yes_i_m_sure),
                confirmButton = {
                    orderScreenViewModel.closeAlertDialogMessage()
                    orderScreenViewModel.cancelOrder(
                        orderId = order.orderId
                    )
                },
                dismissButtonText = stringResource(R.string.cancel),
                dismissButton = { orderScreenViewModel.closeAlertDialogMessage() }
            )
        }

        ActiveOrderDialog.CancelFailed -> {
            val order = selectedOrder
            if(order != null) AlertDialogMessage(
                title = stringResource(R.string.sorry),
                content = stringResource(R.string.the_order_could_not_be_cancelled_please_try_again_later),
                confirmButtonText = stringResource(R.string.try_again),
                confirmButton = {
                    orderScreenViewModel.resetActionState()
                    orderScreenViewModel.cancelOrder(
                        orderId = order.orderId
                    )
                },
                dismissButtonText = stringResource(R.string.cancel),
                dismissButton = { orderScreenViewModel.resetActionState() }
            )
        }


        // --------------------------------------------------- Repurchase -------------

        ActiveOrderDialog.ConfirmRepurchase -> {
            val order = selectedOrder
            if(order != null) AlertDialogMessage(
                title = stringResource(R.string.disclaimer),
                content = stringResource(R.string.all_previous_meals_will_be_deleted_from_the_shopping_cart),
                confirmButtonText = stringResource(R.string.add_anyway),
                confirmButton = {
                    orderScreenViewModel.closeAlertDialogMessage()
                    orderScreenViewModel.repurchaseOrder(order)
                },
                dismissButtonText = stringResource(R.string.cancel),
                dismissButton = { orderScreenViewModel.closeAlertDialogMessage() }
            )
        }

        is ActiveOrderDialog.RepurchaseResult -> {
            when(val state = dialog.state){
                is RepurchaseOrderStates.Success -> {
                    AlertDialogMessage(
                        title = stringResource(R.string.success),
                        content = stringResource(state.message),
                        confirmButtonText = stringResource(R.string.go_to_cart),
                        confirmButton = {
                            orderScreenViewModel.closeAlertDialogMessage()
                            navigationController.navigate(Screens.Cart.screen){ launchSingleTop = true }
                        },
                        dismissButtonText = stringResource(R.string.cancel),
                        dismissButton = { orderScreenViewModel.closeAlertDialogMessage() }
                    )
                }

                is RepurchaseOrderStates.RestaurantIsDeleted -> {
                    AlertDialogMessage(
                        title = stringResource(R.string.sorry),
                        content = stringResource(state.message),
                        confirmButtonText = stringResource(R.string.done),
                        confirmButton = { orderScreenViewModel.closeAlertDialogMessage() }
                    )
                }

                is RepurchaseOrderStates.MealsAreDeleted -> {
                    val order = selectedOrder
                    if(order != null) AlertDialogMessage(
                        title = stringResource(R.string.disclaimer),
                        content = stringResource(state.message),
                        confirmButtonText = stringResource(R.string.add_anyway),
                        confirmButton = {
                            orderScreenViewModel.closeAlertDialogMessage()
                            orderScreenViewModel.filterOrderItems(order)
                        },
                        dismissButtonText = stringResource(R.string.cancel),
                        dismissButton = { orderScreenViewModel.closeAlertDialogMessage() }
                    )
                }

                is RepurchaseOrderStates.ALLMealsAreDeleted -> {
                    AlertDialogMessage(
                        title = stringResource(R.string.sorry),
                        content = stringResource(state.message),
                        confirmButtonText = stringResource(R.string.done),
                        confirmButton = { orderScreenViewModel.closeAlertDialogMessage() }
                    )
                }
            }
        }
    }
}