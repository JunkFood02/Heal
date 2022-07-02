package io.github.junkfood.heal.ui.destination.podcast

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import io.github.junkfood.heal.ui.common.NavigationGraph
import io.github.junkfood.heal.ui.common.NavigationGraph.toId
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
    var reverseList by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier
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
        }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn {
                item {
                    viewState.value.run {
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
                        HtmlText(
                            text = podcast.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                                .padding(bottom = 12.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, end = 3.dp)
                        ) {
                            LabelLarge(
                                text = stringResource(R.string.episodes).format(episodes.size),
                                modifier = Modifier.align(Alignment.CenterStart),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            IconButton(
                                onClick = {
                                    reverseList = !reverseList
                                },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                Icon(
                                    Icons.Rounded.FilterList,
                                    null
                                )
                            }
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
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

                items(if (reverseList) episodes.reversed() else episodes) { episode ->
                    EpisodeItem(
                        imageModel = episode.cover,
                        episodeTitle = episode.title,
                        episodeDescription = episode.description,
                        onClick = {
                            navHostController.navigate(
                                NavigationGraph.EPISODE.toId(
                                    episode.id
                                )
                            )
                        }, episodeDate = TextUtil.formatString(episode.pubDate)
                    )
                    Divider(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(horizontal = 12.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )

                }

            }

        }
    }
//    FilterDrawer()
/*    if (showFilterDialog)
        AlertDialog(onDismissRequest = {}, dismissButton = {
            TextButton(onClick = {
                showFilterDialog = false
            }) {
                Text("Cancel")
            }
        }, confirmButton = {
            TextButton(onClick = {}) {
                Text("Confirm")
            }
        }, text = {
            Column() {
                for (i in 1..5)
                    SingleChoiceItem(text = "Select Item $i", selected = i == 1) {}
            }
        })*/
}