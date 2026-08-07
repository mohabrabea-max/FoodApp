package com.example.applicationhome.features.confirmorder.ui

import android.preference.PreferenceManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.applicationhome.core.ui.theme.DarkOrange
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

val CARTO_VOYAGER = XYTileSource(
    "CartoDB-Voyager",
    0, 19, 256, ".png",
    arrayOf(
        "https://a.basemaps.cartocdn.com/rastertiles/voyager/",
        "https://b.basemaps.cartocdn.com/rastertiles/voyager/",
        "https://c.basemaps.cartocdn.com/rastertiles/voyager/"
    )
)

@Composable
fun StreetMapComposable(
    modifier : Modifier = Modifier,
    initialLatitude : Double = 30.0444,
    initialLongitude : Double = 31.2357,
    onLocationSelected : (latitude : Double, longitude : Double) -> Unit
){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Configuration.getInstance().load(
        context,
        PreferenceManager.getDefaultSharedPreferences(context)
    )
    Configuration.getInstance().userAgentValue = "MyFoodDeliveryApp_Android_Client_v1.0"

    val mapView = remember {
        MapView(context).apply {
            setTileSource(CARTO_VOYAGER) // الشكل القياسي لخرائط OSM
            setMultiTouchControls(true) // تفعيل اللمس المتعدد (Zoom in/out)
        }
    }

    //  إدارة دورة حياة الخريطة (Lifecycle) لمنع تسريب الميموري
    DisposableEffect(lifecycleOwner){
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        //  عرض الخريطة وتحديد الإحداثيات والـ Marker
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory =  {
                mapView.apply {
                    // ضبط مستوى التكبير (Zoom) وموقع الكاميرا
                    controller.setZoom(17.0)
                    controller.setCenter(GeoPoint(initialLatitude, initialLongitude))

                    addMapListener(object : MapListener{
                        override fun onScroll(event: ScrollEvent?): Boolean {
                            val center = mapCenter as GeoPoint
                            onLocationSelected(center.latitude, center.longitude)
                            return true
                        }

                        override fun onZoom(event: ZoomEvent?): Boolean {
                            val center = mapCenter as GeoPoint
                            onLocationSelected(center.latitude, center.longitude)
                            return true
                        }
                    })
                }
            }
        )

        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Picker",
            tint = Color.DarkOrange,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
        )
    }
}