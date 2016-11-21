package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
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
public class JCMediaManager implements ExoPlayer.EventListener, SimpleExoPlayer.VideoListener {
    public static String TAG = "JieCaoVideoPlayer";

    private static JCMediaManager JCMediaManager;
    public static JCResizeTextureView textureView;
    public static SimpleExoPlayer simpleExoPlayer;
    public static String CURRENT_PLAYING_URL;


    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;
    public int lastState;
    public int bufferPercent;
    public int backUpBufferState = -1;
    public int videoRotation;

    public static final int HANDLER_PREPARE = 0;
    public static final int HANDLER_SETDISPLAY = 1;
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
                        TrackSelection.Factory videoTrackSelectionFactory =
                                new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
                        trackSelector = new DefaultTrackSelector(mMediaHandler, videoTrackSelectionFactory);
                        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(((FuckBean) msg.obj).context, trackSelector, new DefaultLoadControl(),
                                null, false);
                        simpleExoPlayer.setPlayWhenReady(true);
                        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(((FuckBean) msg.obj).context, BANDWIDTH_METER,
                                new DefaultHttpDataSourceFactory("android_jcvd", BANDWIDTH_METER));
                        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(((FuckBean) msg.obj).url), mediaDataSourceFactory,
                                new DefaultExtractorsFactory(), mMediaHandler, null);
                        simpleExoPlayer.addListener(JCMediaManager.this);
                        simpleExoPlayer.setVideoListener(JCMediaManager.this);
                        isPreparing = true;
                        CURRENT_PLAYING_URL = ((FuckBean) msg.obj).url;
                        simpleExoPlayer.prepare(mediaSource, true, true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case HANDLER_SETDISPLAY:
                    if (msg.obj == null) {
                        simpleExoPlayer.setVideoSurface(null);
                    } else {
                        Surface holder = (Surface) msg.obj;
                        if (holder.isValid()) {
                            Log.i(TAG, "set surface");
                            simpleExoPlayer.setVideoSurface(holder);
                            mainThreadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textureView.requestLayout();
                                }
                            });
                        }
                    }
                    break;
                case HANDLER_RELEASE:
                    simpleExoPlayer.release();
                    break;
            }
        }


    }

    public void prepare(final Context context, final String url, final Map<String, String> mapHeadData, boolean loop) {
        if (TextUtils.isEmpty(url)) return;
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

    public void setDisplay(Surface holder) {
        Message msg = new Message();
        msg.what = HANDLER_SETDISPLAY;
        msg.obj = holder;
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
                    if (JCVideoPlayerManager.getFirst() != null) {
                        JCVideoPlayerManager.getFirst().onPrepared();
                    }
                }
            });
        } else if (playbackState == ExoPlayer.STATE_ENDED) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (JCVideoPlayerManager.getFirst() != null) {
                        JCVideoPlayerManager.getFirst().onAutoCompletion();
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
                if (JCVideoPlayerManager.getFirst() != null) {
                    JCVideoPlayerManager.getFirst().onError(-10000, -10000);
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
                if (JCVideoPlayerManager.getFirst() != null) {
                    JCVideoPlayerManager.getFirst().onVideoSizeChanged();
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
//    @Override
//    public void onBufferingUpdate(IMediaPlayer mp, final int percent) {
//        mainThreadHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (JCVideoPlayerManager.getFirst() != null) {
//                    JCVideoPlayerManager.getFirst().onBufferingUpdate(percent);
//                }
//            }
//        });
//    }

//    @Override
//    public void onSeekComplete(IMediaPlayer mp) {
//        mainThreadHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (JCVideoPlayerManager.getFirst() != null) {
//                    JCVideoPlayerManager.getFirst().onSeekComplete();
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
//                if (JCVideoPlayerManager.getFirst() != null) {
//                    JCVideoPlayerManager.getFirst().onInfo(what, extra);
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
//                if (JCVideoPlayerManager.getFirst() != null) {
//                    JCVideoPlayerManager.getFirst().onVideoSizeChanged();
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
