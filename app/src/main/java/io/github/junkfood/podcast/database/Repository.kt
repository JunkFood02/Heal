package io.github.junkfood.podcast.database

import androidx.room.Room
import com.icosillion.podengine.models.Podcast
import io.github.junkfood.podcast.BaseApplication.Companion.context
import io.github.junkfood.podcast.database.model.Episode
import io.github.junkfood.podcast.database.model.Record
import io.github.junkfood.podcast.util.TextUtil
import kotlinx.coroutines.flow.Flow

object Repository {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database"
    ).build()
    private val episodeDao = db.episodeDao()
    private val podcastDao = db.podcastDao()
    private val recordDao = db.recordDao()

    fun getPodcastsWithEpisodes() = podcastDao.getPodcastsWithEpisodes()

    fun getEpisodeAndRecord() = recordDao.getEpisodeAndRecord()

    suspend fun deleteAllRecords() = recordDao.deleteAllRecords()

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

    suspend fun insertRecord(id: Long) {
        recordDao.deleteRecordById(id)
        recordDao.insertRecord(Record(episodeId = id))
    }

//    fun getEpisodeHistoryList(): Flow<List<Episode>> {
//        val recordList =  recordDao.getRecord()
//        val episodeIdList = ArrayList<Long>()
//        for (item in recordList) {
//            episodeIdList.add(item.id)
//        }
//        return episodeDao.getEpisodeById(episodeIdList)
//    }
}