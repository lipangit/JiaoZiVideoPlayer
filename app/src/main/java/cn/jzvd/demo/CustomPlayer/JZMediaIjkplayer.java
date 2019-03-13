package cn.jzvd.demo.CustomPlayer;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import java.io.IOException;

import cn.jzvd.JZMediaInterface;
import cn.jzvd.Jzvd;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * Created by Nathen on 2017/11/18.
 */

public class JZMediaIjkplayer extends JZMediaInterface implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnTimedTextListener {
    IjkMediaPlayer ijkMediaPlayer;

    public JZMediaIjkplayer(Jzvd jzvd) {
        super(jzvd);
    }

    @Override
    public void start() {
        ijkMediaPlayer.start();
    }

    @Override
    public void prepare() {

        release();
        mMediaHandlerThread = new HandlerThread("JZVD");
        mMediaHandlerThread.start();
        mMediaHandler = new Handler(mMediaHandlerThread.getLooper());//主线程还是非主线程，就在这里
        handler = new Handler();

        mMediaHandler.post(() -> {

            ijkMediaPlayer = new IjkMediaPlayer();
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 1024 * 1024);

            ijkMediaPlayer.setOnPreparedListener(JZMediaIjkplayer.this);
            ijkMediaPlayer.setOnVideoSizeChangedListener(JZMediaIjkplayer.this);
            ijkMediaPlayer.setOnCompletionListener(JZMediaIjkplayer.this);
            ijkMediaPlayer.setOnErrorListener(JZMediaIjkplayer.this);
            ijkMediaPlayer.setOnInfoListener(JZMediaIjkplayer.this);
            ijkMediaPlayer.setOnBufferingUpdateListener(JZMediaIjkplayer.this);
            ijkMediaPlayer.setOnSeekCompleteListener(JZMediaIjkplayer.this);
            ijkMediaPlayer.setOnTimedTextListener(JZMediaIjkplayer.this);

            try {
                ijkMediaPlayer.setDataSource(jzvd.jzDataSource.getCurrentUrl().toString());
                ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                ijkMediaPlayer.setScreenOnWhilePlaying(true);
                ijkMediaPlayer.prepareAsync();

                ijkMediaPlayer.setSurface(new Surface(jzvd.textureView.getSurfaceTexture()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void pause() {
        ijkMediaPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return ijkMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        ijkMediaPlayer.seekTo(time);
    }

    @Override
    public void release() {
        if (ijkMediaPlayer != null)
            ijkMediaPlayer.release();
    }

    @Override
    public long getCurrentPosition() {
        return ijkMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return ijkMediaPlayer.getDuration();
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        ijkMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public void setSpeed(float speed) {
        ijkMediaPlayer.setSpeed(speed);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        ijkMediaPlayer.start();
        if (jzvd.jzDataSource.getCurrentUrl().toString().toLowerCase().contains("mp3") ||
                jzvd.jzDataSource.getCurrentUrl().toString().toLowerCase().contains("wav")) {
            handler.post(() -> jzvd.onPrepared());
        }
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        handler.post(() -> jzvd.onVideoSizeChanged(iMediaPlayer.getVideoWidth(), iMediaPlayer.getVideoHeight()));
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        handler.post(() -> jzvd.onError(what, extra));
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        handler.post(() -> {
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                jzvd.onPrepared();
            } else {
                jzvd.onInfo(what, extra);
            }
        });
        return false;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, final int percent) {
        handler.post(() -> jzvd.setBufferProgress(percent));
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        handler.post(() -> jzvd.onSeekComplete());
    }

    @Override
    public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {

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

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        handler.post(() -> jzvd.onAutoCompletion());
    }
}
