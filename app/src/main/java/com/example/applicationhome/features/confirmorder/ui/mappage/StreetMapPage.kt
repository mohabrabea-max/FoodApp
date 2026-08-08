package com.example.applicationhome.features.confirmorder.ui.mappage

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.components.forHomeScreenOrMenu.bottomSnackBar
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.DeepMatteBlack
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreetMapPage(
    modifier : Modifier = Modifier,
    initialLatitude : Double = 30.0444,
    initialLongitude : Double = 31.2357,
    snackBarHostState : SnackbarHostState,
    scope : CoroutineScope,
    uiEvent: Flow<UiEvent>,
    isNetworkAvailable : Boolean,
    onLocationSelected : (latitude : Double, longitude : Double) -> Unit,
    changePage : () -> Unit,
    retryNetwork : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    val lifecycleOwner = LocalLifecycleOwner.current

    var location = rememberSaveable { Pair(30.0444, 31.2357) }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),

        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    Snackbar(
                        modifier = Modifier.padding(12.dp).height(50.dp).clip(RoundedCornerShape(10.dp)),
                        containerColor = Color.DeepMatteBlack,
                        contentColor = Color.White
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Error Icon",
                                    tint = Color.White
                                )

                                Text(text = data.visuals.message)
                            }

                            Box(
                                modifier = Modifier.clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ){
                                    data.performAction()
                                }
                            ){
                                Text(text = data.visuals.actionLabel.toString())
                            }
                        }
                    }
                }
            )
        },

        bottomBar = {
            val buttonColor = if(isNetworkAvailable) Color.DarkOrange else Color.VeryLightGray
            val fontButtonColor = if(isNetworkAvailable) Color.White else Color.LightGray

            Box(
                modifier = Modifier.fillMaxWidth().
                height(80.dp).
                shadow(elevation = 7.dp).
                background(Color.White).
                pointerInput(Unit) {
                    detectTapGestures { }
                }.
                padding(horizontal = 15.dp),
                contentAlignment = Alignment.Center
            ){
                MyButton(
                    loading = false,
                    backgroundcolor = buttonColor,
                    fontcolor = fontButtonColor,
                    horizontalPadding = 30.dp,
                    title = "Select Location",
                    action = {
                        if(isNetworkAvailable){
                            onLocationSelected(location.first, location.second)
                            changePage()
                        }
                    }
                )
            }
        }
    ) {
        StreetMapComposable(
            modifier,
            initialLatitude,
            initialLongitude
        ) { lat, lng ->
            location = location.copy(first = lat, second = lng)
        }

        LaunchedEffect(uiEvent) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                uiEvent.collect { event ->
                    when(event){
                        UiEvent.ShowNetworkError -> {
                            scope.bottomSnackBar(
                                snackBarHostState = snackBarHostState,
                                onActionClicked = { retryNetwork() },
                                message = "Error getting location information",
                                actionLabel = "Retry"
                            )
                        }
                    }
                }
            }
        }
    }
}