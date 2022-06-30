package io.github.junkfood.podcast.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity
data class Podcast(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val author: String,
    val description: String,
    @ColumnInfo(defaultValue = "")
    val url: String = "",
    val coverUrl: String
)
