package io.github.junkfood.heal.ui.destination.listen

import android.support.v4.media.session.MediaControllerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import io.github.junkfood.heal.MainActivity
import android.support.v4.media.session.PlaybackStateCompat
import io.github.junkfood.heal.player.PodcastService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ListenViewModel: ViewModel() {
    val mediaController = MainActivity.Companion.mediaController

    val state: Flow<PlaybackStateCompat> = flow {
        while (true) {
            val state = mediaController.playbackState
        }

    }

    fun getProgress() {
        PodcastService.progress
    }

    fun seekTo(pos: Long) {
        mediaController.transportControls.seekTo(pos)
    }

    fun skipToNext() {
        mediaController.transportControls.skipToNext()
    }

    fun skipToPrevious() {
        mediaController.transportControls.skipToPrevious()
    }

    fun playOrPause() {
       if (mediaController.playbackState.state == PlaybackStateCompat.STATE_PLAYING) {
           mediaController.transportControls.pause()
       } else {
           mediaController.transportControls.play()
       }
    }

}