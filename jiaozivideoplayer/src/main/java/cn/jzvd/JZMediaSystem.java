package cn.jzvd;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.RequiresApi;
import android.view.Surface;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Nathen on 2017/11/8.
 * 实现系统的播放引擎
 */
public class JZMediaSystem extends JZMediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {

    public MediaPlayer mediaPlayer;

    public JZMediaSystem(Jzvd jzvd) {
        super(jzvd);
    }

    @Override
    public void prepare() {
        release();
        mMediaHandlerThread = new HandlerThread("JZVD");
        mMediaHandlerThread.start();
        mMediaHandler = new Handler(mMediaHandlerThread.getLooper());//主线程还是非主线程，就在这里
        handler = new Handler();

        mMediaHandler.post(() -> {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(jzvd.jzDataSource.looping);
                mediaPlayer.setOnPreparedListener(JZMediaSystem.this);
                mediaPlayer.setOnCompletionListener(JZMediaSystem.this);
                mediaPlayer.setOnBufferingUpdateListener(JZMediaSystem.this);
                mediaPlayer.setScreenOnWhilePlaying(true);
                mediaPlayer.setOnSeekCompleteListener(JZMediaSystem.this);
                mediaPlayer.setOnErrorListener(JZMediaSystem.this);
                mediaPlayer.setOnInfoListener(JZMediaSystem.this);
                mediaPlayer.setOnVideoSizeChangedListener(JZMediaSystem.this);
                Class<MediaPlayer> clazz = MediaPlayer.class;
                Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
                method.invoke(mediaPlayer, jzvd.jzDataSource.getCurrentUrl().toString(), jzvd.jzDataSource.headerMap);
                mediaPlayer.prepareAsync();
                mediaPlayer.setSurface(new Surface(SAVED_SURFACE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void start() {
        mMediaHandler.post(() -> mediaPlayer.start());
    }

    @Override
    public void pause() {
        mMediaHandler.post(() -> mediaPlayer.pause());
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        mMediaHandler.post(() -> {
            try {
                mediaPlayer.seekTo((int) time);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void release() {//not perfect change you later
        if (mMediaHandler != null && mMediaHandlerThread != null && mediaPlayer != null) {//不知道有没有妖孽
            HandlerThread tmpHandlerThread = mMediaHandlerThread;
            MediaPlayer tmpMediaPlayer = mediaPlayer;
            mMediaHandler.post(() -> {
                tmpMediaPlayer.setSurface(null);
                tmpMediaPlayer.release();
                tmpHandlerThread.quit();
            });
            mediaPlayer = null;
        }
    }

    //TODO 测试这种问题是否在threadHandler中是否正常，所有的操作mediaplayer是否不需要thread，挨个测试，是否有问题
    @Override
    public long getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    @Override
    public long getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        if (mMediaHandler == null) return;
        mMediaHandler.post(() -> {
            if (mediaPlayer != null) mediaPlayer.setVolume(leftVolume, rightVolume);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void setSpeed(float speed) {
        PlaybackParams pp = mediaPlayer.getPlaybackParams();
        pp.setSpeed(speed);
        mediaPlayer.setPlaybackParams(pp);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        if (jzvd.jzDataSource.getCurrentUrl().toString().toLowerCase().contains("mp3") ||
                jzvd.jzDataSource.getCurrentUrl().toString().toLowerCase().contains("wav")) {
            handler.post(() -> jzvd.onPrepared());//如果是mp3音频，走这里
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        handler.post(() -> jzvd.onAutoCompletion());
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, final int percent) {
        handler.post(() -> jzvd.setBufferProgress(percent));
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        handler.post(() -> jzvd.onSeekComplete());
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, final int what, final int extra) {
        handler.post(() -> jzvd.onError(what, extra));
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, final int what, final int extra) {
        handler.post(() -> {
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                if (jzvd.state == Jzvd.STATE_PREPARING
                        || jzvd.state == Jzvd.STATE_PREPARING_CHANGING_URL) {
                    jzvd.onPrepared();//真正的prepared
                }
            } else {
                jzvd.onInfo(what, extra);
            }
        });
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
        handler.post(() -> jzvd.onVideoSizeChanged(width, height));
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (SAVED_SURFACE == null) {
            SAVED_SURFACE = surface;
            prepare();
        } else {
            jzvd.textureView.setSurfaceTexture(SAVED_SURFACE);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
