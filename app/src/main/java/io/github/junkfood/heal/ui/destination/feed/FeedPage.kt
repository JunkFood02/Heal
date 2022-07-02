package io.github.junkfood.heal.ui.destination.feed

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentPaste
import androidx.compose.material.icons.rounded.RssFeed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.icosillion.podengine.models.Episode
import io.github.junkfood.heal.ui.common.NavigationGraph
import io.github.junkfood.heal.ui.common.NavigationGraph.toId
import io.github.junkfood.heal.ui.component.FeedItem
import io.github.junkfood.heal.util.TextUtil


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedPage(navHostController: NavHostController, feedViewModel: FeedViewModel) {

    val viewState = feedViewModel.stateFlow.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier
        .padding()
        .fillMaxSize(), floatingActionButton = {
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .padding(bottom = 36.dp, end = 24.dp)
        ) { Icon(Icons.Rounded.RssFeed, null) }
    }) {

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("RSS Feed") },
                text = {
                    TextField(
                        value = viewState.value.url,
                        onValueChange = { feedViewModel.updateUrl(it) }, trailingIcon = {
                            IconButton(
                                onClick = {
                                    clipboardManager.getText()?.text?.let {
                                        feedViewModel.updateUrl(it)
                                    }
                                }) { Icon(Icons.Rounded.ContentPaste, null) }
                        })
                },
                confirmButton = {
                    TextButton(onClick = {
                        feedViewModel.fetchPodcast()
                        showDialog = false
                    }) {
                        Text("Fetch podcast")
                    }
                })
        }
        Column(
            Modifier
                .statusBarsPadding()
                .fillMaxSize()
        ) {

            viewState.value.run {
                val feedItemList = ArrayList<io.github.junkfood.heal.database.model.Episode>()
                LazyColumn {
                    for (item in feedItems) {
                        item {
                            FeedItem(
                                imageModel = item.imageUrl,
                                title = item.podcastTitle,
                                episodeTitle = item.title,
                                episodeDescription = item.description,
                                onClick = {
                                    feedViewModel.insertToHistory(item.episodeId)
//                                        feedViewModel.jumpToEpisode(i)
//                                        navHostController.navigate(RouteName.EPISODE)
                                    navHostController.navigate(
                                        NavigationGraph.EPISODE.toId(
                                            item.episodeId
                                        )
                                    )
                                },
                                episodeDate = TextUtil.formatString(item.pubDate),
                                onAddButtonClick = {},
                                onDownloadButtonClick = {},
                                onMoreButtonClick = {},
                                onPlayButtonClick = {
                                    feedViewModel.insertToHistory(item.episodeId)
                                }
                            )
                        }


                    }
                }
            }
        }
    }
}


@Composable
fun CardContent(title: String, episode: Episode) {

    AsyncImage(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .aspectRatio(1f, matchHeightConstraintsFirst = true),
        model = episode.iTunesInfo.image,
        contentDescription = null
    )
    Text(
        episode.title,
        modifier = Modifier.padding(
            top = 9.dp,
            bottom = 3.dp,
            start = 12.dp,
            end = 12.dp
        ),
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
    Text(
        title,
        modifier = Modifier.padding(bottom = 15.dp, start = 12.dp, end = 12.dp),
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

}