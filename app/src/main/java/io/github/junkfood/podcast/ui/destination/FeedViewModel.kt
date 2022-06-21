package io.github.junkfood.podcast.ui.destination

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icosillion.podengine.models.Episode
import com.icosillion.podengine.models.Podcast
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.junkfood.podcast.BaseApplication.Companion.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class FeedViewModel @Inject constructor() : ViewModel() {
    fun fetchPodcast() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val podcast = Podcast(URL("https://anchor.fm/s/473e5930/podcast/rss"))
                mutableStateFlow.update {
                    it.copy(
                        podcastTitle = podcast.title,
                        episodeList = podcast.episodes,
                        currentEpisodeIndex = -1
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Error fetching podcast",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    fun jumpToEpisode(i: Int) {
        mutableStateFlow.update { it.copy(currentEpisodeIndex = i) }
    }

    data class FeedViewState(
        val podcastTitle: String = "",
        val episodeList: List<Episode> = ArrayList(), val currentEpisodeIndex: Int = 0
    )

    private val mutableStateFlow = MutableStateFlow(FeedViewState())

    val stateFlow = mutableStateFlow.asStateFlow()

}