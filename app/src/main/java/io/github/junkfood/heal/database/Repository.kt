package io.github.junkfood.heal.database

import androidx.room.Room
import com.icosillion.podengine.models.Podcast
import io.github.junkfood.heal.BaseApplication.Companion.applicationScope
import io.github.junkfood.heal.BaseApplication.Companion.context
import io.github.junkfood.heal.database.model.Episode
import io.github.junkfood.heal.database.model.Record
import io.github.junkfood.heal.util.TextUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Repository {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database"
    ).build()
    private val episodeDao = db.episodeDao()
    private val podcastDao = db.podcastDao()
    private val recordDao = db.recordDao()

    fun unsubscribePodcastById(Id: Long) {
        applicationScope.launch(Dispatchers.IO) {
            recordDao.getRecord().forEach {
                if (episodeDao.getEpisodeById(it.episodeId).podcastID == Id)
                    recordDao.deleterecord(it)
            }
            podcastDao.deletePodcastById(Id)
            episodeDao.deleteAllEpisodesByPodcastId(Id)
        }
    }

    fun getPodcastsWithEpisodes() = podcastDao.getPodcastsWithEpisodes()

    fun getPodcasts() = podcastDao.getAllPodcasts()

    fun getEpisodeAndRecord() = recordDao.getEpisodeAndRecordFlow()

    suspend fun deleteAllRecords() = recordDao.deleteAllRecords()

    suspend fun getEpisodeById(Id: Long) = episodeDao.getEpisodeById(Id)

    fun getEpisodesByPodcastId(podcastId: Long) = episodeDao.getEpisodesByPodcastId(podcastId)

    fun getLatestRecord() = recordDao.getLastRecord()

    suspend fun getPodcastById(Id: Long) = podcastDao.getPodcastById(Id)

    fun getPodcastFlowById(Id: Long) = podcastDao.getPodcastFlowById(Id)

    suspend fun importRssData(podcast: Podcast) {
        val podcastId = podcastDao.insert(
            io.github.junkfood.heal.database.model.Podcast(
                title = podcast.title,
                description = podcast.description,
                author = podcast.iTunesInfo.author.toString(),
                coverUrl = podcast.imageURL.toExternalForm(),
                url = podcast.link.toExternalForm(),
                feedUrl = podcast.feedURL.toExternalForm()
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
                    duration = episode.enclosure.length,
                    author = episode.author ?: "",
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