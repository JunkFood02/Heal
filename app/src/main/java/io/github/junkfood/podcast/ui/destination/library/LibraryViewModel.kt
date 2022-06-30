package io.github.junkfood.podcast.ui.destination.library

import androidx.lifecycle.ViewModel
import com.icosillion.podengine.models.Episode
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.junkfood.podcast.BaseApplication
import io.github.junkfood.podcast.database.Repository
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor() : ViewModel() {
    private val mutableStateFlow = MutableStateFlow(HistoryState())
    val stateFlow = mutableStateFlow.asStateFlow()
    val episodeAndRecordFlow = Repository.getEpisodeHistory()

    data class HistoryState(
        val url: String = "https://justpodmedia.com/rss/left-right.xml",
        val podcastTitle: String = "", val podcastCover: String = "",
        val author: String = "", val description: String = "",
        val episodeList: List<Episode> = ArrayList()
    )
}
