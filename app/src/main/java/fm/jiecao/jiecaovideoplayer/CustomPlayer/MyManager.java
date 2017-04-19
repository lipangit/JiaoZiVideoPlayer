package fm.jiecao.jiecaovideoplayer.CustomPlayer;

/**
 * Created by tysheng
 * Date: 2017/4/18 19:34.
 * Email: tyshengsx@gmail.com
 */

public class MyManager extends fm.jiecao.jcvideoplayer_lib.JCMediaManager {
    private static MyManager sMyManager;

    public MyManager() {
        setWrapper(new MyPlayerController());
    }

    public static MyManager getInstance() {
        if (sMyManager == null) {
            sMyManager = new MyManager();
        }
        return sMyManager;
    }
}
