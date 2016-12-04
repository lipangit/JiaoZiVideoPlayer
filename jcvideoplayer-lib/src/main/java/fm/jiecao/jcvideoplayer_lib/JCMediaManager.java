package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.Map;

/**
 * <p>统一管理MediaPlayer的地方,只有一个mediaPlayer实例，那么不会有多个视频同时播放，也节省资源。</p>
 * <p>Unified management MediaPlayer place, there is only one MediaPlayer instance, then there will be no more video broadcast at the same time, also save resources.</p>
 * Created by Nathen
 * On 2015/11/30 15:39
 */
public class JCMediaManager implements ExoPlayer.EventListener, SimpleExoPlayer.VideoListener, TextureView.SurfaceTextureListener {
    public static String TAG = "JieCaoVideoPlayer";

    public static String USER_AGENT = "android_jcvd";
    private static JCMediaManager JCMediaManager;
    public static JCResizeTextureView textureView;
    public static SurfaceTexture savedSurfaceTexture;
    public SimpleExoPlayer simpleExoPlayer;
    public static String CURRENT_PLAYING_URL;


    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;
    public int lastState;

    public static final int HANDLER_PREPARE = 0;
    //    public static final int HANDLER_SETDISPLAY = 1;
    public static final int HANDLER_RELEASE = 2;
    HandlerThread mMediaHandlerThread;
    MediaHandler mMediaHandler;
    Handler mainThreadHandler;

    public static JCMediaManager instance() {
        if (JCMediaManager == null) {
            JCMediaManager = new JCMediaManager();
        }
        return JCMediaManager;
    }

    public JCMediaManager() {
        mMediaHandlerThread = new HandlerThread(TAG);
        mMediaHandlerThread.start();
        mMediaHandler = new MediaHandler((mMediaHandlerThread.getLooper()));
        mainThreadHandler = new Handler();
    }

    public Point getVideoSize() {
        if (currentVideoWidth != 0 && currentVideoHeight != 0) {
            return new Point(currentVideoWidth, currentVideoHeight);
        } else {
            return null;
        }
    }

