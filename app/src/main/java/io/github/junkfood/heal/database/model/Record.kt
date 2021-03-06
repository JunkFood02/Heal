package io.github.junkfood.heal.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Record(
    @PrimaryKey(autoGenerate = true)
    val recordId: Long = 0,
    val episodeId: Long
)