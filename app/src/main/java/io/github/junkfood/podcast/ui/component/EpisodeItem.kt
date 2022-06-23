package io.github.junkfood.podcast.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DownloadForOffline
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import io.github.junkfood.podcast.util.TextUtil
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun EpisodeItem(
    imageModel: Any,
    episodeTitle: String,
    episodeDescription: String,
    onClick: () -> Unit,
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
                .aspectRatio(5f, matchHeightConstraintsFirst = true)
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
                episodeDate?.let {
                    val dayCount = TimeUnit.DAYS.convert(
                        (Calendar.getInstance().time.time - episodeDate.time),
                        TimeUnit.MILLISECONDS
                    )
                    val text =
                        if (dayCount == 0L) "今天" else if (dayCount <= 60L) dayCount.toString() + "天前"
                        else TextUtil.parseDate(episodeDate)
                    SubtitleSmall(text)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedItem(
    imageModel: Any,
    title: String,
    episodeTitle: String,
    episodeDescription: String,
    onClick: () -> Unit,
    episodeDate: Date? = null
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp).padding(top = 15.dp)
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
                    SubtitleSmall(text)
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
            modifier = Modifier
                .padding(top = 9.dp)
                .padding(horizontal = 12.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding( start = 3.dp),
            horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically
        ) {

            Row(modifier = Modifier.weight(1f)) {
                IconButton(
                    onClick = { },
                    modifier = Modifier.padding()
                ) {
                    Icon(
                        Icons.Rounded.PlaylistAdd,
                        null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier.padding()
                ) {
                    Icon(
                        Icons.Outlined.DownloadForOffline,
                        null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier.padding()
                ) {
                    Icon(
                        Icons.Outlined.Share,
                        null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier.padding()
                ) {
                    Icon(
                        Icons.Rounded.MoreVert,
                        null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            FilledIconButton(
                onClick = { },
                modifier = Modifier
                    .padding(end = 18.dp)
                    .size(32.dp)
            ) { Icon(Icons.Rounded.PlayArrow, null) }

        }

    }
}