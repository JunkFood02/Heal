package io.github.junkfood.podcast.ui.destination

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.rounded.RssFeed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.android.material.color.DynamicColors
import com.icosillion.podengine.models.Episode
import com.icosillion.podengine.models.Podcast
import io.github.junkfood.podcast.ui.color.hct.Hct
import io.github.junkfood.podcast.ui.color.palettes.CorePalette
import io.github.junkfood.podcast.ui.common.LocalDarkTheme
import io.github.junkfood.podcast.ui.common.LocalSeedColor
import io.github.junkfood.podcast.ui.common.RouteName
import io.github.junkfood.podcast.ui.component.PodcastItem
import io.github.junkfood.podcast.ui.theme.ColorScheme.DEFAULT_SEED_COLOR
import io.github.junkfood.podcast.util.PreferenceUtil.modifyThemeColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedPage(navHostController: NavHostController, feedViewModel: FeedViewModel) {

    val viewState = feedViewModel.stateFlow.collectAsState()
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
                        onValueChange = { feedViewModel.updateUrl(it) })
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


            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(12.dp)
            ) {
                if (DynamicColors.isDynamicColorAvailable()) {
                    ColorButton(color = dynamicDarkColorScheme(LocalContext.current).primary)
                    ColorButton(color = dynamicDarkColorScheme(LocalContext.current).tertiary)
                }
                ColorButton(color = Color(DEFAULT_SEED_COLOR))
                ColorButton(color = Color.Yellow)
                ColorButton(color = Color(Hct.from(60.0, 150.0, 70.0).toInt()))
                ColorButton(color = Color(Hct.from(125.0, 50.0, 60.0).toInt()))
                ColorButton(color = Color.Red)
                ColorButton(color = Color.Magenta)
                ColorButton(color = Color.Blue)
            }

            viewState.value.run {
                LazyColumn {
                    for (i in episodeList.indices) {
                        val episode = episodeList[i]
                        item {
                            PodcastItem(
                                imageModel = episode.iTunesInfo.imageString ?: podcastCover,
                                title = podcastTitle,
                                episodeTitle = episode.title,
                                episodeDescription = episode.iTunesInfo.summary
                                    ?: episode.description
                            ) {
                                feedViewModel.jumpToEpisode(i)
                                navHostController.navigate(RouteName.EPISODE)
                            }
                        }
                    }
                }
/*            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (episode in podcast.episodes) {
                    item {
                        ElevatedCard(modifier = Modifier.padding(6.dp), onClick = {}) {
                            CardContent(
                                podcast.title,
                                episode
                            )
                        }
                    }
                }
            }*/
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorButton(modifier: Modifier = Modifier, color: Color) {
    val corePalette = CorePalette.of(color.toArgb())
    val lightColor = corePalette.a2.tone(80)
    val seedColor = corePalette.a2.tone(80)
    val darkColor = corePalette.a2.tone(60)

    val showColor = if (LocalDarkTheme.current.isDarkTheme()) darkColor else lightColor
    val currentColor = LocalSeedColor.current == seedColor
    val state = animateDpAsState(targetValue = if (currentColor) 48.dp else 36.dp)
    val state2 = animateDpAsState(targetValue = if (currentColor) 18.dp else 0.dp)
    ElevatedCard(modifier = modifier
        .padding(4.dp)
        .size(72.dp), onClick = { modifyThemeColor(seedColor) }) {
        Box(Modifier.fillMaxSize()) {
            Box(
                modifier = modifier
                    .size(state.value)
                    .clip(CircleShape)
                    .background(Color(showColor))
                    .align(Alignment.Center)
            ) {

                Icon(
                    Icons.Outlined.Check,
                    null,
                    modifier = Modifier
                        .size(state2.value)
                        .align(Alignment.Center)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
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