@file:OptIn(ExperimentalMaterialApi::class)

package io.github.junkfood.heal.ui.destination.podcast

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.ViewModel
import io.github.junkfood.heal.database.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PodcastViewModel constructor(podcastId: Long) : ViewModel() {
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