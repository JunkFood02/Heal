package io.github.junkfood.podcast.ui.destination.episode

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import io.github.junkfood.podcast.R
import io.github.junkfood.podcast.ui.component.BackButton
import io.github.junkfood.podcast.ui.component.HtmlText
import io.github.junkfood.podcast.ui.destination.FeedViewModel

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
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    io.github.junkfood.podcast.ui.component.MediumTopAppBar(
                        title = {
                            Text(
                                text = podcastTitle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            BackButton { navHostController.popBackStack() }
                        },
                        actions = {
                            IconButton(onClick = {}) {
                                Icon(Icons.Outlined.MoreVert, stringResource(R.string.more))
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                content = {
                    LazyColumn(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                    ) {
                        item {
                            Row(modifier = Modifier.fillParentMaxWidth()) {
                                AsyncImage(
                                    modifier = Modifier
                                        .padding(18.dp)
                                        .fillMaxWidth(0.4f)
                                        .clip(MaterialTheme.shapes.small)
                                        .aspectRatio(1f, matchHeightConstraintsFirst = true),
                                    model = episode.iTunesInfo.imageString,
                                    contentDescription = null
                                )
                                Column(Modifier.padding()) {

                                }
                            }

                        }
                        item {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 18.dp),
                                text = episode.title,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        item {
                            HtmlText(
                                modifier = Modifier.padding(18.dp),
                                text = episode.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                urlSpanStyle = SpanStyle(color = MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            )
        }
    }
}