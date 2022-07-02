package io.github.junkfood.heal.ui.destination.library

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import io.github.junkfood.heal.R
import io.github.junkfood.heal.ui.common.NavigationGraph
import io.github.junkfood.heal.ui.common.NavigationGraph.toId
import io.github.junkfood.heal.ui.component.BackButton

private const val TAG = "LibraryPage"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun LibraryPage(
    navHostController: NavHostController,
    libraryViewModel: LibraryViewModel
) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarScrollState()
    )
    val viewState = libraryViewModel.stateFlow.collectAsState()
    val libraryDataState = libraryViewModel.episodeAndRecordFlow.collectAsState(ArrayList())
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .padding()
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            io.github.junkfood.heal.ui.component.LargeTopAppBar(
                title = { Text(stringResource(id = R.string.library)) },
                navigationIcon = { BackButton { navHostController.popBackStack() } },
                scrollBehavior = scrollBehavior
            )
        },
        content = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Card(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "历史记录",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        TextButton(
                            modifier = Modifier
                                .padding(end = 10.dp),
                            onClick = {
                                libraryViewModel.deleteAllRecords()
                            }) {
                            Text("清除记录")
                        }
                    }


                    libraryDataState.value.run {
                        //val episodeList = ArrayList<io.github.junkfood.podcast.database.model.Episode>()
                        LazyRow(
                            modifier = Modifier
                                .height(180.dp)
                        ) {
                            val episodeList = libraryDataState.value.reversed()
                            for (item in episodeList) {
                                item {
                                    HistoryCard(
                                        imageModel = item.episode.cover,
                                        title = item.episode.title,
                                        author = item.episode.author,
                                        length = item.episode.duration,
                                        progress = item.episode.progress,
                                        onClick = {
                                            Log.d(TAG, "LibraryPage: onClick")
                                            navHostController.navigate(
                                                NavigationGraph.EPISODE.toId(
                                                    item.episode.id
                                                )
                                            )
                                            libraryViewModel.insertToHistory(item.episode.id)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable {}
                                .padding(12.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Rounded.Download,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "下载内容",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        LibraryDivider()
                        Row(
                            modifier = Modifier
                                .clickable {

                                    navHostController.navigate(NavigationGraph.SETTINGS) {
                                        launchSingleTop = true
                                    }
                                }
                                .padding(12.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Rounded.Settings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "设置",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun LibraryDivider() {
    Divider(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
    )
}

@Composable
fun HistoryCard(
    imageModel: Any,
    title: String,
    author: String,
    length: String,
    progress: Float,
    onClick: () -> Unit
) {
    Column(
        Modifier
            .padding(12.dp)
            .clickable {
                onClick()
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .size(100.dp)
                .clip(MaterialTheme.shapes.medium)
                .aspectRatio(1f, matchHeightConstraintsFirst = true),
            model = imageModel,
            contentDescription = null
        )
        Text(
            text = title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 10.sp,
            modifier = Modifier
                .width(100.dp)
        )
        Text(
            text = author,
            fontSize = 10.sp,
            modifier = Modifier
                .width(100.dp)
        )
        LinearProgressIndicator(
            progress = progress,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(100.dp)
        )
    }
}


