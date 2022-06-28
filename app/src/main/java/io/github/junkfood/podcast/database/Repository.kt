package io.github.junkfood.podcast.database

import androidx.room.Room
import com.icosillion.podengine.models.Podcast
import io.github.junkfood.podcast.BaseApplication.Companion.context
import io.github.junkfood.podcast.database.model.Episode
import io.github.junkfood.podcast.util.PreferenceUtil

object Repository {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database"
    ).build()
    private val episodeDao = db.episodeDao()
    private val podcastDao = db.podcastDao()
    private val recordDao = db.recordDao()

    fun getPodcastsWithEpisodes() = podcastDao.getPodcastsWithEpisodes()

    fun getEpisodeAndRecord() = recordDao.getEpisodeAndRecord(PreferenceUtil.getHistory())

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
                    pubDate = episode.pubDate.toString(),
                    duration = episode.iTunesInfo.duration, author = episode.author
                )
            )
        }
    }
}