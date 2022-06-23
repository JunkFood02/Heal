package io.github.junkfood.podcast.database.model

import androidx.room.Embedded
import androidx.room.Relation
import io.github.junkfood.podcast.database.model.Episode
import io.github.junkfood.podcast.database.model.Podcast

data class PodcastWithEpisodes(
    @Embedded val podcast: Podcast,
    @Relation(
        parentColumn = "podCastId",
        entityColumn = "podCastID"
    )
    val Episodes: List<Episode>
)
