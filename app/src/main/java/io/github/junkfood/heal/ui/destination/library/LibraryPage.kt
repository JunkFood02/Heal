package io.github.junkfood.heal.ui.destination.library

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.ContentPaste
import androidx.compose.material.icons.rounded.RssFeed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import io.github.junkfood.heal.R
import io.github.junkfood.heal.ui.common.LocalNavHostController
import io.github.junkfood.heal.ui.common.NavigationGraph
import io.github.junkfood.heal.ui.common.NavigationGraph.toId
import io.github.junkfood.heal.util.DatabaseUtil

private const val TAG = "LibraryPage"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun LibraryPage(
    navHostController: NavHostController = LocalNavHostController.current,
    libraryViewModel: LibraryViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    val viewState = libraryViewModel.stateFlow.collectAsState()
    val podcasts = libraryViewModel.podcastFlow.collectAsState(ArrayList()).value
    Scaffold(modifier = Modifier
        .padding()
        .fillMaxSize(), topBar = {
        io.github.junkfood.heal.ui.component.LargeTopAppBar(title = { Text(stringResource(id = R.string.library)) },
            actions = {
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.ImportExport, null)
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.Search, null)
                }
            })
    }, backgroundColor = MaterialTheme.colorScheme.surface, floatingActionButton = {
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier.padding(bottom = 36.dp, end = 24.dp)
        ) { Icon(Icons.Rounded.RssFeed, null) }
    }, content = {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Box(
                modifier = Modifier
                    .padding(
                        horizontal = 18.dp, vertical = 12.dp
                    )
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    Icon(
                        Icons.Outlined.Subscriptions,
                        null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        stringResource(R.string.subscriptions),
                        modifier = Modifier.padding(horizontal = 12.dp),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                TextButton(modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = { navHostController.navigate(NavigationGraph.SUBSCRIPTIONS) }) {
                    Text(stringResource(R.string.more))
                }

            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 18.dp), horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {

                podcasts.forEach { podcastItem ->
                    item {
                        OutlinedCard(onClick = {
                            navHostController.navigate(
                                NavigationGraph.PODCAST.toId(
                                    podcastItem.id
                                )
                            )
                        }) {
                            AsyncImage(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.medium)
                                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                    .size(120.dp),
                                model = podcastItem.coverUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            LibraryItem(
                icon = Icons.Outlined.DownloadForOffline,
                title = stringResource(R.string.downloads)
            ) {}
            LibraryItem(
                icon = Icons.Outlined.Queue, title = stringResource(R.string.queue)
            ) {}
            LibraryItem(
                icon = Icons.Outlined.History, title = stringResource(R.string.history)
            ) {}
        }

    }

    )
    if (showDialog) {
        val clipboardManager = LocalClipboardManager.current
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.subscribe_rss)) },
            text = {
                TextField(
                    value = viewState.value.url,
                    onValueChange = { libraryViewModel.updateUrl(it) }, trailingIcon = {
                        IconButton(
                            onClick = {
                                clipboardManager.getText()?.text?.let {
                                    libraryViewModel.updateUrl(it)
                                }
                            }) { Icon(Icons.Rounded.ContentPaste, null) }
                    })
            },
            confirmButton = {
                TextButton(onClick = {
                    DatabaseUtil.fetchPodcast(viewState.value.url)
                    showDialog = false
                }) {
                    Text("Fetch podcast")
                }
            })
    }
}

@Composable
fun LibraryItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

}






