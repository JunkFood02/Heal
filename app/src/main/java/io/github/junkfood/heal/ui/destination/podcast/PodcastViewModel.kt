@file:OptIn(ExperimentalMaterialApi::class)

package io.github.junkfood.heal.ui.destination.podcast

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.junkfood.heal.database.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PodcastViewModel constructor(var podcastId: Long) :
    ViewModel() {

    var podcastFlow = Repository.getPodcastFlowById(podcastId)
    var episodeFlow = Repository.getEpisodesByPodcastId(podcastId)

    data class ViewState(
        val podcastImageUrl: String = "",
    )

    private val mutableStateFlow = MutableStateFlow(ViewState())
    val stateFlow = mutableStateFlow.asStateFlow()

}

class PodcastViewModelFactory(private val podcastId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PodcastViewModel(podcastId) as T
    }
}