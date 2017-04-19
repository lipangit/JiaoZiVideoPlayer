package fm.jiecao.jiecaovideoplayer.CustomPlayer;

import fm.jiecao.jcvideoplayer_lib.AbstractPlayer;
import fm.jiecao.jcvideoplayer_lib.MediaManager;

/**
 * Created by tysheng
 * Date: 2017/4/19 09:02.
 * Email: tyshengsx@gmail.com
 */

public class MyMultiPlayer extends AbstractPlayer {
    public MyMultiPlayer(int whichPlayer) {
        super(whichPlayer);
    }

    @Override
    public MediaManager getMediaManagerInstance() {
        return MyManager.getInstance();
    }
}
