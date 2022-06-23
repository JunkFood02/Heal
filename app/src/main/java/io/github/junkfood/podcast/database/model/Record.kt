package io.github.junkfood.podcast.database.model

import androidx.room.PrimaryKey

data class Record(
    @PrimaryKey(autoGenerate = true)
    val recordId: Long,
    val isFinishedListening: Boolean = false,
    val process : Float = 0F,
    val episodeOwnerId: Long
)
