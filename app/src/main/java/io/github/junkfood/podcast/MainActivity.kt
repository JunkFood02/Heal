package io.github.junkfood.podcast

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import io.github.junkfood.podcast.player.PodcastService
import io.github.junkfood.podcast.ui.HomeEntry

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var mediaBrowser: MediaBrowserCompat

    private val connectionCallbacks = object  : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {

        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaBrowser = MediaBrowserCompat(
            this,
            ComponentName(this, PodcastService::class.java),
            connectionCallbacks,
            null
        )

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            v.setPadding(0, 0, 0, 0)
            insets
        }
        setContent {
            HomeEntry()
        }
    }

}

