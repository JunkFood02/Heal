package io.github.junkfood.podcast.ui.destination.episode

import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.junkfood.podcast.database.Repository
import io.github.junkfood.podcast.database.model.Episode
import io.github.junkfood.podcast.util.TextUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class EpisodeViewModel constructor(private val episodeId: Long) : ViewModel() {

    data class EpisodeViewState(
        val podcastId: Long = 0, val podcastTitle: String = "",
        val title: String = "",
        val author: String = "",
        val imageUrl: String = "",
        val description: String = "",
        val duration: String = "",
        val pubDate: Date = Date()
    )

    private val mutableStateFlow = MutableStateFlow(EpisodeViewState())
    val stateFlow = mutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val episode = Repository.getEpisodeById(episodeId)
            val podcast = Repository.getPodcastById(episode.podcastID)

            mutableStateFlow.update {
                it.copy(
                    podcastId = episode.podcastID,
                    podcastTitle = podcast.title,
                    title = episode.title,
                    author = episode.author,
                    imageUrl = episode.cover,
                    description = episode.description,
                    duration = episode.duration,
                    pubDate = TextUtil.formatString(episode.pubDate) ?: Date()
                )
            }
        }
    }

}