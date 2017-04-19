package fm.jiecao.jiecaovideoplayer.CustomPlayer;

import android.content.Context;
import android.util.AttributeSet;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.MediaManager;

/**
 * Created by tysheng
 * Date: 2017/4/19 10:20.
 * Email: tyshengsx@gmail.com
 */

public class MyPlayer extends JCVideoPlayerStandard {
    public MyPlayer(Context context) {
        super(context);
    }

    public MyPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public MediaManager getMediaManagerInstance() {
        return MyManager.getInstance();
    }
}
