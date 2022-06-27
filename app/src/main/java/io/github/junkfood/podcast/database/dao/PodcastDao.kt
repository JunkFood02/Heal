package io.github.junkfood.podcast.database.dao

import androidx.room.*
import io.github.junkfood.podcast.database.model.Podcast
import io.github.junkfood.podcast.database.model.PodcastWithEpisodes
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {
    @Insert
    fun insert(vararg podcasts: Podcast)

    @Insert
    fun insert(podcast: Podcast): Long

    @Delete
    fun delete(podcast: Podcast)

    @Query("SELECT * FROM podcast")
    fun getAllPodcasts(): Flow<List<Podcast>>

    @Query("SELECT * FROM podcast WHERE title LIKE :search")
    fun searchPodcastByTitle(search: String): Flow<List<Podcast>>

    @Update
    fun update(vararg podcast: Podcast)

    @Transaction
    @Query("select * from podcast")
    fun getPodcastsWithEpisodes(): Flow<List<PodcastWithEpisodes>>
}