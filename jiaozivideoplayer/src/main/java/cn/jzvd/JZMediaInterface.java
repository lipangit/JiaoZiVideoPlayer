package cn.jzvd;

import android.graphics.SurfaceTexture;
import android.view.Surface;

import java.util.Map;

/**
 * Created by Nathen on 2017/11/7.
 * 自定义播放器
 */
public abstract class JZMediaInterface {

    public static JZResizeTextureView textureView;
    public static SurfaceTexture savedSurfaceTexture;
    public static Surface surface;

    public static String CURRENT_PLAYING_URL;
    public static boolean CURRENT_PLING_LOOP;
    public static Map<String, String> MAP_HEADER_DATA;
    public static int positionInList = -1;


    abstract void setDataSource();

    abstract void start();

    abstract void prepare();

    abstract void pause();

    abstract void isPlaying();

    abstract void seekTo();

    abstract void release();

    abstract void getCurrentPosition();

    abstract void getDuration();


}
