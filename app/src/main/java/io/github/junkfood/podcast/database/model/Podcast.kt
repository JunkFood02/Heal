package io.github.junkfood.podcast.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Podcast(
    @PrimaryKey(autoGenerate = true)
    val podCastId: Long,
    val title: String,
    val author: String)
