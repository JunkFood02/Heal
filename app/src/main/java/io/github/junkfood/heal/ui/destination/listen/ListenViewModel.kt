package io.github.junkfood.heal.ui.destination.listen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import io.github.junkfood.heal.MainActivity
import io.github.junkfood.heal.database.Repository
import io.github.junkfood.heal.database.model.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListenViewModel : ViewModel() {
    val exoPlayer = MainActivity.exoPlayer
    private val TAG = "ListenViewModel"
    val mutableStateFlow = MutableStateFlow(ViewState())
    val stateFlow = mutableStateFlow.asStateFlow()
    lateinit var episode: Episode

    init {
        viewModelScope.launch(Dispatchers.Main) {
            Repository.getLatestRecord().filterNotNull().collect {
                episode = it.episode
                val podcast = Repository.getPodcastById(it.episode.podcastID)
                mutableStateFlow.update { viewState ->
                    viewState.copy(
                        episodeTitle = episode.title,
                        podcastTitle = podcast.title,
                        imageUrl = episode.cover,
                        podcastId = podcast.id,
                        episodeId = episode.id
                    )
                }
                Log.d(TAG, episode.audioUrl)
                val mediaItem =
                    MediaItem.Builder().setMediaId(episode.id.toString()).setUri(episode.audioUrl)
                        .build()
                if (exoPlayer.mediaItemCount == 0 || exoPlayer.currentMediaItem!!.mediaId != episode.id.toString()) {
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            while (true) {
                delay(200)
                mutableStateFlow.update {
                    it.copy(
                        isPlaying = exoPlayer.playbackState == ExoPlayer.STATE_READY && exoPlayer.isPlaying,
                        progress = getProgress(),
                        duration = exoPlayer.duration
                    )
                }
                withContext(Dispatchers.IO) {
                    Repository.updateEpisode(
                        episode.copy(
                            progress = stateFlow.value.progress,
                            duration = stateFlow.value.duration
                        )
                    )
                }
            }
        }


    }

    data class ViewState(
        val episodeId: Long = 0,
        val episodeTitle: String = "",
        val podcastTitle: String = "",
        val podcastId: Long = 0,
        val imageUrl: String = "",
        val duration: Long = 0,
        val progress: Float = 0F,
        val isPlaying: Boolean = false
    )


    private suspend fun getProgress(): Float {
        return withContext(Dispatchers.Main) { exoPlayer.currentPosition.toFloat() / exoPlayer.duration.toFloat() }
    }

    fun forward() {
        exoPlayer.seekTo(exoPlayer.currentPosition + 30000L)
    }

    fun replay() {
        exoPlayer.seekTo(exoPlayer.currentPosition - 10000L)
    }

    fun setPlayBackSpeed(speed: Float) {
        exoPlayer.setPlaybackSpeed(speed)
    }

    fun seekTo(progress: Float) {
        exoPlayer.seekTo((exoPlayer.duration * progress).toLong())
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