    MappingTrackSelector trackSelector;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    public class MediaHandler extends Handler {
        public MediaHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_PREPARE:
                    try {
                        currentVideoWidth = 0;
                        currentVideoHeight = 0;
                        if (simpleExoPlayer != null) simpleExoPlayer.release();
                        isPreparing = true;
                        TrackSelection.Factory videoTrackSelectionFactory =
                                new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
                        trackSelector = new DefaultTrackSelector(mMediaHandler, videoTrackSelectionFactory);
                        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(((FuckBean) msg.obj).context, trackSelector, new DefaultLoadControl(),
                                null, false);
                        simpleExoPlayer.setPlayWhenReady(true);
                        MediaSource mediaSource = buildMediaSource(((FuckBean) msg.obj).context, Uri.parse(((FuckBean) msg.obj).url));
                        simpleExoPlayer.addListener(JCMediaManager.this);
                        simpleExoPlayer.setVideoListener(JCMediaManager.this);
                        simpleExoPlayer.prepare(mediaSource, true, true);
                        simpleExoPlayer.setVideoSurface(new Surface(savedSurfaceTexture));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case HANDLER_RELEASE:
                    if (simpleExoPlayer != null) {
                        simpleExoPlayer.release();
                    }
                    simpleExoPlayer = null;
                    break;
            }
        }
    }

    private MediaSource buildMediaSource(Context context, Uri uri) {
        int type = JCUtils.getUrlType(uri.toString());
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, new DefaultDataSourceFactory(context, null,
                        new DefaultHttpDataSourceFactory(USER_AGENT, null)),
                        new DefaultSsChunkSource.Factory(new DefaultDataSourceFactory(context, BANDWIDTH_METER,
                                new DefaultHttpDataSourceFactory(USER_AGENT, BANDWIDTH_METER))), mMediaHandler, null);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, new DefaultDataSourceFactory(context, null,
                        new DefaultHttpDataSourceFactory(USER_AGENT, null)),
                        new DefaultDashChunkSource.Factory(new DefaultDataSourceFactory(context, BANDWIDTH_METER,
                                new DefaultHttpDataSourceFactory(USER_AGENT, BANDWIDTH_METER))), mMediaHandler, null);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, new DefaultDataSourceFactory(context, BANDWIDTH_METER,
                        new DefaultHttpDataSourceFactory(USER_AGENT, BANDWIDTH_METER)), mMediaHandler, null);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, new DefaultDataSourceFactory(context, BANDWIDTH_METER,
                        new DefaultHttpDataSourceFactory(USER_AGENT, BANDWIDTH_METER)), new DefaultExtractorsFactory(),
                        mMediaHandler, null);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    public void prepare(final Context context, final String url, final Map<String, String> mapHeadData, boolean loop) {
        if (TextUtils.isEmpty(url)) return;
        releaseMediaPlayer();
        Message msg = new Message();
        msg.what = HANDLER_PREPARE;
        FuckBean fb = new FuckBean(context, url, mapHeadData, loop);
        msg.obj = fb;
        mMediaHandler.sendMessage(msg);
    }

    public void releaseMediaPlayer() {
        Message msg = new Message();
        msg.what = HANDLER_RELEASE;
        mMediaHandler.sendMessage(msg);
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    public static boolean isPreparing = false;

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (isPreparing && playbackState == ExoPlayer.STATE_READY) {
            // this is accurate
            isPreparing = false;
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (JCVideoPlayerManager.getCurrentJcvdOnFirtFloor() != null) {
                        JCVideoPlayerManager.getCurrentJcvdOnFirtFloor().onPrepared();
                    }
                }
            });
        } else if (playbackState == ExoPlayer.STATE_ENDED) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (JCVideoPlayerManager.getCurrentJcvdOnFirtFloor() != null) {
                        JCVideoPlayerManager.getCurrentJcvdOnFirtFloor().onAutoCompletion();
                    }
                }
            });
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManager.getCurrentJcvdOnFirtFloor() != null) {
                    JCVideoPlayerManager.getCurrentJcvdOnFirtFloor().onError(-10000, -10000);
                }
            }
        });
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        currentVideoWidth = width;
        currentVideoHeight = height;
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManager.getCurrentJcvdOnFirtFloor() != null) {
                    JCVideoPlayerManager.getCurrentJcvdOnFirtFloor().onVideoSizeChanged();
                }
            }
        });
    }

    @Override
    public void onRenderedFirstFrame() {

    }

    @Override
    public void onVideoTracksDisabled() {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i(TAG, "onSurfaceTextureAvailable [" + this.hashCode() + "] ");
        if (savedSurfaceTexture == null) {
            savedSurfaceTexture = surfaceTexture;
            prepare(textureView.getContext(), CURRENT_PLAYING_URL, null, false);
        } else {
            textureView.setSurfaceTexture(savedSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        // 如果SurfaceTexture还没有更新Image，则记录SizeChanged事件，否则忽略
        Log.i(TAG, "onSurfaceTextureSizeChanged [" + this.hashCode() + "] ");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return savedSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }
//    @Override
//    public void onBufferingUpdate(IMediaPlayer mp, final int percent) {
//        mainThreadHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (JCVideoPlayerManager.getCurrentJcvdOnFirtFloor() != null) {
//                    JCVideoPlayerManager.getCurrentJcvdOnFirtFloor().onBufferingUpdate(percent);
//                }
//            }
//        });
//    }

//    @Override
//    public void onSeekComplete(IMediaPlayer mp) {
//        mainThreadHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (JCVideoPlayerManager.getCurrentJcvdOnFirtFloor() != null) {
//                    JCVideoPlayerManager.getCurrentJcvdOnFirtFloor().onSeekComplete();
//                }
//            }
//        });
//    }

//    @Override
//    public boolean onError(IMediaPlayer mp, final int what, final int extra) {
//
//        return true;
//    }

//    @Override
//    public boolean onInfo(IMediaPlayer mp, final int what, final int extra) {
//        mainThreadHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (JCVideoPlayerManager.getCurrentJcvdOnFirtFloor() != null) {
//                    JCVideoPlayerManager.getCurrentJcvdOnFirtFloor().onInfo(what, extra);
//                }
//            }
//        });
//        return false;
//    }

//    @Override
//    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
//        currentVideoWidth = mp.getVideoWidth();
//        currentVideoHeight = mp.getVideoHeight();
//        mainThreadHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (JCVideoPlayerManager.getCurrentJcvdOnFirtFloor() != null) {
//                    JCVideoPlayerManager.getCurrentJcvdOnFirtFloor().onVideoSizeChanged();
//                }
//            }
//        });
//    }


    private class FuckBean {
        Context context;
        String url;
        Map<String, String> mapHeadData;
        boolean looping;

        FuckBean(Context context, String url, Map<String, String> mapHeadData, boolean loop) {
            this.context = context;
            this.url = url;
            this.mapHeadData = mapHeadData;
            this.looping = loop;
        }
    }
}
