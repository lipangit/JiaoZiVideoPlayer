package cn.jzvd;

import android.graphics.SurfaceTexture;
import android.view.Surface;

import java.util.Map;

/**
 * 这个类用来和jzvd互相调用，当jzvd需要调用Media的时候调用这个类，当MediaPlayer有回调的时候，通过这个类回调JZVD
 * Created by Nathen on 2017/11/18.
 */
public class JZMediaManagerNew {

    public static JZResizeTextureView textureView;
    public static SurfaceTexture savedSurfaceTexture;
    public static Surface surface;

    public static JZMediaInterface jzMediaInterface;

    public static void setDataSource(Object[] dataSourceObjects) {
        jzMediaInterface.setDataSource(dataSourceObjects);
    }

    public static Object[] getDataSource() {
        return jzMediaInterface.getDataSource();
    }

    //正在播放的url或者uri
    public static Object getCurrentDataSource() {

        return null;
    }

}
