package io.github.junkfood.heal.ui.destination.listen


import android.annotation.SuppressLint
import android.widget.ProgressBar
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.Nightlife
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.android.material.slider.Slider
import io.github.junkfood.heal.R
import io.github.junkfood.heal.ui.common.LocalNavHostController
import io.github.junkfood.heal.util.TextUtil
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun ListenPage(
    navHostController: NavHostController = LocalNavHostController.current,
    listenViewModel: ListenViewModel
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
                Column(modifier = Modifier.padding(paddingValues)) {
                    Column(
                        modifier = Modifier.weight(5f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .aspectRatio(1f, true)
                                .padding(24.dp)
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
                            modifier = Modifier.padding(bottom = 3.dp),
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = podcastTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        var progress by remember { mutableStateOf(0f) }
                        Slider(
                            value = progress,
                            onValueChange = { progress = it },
                            modifier = Modifier.padding(top = 12.dp)
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                TextUtil.durationToText(
                                    (duration * progress).toLong()
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.align(Alignment.CenterStart)
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(onClick = {}) {
                                Text(text = "0.8x", fontWeight = FontWeight.Bold)
                            }
                            IconButton(modifier = Modifier.size(48.dp), onClick = {}) {
                                Icon(Icons.Outlined.Replay10, null, modifier = Modifier.size(28.dp))
                            }
                            FilledIconButton(modifier = Modifier.size(54.dp), onClick = {
                                listenViewModel.playOrPause()
                            }) {
                                Icon(Icons.Rounded.PlayArrow, null, modifier = Modifier.size(36.dp))
                            }
                            IconButton(modifier = Modifier.size(48.dp), onClick = {}) {
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