package io.github.junkfood.podcast.player

import android.app.Activity
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerLibraryInfo.TAG
import com.google.android.exoplayer2.util.Log


class PlayerActivity : Activity() {

    var playbackState = PlaybackStateCompat.fromPlaybackState(mediaController.playbackState)
/*
    当用户点击了播放/暂停按钮后，获取当前的播放状态，
    通过mediaController.getTransportControls给到通过Binder给到mediaSession，
    在service中MediaSessionCompat.Callback改变Exoplayer的播放状态，
    exoplayer的onPlaybackStateChanged收到播放状态改变的通知后触发，
    给mediasession设置mediaSession.setPlaybackState
 */
    fun updateState() {
        var state = playbackState.state
        Log.i(TAG, "onClick.state=" + state)//?tag
        when(state) {
            PlaybackStateCompat.STATE_PLAYING ->
                mediaController.transportControls.play()
            PlaybackStateCompat.STATE_PAUSED ->
                mediaController.transportControls.pause()
            //
            PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS ->
                mediaController.transportControls.skipToPrevious()
            PlaybackStateCompat.STATE_SKIPPING_TO_NEXT ->
                mediaController.transportControls.skipToNext()
//            PlaybackStateCompat.STATE_FAST_FORWARDING
        }
    }
}