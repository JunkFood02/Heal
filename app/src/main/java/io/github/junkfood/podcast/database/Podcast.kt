package io.github.junkfood.podcast.database

import androidx.room.Entity

@Entity
data class Podcast(val title: String, val list: List<Episode>)
