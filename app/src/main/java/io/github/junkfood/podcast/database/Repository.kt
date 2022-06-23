package io.github.junkfood.podcast.database

import androidx.room.Room
import io.github.junkfood.podcast.BaseApplication.Companion.context

object Repository {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database"
    ).build()
    val episodeDao = db.episodeDao()
    val podcastDao = db.podcastDao()
    val recordDao = db.recordDao()

}