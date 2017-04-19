package fm.jiecao.jcvideoplayer_lib;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;

import java.lang.reflect.Method;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by tysheng
 * Date: 2017/4/17 14:22.
 * Email: tyshengsx@gmail.com
 */

public class AbstractPlayer implements PlayerAdapter, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {
    private int whichPlayer;
    private IjkMediaPlayer mIjkMediaPlayer;
    private MediaPlayer mMediaPlayer;

    public AbstractPlayer(int whichPlayer) {
        this.whichPlayer = whichPlayer;
        if (whichPlayer == JCMediaManager.IJK) {
            mIjkMediaPlayer = new IjkMediaPlayer();
        } else if (whichPlayer == JCMediaManager.MEDIA) {
            mMediaPlayer = new MediaPlayer();
        }
    }

    @Override
    public boolean isPlaying() {
        if (whichPlayer == JCMediaManager.IJK) {
            return mIjkMediaPlayer.isPlaying();
        } else if (whichPlayer == JCMediaManager.MEDIA) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void pause() {
        if (whichPlayer == JCMediaManager.IJK) {
            mIjkMediaPlayer.pause();
        } else if (whichPlayer == JCMediaManager.MEDIA) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void seekTo(long var1) {
        if (whichPlayer == JCMediaManager.IJK) {
            mIjkMediaPlayer.seekTo(var1);
        } else if (whichPlayer == JCMediaManager.MEDIA) {
            mMediaPlayer.seekTo((int) var1);
        }
    }

    @Override
    public long getCurrentPosition() {
        if (whichPlayer == JCMediaManager.IJK) {
            return mIjkMediaPlayer.getCurrentPosition();
        } else if (whichPlayer == JCMediaManager.MEDIA) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public long getDuration() {
        if (whichPlayer == JCMediaManager.IJK) {
            return mIjkMediaPlayer.getDuration();
        } else if (whichPlayer == JCMediaManager.MEDIA) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public void start() {
        if (whichPlayer == JCMediaManager.IJK) {
            mIjkMediaPlayer.start();
        } else if (whichPlayer == JCMediaManager.MEDIA) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void release() {
        if (whichPlayer == JCMediaManager.IJK) {
            mIjkMediaPlayer.release();
        } else if (whichPlayer == JCMediaManager.MEDIA) {
            mMediaPlayer.release();
        }
    }

    @Override
    public void setListener() throws Exception {
        if (whichPlayer == JCMediaManager.IJK) {
            mIjkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mIjkMediaPlayer.setDataSource(JCMediaManager.CURRENT_PLAYING_URL, JCMediaManager.MAP_HEADER_DATA);
            mIjkMediaPlayer.setLooping(JCMediaManager.CURRENT_PLING_LOOP);
            mIjkMediaPlayer.setOnPreparedListener(this);
            mIjkMediaPlayer.setOnCompletionListener(this);
            mIjkMediaPlayer.setOnBufferingUpdateListener(this);
            mIjkMediaPlayer.setScreenOnWhilePlaying(true);
            mIjkMediaPlayer.setOnSeekCompleteListener(this);
            mIjkMediaPlayer.setOnErrorListener(this);
            mIjkMediaPlayer.setOnInfoListener(this);
            mIjkMediaPlayer.setOnVideoSizeChangedListener(this);
            mIjkMediaPlayer.prepareAsync();
            mIjkMediaPlayer.setSurface(new Surface(JCMediaManager.savedSurfaceTexture));
            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1);
            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "protocol_whitelist", "http,https,tls,rtp,tcp,udp,crypto,httpproxy");
        } else if (whichPlayer == JCMediaManager.MEDIA) {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Class<MediaPlayer> clazz = MediaPlayer.class;
            Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
            method.invoke(mMediaPlayer, JCMediaManager.CURRENT_PLAYING_URL, JCMediaManager.MAP_HEADER_DATA);
            mMediaPlayer.setLooping(JCMediaManager.CURRENT_PLING_LOOP);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.setOnSeekCompleteListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnInfoListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setSurface(new Surface(JCMediaManager.savedSurfaceTexture));
        }
    }

    public MediaManager getMediaManagerInstance() {
        return JCMediaManager.instance();
    }

    private void onPrepared() {
        getMediaManagerInstance().onPrepared();
    }

    private void onCompletion() {
        getMediaManagerInstance().onCompletion();
    }

    private void onBufferingUpdate(int i) {
        getMediaManagerInstance().onBufferingUpdate(i);
    }

    private void onSeekComplete() {
        getMediaManagerInstance().onSeekComplete();
    }

    private boolean onError(int i, int i1) {
        return getMediaManagerInstance().onError(i, i1);
    }

    private boolean onInfo(int i, int i1) {
        return getMediaManagerInstance().onInfo(i, i1);
    }

    private void onVideoSizeChanged(int i, int i1) {
        getMediaManagerInstance().onVideoSizeChanged(i, i1);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        onPrepared();
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        onCompletion();
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        onBufferingUpdate(i);
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        onSeekComplete();
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        return onError(i, i1);
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        return onInfo(what, extra);
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        onVideoSizeChanged(i, i1);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        onPrepared();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        onCompletion();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        onBufferingUpdate(percent);
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        onSeekComplete();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return onError(what, extra);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return onInfo(what, extra);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        onVideoSizeChanged(width, height);
    }
}
