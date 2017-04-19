package fm.jiecao.jiecaovideoplayer.CustomPlayer;

import fm.jiecao.jcvideoplayer_lib.AbstractPlayerControllerWrapper;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;

/**
 * Created by tysheng
 * Date: 2017/4/19 09:03.
 * Email: tyshengsx@gmail.com
 */

public class MyPlayerController extends AbstractPlayerControllerWrapper {
    @Override
    public void initPlayer() {
        setMediaPlayer(new MyMultiPlayer(JCMediaManager.whichPlayer));
    }
}
