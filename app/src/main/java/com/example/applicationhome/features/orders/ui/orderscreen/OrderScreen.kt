package com.example.applicationhome.features.orders.ui.orderscreen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.bars.NetworkErrorTopBar
import com.example.applicationhome.core.ui.components.designsystem.SquerButton
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.LoadingDialog
import com.example.applicationhome.core.ui.theme.BrandBlue
import com.example.applicationhome.core.ui.theme.DeepMatteBlack
import com.example.applicationhome.data.data.model.ActionsStates
import com.example.applicationhome.data.data.model.OrderStatesEnum
import com.example.applicationhome.data.data.model.OrderUiClass
import com.example.applicationhome.data.data.model.TimelineStep

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderScreen(
    order : OrderUiClass,
    isNetworkAvailable : Boolean,
    timelineStep : List<TimelineStep>,
    animateIn : MutableTransitionState<Boolean>,
    animDuration : Int,
    actionState : ActionsStates,
    onRepurchase : () -> Unit,
    onCancel : () -> Unit,
    onCloseOrderScreen : () -> Unit
){
    val isCancelled = order.state.enumState == OrderStatesEnum.CANCELLED
    val isContinuous = order.state.enumState == OrderStatesEnum.DELIVERING || order.state.enumState == OrderStatesEnum.PREPARING

    BackHandler(enabled = true){
        onCloseOrderScreen()
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
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.BottomCenter
        ){
            Scaffold(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxSize(),

                containerColor = Color.White,

                topBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        MyTopBar(
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth().height(100.dp).shadow(elevation = 5.dp),
                            title = stringResource(R.string.order_details),
                            titleColor = Color.DeepMatteBlack,
                            startaction = {
                                IconButton(
                                    onClick = { onCloseOrderScreen() },
                                    modifier = Modifier.padding(5.dp).clip(CircleShape).size(40.dp)
                                        .background(Color.White)
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null,
                                        tint = Color.DeepMatteBlack
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {  },
                                    modifier = Modifier.padding(5.dp).clip(CircleShape).size(40.dp)
                                        .background(Color.White)
                                ) {
                                    Icon(
                                        Icons.Default.Report,
                                        contentDescription = null,
                                        tint = Color.DeepMatteBlack
                                    )
                                }
                            }
                        )

                        NetworkErrorTopBar(isNetworkAvailable = isNetworkAvailable)
                    }
                }
            ){ paddingValues ->
                LazyColumn(
                    modifier = Modifier.padding(paddingValues).fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    item { Spacer(modifier = Modifier.height(5.dp)) }

                    item {
                        timelineStep.forEach { item ->
                            TimelineItem(
                                step = item,
                                isLast = item == timelineStep.last(),
                                isCancelled = isCancelled
                            )
                        }
                    }

                    if(isCancelled) item{
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp)
                                .background(Color(0xFFF9E2DE)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ){
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = "Warning",
                                tint = Color(0xFF4A1211),
                                modifier = Modifier.padding(start = 15.dp, end = 10.dp).size(18.dp)
                            )

                            Text(
                                text = order.orderHistory.last().details,
                                fontSize = 14.sp,
                                color = Color.Red
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }

                    items(order.orderItems) { item ->
                        OrderDetelseBox(item)
                    }

                    item {
                        PaymentSummaryForOrderScreen(order)
                    }

                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ){
                    Row(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        SquerButton(
                            modifier = Modifier.weight(1f),
                            loading = false,
                            backgroundcolor = Color.BrandBlue,
                            fontcolor = Color.White,
                            horizontalPadding = 0.dp,
                            title = stringResource(R.string.repurchase)
                        ){ onRepurchase() }

                        if(isContinuous) SquerButton(
                            modifier = Modifier.weight(1f),
                            loading = false,
                            backgroundcolor = Color.Red,
                            fontcolor = Color.White,
                            horizontalPadding = 0.dp,
                            title = stringResource(R.string.cancel_order)
                        ){ onCancel() }
                    }
                }
            }
            LoadingDialog(isLoading = actionState == ActionsStates.Loading)
        }
    }
}