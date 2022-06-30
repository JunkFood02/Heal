package io.github.junkfood.podcast.ui.destination.library

import androidx.compose.material.Scaffold
import android.annotation.SuppressLint
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import io.github.junkfood.podcast.ui.common.NavigationUtil

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
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar =  {
                  Column(
                      horizontalAlignment = Alignment.CenterHorizontally,
                      modifier = Modifier
                          .background(color = MaterialTheme.colorScheme.primaryContainer)
                  ) {
                      Text(
                          text = "我 的",
                          color = MaterialTheme.colorScheme.onPrimaryContainer,
                          fontSize = 25.sp,
                          fontWeight = FontWeight.Bold,
                          textAlign = TextAlign.Center,
                          modifier = Modifier
                              .padding(top = 30.dp, bottom = 5.dp)
                              .fillMaxWidth()
                      )
                  }
        },
        content = {
            Column() {
                Card(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "历史记录",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    libraryDataState.value.run {
                        val episodeList = ArrayList<io.github.junkfood.podcast.database.model.Episode>()
                        LazyRow (
                            modifier = Modifier
                                .height(180.dp)
                        ) {
                            for (item in episodeList) {
                                item {
                                    HistoryCard(
                                        imageModel = item.cover,
                                        title = item.title,
                                        author = item.author,
                                        length = item.duration,
                                        progress = 0F,
                                        onClick = {}
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

                                    navHostController.navigate(NavigationUtil.SETTINGS) {
                                        launchSingleTop = true                                    }
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


