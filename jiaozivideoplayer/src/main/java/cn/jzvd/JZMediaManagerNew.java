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
    public int positionInList = -1;
    public JZMediaInterface jzMediaInterface;

    public static JZMediaManagerNew jzMediaManagerNew;

    public static JZMediaManagerNew instance() {
        if (jzMediaManagerNew == null) {
            jzMediaManagerNew = new JZMediaManagerNew();
        }
        return jzMediaManagerNew;
    }

    public static void setDataSource(Object[] dataSourceObjects) {
        instance().jzMediaInterface.dataSourceObjects = dataSourceObjects;
    }

    public static Object[] getDataSource() {
        return instance().jzMediaInterface.dataSourceObjects;
    }

    public static void setCurrentDataSource(Object currentDataSource) {
        instance().jzMediaInterface.currentDataSource = currentDataSource;
    }

    //正在播放的url或者uri
    public static Object getCurrentDataSource() {
        return instance().jzMediaInterface.currentDataSource;
    }

}
