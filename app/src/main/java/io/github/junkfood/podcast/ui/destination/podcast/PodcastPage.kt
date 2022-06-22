package io.github.junkfood.podcast.ui.destination.podcast

import android.annotation.SuppressLint
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import io.github.junkfood.podcast.R
import io.github.junkfood.podcast.ui.common.RouteName
import io.github.junkfood.podcast.ui.component.*
import io.github.junkfood.podcast.ui.destination.FeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PodcastPage(feedViewModel: FeedViewModel, navHostController: NavHostController) {
    val viewState = feedViewModel.stateFlow.collectAsState()
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarScrollState()
    )
    Scaffold(modifier = Modifier
        .padding()
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        io.github.junkfood.podcast.ui.component.SmallTopAppBar(
            title = {},
            navigationIcon = {
                BackButton { navHostController.popBackStack() }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Rounded.MoreVert, stringResource(R.string.more))
                }
            }, scrollBehavior = scrollBehavior
        )
    }, content = {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            viewState.value.run {

                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 18.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth(0.4f)
                                    .clip(MaterialTheme.shapes.small)
                                    .aspectRatio(1f, matchHeightConstraintsFirst = true),
                                model = podcastCover,
                                contentDescription = null
                            )
                            Column(
                                Modifier
                                    .padding(horizontal = 18.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                HeadlineSmall(podcastTitle)
                                SubtitleMedium(author)
                            }
                        }
                        HtmlText(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                                .padding(bottom = 9.dp)
                        )
                    }
                    item {
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(horizontal = 12.dp)
                        ) {
//                            TextButton(onClick = { }) {
//                                Icon(
//                                    Icons.Rounded.Sort,
//                                    null,
//                                    modifier = Modifier
//                                        .padding(end = 8.dp)
//                                        .size(18.dp)
//                                )
//                                Text(text = "排序与筛选")
//                            }
                            IconButton(onClick = { }) {
                                Icon(Icons.Rounded.Sort, null)
                            }
                        }
                    }
                    for (i in episodeList.indices) {
                        val episode = episodeList[i]
                        item {
                            PodcastItem(
                                imageModel = episode.iTunesInfo.imageString ?: podcastCover,
                                title = podcastTitle,
                                episodeTitle = episode.title,
                                episodeDescription = episode.iTunesInfo.summary
                                    ?: episode.description,
                                onClick = {
                                    feedViewModel.jumpToEpisode(i)
                                    navHostController.navigate(RouteName.EPISODE)
                                }, inPodcastPage = true, episodeDate = episode.pubDate
                            )
                        }
                    }
                }
            }
        }
    })
}