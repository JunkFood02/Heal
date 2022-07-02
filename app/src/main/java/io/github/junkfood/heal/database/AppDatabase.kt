package io.github.junkfood.heal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.junkfood.heal.database.dao.EpisodeDao
import io.github.junkfood.heal.database.dao.PodcastDao
import io.github.junkfood.heal.database.dao.RecordDao
import io.github.junkfood.heal.database.model.Episode
import io.github.junkfood.heal.database.model.Podcast
import io.github.junkfood.heal.database.model.Record

@Database(entities = [Episode::class, Podcast::class, Record::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao
    abstract fun podcastDao(): PodcastDao
    abstract fun recordDao(): RecordDao
}