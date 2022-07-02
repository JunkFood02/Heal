package io.github.junkfood.heal.ui.destination.feed

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentPaste
import androidx.compose.material.icons.rounded.RssFeed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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
                    item {
                        Column() {
                            Text(
                                "Resume listening",
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                                style = MaterialTheme.typography.titleLarge
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
                                            timeLeft = "12 mins left",
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


