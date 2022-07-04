package io.github.junkfood.heal.ui.destination.listen

import android.annotation.SuppressLint
import android.media.browse.MediaBrowser
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DownloadForOffline
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import io.github.junkfood.heal.MainActivity
import io.github.junkfood.heal.player.MyMediaSessionCallBack

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun ListenPage(navHostController: NavHostController) {
    Scaffold(modifier = Modifier
        .padding()
        .fillMaxSize()) {
        Column() {
            Row(modifier = Modifier.weight(1f)) {
                IconButton(
                    onClick = {
                        val mediaController = MainActivity.Companion.mediaController
                        val pbState = mediaController.playbackState.state
                        if (pbState == PlaybackStateCompat.STATE_PLAYING) {
                            mediaController.transportControls.pause()
                        } else {
                            mediaController.transportControls.play()
                        }
                    },
                    modifier = Modifier.padding()
                ) {
                    Icon(
                        Icons.Rounded.PlayArrow,
                        null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(
                    onClick = {  },
                    modifier = Modifier.padding()
                ) {
                    Icon(
                        Icons.Outlined.SkipNext,
                        null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(
                    onClick = {  },
                    modifier = Modifier.padding()
                ) {
                    Icon(
                        Icons.Rounded.SkipPrevious,
                        null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}