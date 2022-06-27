package io.github.junkfood.podcast.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Podcast(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val author: String,
    val description: String,
    val coverUrl: String
)
