package io.github.junkfood.heal.ui.destination.subscription

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icosillion.podengine.models.Podcast
import io.github.junkfood.heal.BaseApplication
import io.github.junkfood.heal.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URL

class SubscriptionViewModel : ViewModel() {
    val subscriptionFlow = Repository.getPodcastsWithEpisodes()

    data class ViewState(val url: String = "https://anchor.fm/s/473e5930/podcast/rss")

    private val mutableStateFlow = MutableStateFlow(ViewState())
    val urlState = mutableStateFlow.asStateFlow()
    fun updateUrl(url: String) {
        mutableStateFlow.update { it.copy(url = url) }
    }

    fun fetchPodcast() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val podcast = Podcast(
                    URL(mutableStateFlow.value.url)
                )
                Repository.importRssData(podcast)
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    Toast.makeText(
                        BaseApplication.context,
                        "Error fetching podcast",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}