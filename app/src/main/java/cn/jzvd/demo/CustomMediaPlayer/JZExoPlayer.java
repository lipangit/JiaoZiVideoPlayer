package cn.jzvd.demo.CustomMediaPlayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.demo.R;

/**
 * Created by MinhDV on 5/3/18.
 */

public class JZExoPlayer extends JZMediaInterface implements Player.EventListener, VideoListener {
    private SimpleExoPlayer simpleExoPlayer;
    private Handler mainHandler;
    private Runnable callback;
    private String TAG = "JZExoPlayer";
    private MediaSource videoSource;

    @Override
    public void start() {
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    public void prepare() {
        Log.e(TAG, "prepare");
        mainHandler = new Handler();
        Context context = JZUtils.getCurrentContext();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE),
                360000, 600000, 1000, 5000,
                C.LENGTH_UNSET,
                false);

        // 2. Create the player

        RenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getResources().getString(R.string.app_name)));

        String currUrl = currentDataSource.toString();
        if (currUrl.contains(".m3u8") == true) {
            videoSource = new HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(currUrl), mainHandler, null);
        } else {
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(currUrl));
        }
        simpleExoPlayer.addVideoListener(this);

        Log.e(TAG, "URL Link = " + currUrl);

        simpleExoPlayer.addListener(this);

        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayer.setPlayWhenReady(true);
        callback = new onBufferingUpdate();
        mainHandler.post(callback);
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        JZMediaManager.instance().currentVideoWidth = width;
        JZMediaManager.instance().currentVideoHeight = height;
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onVideoSizeChanged();
                }
            }
        });
    }

    @Override
    public void onRenderedFirstFrame() {
        Log.e(TAG, "onRenderedFirstFrame");
    }

    private class onBufferingUpdate implements Runnable {
        @Override
        public void run() {
            final int percent = simpleExoPlayer.getBufferedPercentage();
            JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                        JZVideoPlayerManager.getCurrentJzvd().setBufferProgress(percent);
                    }
                }
            });
        }
    }

    @Override
    public void pause() {
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    public boolean isPlaying() {
        return simpleExoPlayer.getPlayWhenReady();
    }

    @Override
    public void seekTo(long time) {
        simpleExoPlayer.seekTo(time);
    }

    @Override
    public void release() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
        }
        if (mainHandler != null)
            mainHandler.removeCallbacks(callback);
    }

    @Override
    public long getCurrentPosition() {
        if (simpleExoPlayer != null)
            return simpleExoPlayer.getCurrentPosition();
        else return 0;
    }

    @Override
    public long getDuration() {
        if (simpleExoPlayer != null)
            return simpleExoPlayer.getDuration();
        else return 0;
    }

    @Override
    public void setSurface(Surface surface) {
        simpleExoPlayer.setVideoSurface(surface);
        Log.e(TAG, "setSurface");
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        simpleExoPlayer.setVolume(leftVolume);
        simpleExoPlayer.setVolume(rightVolume);
    }

    @Override
    public void onTimelineChanged(final Timeline timeline, Object manifest, final int reason) {
        Log.e(TAG, "onTimelineChanged");
//        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
//
//            @Override
//            public void run() {
//                if (reason == 0) {
//
//                    JZVideoPlayerManager.getCurrentJzvd().onInfo(reason, timeline.getPeriodCount());
//                }
//            }
//        });
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.e(TAG, "onLoadingChanged");
    }

    @Override
    public void onPlayerStateChanged(final boolean playWhenReady, final int playbackState) {
        Log.e(TAG, "onPlayerStateChanged" + playbackState + "/ready=" + String.valueOf(playWhenReady));
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    switch (playbackState) {
                        case Player.STATE_IDLE: {
                        }
                        break;
                        case Player.STATE_BUFFERING: {
                        }
                        break;
                        case Player.STATE_READY: {
                            if (playWhenReady) {
                                JZVideoPlayerManager.getCurrentJzvd().onPrepared();
                            } else {
                            }
                        }
                        break;
                        case Player.STATE_ENDED: {
                            JZVideoPlayerManager.getCurrentJzvd().onAutoCompletion();
                        }
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.e(TAG, "onPlayerError" + error.toString());
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onError(1000, 1000);
                }
            }
        });
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onSeekComplete();
                }
            }
        });
    }

}
