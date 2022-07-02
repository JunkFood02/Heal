package io.github.junkfood.heal.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class EpisodeAndRecord (
    @Embedded val record: Record,
    @Relation(
        parentColumn = "episodeId",
        entityColumn = "id"
    )
    val episode: Episode
)