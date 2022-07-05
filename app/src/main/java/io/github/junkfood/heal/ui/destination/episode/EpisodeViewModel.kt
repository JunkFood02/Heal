package io.github.junkfood.heal.ui.destination.episode

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.junkfood.heal.database.Repository
import io.github.junkfood.heal.util.TextUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class EpisodeViewModel constructor(private val episodeId: Long) : ViewModel() {

    data class EpisodeViewState(
        val podcastId: Long = 0, val podcastTitle: String = "",
        val title: String = "",
        val author: String = "",
        val podcastImageUrl: String = "",
        val imageUrl: String = "",
        val description: String = "",
        val duration: Long = 0,
        val pubDate: Date = Date()
    )

    private val mutableStateFlow = MutableStateFlow(EpisodeViewState())
    val stateFlow = mutableStateFlow.asStateFlow()

    private fun initEpisodeContent() {
        viewModelScope.launch(Dispatchers.IO) {
            val episode = Repository.getEpisodeById(episodeId)
            val podcast = Repository.getPodcastById(episode.podcastID)
            mutableStateFlow.update {
                it.copy(
                    podcastId = episode.podcastID,
                    podcastTitle = podcast.title,
                    podcastImageUrl = podcast.coverUrl,
                    title = episode.title,
                    author = podcast.author,
                    imageUrl = episode.cover,
                    description = episode.description,
                    duration = (episode.duration / 60000),
                    pubDate = TextUtil.formatString(episode.pubDate) ?: Date()
                )
            }
        }
    }

    init {
        initEpisodeContent()
    }

}

class EpisodeViewModelProvider(private val episodeId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EpisodeViewModel(episodeId) as T
    }
}