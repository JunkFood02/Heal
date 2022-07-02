package io.github.junkfood.heal.ui.destination.podcast

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.MoreVert
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
import io.github.junkfood.heal.R
import io.github.junkfood.heal.database.model.Podcast
import io.github.junkfood.heal.ui.common.NavigationUtil
import io.github.junkfood.heal.ui.common.NavigationUtil.toId
import io.github.junkfood.heal.ui.component.*
import io.github.junkfood.heal.util.TextUtil

private const val TAG = "PodcastPage"

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PodcastPage(
    navHostController: NavHostController,
    podcastId: Long
) {

    Log.d(TAG, "PodcastPage: $podcastId")
    val podcastViewModel = PodcastViewModel(podcastId)
    val viewState = podcastViewModel.stateFlow.collectAsState()
    val podcast = podcastViewModel.podcastFlow.collectAsState(Podcast()).value
    val episodes = podcastViewModel.episodeFlow.collectAsState(ArrayList()).value
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarScrollState()
    )
    Scaffold(modifier = Modifier
        .padding()
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            io.github.junkfood.heal.ui.component.SmallTopAppBar(
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
                                    model = podcast.coverUrl,
                                    contentDescription = null
                                )
                                Column(
                                    Modifier
                                        .padding(horizontal = 18.dp)
                                        .align(Alignment.CenterVertically)
                                ) {
                                    HeadlineSmall(podcast.title)
                                    SubtitleMedium(podcast.author)
                                }
                            }

                        }
/*                    item {

                        Row(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(start = 12.dp, end = 9.dp, bottom = 6.dp)
                        ) {
                            FilledTonalIconToggleButton(checked = true, onCheckedChange = {}, modifier = Modifier.padding(end = 6.dp)) {
                                Icon(Icons.Rounded.LibraryAddCheck, null)
                            }
                            IconButton(onClick = {}, modifier = Modifier.padding(end = 6.dp)) {
                                Icon(
                                    Icons.Rounded.RssFeed,
                                    null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                            IconButton(onClick = {}, modifier = Modifier.padding(end = 6.dp)) {
                                Icon(
                                    Icons.Rounded.Share,
                                    null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }

                    }*/

                        item {
                            HtmlText(
                                text = podcast.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(horizontal = 18.dp)
                                    .padding(bottom = 12.dp)
                            )
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .padding(start = 12.dp, end = 3.dp)
                            ) {
                                LabelLarge(
                                    text = stringResource(R.string.episodes).format(episodes.size),
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                ) {
                                    Icon(
                                        Icons.Rounded.FilterList,
                                        null
                                    )
                                }
                            }
                            Divider(
                                modifier = Modifier.fillParentMaxWidth(),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )
                        }
                        for (i in episodes.indices) {
                            val episode = episodes[i]
                            item {
                                EpisodeItem(
                                    imageModel = episode.cover,
                                    episodeTitle = episode.title,
                                    episodeDescription = episode.description,
                                    onClick = {
                                        navHostController.navigate(
                                            NavigationUtil.EPISODE.toId(
                                                episode.id
                                            )
                                        )
                                    }, episodeDate = TextUtil.formatString(episode.pubDate)
                                )
                                Divider(
                                    modifier = Modifier
                                        .fillParentMaxWidth()
                                        .padding(horizontal = 3.dp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                                )

                            }
                        }
                    }
                }
            }
        })
    FilterDrawer()

}