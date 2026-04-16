package com.example.game

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
@Composable
fun MusicPlayer(
    isSoundOn: Boolean,
    songResId: Int
) {
    val context = LocalContext.current

    val mediaPlayer = remember {
        MediaPlayer.create(context, songResId).apply {
            isLooping = true
        }
    }

    LaunchedEffect(isSoundOn, mediaPlayer) {
        if (isSoundOn) {
            mediaPlayer.start()
        } else {
            mediaPlayer.pause()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}