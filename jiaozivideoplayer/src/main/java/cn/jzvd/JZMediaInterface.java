package cn.jzvd;

import android.view.Surface;

/**
 * Created by Nathen on 2017/11/7.
 * 自定义播放器
 */
public abstract class JZMediaInterface {

    /**
     * Current URI, URL or AssetFileDescriptor being played
     */
    public Object currentPath;

    public JZDataSource dataSource;

    public abstract void start();

    public abstract void prepare();

    public abstract void pause();

    public abstract boolean isPlaying();

    public abstract void seekTo(long time);

    public abstract void release();

    public abstract long getCurrentPosition();

    public abstract long getDuration();

    public abstract void setSurface(Surface surface);

    public abstract void setVolume(float leftVolume, float rightVolume);
}
