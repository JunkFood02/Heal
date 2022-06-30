package io.github.junkfood.podcast.player

import android.app.Activity
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.util.Log


class PlayerActivity : Activity() {

    lateinit var mediaBrowser: MediaBrowserCompat
    var sessionToken = mediaBrowser.getSessionToken();

    //建立连接之后再创建MediaController
    var mediaController = MediaControllerCompat.getMediaController(this@PlayerActivity)


    var playbackState = PlaybackStateCompat.fromPlaybackState(mediaController.playbackState)

    var state = mediaController.playbackState

}