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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.applicationhome.core.ui.theme.DeepMatteBlack
import com.example.applicationhome.data.data.model.OrdersHistoryScreens
import com.example.applicationhome.features.orders.ui.OrderScreenViewModel
import com.example.applicationhome.features.orders.ui.orderscreen.OrderScreen
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

    val screens = orderScreenViewModel.statesBar
    val pagerState = rememberPagerState(pageCount = { screens.size })

    val preparingOrdersHistory by orderScreenViewModel.preparingOrdersHistory.collectAsStateWithLifecycle()
    val deliveredOrdersHistory by orderScreenViewModel.deliveredOrdersHistory.collectAsStateWithLifecycle()
    val cancelledOrdersHistory by orderScreenViewModel.cancelledOrdersHistory.collectAsStateWithLifecycle()

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
        ) { page ->
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxSize()
            ){
                item{Spacer(modifier = Modifier.height(160.dp))}

                if(
                    preparingOrdersHistory.isNotEmpty()
                    || deliveredOrdersHistory.isNotEmpty()
                    || cancelledOrdersHistory.isNotEmpty()
                ){
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

                            }
                        }
                    }
                }

                item{Spacer(modifier = Modifier.height(50.dp))}
            }
        }
    }

    selectedOrder?.let { item ->
        OrderScreen(
            order = item,
            timelineStep = correctTimelineStep,
            animateIn = animateIn,
            animDuration = animDuration,
            onCloseOrderScreen = {
                scope.launch {
                    animateIn.targetState = false
                    delay(animDuration.toLong().milliseconds)
                    orderScreenViewModel.closeOrderScreen()
                }
            }
        )
    }
}