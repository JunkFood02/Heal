package io.github.junkfood.podcast.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Episode(
    @PrimaryKey(autoGenerate = true)
    val episodeID: Long,
    val author: String?,
    val title: String,
    val cover: String,
    val description: String?,
    val summary: String?,
    val pubDate: String,
    val length: Long,
    val categories: Set<String>,
    val duration: String?,
    val podcastID: Long
)

