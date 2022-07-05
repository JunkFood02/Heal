package io.github.junkfood.heal.player

import android.support.v4.media.session.PlaybackStateCompat

public abstract class PlaybackInfoListener {

    /**
     * 播放状态变化
     *
     * @param state
     */
    public abstract fun onPlaybackStateChange (state: PlaybackStateCompat)


    /**
     * 播放完成
     */
    public fun onPlaybackCompleted() {
    }
}