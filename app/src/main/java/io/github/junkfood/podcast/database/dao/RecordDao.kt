package io.github.junkfood.podcast.database.dao

import androidx.room.*
import io.github.junkfood.podcast.database.model.Episode
import io.github.junkfood.podcast.database.model.EpisodeAndRecord
import io.github.junkfood.podcast.database.model.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Insert
    fun insert(vararg record: Record)

    @Update
    fun update(vararg record: Record)

    @Delete
    fun delete(vararg record: Record)

    @Query("SELECT * FROM record WHERE episodeOwnerId = :episodeId")
    fun searchRecordByEpisode(episodeId: Long): Flow<Record>

    @Query("SELECT * FROM record WHERE episodeOwnerId IN (:idList)")
    fun getEpisodeAndRecord(idList: List<Long>): Flow<List<EpisodeAndRecord>>
}