package cn.jzvd;

import android.view.Surface;

/**
 * Created by Nathen on 2017/11/7.
 * 自定义播放器
 */
public abstract class JZMediaInterface {

    public Object currentDataSource;//正在播放的当前url或uri
    /**
     * 第一个是url的map
     * 第二个是loop
     * 第三个是header
     * 第四个是context
     */
    public Object[] dataSourceObjects;//包含了地址的map（多分辨率用），context，loop，header等

    public abstract void start();

    public abstract void prepare();

    public abstract void pause();

    public abstract boolean isPlaying();

    public abstract void seekTo(int time);

    public abstract void release();

    public abstract int getCurrentPosition();

    public abstract int getDuration();

    public abstract void setSurface(Surface surface);
}
