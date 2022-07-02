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
import io.github.junkfood.podcast.util.TextUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class FeedViewModel @Inject constructor() : ViewModel() {
    //    init { fetchPodcast() }
    private val mutableStateFlow = MutableStateFlow(FeedViewState())
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

    init {
        viewModelScope.launch {
            Repository.getPodcastsWithEpisodes().collect {
                val feedItems: MutableList<FeedItem> = ArrayList()
                it.forEach { item ->
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
                mutableStateFlow.update { stateFlow -> stateFlow.copy(feedItems = feedItems.reversed()) }
            }
        }
    }

    fun updateUrl(url: String) {
        mutableStateFlow.update { it.copy(url = url) }
    }

    data class FeedViewState(
        val url: String = "https://justpodmedia.com/rss/left-right.xml",
        val feedItems: List<FeedItem> = ArrayList()
    )

    data class FeedItem(
        val episodeId: Long = 0,
        val imageUrl: String = "",
        val podcastTitle: String = "",
        val pubDate: String = "",
        val title: String = "",
        val description: String = "",
    )

    fun insertToHistory(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.insertRecord(id)
        }
    }
}