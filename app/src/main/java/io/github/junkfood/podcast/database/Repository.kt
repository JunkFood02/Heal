package io.github.junkfood.podcast.database

import androidx.room.Room
import com.icosillion.podengine.models.Podcast
import io.github.junkfood.podcast.BaseApplication.Companion.context
import io.github.junkfood.podcast.database.model.Episode
import io.github.junkfood.podcast.util.PreferenceUtil
import io.github.junkfood.podcast.util.TextUtil

object Repository {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database"
    ).build()
    private val episodeDao = db.episodeDao()
    private val podcastDao = db.podcastDao()

    fun getPodcastsWithEpisodes() = podcastDao.getPodcastsWithEpisodes()

    suspend fun getEpisodeHistory() = episodeDao.getEpisodeById(PreferenceUtil.getHistory())

    suspend fun getEpisodeById(Id: Long) = episodeDao.getEpisodeById(Id)

    suspend fun getPodcastById(Id: Long) = podcastDao.getPodcastById(Id)

    suspend fun importRssData(podcast: Podcast) {
        val podcastId = podcastDao.insert(
            io.github.junkfood.podcast.database.model.Podcast(
                title = podcast.title,
                description = podcast.description,
                author = podcast.iTunesInfo.author.toString(),
                coverUrl = podcast.imageURL.toExternalForm()
            )
        )
        for (episode in podcast.episodes) {
            episodeDao.insert(
                Episode(
                    podcastID = podcastId,
                    title = episode.title,
                    description = episode.iTunesInfo.summary ?: episode.description,
                    cover = episode.iTunesInfo.imageString
                        ?: podcast.imageURL.toExternalForm(),
                    pubDate = TextUtil.formatDate(episode.pubDate),
                    duration = episode.iTunesInfo.duration,
                    author = episode.author,
                    audioUrl = episode.enclosure.url.toExternalForm()
                )
            )
        }
    }
}