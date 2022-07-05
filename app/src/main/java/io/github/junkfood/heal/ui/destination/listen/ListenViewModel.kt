package io.github.junkfood.heal.ui.destination.listen

import android.support.v4.media.session.MediaControllerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import io.github.junkfood.heal.MainActivity
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.viewModelScope
import io.github.junkfood.heal.database.Repository
import io.github.junkfood.heal.player.PodcastService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ListenViewModel : ViewModel() {
    val mediaController = MainActivity.Companion.mediaController

    val latestRecord = Repository.getLatestRecord().filterNotNull()
    val mutableStateFlow = MutableStateFlow(ViewState())
    val stateFlow = mutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            latestRecord.collect {
                val episode = it.episode
                val podcast = Repository.getPodcastById(it.episode.podcastID)
                mutableStateFlow.update { viewState ->
                    viewState.copy(
                        title = episode.title,
                        podcastTitle = podcast.title,
                        imageUrl = episode.cover, duration = episode.duration
                    )
                }
            }
        }
    }

    data class ViewState(
        val title: String = "",
        val podcastTitle: String = "",
        val imageUrl: String = "",
        val duration: Long = 0
    )

    fun getProgress() = PodcastService.progress

    fun setPlayBackSpeed(speed: Float) {
        mediaController.transportControls.setPlaybackSpeed(speed)
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