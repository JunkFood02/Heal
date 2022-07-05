package io.github.junkfood.heal.ui.destination.listen

import android.util.Log
import androidx.lifecycle.ViewModel

import io.github.junkfood.heal.MainActivity
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import io.github.junkfood.heal.database.Repository
import io.github.junkfood.heal.database.model.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class ListenViewModel : ViewModel() {
    val exoPlayer = MainActivity.exoPlayer
    private val TAG = "ListenViewModel"
    val latestRecord = Repository.getLatestRecord().filterNotNull()
    val mutableStateFlow = MutableStateFlow(ViewState())
    val stateFlow = mutableStateFlow.asStateFlow()
    lateinit var episode: Episode

    init {

        viewModelScope.launch (Dispatchers.Main) {
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
                val mediaItem = MediaItem.fromUri(episode.audioUrl)
                exoPlayer.setMediaItem(mediaItem)
            }
        }


    }

    data class ViewState(
        val title: String = "",
        val podcastTitle: String = "",
        val imageUrl: String = "",
        val duration: Long = 0
    )

    fun getProgress(): Float {
        return (exoPlayer.currentPosition / exoPlayer.duration).toFloat()

    }

    fun setPlayBackSpeed(speed: Float) {
        exoPlayer.setPlaybackSpeed(speed)
    }

    fun seekTo(pos: Long) {

    }

    fun skipToNext() {
    }

    fun skipToPrevious() {

    }

    fun playOrPause() {
        Log.d(TAG, "playOrPause")
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }


}