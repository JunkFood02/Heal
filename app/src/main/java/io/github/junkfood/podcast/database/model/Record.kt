package io.github.junkfood.podcast.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Record(
    @PrimaryKey(autoGenerate = true)
    val recordId: Long,
    val isFinishedListening: Boolean = false,
    val progress : Float = 0F,
    val episodeOwnerId: Long
)
