package fm.jiecao.jcvideoplayer_lib;

import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import fm.jiecao.jcvideoplayer_lib.player.AbstractPlayer;

import java.util.Map;

/**
 * <p>统一管理MediaPlayer的地方,只有一个mediaPlayer实例，那么不会有多个视频同时播放，也节省资源。</p>
 * <p>Unified management MediaPlayer place, there is only one MediaPlayer instance, then there will be no more video broadcast at the same time, also save resources.</p>
 * Created by Nathen
 * On 2015/11/30 15:39
 */
public class JCMediaManager implements TextureView.SurfaceTextureListener, AbstractPlayer.OnPreparedListener, AbstractPlayer.OnCompletionListener,
        AbstractPlayer.OnBufferingUpdateListener, AbstractPlayer.OnSeekCompleteListener, AbstractPlayer.OnErrorListener,
        AbstractPlayer.OnInfoListener, AbstractPlayer.OnVideoSizeChangedListener {

    public static String TAG = "JieCaoVideoPlayer";

    private static JCMediaManager JCMediaManager;
    public static JCResizeTextureView textureView;
    public static SurfaceTexture savedSurfaceTexture;
    public static String CURRENT_PLAYING_URL;
    public static boolean CURRENT_PLING_LOOP;
    public static Map<String, String> MAP_HEADER_DATA;
    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;

    public static final int HANDLER_PREPARE = 0;
    public static final int HANDLER_RELEASE = 2;
    HandlerThread mMediaHandlerThread;
    MediaHandler mMediaHandler;
    Handler mainThreadHandler;
    private AbstractPlayer mediaPlayer;

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
                        release();
                        mediaPlayer = JCPlayerFactory.buildMediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setLooping(CURRENT_PLING_LOOP);
                        mediaPlayer.setOnPreparedListener(JCMediaManager.this);
                        mediaPlayer.setOnCompletionListener(JCMediaManager.this);
                        mediaPlayer.setOnBufferingUpdateListener(JCMediaManager.this);
                        mediaPlayer.setScreenOnWhilePlaying(true);
                        mediaPlayer.setOnSeekCompleteListener(JCMediaManager.this);
                        mediaPlayer.setOnErrorListener(JCMediaManager.this);
                        mediaPlayer.setOnInfoListener(JCMediaManager.this);
                        mediaPlayer.setOnVideoSizeChangedListener(JCMediaManager.this);
                        mediaPlayer.setDataSource(CURRENT_PLAYING_URL, MAP_HEADER_DATA);
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setSurface(new Surface(savedSurfaceTexture));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    break;
                case HANDLER_RELEASE:
                    release();
                    break;
            }
        }


        private void release() {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

    }

    public void prepare() {
        releaseMediaPlayer();
        Message msg = new Message();
        msg.what = HANDLER_PREPARE;
        mMediaHandler.sendMessage(msg);
    }

    public void releaseMediaPlayer() {
        Message msg = new Message();
        msg.what = HANDLER_RELEASE;
        mMediaHandler.sendMessage(msg);
    }

    public void start() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setLooping(boolean looping) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(looping);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        try {
            if (mediaPlayer != null) {
                return mediaPlayer.isPlaying();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public void pause() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void seekTo(int msec) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(msec);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public int getCurrentPosition() {
        try {
            if (mediaPlayer != null) {
                return mediaPlayer.getCurrentPosition();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getDuration() {
        try {
            if (mediaPlayer != null) {
                return mediaPlayer.getDuration();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void openVideoMute() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(0f, 0f);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void closeVideoMute(AudioManager audioManager) {
        try {
            if (mediaPlayer != null && audioManager != null) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                int a = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mediaPlayer.setVolume(a, a);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i(TAG, "onSurfaceTextureAvailable [" + JCVideoPlayerManager.getCurrentJcvd().hashCode() + "] ");
        if (savedSurfaceTexture == null) {
            savedSurfaceTexture = surfaceTexture;
            prepare();
        } else {
            textureView.setSurfaceTexture(savedSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        // 如果SurfaceTexture还没有更新Image，则记录SizeChanged事件，否则忽略
        Log.i(TAG, "onSurfaceTextureSizeChanged [" + JCVideoPlayerManager.getCurrentJcvd().hashCode() + "] ");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return savedSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    @Override
    public void onPrepared(Object mp) {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onCompletion(Object mp) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    JCVideoPlayerManager.getCurrentJcvd().onAutoCompletion();
                }
            }
        });
    }

    @Override
    public void onBufferingUpdate(Object mp, final int percent) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    JCVideoPlayerManager.getCurrentJcvd().setBufferProgress(percent);
                }
            }
        });
    }

    @Override
    public void onSeekComplete(Object mp) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    JCVideoPlayerManager.getCurrentJcvd().onSeekComplete();
                }
            }
        });
    }

    @Override
    public boolean onError(Object mp, final int what, final int extra) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    JCVideoPlayerManager.getCurrentJcvd().onError(what, extra);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(Object mp, final int what, final int extra) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    JCVideoPlayerManager.getCurrentJcvd().onInfo(what, extra);
                }
            }
        });
        return false;
    }

    @Override
    public void onVideoSizeChanged(Object mp, int width, int height) {
        currentVideoWidth = width;
        currentVideoHeight = height;
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    JCVideoPlayerManager.getCurrentJcvd().onVideoSizeChanged();
                }
            }
        });
    }

}
