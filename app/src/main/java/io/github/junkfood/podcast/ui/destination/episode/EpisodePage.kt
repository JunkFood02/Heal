package io.github.junkfood.podcast.ui.destination.episode

import android.annotation.SuppressLint
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import io.github.junkfood.podcast.R
import io.github.junkfood.podcast.ui.common.RouteName
import io.github.junkfood.podcast.ui.component.*
import io.github.junkfood.podcast.ui.destination.feed.FeedViewModel
import io.github.junkfood.podcast.util.TextUtil

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun EpisodePage(
    navHostController: NavHostController, feedViewModel: FeedViewModel
) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarScrollState()
    )
    val viewState = feedViewModel.stateFlow.collectAsState()
    viewState.value.run {
        val episode = episodeList[currentEpisodeIndex]
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            backgroundColor = MaterialTheme.colorScheme.surface,
            topBar = {
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
            },
            content = {
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .clickable { navHostController.navigate(RouteName.PODCAST) }
                                .padding(vertical = 12.dp, horizontal = 18.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth(0.25f)
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
                                TitleMedium(podcastTitle)
                                SubtitleMedium(author)
                            }

                        }

                    }
                    item {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                                .padding(top = 6.dp)
                        ) {
                            HeadlineSmall(episode.title)
                            Row(modifier = Modifier.padding(top = 3.dp)) {
                                LabelMedium(
                                    text = stringResource(R.string.publish_date).format(
                                        TextUtil.parseDate(
                                            episode.pubDate
                                        )
                                    ),
                                    modifier = Modifier.padding(end = 9.dp)
                                )
                                LabelMedium(
                                    stringResource(R.string.duration).format(episode.iTunesInfo.duration),
                                    modifier = Modifier.padding(end = 18.dp)
                                )
                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(top = 9.dp)
                                .padding(horizontal = 9.dp),
                            horizontalArrangement = Arrangement.End
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
                                        Icons.Rounded.DownloadForOffline,
                                        null,
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier.padding()
                                ) {
                                    Icon(
                                        Icons.Rounded.Share,
                                        null,
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                            FilledIconButton(
                                onClick = { },
                                modifier = Modifier.padding(end = 9.dp)
                            ) { Icon(Icons.Rounded.PlayArrow, null) }

                        }
                    }
                    item {
                        Column(
                            Modifier
                                .padding(horizontal = 18.dp)
                        ) {

                            Text(
                                text = stringResource(R.string.episode_description),
                                modifier = Modifier
                                    .padding(top = 18.dp),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            SelectionContainer {
                                HtmlText(
                                    modifier = Modifier.padding(top = 9.dp),
                                    text = episode.iTunesInfo.summary ?: episode.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = (MaterialTheme.typography.bodyMedium.lineHeight.value + 2f).sp
                                )
                            }
                        }
                    }
                    episode.iTunesInfo.imageString?.let {
                        item {
                            AsyncImage(
                                modifier = Modifier
                                    .padding(18.dp)
                                    .fillParentMaxWidth()
                                    .clip(MaterialTheme.shapes.large)
                                    .aspectRatio(1f),
                                model = it, contentScale = ContentScale.FillBounds,
                                contentDescription = null
                            )
                        }
                    }

                }
            }
        )
    }

}
