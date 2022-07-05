package io.github.junkfood.heal.player

import android.os.Binder
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.TextUtils
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.Log
import io.github.junkfood.heal.MainActivity
import io.github.junkfood.heal.database.model.Episode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class PodcastService : MediaBrowserServiceCompat() {

    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    private val TAG = "PodcastService"
    private lateinit var episodes : List<Episode>//传入的数据集

    companion object {
        lateinit var exoPlayer: ExoPlayer
        lateinit var mediaSession: MediaSessionCompat


        val progress: Flow<Float> = flow {
            while (true) {
                val progress = (exoPlayer.contentPosition / exoPlayer.contentDuration).toFloat()
                emit(progress)
                delay(100)
            }
        }

     }

    /**
     * 当服务收到onCreate（）生命周期回调方法时，它应该执行以下步骤：
     * 1. 创建并初始化media session
     * 2. 设置media session回调
     * 3. 设置media session token
     */
    override fun onCreate() {
        Log.i(TAG, "onCreate: ")
        super.onCreate()
        exoPlayer = ExoPlayer.Builder(applicationContext).build()
        //1. 创建并初始化MediaSession
        mediaSession = MediaSessionCompat(applicationContext, TAG).apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                    or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )

            // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())


            // MySessionCallback() has methods that handle callbacks from a media controller
            setCallback(MyMediaSessionCallBack(exoPlayer))

            // Set the session's token so that client activities can communicate with it.
            setSessionToken(sessionToken)
        }

        val playbackState = PlaybackStateCompat.Builder()
            .setActions(
                (PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE
                        or PlaybackStateCompat.ACTION_STOP or PlaybackStateCompat.ACTION_PLAY_PAUSE or
                        PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                        PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SEEK_TO)
            )
            .build()
        mediaSession!!.setPlaybackState(playbackState)

    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        Log.i(TAG, "onGetRoot: clientPackageName=" + clientPackageName + " clientUid=" + clientUid + " pid=" + Binder.getCallingPid()
                + " uid=" + Binder.getCallingUid())
        //返回非空，表示连接成功
        return BrowserRoot("media_root_id", null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        Log.i(TAG, "onLoadChildren: parentId=" + parentId)
        val mediaItems = mutableListOf<MediaBrowserCompat.MediaItem>()

        when {
            !TextUtils.equals("media_root_id", parentId) -> {
            }
        }

        val episodeList = mutableListOf<Episode>()//getMusicEntityList();
//创建播放列表
        val num = 0
        val last = 10

        val metadata = MediaMetadata.Builder()

        for (i in num..last) {
            var episode = episodeList.add(episodes[i])
            //从Episodes列表中将需要的数据放入新建的list中
            //MediaMetadataCompat


            if( episode ) {

//                metadata.setArtworkUri(Uri.parse(episodes[i].audioUrl))//*****文件
                metadata.setAlbumArtist(episodes[i].author)
                metadata.setDisplayTitle(episodes[i].description)
                metadata.setDisplayTitle(episodes[i].title)
                //数据初始化


                if (0 == i) {
                    mediaSession?.setMetadata(MediaMetadataCompat.fromMediaMetadata(metadata))//setMetadata(metadataCompat)
                }

                mediaItems.add(MediaBrowserCompat.MediaItem(MediaMetadataCompat.fromMediaMetadata(metadata).description, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE))

                exoPlayer?.addMediaItem(MediaItem.fromUri(episodes[i].audioUrl))
            }
        }
        //当设置多首歌曲组成队列时报错
        // IllegalStateException: sendResult() called when either sendResult() or sendError() had already been called for: media_root_id
        //原因，之前在for处理了，应该在设置好mediaItems列表后，统一设置result
        result.sendResult(mediaItems)
        Log.i(TAG, "onLoadChildren: addMediaItem")

        mediaSession?.controller?.playbackState?.let { initExoPlayerListener(it) }

        exoPlayer?.prepare()
        Log.i(TAG, "onLoadChildren: prepare")

    }


    //TODO
    private fun initExoPlayerListener(state: PlaybackStateCompat) {
//            mediaSession?.setPlaybackState(state)



//            fun Player.Listener(){         // onPlaybackStateChanged(int state) {
//                var currentPosition = exoPlayer!!. getCurrentPosition ();
//                var duration = exoPlayer!!. getDuration ();
//
//                //状态改变（播放器内部发生状态变化的回调，
//                // 包括
//                // 1. 用户触发的  比如： 手动切歌曲、暂停、播放、seek等；
//                // 2. 播放器内部触发 比如： 播放结束、自动切歌曲等）
//
//                //该如何通知给ui业务层呐？？好些只能通过回调
//                //那有该如何 --》查看源码得知通过setPlaybackState设置
//                Log.i(
//                    TAG,
//                    "onPlaybackStateChanged: currentPosition=" + currentPosition + " duration=" + duration + " state=" + state
//                );
//
//                int playbackState;
//                switch(state) {
//                    default:
//                    case Player . STATE_IDLE :
//                    playbackState = PlaybackStateCompat.STATE_NONE;
//                    break;
//                    case Player . STATE_BUFFERING :
//                    playbackState = PlaybackStateCompat.STATE_BUFFERING;
//                    break;
//                    case Player . STATE_READY :
//                    if (exoPlayer.getPlayWhenReady()) {
//                        playbackState = PlaybackStateCompat.STATE_PLAYING;
//                    } else {
//                        playbackState = PlaybackStateCompat.STATE_PAUSED;
//                    }
//                    break;
//                    case Player . STATE_ENDED :
//                    playbackState = PlaybackStateCompat.STATE_STOPPED;
//                    break;
//                }
//                //播放器的状态变化，通过mediasession告诉在ui业务层注册的MediaControllerCompat.Callback进行回调
//
//                setPlaybackState(playbackState);
//
//            }
//        }
//        )
    }

    private fun setPlaybackState(playbackState: Any?) {

    }


}



