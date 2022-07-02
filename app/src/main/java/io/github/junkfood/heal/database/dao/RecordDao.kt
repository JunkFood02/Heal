package io.github.junkfood.heal.database.dao

import androidx.room.*
import io.github.junkfood.heal.database.model.EpisodeAndRecord
import io.github.junkfood.heal.database.model.Record
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