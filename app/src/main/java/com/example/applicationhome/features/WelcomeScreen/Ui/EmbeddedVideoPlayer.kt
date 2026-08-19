package com.example.applicationhome.features.WelcomeScreen.Ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@SuppressLint("UseKtx")
@OptIn(UnstableApi::class)
@Composable
fun EmbeddedVideoPlayer(
    @RawRes videoResId: Int,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current

    // بناء الـ Uri المباشر للـ Raw Resource
    val videoUri = remember(videoResId){
        Uri.parse("android.resource://${context.packageName}/$videoResId")
    }

    val exoPlayer = remember(context, videoUri){
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    // تفريغ ذاكرة الكارت والرام فور الخروج من الشاشة
    DisposableEffect(Unit){
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = false // إخفاء واجهة أزرار التقديم والتأخير
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        },
        modifier = modifier
    )
}