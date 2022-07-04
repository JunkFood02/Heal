package io.github.junkfood.heal.ui.destination.feed

import android.annotation.SuppressLint
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import io.github.junkfood.heal.R
import io.github.junkfood.heal.ui.common.NavigationGraph
import io.github.junkfood.heal.ui.common.NavigationGraph.toId
import io.github.junkfood.heal.ui.component.FeedItem
import io.github.junkfood.heal.ui.component.HistoryCard
import io.github.junkfood.heal.util.TextUtil


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedPage(navHostController: NavHostController, feedViewModel: FeedViewModel) {
    val viewState = feedViewModel.stateFlow.collectAsState()
    val libraryDataState = feedViewModel.episodeAndRecordFlow.collectAsState(ArrayList())
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarScrollState()
    )
    Scaffold(
        modifier = Modifier
            .padding()
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            io.github.junkfood.heal.ui.component.SmallTopAppBar(title = {}, actions = {
                IconButton(onClick = { navHostController.navigate(NavigationGraph.SETTINGS) }) {
                    Icon(Icons.Outlined.Settings, null)
                }
            }, scrollBehavior = scrollBehavior)
        }
    ) {
        viewState.value.run {
            LazyColumn(modifier = Modifier.padding(it)) {
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
                            modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item { Spacer(modifier = Modifier.size(0.dp)) }
                            val episodeList = libraryDataState.value.reversed()
                            for (item in episodeList) {
                                item {
                                    HistoryCard(
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
                            item { Spacer(modifier = Modifier.size(0.dp)) }
                        }

                    }
                }
                item {
/*                    Text(
                        stringResource(R.string.subscriptions),
                        modifier = Modifier
                            .padding(horizontal = 18.dp)
                            .padding(top = 12.dp, bottom = 6.dp),
                        style = MaterialTheme.typography.titleMedium,
//                            fontWeight = FontWeight.SemiBold
                    )*/

                }
/*                item {
                    if (feedItems.isNotEmpty())
                        Text(
                            stringResource(R.string.latest_episodes),
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                                .padding(top = 12.dp, bottom = 6.dp),
                            style = MaterialTheme.typography.titleMedium,
//                            fontWeight = FontWeight.SemiBold
                        )
                }*/
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


