package io.github.junkfood.heal.ui.destination.feed

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentPaste
import androidx.compose.material.icons.rounded.RssFeed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.junkfood.heal.R
import io.github.junkfood.heal.ui.common.NavigationGraph
import io.github.junkfood.heal.ui.common.NavigationGraph.toId
import io.github.junkfood.heal.ui.component.FeedItem
import io.github.junkfood.heal.ui.destination.library.CardContent
import io.github.junkfood.heal.util.TextUtil


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedPage(navHostController: NavHostController, feedViewModel: FeedViewModel) {
    val viewState = feedViewModel.stateFlow.collectAsState()
    val libraryDataState = feedViewModel.episodeAndRecordFlow.collectAsState(ArrayList())

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
                LazyColumn {
                    if (libraryDataState.value.isNotEmpty())
                        item {
                            Column {
                                Text(
                                    stringResource(R.string.resume_listening),
                                    modifier = Modifier.padding(
                                        horizontal = 18.dp,
                                        vertical = 12.dp
                                    ),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                LazyRow(
                                    modifier = Modifier
                                ) {
                                    val episodeList = libraryDataState.value.reversed()
                                    for (item in episodeList) {
                                        item {
                                            CardContent(
                                                imageModel = item.episode.cover,
                                                title = item.episode.title,
                                                timeLeft = stringResource(R.string.minutes_left).format(
                                                    (item.episode.progress * 60000 / 6000).toInt()
                                                ),
                                                /*length = item.episode.duration,
                                                progress = item.episode.progress,*/
                                                onClick = {
                                                    navHostController.navigate(
                                                        NavigationGraph.EPISODE.toId(
                                                            item.episode.id
                                                        )
                                                    )
//                                                libraryViewModel.insertToHistory(item.episode.id)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    item {
                        if (feedItems.isNotEmpty())
                            Text(
                                stringResource(R.string.latest_episodes),
                                modifier = Modifier
                                    .padding(horizontal = 18.dp)
                                    .padding(top = 12.dp, bottom = 6.dp),
                                style = MaterialTheme.typography.titleMedium,
                            )
                    }
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
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.5f.dp)
                                    .clip(MaterialTheme.shapes.extraLarge),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )
                        }

                    }
                }
            }
        }
    }
}


