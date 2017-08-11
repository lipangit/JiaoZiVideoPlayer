package fm.jiecao.jcvideoplayer_lib.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 原生播放器
 * Created by lidongyang on 2017/8/10.
 */
public class JCMediaPlayer extends AbstractPlayer<MediaPlayer> {

    public JCMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
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
            Class<MediaPlayer> clazz = MediaPlayer.class;
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
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
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
    }

    @Override
    public boolean isPlayable() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnPreparedListener(final OnPreparedListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    if (listener != null) {
                        listener.onPrepared(mediaPlayer);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnCompletionListener(final OnCompletionListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (listener != null) {
                        listener.onCompletion(mediaPlayer);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnBufferingUpdateListener(final OnBufferingUpdateListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                    if (listener != null) {
                        listener.onBufferingUpdate(mediaPlayer, i);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnSeekCompleteListener(final OnSeekCompleteListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mediaPlayer) {
                    if (listener != null) {
                        listener.onSeekComplete(mediaPlayer);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnVideoSizeChangedListener(final OnVideoSizeChangedListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
                    if (listener != null) {
                        listener.onVideoSizeChanged(mediaPlayer, width, height);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setOnErrorListener(final OnErrorListener listener) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    if (listener != null) {
                        return listener.onError(mediaPlayer, i, i1);
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
            mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                    if (listener != null) {
                        return listener.onInfo(mediaPlayer, i, i1);
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
            mMediaPlayer.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
                @Override
                public void onTimedText(MediaPlayer mediaPlayer, TimedText timedText) {
                    if (listener != null) {
                        listener.onTimedText(mediaPlayer, timedText);
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
    }

    @Override
    public int getVideoSarNum() {
        return 0;
    }

    @Override
    public int getVideoSarDen() {
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