package fm.jiecao.jcvideoplayer_lib.player;

import android.content.Context;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.Map;

/**
 * Created by lidongyang on 2017/8/10.
 */
public abstract class AbstractPlayer<F> {

    protected F mMediaPlayer;

    public abstract void setDisplay(SurfaceHolder surfaceHolder);

    public abstract void setDataSource(String path, Map<String, String> headers) throws Throwable;

    public abstract void prepareAsync() throws IllegalStateException;

    public abstract void start() throws IllegalStateException;

    public abstract void stop() throws IllegalStateException;

    public abstract void pause() throws IllegalStateException;

    public abstract void setScreenOnWhilePlaying(boolean screenOn);

    public abstract int getVideoWidth();

    public abstract int getVideoHeight();

    public abstract boolean isPlaying();

    public abstract void seekTo(int position) throws IllegalStateException;

    public abstract int getCurrentPosition();

    public abstract int getDuration();

    public abstract void release();

    public abstract void reset();

    public abstract void setVolume(float var1, float var2);

    public abstract int getAudioSessionId();

    /**
     * @deprecated
     */
    @Deprecated
    public abstract void setLogEnabled(boolean var1);

    /**
     * @deprecated
     */
    @Deprecated
    public abstract boolean isPlayable();

    public abstract void setOnPreparedListener(OnPreparedListener listener);

    public abstract void setOnCompletionListener(OnCompletionListener listener);

    public abstract void setOnBufferingUpdateListener(OnBufferingUpdateListener listener);

    public abstract void setOnSeekCompleteListener(OnSeekCompleteListener listener);

    public abstract void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener);

    public abstract void setOnErrorListener(OnErrorListener listener);

    public abstract void setOnInfoListener(OnInfoListener listener);

    public abstract void setOnTimedTextListener(OnTimedTextListener listener);

    public abstract void setAudioStreamType(int var1);

    /**
     * @deprecated
     */
    @Deprecated
    public abstract void setKeepInBackground(boolean var1);

    public abstract int getVideoSarNum();

    public abstract int getVideoSarDen();

    /**
     * @deprecated
     */
    @Deprecated
    public abstract void setWakeMode(Context var1, int var2);

    public abstract void setLooping(boolean var1);

    public abstract boolean isLooping();

    public abstract void setSurface(Surface var1);

    public interface OnTimedTextListener<T, V> {
        void onTimedText(T var1, V var2);
    }

    public interface OnInfoListener<P> {
        boolean onInfo(P var1, int var2, int var3);
    }

    public interface OnErrorListener<P> {
        boolean onError(P var1, int var2, int var3);
    }

    public interface OnVideoSizeChangedListener<P> {
        void onVideoSizeChanged(P var1, int width, int height);
    }

    public interface OnSeekCompleteListener<P> {
        void onSeekComplete(P var1);
    }

    public interface OnBufferingUpdateListener<P> {
        void onBufferingUpdate(P var1, int var2);
    }

    public interface OnCompletionListener<P> {
        void onCompletion(P var1);
    }

    public interface OnPreparedListener<P> {
        void onPrepared(P var1);
    }

}
