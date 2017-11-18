package cn.jzvd;

import android.graphics.SurfaceTexture;
import android.view.Surface;

import java.util.Map;

/**
 * Created by Nathen on 2017/11/7.
 * 自定义播放器
 */
public abstract class JZMediaInterface {

    public Object currentDataSource;//正在播放的当前url或uri
    public Object[] dataSourceObjects;//包含了地址的map（多分辨率用），context，loop，header等

    public abstract void start();

    public abstract void prepare();

    public abstract void pause();

    public abstract void isPlaying();

    public abstract void seekTo();

    public abstract void release();

    public abstract void getCurrentPosition();

    public abstract void getDuration();


}
