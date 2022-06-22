package io.github.junkfood.podcast.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import io.github.junkfood.podcast.util.TextUtil
import java.time.Duration
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun PodcastItem(
    imageModel: Any,
    title: String,
    episodeTitle: String,
    episodeDescription: String,
    onClick: () -> Unit,
    inPodcastPage: Boolean = false,
    episodeDate: Date? = null
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .aspectRatio(4.5f, matchHeightConstraintsFirst = true)
                .height(IntrinsicSize.Min)
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .aspectRatio(1f, matchHeightConstraintsFirst = true),
                model = imageModel,
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 9.dp)
                    .fillMaxHeight(), verticalArrangement = Arrangement.Top
            ) {
                Text(
                    episodeTitle,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (!inPodcastPage)
                    Text(
                        title,
                        modifier = Modifier.padding(),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                episodeDate?.let {
                    val dayCount = TimeUnit.DAYS.convert(
                        (Calendar.getInstance().time.time - episodeDate.time),
                        TimeUnit.MILLISECONDS
                    )
                    val text =
                        if (dayCount == 0L) "今天" else if (dayCount <= 60L) dayCount.toString() + "天前"
                        else TextUtil.parseDate(episodeDate)
                    LabelMedium(text)
                }
            }

        }
        Text(
            text = HtmlCompat.fromHtml(episodeDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)
                .toString(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 9.dp),
        )

    }
}