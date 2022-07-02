package io.github.junkfood.podcast.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.junkfood.podcast.database.model.EpisodeAndRecord
import io.github.junkfood.podcast.database.model.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Insert
    fun insertRecord(record: Record)

    @Delete
    fun deleterecord(record: Record)

    @Query("DELETE FROM record")
    fun deleteAllRecords()

    @Query("SELECT * FROM record WHERE episodeId = :id")
    fun getRecordByEpisodeId(id: Long): List<Record>

    @Query("SELECT * FROM record")
    fun getRecord(): List<Record>

    @Query("DELETE FROM record WHERE episodeId = :id")
    fun deleteRecordById(id: Long)

    @Transaction
    @Query("SELECT * FROM record")
    fun getEpisodeAndRecord(): Flow<List<EpisodeAndRecord>>

}