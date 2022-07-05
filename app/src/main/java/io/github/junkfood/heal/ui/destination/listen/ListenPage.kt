package io.github.junkfood.heal.ui.destination.listen


import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import io.github.junkfood.heal.ui.common.LocalNavHostController
import io.github.junkfood.heal.ui.common.NavigationGraph
import io.github.junkfood.heal.ui.common.NavigationGraph.toId
import io.github.junkfood.heal.util.TextUtil

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun ListenPage(
    navHostController: NavHostController = LocalNavHostController.current,
    listenViewModel: ListenViewModel = viewModel()
) {
    val viewState = listenViewModel.stateFlow.collectAsState()
    Scaffold(
        modifier = Modifier
            .padding()
            .fillMaxSize(), topBar = {
            io.github.junkfood.heal.ui.component.SmallTopAppBar(actions = {
                IconButton(

                    onClick = {}) {
                    Icon(Icons.Outlined.MoreVert, null)

                }
            })
        }, content = { paddingValues ->
            viewState.value.run {
                val padding = animateDpAsState(
                    targetValue = if (isPlaying) 24.dp else 48.dp
                )
                Column(modifier = Modifier.padding(paddingValues)) {
                    Column(
                        modifier = Modifier.weight(5f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .aspectRatio(1f, true)
                                .padding(padding.value)
                                .clip(MaterialTheme.shapes.large),
                            model = imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 3.dp)
                                .clip(MaterialTheme.shapes.large)
                                .clickable {
                                    navHostController.navigate(
                                        NavigationGraph.EPISODE.toId(episodeId)
                                    )
                                }
                                .padding(12.dp, 6.dp),
                            text = episodeTitle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = podcastTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.large)
                                .clickable {
                                    navHostController.navigate(
                                        NavigationGraph.PODCAST.toId(podcastId)
                                    )
                                }
                                .padding(12.dp, 6.dp)
                        )
                        var slider by remember { mutableStateOf(0f) }
                        Slider(
                            value = progress,
                            onValueChange = { slider = it },
                            modifier = Modifier.padding(top = 12.dp),
                            onValueChangeFinished = { listenViewModel.seekToProgress(slider) }
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                TextUtil.durationToText(
                                    (duration * progress).toLong()
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                            )
                            Text(
                                TextUtil.durationToText(duration),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                        /*LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp, bottom = 12.dp)
                                .clip(MaterialTheme.shapes.small)
                        )*/
                        /*var offsetX by remember { mutableStateOf(0f) }
                        Text(
                            modifier = Modifier
                                .offset { IntOffset(offsetX.roundToInt(), 0) }
                                .draggable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberDraggableState { delta ->
                                        offsetX += delta
                                    }
                                ),
                            text = "Drag me!"
                        )*/
                        val speedList = listOf(1.0f, 1.2f, 1.5f, 0.8f)
                        var speedIndex by rememberSaveable { mutableStateOf(0) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(onClick = {
                                if (speedIndex == 3) speedIndex = 0 else speedIndex += 1
                                listenViewModel.setPlayBackSpeed(speedList[speedIndex])
                            }) {
                                Text(
                                    text = ("${speedList[speedIndex]}x"),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(
                                modifier = Modifier.size(48.dp),
                                onClick = { listenViewModel.replay() }) {
                                Icon(Icons.Outlined.Replay10, null, modifier = Modifier.size(28.dp))
                            }
                            FilledIconButton(modifier = Modifier.size(54.dp), onClick = {
                                listenViewModel.playOrPause()
                            }) {
                                Icon(
                                    if (!isPlaying) Icons.Filled.PlayArrow else Icons.Outlined.Pause,
                                    null,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            IconButton(
                                modifier = Modifier.size(48.dp),
                                onClick = { listenViewModel.forward() }) {
                                Icon(
                                    Icons.Outlined.Forward30,
                                    null,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            IconButton(onClick = {}) {
                                Icon(Icons.Filled.DarkMode, null)
                            }
                        }
                    }
                }
            }
        }
    )
}