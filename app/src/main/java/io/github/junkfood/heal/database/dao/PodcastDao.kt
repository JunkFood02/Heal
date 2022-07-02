package io.github.junkfood.heal.database.dao

import androidx.room.*
import io.github.junkfood.heal.database.model.Podcast
import io.github.junkfood.heal.database.model.PodcastWithEpisodes
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {
    @Insert
    fun insert(vararg podcasts: Podcast)

    @Insert
    fun insert(podcast: Podcast): Long

    @Delete
    fun delete(podcast: Podcast)

    @Query("select * from podcast where id=:Id ")
    suspend fun getPodcastById(Id: Long): Podcast

    @Query("select * from podcast where id=:Id ")
    fun getPodcastFlowById(Id: Long): Flow<Podcast>

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