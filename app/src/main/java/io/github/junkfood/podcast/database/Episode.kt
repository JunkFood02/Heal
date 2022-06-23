package io.github.junkfood.podcast.database

import androidx.room.Entity

@Entity
data class Episode (val author: String?, val title: String, val cover: String,
    val description: String?, val summary: String?, val pubDate: String)
