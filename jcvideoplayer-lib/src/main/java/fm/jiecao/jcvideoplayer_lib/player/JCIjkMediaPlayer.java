package fm.jiecao.jcvideoplayer_lib.player;

import android.content.Context;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.lang.reflect.Method;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * IjkMediaPlayer播放器
 * Created by lidongyang on 2017/8/10.
 */
public class JCIjkMediaPlayer extends AbstractPlayer<IjkMediaPlayer> {

    public JCIjkMediaPlayer() {
        mMediaPlayer = new IjkMediaPlayer();
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(surfaceHolder);
        }
    }

    @Override
    public void setDataSource(String path, Map<String, String> headers) throws Throwable {
        if (mMediaPlayer != null) {
            Class<IjkMediaPlayer> clazz = IjkMediaPlayer.class;
            Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
            method.invoke(mMediaPlayer, path, headers);
        }
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        if (mMediaPlayer != null) {
            mMediaPlayer.prepareAsync();
        }
    }

    @Override
    public void start() throws IllegalStateException {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void stop() throws IllegalStateException {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    @Override
    public void pause() throws IllegalStateException {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void setScreenOnWhilePlaying(boolean screenOn) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setScreenOnWhilePlaying(screenOn);
        }
    }

    @Override
    public int getVideoWidth() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getVideoWidth();
        }
        return 0;
    }

    @Override
    public int getVideoHeight() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getVideoHeight();
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void seekTo(int position) throws IllegalStateException {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(position);
        }
    }

    @Override
    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return (int) mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if (mMediaPlayer != null) {
            return (int) mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }

    @Override
    public void setVolume(float var1, float var2) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(var1, var2);
        }
    }

    @Override
    public int getAudioSessionId() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getAudioSessionId();
        }
        return 0;
    }

    @Override
    public void setLogEnabled(boolean var1) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setLogEnabled(var1);
        }
    }

    @Override
    public boolean isPlayable() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlayable();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnPreparedListener(final OnPreparedListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {
                    if (listener != null) {
                        listener.onPrepared(iMediaPlayer);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnCompletionListener(final OnCompletionListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    if (listener != null) {
                        listener.onCompletion(iMediaPlayer);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnBufferingUpdateListener(final OnBufferingUpdateListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                    if (listener != null) {
                        listener.onBufferingUpdate(iMediaPlayer, i);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnSeekCompleteListener(final OnSeekCompleteListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                    if (listener != null) {
                        listener.onSeekComplete(iMediaPlayer);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnVideoSizeChangedListener(final OnVideoSizeChangedListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                    if (listener != null) {
                        listener.onVideoSizeChanged(iMediaPlayer, i, i1);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnErrorListener(final OnErrorListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                    if (listener != null) {
                        return listener.onError(iMediaPlayer, i, i1);
                    }
                    return false;
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnInfoListener(final OnInfoListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                    if (listener != null) {
                        return listener.onInfo(iMediaPlayer, i, i1);
                    }
                    return false;
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnTimedTextListener(final OnTimedTextListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnTimedTextListener(new IMediaPlayer.OnTimedTextListener() {
                @Override
                public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                    if (listener != null) {
                        listener.onTimedText(iMediaPlayer, ijkTimedText);
                    }
                }
            });
        }
    }

    @Override
    public void setAudioStreamType(int var1) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setAudioStreamType(var1);
        }
    }

    @Override
    public void setKeepInBackground(boolean var1) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setKeepInBackground(var1);
        }
    }

    @Override
    public int getVideoSarNum() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getVideoSarNum();
        }
        return 0;
    }

    @Override
    public int getVideoSarDen() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getVideoSarDen();
        }
        return 0;
    }

    @Override
    public void setWakeMode(Context var1, int var2) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setWakeMode(var1, var2);
        }
    }

    @Override
    public void setLooping(boolean var1) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(var1);
        }
    }

    @Override
    public boolean isLooping() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isLooping();
        }
        return false;
    }

    @Override
    public void setSurface(Surface var1) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setSurface(var1);
        }
    }

}
