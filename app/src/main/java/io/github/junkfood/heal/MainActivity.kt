package io.github.junkfood.heal

import android.content.ComponentName
import android.media.AudioManager
import android.media.session.MediaController
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaControllerCompat.getMediaController
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import io.github.junkfood.heal.player.MyMediaSessionCallBack
import io.github.junkfood.heal.player.PodcastService
import io.github.junkfood.heal.ui.HomeEntry
import io.github.junkfood.heal.util.PreferenceUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    lateinit var mediaBrowser: MediaBrowserCompat

    private val connectionCallbacks = object  : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaBrowser.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(
                    this@MainActivity,
                    token
                )

                MediaControllerCompat.setMediaController(this@MainActivity, mediaController)
            }

            buildTransportControls()
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
        }
    }

    private val controllerCallback = object : MediaControllerCompat.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {}

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {}

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        runBlocking {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(PreferenceUtil.getLanguageConfiguration()))
        }
        
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

    public override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
    }

    public override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    public override fun onStop() {
        super.onStop()
        getMediaController(this)?.unregisterCallback(controllerCallback)
        mediaBrowser.disconnect()
    }

    fun buildTransportControls() {
        MainActivity.Companion.mediaController = getMediaController(this@MainActivity)


        // Display the initial state
        val metadata = mediaController.metadata
        val pbState = mediaController.playbackState

        // Register a Callback to stay in sync
        getMediaController(this)?.registerCallback(controllerCallback)
    }


    companion object {
        fun setLanguage(locale: String) {
            if (locale.isEmpty()) return
            BaseApplication.applicationScope.launch(Dispatchers.Main) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
            }
        }


        lateinit var mediaController: MediaControllerCompat
    }
}

