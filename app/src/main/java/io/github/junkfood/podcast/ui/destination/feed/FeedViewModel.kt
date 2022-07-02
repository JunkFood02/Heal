package io.github.junkfood.podcast.ui.destination.feed

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icosillion.podengine.models.Episode
import com.icosillion.podengine.models.Podcast
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.junkfood.podcast.BaseApplication.Companion.context
import io.github.junkfood.podcast.database.Repository
import io.github.junkfood.podcast.util.PreferenceUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor() : ViewModel() {
    //    init { fetchPodcast() }
    private val mutableStateFlow = MutableStateFlow(FeedViewState())
    val podcastWithEpisodesFlow = Repository.getPodcastsWithEpisodes()
    val stateFlow = mutableStateFlow.asStateFlow()
    private val TAG = "FeedViewModel"
    fun fetchPodcast() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val podcast = Podcast(
                    URL(mutableStateFlow.value.url)
                )
                Log.d(TAG, "fetchPodcast: ")
                Repository.importRssData(podcast)
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

    fun updateUrl(url: String) {
        mutableStateFlow.update { it.copy(url = url) }
    }

    fun jumpToEpisode(i: Int) {
        mutableStateFlow.update { it.copy(currentEpisodeIndex = i) }
    }

    fun toast(item: String) {
        Toast.makeText(
            context,
            item + mutableStateFlow.value.currentEpisodeIndex,
            Toast.LENGTH_SHORT
        ).show()
    }

    data class FeedViewState(
        val url: String = "https://storyfm.cn/feed/episodes",
        val podcastTitle: String = "", val podcastCover: String = "",
        val author: String = "", val description: String = "",
        val episodeList: List<Episode> = ArrayList(), val currentEpisodeIndex: Int = 0
    )

    fun insertToHistory(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.insertRecord(id)
        }
    }

    fun getLastEpisode() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }
}