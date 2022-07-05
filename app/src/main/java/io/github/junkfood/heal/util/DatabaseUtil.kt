package io.github.junkfood.heal.util

import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.icosillion.podengine.models.Podcast
import io.github.junkfood.heal.BaseApplication
import io.github.junkfood.heal.BaseApplication.Companion.applicationScope
import io.github.junkfood.heal.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

object DatabaseUtil {

    fun fetchPodcast(url: String) {
        applicationScope.launch(Dispatchers.IO) {
            try {
                val podcast = Podcast(URL(url))
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