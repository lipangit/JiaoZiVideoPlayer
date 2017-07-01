package fm.jiecao.jiecaovideoplayer.CustomView;

import android.content.Context;
import android.util.AttributeSet;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen on 2016/11/26.
 */

public class JCVideoPlayerStandardAutoCompleteAfterFullscreen extends JCVideoPlayerStandard {
    public JCVideoPlayerStandardAutoCompleteAfterFullscreen(Context context) {
        super(context);
    }

    public JCVideoPlayerStandardAutoCompleteAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAutoCompletion() {
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
<<<<<<< 3ef93e840f336e09e9053f2918a0358bb1d289ba
            onStateAutoComplete();
=======
//            setUiWitStateAndScreen(CURRENT_STATE_AUTO_COMPLETE);
>>>>>>> on state call back
        } else {
            super.onAutoCompletion();
        }

    }
}
