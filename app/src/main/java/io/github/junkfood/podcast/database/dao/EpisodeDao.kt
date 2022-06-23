package io.github.junkfood.podcast.database.dao

import androidx.room.*
import io.github.junkfood.podcast.database.model.Episode
import io.github.junkfood.podcast.database.model.Podcast
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {
    @Insert
    fun insert(vararg episodes: Episode)

    @Update
    fun update(vararg episodes: Episode)

    @Delete
    fun delete(episode: Episode)

    @Query("SELECT * FROM episode")
    fun getAllEpisodes(): Flow<List<Episode>>

    @Query(
        "SELECT * FROM podcast " +
                "JOIN episode ON episode.episodeID = podcast.podcastId"
    )
    fun loadPodcastAndEpisode(): Flow<Map<Podcast, List<Episode>>>

}