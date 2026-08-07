package com.example.applicationhome.features.confirmorder.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.theme.DarkOrange

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreetMapPage(
    modifier : Modifier = Modifier,
    initialLatitude : Double = 30.0444,
    initialLongitude : Double = 31.2357,
    onLocationSelected : (latitude : Double, longitude : Double) -> Unit,
    changePage : (Int) -> Unit
){
    var location = rememberSaveable { Pair(30.0444, 31.2357) }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),

        bottomBar = {
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
                    backgroundcolor = Color.DarkOrange,
                    fontcolor = Color.White,
                    horizontalPadding = 30.dp,
                    title = "Select Location",
                    action = {
                        onLocationSelected(location.first, location.second)
                        changePage(1)
                    }
                )
            }
        }
    ) {
        StreetMapComposable(
            modifier,
            initialLatitude,
            initialLongitude
        ){ lat, lng ->
            location = location.copy(first = lat, second = lng)
        }
    }
}