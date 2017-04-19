package fm.jiecao.jcvideoplayer_lib;

import android.graphics.Point;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import static fm.jiecao.jcvideoplayer_lib.JCMediaManager.HANDLER_PREPARE;
import static fm.jiecao.jcvideoplayer_lib.JCMediaManager.HANDLER_RELEASE;
import static fm.jiecao.jcvideoplayer_lib.JCMediaManager.whichPlayer;

/**
 * Created by tysheng
 * Date: 2017/4/17 20:24.
 * Email: tyshengsx@gmail.com
 */

public class AbstractPlayerControllerWrapper {
    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;
    private MediaHandler mMediaHandler;
    private Handler mainThreadHandler;
    private PlayerAdapter mediaPlayer;

    public AbstractPlayerControllerWrapper() {
        HandlerThread mediaHandlerThread = new HandlerThread(JCMediaManager.TAG);
        mediaHandlerThread.start();
        mMediaHandler = new MediaHandler((mediaHandlerThread.getLooper()));
        mainThreadHandler = new Handler();
    }

    public PlayerAdapter getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(PlayerAdapter mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void initPlayer() {
        setMediaPlayer(new AbstractPlayer(whichPlayer));
    }

    public Point getVideoSize() {
        if (currentVideoWidth != 0 && currentVideoHeight != 0) {
            return new Point(currentVideoWidth, currentVideoHeight);
        } else {
            return null;
        }
    }

    public void onCompletion() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    JCVideoPlayerManager.getCurrentJcvd().onAutoCompletion();
                }
            }
        });
    }

    public void onBufferingUpdate(final int percent) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    JCVideoPlayerManager.getCurrentJcvd().setBufferProgress(percent);
                }
            }
        });
    }

    public void onSeekComplete() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    JCVideoPlayerManager.getCurrentJcvd().onSeekComplete();
                }
            }
        });
    }

    public boolean onError(final int what, final int extra) {
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

    public boolean onInfo(final int what, final int extra) {
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

    public void onVideoSizeChanged(int width, int height) {
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

    public void onPrepared() {
        start();
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                    JCVideoPlayerManager.getCurrentJcvd().onPrepared();
                }
            }
        });
    }

    public void initPlayerAdapter() throws Exception {
        release();
        initPlayer();
        mediaPlayer.setListener();
    }

    public void start() {
        mediaPlayer.start();
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void seekTo(long var1) {
        mediaPlayer.seekTo(var1);
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

    public void setCurrentVideoWH(int width, int height) {
        currentVideoWidth = width;
        currentVideoHeight = height;
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
                        initPlayerAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case HANDLER_RELEASE:
                    release();
                    break;
            }
        }
    }

}
