
import androidx.room.Embedded
import androidx.room.Relation
import io.github.junkfood.podcast.database.model.Episode
import io.github.junkfood.podcast.database.model.Record

data class EpisodeAndRecord(
    @Embedded val episode: Episode,
    @Relation(
        parentColumn = "episodeID",
        entityColumn = "episodeOwnerId"
    )
    val record: Record
)
