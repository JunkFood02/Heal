package io.github.junkfood.heal.ui.destination.feed

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icosillion.podengine.models.Podcast
import io.github.junkfood.heal.BaseApplication.Companion.context
import io.github.junkfood.heal.database.Repository
import io.github.junkfood.heal.util.TextUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URL

class FeedViewModel : ViewModel() {
    //    init { fetchPodcast() }
    private val mutableStateFlow = MutableStateFlow(FeedViewState())
    val stateFlow = mutableStateFlow.asStateFlow()
    val episodeAndRecodFlow = Repository.getEpisodeAndRecord().filterNotNull()

    private val TAG = "FeedViewModel"


    init {
        viewModelScope.launch {
            Repository.getPodcastsWithEpisodes().collect {
                val feedItems: MutableList<FeedItem> = ArrayList()
                val podcastItems: MutableList<PodcastItem> = ArrayList()
                it.forEach { item ->
                    podcastItems.add(
                        PodcastItem(
                            podcastId = item.podcast.id,
                            title = item.podcast.title,
                            imageUrl = item.podcast.coverUrl
                        )
                    )
                    item.episodes.forEach { episode ->
                        feedItems.add(
                            FeedItem(
                                episodeId = episode.id,
                                imageUrl = episode.cover,
                                podcastTitle = item.podcast.title,
                                pubDate = episode.pubDate,
                                title = episode.title,
                                description = episode.description
                            )
                        )
                    }
                }
                feedItems.sortWith { o1: FeedItem, o2: FeedItem ->
                    TextUtil.compareDate(
                        o1.pubDate,
                        o2.pubDate
                    )
                }
                mutableStateFlow.update { stateFlow ->
                    stateFlow.copy(
                        feedItems = feedItems.reversed(),
                        podcastItems = podcastItems
                    )
                }
            }
        }
    }

    fun updateUrl(url: String) {
        mutableStateFlow.update { it.copy(url = url) }
    }

    data class FeedViewState(
        val url: String = "https://anchor.fm/s/473e5930/podcast/rss",
        val feedItems: List<FeedItem> = ArrayList(),
        val podcastItems: List<PodcastItem> = ArrayList()
    )

    data class FeedItem(
        val episodeId: Long = 0,
        val imageUrl: String = "",
        val podcastTitle: String = "",
        val pubDate: String = "",
        val title: String = "",
        val description: String = "",
    )

    data class PodcastItem(
        val podcastId: Long = 0,
        val title: String = "", val imageUrl: String = ""
    )

    fun insertToHistory(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.insertRecord(id)
        }
    }
}