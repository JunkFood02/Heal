package io.github.junkfood.heal.ui.destination.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icosillion.podengine.models.Episode
import io.github.junkfood.heal.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {
    private val mutableStateFlow = MutableStateFlow(ViewState())
    val stateFlow = mutableStateFlow.asStateFlow()
    val podcastFlow = Repository.getPodcasts().filterNotNull()

    data class ViewState(
        val url: String = "https://justpodmedia.com/rss/left-right.xml",
        val podcastTitle: String = "", val podcastCover: String = "",
        val author: String = "", val description: String = "",
        val episodeList: List<Episode> = ArrayList()
    )
    fun updateUrl(url: String) {
        mutableStateFlow.update { it.copy(url = url) }
    }
    fun insertToHistory(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.insertRecord(id)
        }
    }

    fun deleteAllRecords() {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.deleteAllRecords()
        }
    }
}
