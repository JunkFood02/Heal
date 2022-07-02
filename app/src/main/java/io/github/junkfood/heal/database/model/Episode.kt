package io.github.junkfood.heal.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Episode(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val author: String,//
    val title: String,//
    val cover: String,
    val description: String,//
    val pubDate: String,
    val audioUrl: String,//
    //val categories: List<String>,
    val duration: String,
    val podcastID: Long,
    val progress: Float = 0F,
    val recordId: Long = 0,
    //val uri: String
)

