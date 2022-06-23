package io.github.junkfood.podcast.database.dao

import androidx.room.*
import com.icosillion.podengine.models.Podcast
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {
    @Insert
    fun insert(vararg podcasts: Podcast)

    @Delete
    fun delete(podcast: Podcast)

    @Query("SELECT * FROM podcast WHERE title LIKE :search")
    fun searchPodcastByTitle(search: String): Flow<List<Podcast>>

    @Update
    fun update(vararg podcast: Podcast)
}