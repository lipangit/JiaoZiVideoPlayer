package cn.jzvd;

import android.graphics.SurfaceTexture;
import android.view.Surface;

import java.util.Map;

/**
 * Created by Nathen on 2017/11/7.
 * 自定义播放器
 */
public abstract class JZMediaInterface {


    public abstract void setDataSource(Object[] dataSourceObjects);

    public abstract void getCurrentDataSource(Object[] dataSourceObjects, int currentUrlMapIndex);

    public abstract Object[] getDataSource();

    public abstract void start();

    public abstract void prepare();

    public abstract void pause();

    public abstract void isPlaying();

    public abstract void seekTo();

    public abstract void release();

    public abstract void getCurrentPosition();

    public abstract void getDuration();


}
