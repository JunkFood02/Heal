package io.github.junkfood.heal.database.dao

import androidx.room.*
import io.github.junkfood.heal.database.model.Episode
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

    @Query("SELECT * FROM episode WHERE id IN (:idList)")
    fun getEpisodeById(idList: List<Long>): Flow<List<Episode>>

    @Query("select * from episode where id = :episodeId")
    suspend fun getEpisodeById(episodeId: Long): Episode

    @Query("select * from episode where podcastID=:podcastId")
    fun getEpisodesByPodcastId(podcastId: Long): Flow<List<Episode>>
}