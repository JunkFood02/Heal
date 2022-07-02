@file:OptIn(ExperimentalMaterialApi::class)

package io.github.junkfood.podcast.ui.destination.podcast

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.junkfood.podcast.database.Repository
import io.github.junkfood.podcast.database.model.Episode
import io.github.junkfood.podcast.database.model.Podcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PodcastViewModel constructor(private val podcastId: Long) : ViewModel() {
    val podcastFlow=Repository.getPodcastFlowById(podcastId)
    val episodeFlow=Repository.getEpisodesByPodcastId(podcastId)
    data class ViewState(
        val drawerState: ModalBottomSheetState = ModalBottomSheetState(
            ModalBottomSheetValue.Hidden
        ),
    )

    private val mutableStateFlow = MutableStateFlow(ViewState())
    val stateFlow = mutableStateFlow.asStateFlow()
}