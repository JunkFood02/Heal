package io.github.junkfood.podcast.player

import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerLibraryInfo.TAG
import com.google.android.exoplayer2.util.Log

/**
 * 用于接收由MediaControl触发的改变，内部封装实现播放器和播放状态的改变
 */
class MyMediaSessionCallBack(
    private var exoPlayer: ExoPlayer


    ) : MediaSessionCompat.Callback() {


    override fun onPlay() {
        super.onPlay()

        Log.i(TAG, "onPlay: ")
        exoPlayer.play()


    }

    override fun onPause() {
        super.onPause()

        Log.i(TAG, "onPause: ")
        exoPlayer.pause()
    }

    override fun onSeekTo(pos: Long) {
        super.onSeekTo(pos)

        Log.i(TAG, "onSeekTo: pos=" + pos)

        exoPlayer.seekTo(pos)
    }

    override fun onSkipToNext() {
        super.onSkipToNext()

        Log.i(TAG, "onSkipToNext")
        exoPlayer.seekToNext()
        exoPlayer.setPlayWhenReady(true);
//        setPlaybackState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT);
//        mediaSession.setMetadata(getMediaMetadata(1));
        //
    }

    override fun onSkipToPrevious() {
        super.onSkipToPrevious()

        Log.i(TAG, "onSkipToNext")
    }
}
